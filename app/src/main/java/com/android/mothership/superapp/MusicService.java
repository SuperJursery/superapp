package com.android.mothership.superapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;

public class MusicService extends Service {
    private final String TAG = "MusicService";
    private final IBinder binder = new MusiBinder();
    public ArrayList<PlayerStatus> mPlayerStates;
    public MusicService() {
        mPlayerStates = new ArrayList<PlayerStatus>();
        mPlayerStates.add(new PlayerStatus(R.raw.hhzl,1,true));
        mPlayerStates.add(new PlayerStatus(R.raw.makemoretime,2,false));
        mPlayerStates.add(new PlayerStatus(R.raw.unity,3,false));
        mPlayerStates.add(new PlayerStatus(R.raw.whereareyounow,4,false));
    }

    private void create_init(){
        for(int i=0;i< mPlayerStates.size();i++){
            PlayerStatus  pStatus =  mPlayerStates.get(i);
            pStatus.name = getResources().getResourceEntryName(pStatus.mId);
            pStatus.mediaPlayer=MediaPlayer.create(this,pStatus.mId);
        }
    }

    class PlayerStatus{
        public MediaPlayer mediaPlayer;
        public int  mId;
        public String name;
        public int grade;
        public boolean intact;
        public PlayerStatus(int resId, int gId, boolean cpId ) {
            mId = resId;
            grade = gId;
            intact = cpId;
            //name = getResources().getResourceEntryName(resId);
            //mediaPlayer = MediaPlayer.create(mContext,mId);
        }
    }

    @Override
    public void onCreate(){
        super.onCreate();
        create_init();
        Log.d(TAG,"do onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"do onStartCommand");
        return  super.onStartCommand(intent,flags,startId);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG,"do onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }

    class MusiBinder extends Binder {
        MusicService getService(){
            return MusicService.this;
        }
    }

}
