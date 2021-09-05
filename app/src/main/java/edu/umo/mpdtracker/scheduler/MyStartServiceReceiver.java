package edu.umo.mpdtracker.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;

import edu.umo.mpdtracker.MainActivity;

public class MyStartServiceReceiver extends BroadcastReceiver {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        MainActivity.scheduleJob(context);
    }
}