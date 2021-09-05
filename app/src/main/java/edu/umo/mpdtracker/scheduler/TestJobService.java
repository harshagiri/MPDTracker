package edu.umo.mpdtracker.scheduler;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.List;

import edu.umo.mpdtracker.Model.Medicine;
import edu.umo.mpdtracker.R;

import edu.umo.mpdtracker.MainActivity;
import edu.umo.mpdtracker.persist.DBReader;

/**
 * JobService to be scheduled by the JobScheduler.
 * start another service
 */
@SuppressLint("SpecifyJobSchedulerIdRange")
public class TestJobService extends JobService {
    private static final String TAG = "SyncService";
    private String CHANNEL_ID = "ANDROID";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onStartJob(JobParameters params) {
        Intent service = new Intent(getApplicationContext(), MainActivity.class);
        getApplicationContext().startService(service);
        MainActivity.scheduleJob(getApplicationContext()); // reschedule the job

        if(createNotifyChannel()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                displayNotification();
            }
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private Medicine buildData(){
        //Get current date
        Calendar mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);

        //Query against current date
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        DBReader reader = new DBReader(getApplicationContext());
        List returnedValue = reader.readFormDB(formatter.format(mCalendar.getTime()));

        //Build Medicine model
        if (returnedValue != null && returnedValue.size() > 0) {
            return (Medicine) returnedValue.get(0);
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void displayNotification() {

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.getApplicationContext(), 0, intent, 0);

        Medicine med = buildData();
        if(med != null){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this.getApplicationContext(), CHANNEL_ID)
                    .setSmallIcon(R.drawable.icons8_medicine_64)
                    .setContentTitle(med.getName())
                    .setContentText("("+med.getMorningSwitch()+"-"+med.getAfternoonSwitch()+"-"+med.getNightSwitch()+")")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setCategory(NotificationCompat.CATEGORY_REMINDER)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.getApplicationContext());
            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(1, builder.build());
        }
    }

    private boolean createNotifyChannel(){
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            return true;
        }
        return false;
    }
}