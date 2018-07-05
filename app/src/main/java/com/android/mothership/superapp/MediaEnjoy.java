package com.android.mothership.superapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MediaEnjoy extends AppCompatActivity {
    private final String TAG="MediaEnjoy";
    private MediaPlayer mediaPlayer;
    private MusicService musicService;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            musicService = ((MusicService.MusiBinder)binder).getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_enjoy);

        Intent intent = new Intent();
        intent.setClass(this,MusicService.class);
        startService(intent);
        bindService(intent,conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Intent intent = new Intent();
        intent.setClass(this,MusicService.class);
        unbindService(conn);
        stopService(intent);
    }

}
