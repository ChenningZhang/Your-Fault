package com.example.chenningzhang.yourfault;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by ChenningZhang on 11/3/15.
 */
public class EarthquakeService extends Service {

    PendingIntent pendingIntent;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("SERVICE ONCREATE", "OnCreate Called");
        super.onCreate();
        Intent alarmIntent = new Intent(getApplicationContext(), EarthquakeAlarm.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, alarmIntent, 0);
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000, pendingIntent);
    }

}
