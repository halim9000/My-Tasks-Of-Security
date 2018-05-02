package com.jerbi.halim.mytaskofsecurity.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class CountDownService extends Service {
    public static final String MY_SERVICE = "MY_SERVICE";
    private long time;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    public CountDownService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {

        Log.e("hey you","hey you");
        Log.e("hey you",String.valueOf(time));
         preferences = PreferenceManager.getDefaultSharedPreferences(this);
         editor = preferences.edit();
        new CountDownTimer(time, 1000) {

            public void onTick(long millisUntilFinished) {

                Log.e("TIME", String.valueOf(millisUntilFinished));
                editor.putLong("time",millisUntilFinished);
                editor.apply();



            }

            public void onFinish() {
            startService(new Intent(CountDownService.this,CountDownService.class));
            }

        }.start();

        return START_STICKY ;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("hey you","hey you");
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        time = preferences.getLong("time", 0);

    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
