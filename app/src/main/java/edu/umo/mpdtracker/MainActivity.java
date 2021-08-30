package edu.umo.mpdtracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import edu.umo.mpdtracker.Model.Medicine;
import edu.umo.mpdtracker.adapter.MedicineLVAdapter;
import edu.umo.mpdtracker.persist.DBReader;

public class MainActivity extends AppCompatActivity {

    public static final int RequestPermissionCode = 1;
    private ListView medicinesLV;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonAdd = (Button) findViewById(R.id.floatingActionButton);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in = new Intent(MainActivity.this, AddMedicineActivity.class);
                startActivity(in);

                Log.d("LISTENER", "Executed");
            }
        });

        paintAllMedsDataOnCalendar();

        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                java.util.Calendar clickedDayCalendar = eventDay.getCalendar();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                paintMedicineDataOnCalendar(formatter.format(clickedDayCalendar.getTime()));
            }
        });

        paintMedicineDataOnCalendar(Calendar.getInstance());
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    //TODO: Rename method to appropriate convention
    private void paintMedicineDataOnCalendar(Calendar calendar) {

        String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar);
        DBReader reader = new DBReader(MainActivity.this);

        List returnedValue = reader.readFormDB(selectedDate);
        if (returnedValue != null && returnedValue.size() > 0) {
            MedicineLVAdapter adapter = new MedicineLVAdapter(this, (ArrayList<Medicine>) returnedValue);

            medicinesLV = findViewById(R.id.dynamicList);
            medicinesLV.setAdapter(adapter);
            Log.d("DB READ", returnedValue.get(0).toString());
        }
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

    private void loadDataInListview(List<Medicine> items) {
        // after that we are passing our array list to our adapter class.
        MedicineLVAdapter adapter = new MedicineLVAdapter(this, (ArrayList<Medicine>) items);

        // after passing this array list to our adapter
        // class we are setting our adapter to our list view.
        medicinesLV.setAdapter(adapter);
    }

    public void EnableRuntimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.CAMERA)) {
            Toast.makeText(MainActivity.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }
}