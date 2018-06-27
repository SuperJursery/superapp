package com.android.mothership.superapp;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class StorageInfo extends AppCompatActivity {
    private final String TAG = "StorageInfo";
    private TextView tv_path;
    private TextView tv_total;
    private TextView tv_free;
    private StorageManager mStorageManager;
    private static final int MSG_UI_UPDATE_STORAGE_INFO = 0xA1;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_info);
        tv_path = (TextView) findViewById(R.id.txV00_value);
        tv_total = (TextView) findViewById(R.id.txV01_value);
        tv_free = (TextView) findViewById(R.id.txV02_value);
        mTimer.scheduleAtFixedRate(task,1000,1000);

    }

    private Timer mTimer = new Timer();
    TimerTask task = new TimerTask(){
        @Override
        public void run(){
            Log.d(TAG,"Timer task reach");
            mHandler.sendMessageDelayed(Message.obtain(mHandler,MSG_UI_UPDATE_STORAGE_INFO), 2000);
        }
    };

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UI_UPDATE_STORAGE_INFO:
                    checkStorageInfo();
                    break;
                default:
                    break;
            }
        }
    };

    public void checkStorageInfo(){
        mHandler.removeMessages(MSG_UI_UPDATE_STORAGE_INFO);
        Log.d(TAG,"do checkFreeValue");
        File file = Environment.getExternalStorageDirectory();
        Log.d(TAG,"ExternalStorageDirectory="+file.getPath());
        setPathValue(file.getPath());
        String state = Environment.getExternalStorageState(file);
        if(Environment.MEDIA_MOUNTED.equals(state)){
            StatFs mStatFs = new StatFs(file.getPath());
            Long TotalSize = mStatFs.getTotalBytes();
            Long FreeSize = mStatFs.getFreeBytes();
            Long AvailableSzie = mStatFs.getAvailableBytes();
            Log.d(TAG,"MtpStorage : TotalSize="+TotalSize+",FreeSize="+FreeSize+",AvailableSzie="+AvailableSzie);
            setTotalValue(TotalSize.toString());
            setFreeValue(AvailableSzie.toString());
        }

        mStorageManager = (StorageManager) this.getSystemService(Context.STORAGE_SERVICE);
        StorageVolume storageVolume = mStorageManager.getStorageVolume(file);
        Log.d(TAG,"UUid="+storageVolume.getUuid());
        Log.d(TAG,"Max="+storageVolume.getState());
    }

    private void setPathValue(String str){
        tv_path.setText(str);
    }

    private void setTotalValue(String str){
        tv_total.setText(str);
    }

    private void setFreeValue(String str){
        tv_free.setText(str);
    }
}
