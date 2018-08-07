package com.example.tvd.trm_discon_recon.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.example.tvd.trm_discon_recon.invoke.ApkNotification;
import com.example.tvd.trm_discon_recon.values.FunctionCall;

public class Apk_Update_Service extends Service {
    FunctionCall functionCall;

    public Apk_Update_Service() {
    }

    @Override
    public void onCreate() {
        functionCall = new FunctionCall();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                start_version_check();
            }
        }, 1000 * 3);
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void start_version_check() {
        functionCall.logStatus("Version_receiver Checking..");
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), ApkNotification.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_NO_CREATE) != null);
        if (!alarmRunning) {
            functionCall.logStatus("Version_receiver Started..");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), (10000), pendingIntent);
        } else functionCall.logStatus("Version_receiver Already running..");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
       // stop_version_check();
    }

   /* private void stop_version_check() {
        functionCall.logStatus("Version_receiver Checking..");
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), ApkNotification.class);
        boolean alarmRunning = (PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_NO_CREATE) != null);
        if (alarmRunning) {
            functionCall.logStatus("Version_receiver Stopping..");
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
            alarmManager.cancel(pendingIntent);
        } else functionCall.logStatus("Version_receiver Not yet Started..");
    }*/
}
