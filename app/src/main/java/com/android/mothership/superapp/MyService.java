package com.android.mothership.superapp;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {
    private final String TAG = "MyService";
    Handler mHandler = new Handler();
    int i = 0;
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            Log.d(TAG, "MyServiceLife01: do mRunnable:" + i++);
            mHandler.postDelayed(mRunnable, 1000);
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "MyServiceLife01: do onCreate()");
        mHandler.post(mRunnable);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "MyServiceLife01: onStartCommand:" + startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "MyServiceLife01: do onDestroy()");
        super.onDestroy();
    }
}
