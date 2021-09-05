package edu.umo.mpdtracker;

import android.Manifest;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import edu.umo.mpdtracker.Model.Medicine;
import edu.umo.mpdtracker.adapter.MedicineLVAdapter;
import edu.umo.mpdtracker.persist.DBReader;
import edu.umo.mpdtracker.scheduler.MPDJobService;

public class MainActivity extends AppCompatActivity {

    public static final int RequestPermissionCode = 1;
    private ListView medicinesLV;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Add button click listener to open "Add medicine" screen
        Button buttonAdd = (Button) findViewById(R.id.floatingActionButton);
        buttonAdd.setOnClickListener(v -> {

            Intent in = new Intent(MainActivity.this, AddMedicineActivity.class);
            startActivity(in);

            Log.d("LISTENER", "Executed");
        });

        // Load all the icons and medicine data on entire calendar during create
        paintAllMedsDataOnCalendar();

        //Add day click listener to calendar
        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDayClickListener(eventDay -> {
            java.util.Calendar clickedDayCalendar = eventDay.getCalendar();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            paintMedicineDataOnCalendar(formatter.format(clickedDayCalendar.getTime()));
        });

        //Execute this method to check if app has camera permission, else to get permission from end user
        enableRuntimePermission();

        //Schedule the job as final activity on app startup.
        MainActivity.scheduleJob(MainActivity.this);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        MainActivity.this.finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        paintAllMedsDataOnCalendar();
        Log.d("Resume", "Executed");
    }

    // schedule the start of the service every 10 - 30 seconds
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void scheduleJob(Context context) {
        ComponentName serviceComponent = new ComponentName(context, MPDJobService.class);
        JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponent);
        builder.setMinimumLatency(1 * 60 * 60 * 1000); // wait at least
        builder.setOverrideDeadline(8 * 60 * 60 * 1000); // maximum delay
        builder.setRequiresCharging(false); // we don't care if the device is charging or not
        JobScheduler jobScheduler = context.getSystemService(JobScheduler.class);
        jobScheduler.schedule(builder.build());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    //TODO: Rename method to appropriate convention
    private void paintMedicineDataOnCalendar(String calendarDate) {

        DBReader reader = new DBReader(MainActivity.this);

        List returnedValue = reader.readFormDB(calendarDate);
        medicinesLV = findViewById(R.id.dynamicList);
        if (returnedValue != null && returnedValue.size() > 0) {
            MedicineLVAdapter adapter = new MedicineLVAdapter(this, (ArrayList<Medicine>) returnedValue);
            medicinesLV.setAdapter(adapter);
            Log.d("DB READ", returnedValue.get(0).toString());
        } else {
            medicinesLV.setAdapter(null);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void paintAllMedsDataOnCalendar() {
        DBReader reader = new DBReader(MainActivity.this);
        List<Medicine> items = reader.readFormDB();
        List<EventDay> events = new ArrayList<>();
        java.util.Calendar calendar = java.util.Calendar.getInstance();

        int count = 0;

        if (items != null) {
            for (Medicine medicine : items) {
                if (medicine != null && medicine.getDays() != null) {
                    //if medicine days is greater than 1
                    if (Integer.parseInt(medicine.getDays()) > 1) {
                        //Starting from start date till number of days
                        for (int i = 0; i < Integer.parseInt(medicine.getDays()); i++) {
                            calendar = java.util.Calendar.getInstance();
                            calendar.setTime(new Date(getDateSelected(medicine.getStartDate()).getTime() + TimeUnit.DAYS.toMillis(i)));
                            //add an event in the calendar
                            events.add(new EventDay(calendar, R.drawable.icons8_medicine_64, Color.parseColor("#228B22")));
                        }
                    } else {
                        //else
                        //add event only on the start date
                        calendar = java.util.Calendar.getInstance();
                        calendar.setTime(getDateSelected(medicine.getStartDate()));
                        //add an event in the calendar
                        events.add(new EventDay(calendar, R.drawable.icons8_medicine_64, Color.parseColor("#228B22")));
                    }
                }
            }
        }

        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setEvents(events);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Date getDateSelected(String dateSting) {
        Date inputDate = null;
        if (dateSting != null && !dateSting.isEmpty()) {
            try {
                inputDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateSting);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return inputDate;
    }

    public void enableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.CAMERA)) {
            Toast.makeText(MainActivity.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }
}