package com.android.mothership.superapp;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MediaEnjoy extends AppCompatActivity {
    private final String TAG="MediaEnjoy";
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_enjoy);

        mediaPlayer = new MediaPlayer();
        try{
            //mediaPlayer.setDataSource(R.raw.whereareyounow);
            //mediaPlayer.setDataSource();
            mediaPlayer = MediaPlayer.create(this,R.raw.whereareyounow);
            mediaPlayer.prepare();
            mediaPlayer.setLooping(true);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        mediaPlayer.start();
    }

    @Override
    public void onPause(){
        super.onPause();
        mediaPlayer.stop();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mediaPlayer.release();
    }

}
