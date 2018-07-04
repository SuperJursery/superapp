package com.android.mothership.superapp;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.view.View.OnClickListener;
//import android.os.SystemProperties;

public class superAppActivity extends AppCompatActivity {
    private final String TAG = "superAppActivity";
    private boolean VOLUME_UP_LongPressLock = false;
    private boolean VOLUME_DOWN_LongPressLock = false;
    private final String SleepRequest="com.bbt.action.sleeprequest";
    private BroadcastReceiver mBroadcastReceiver;
    private PowerManager.WakeLock sleepWakeLock;
    private PowerManager.WakeLock usbWakeLock;
    private int addLockNum=0;
    private int releaseLockNum=0;
    private Handler mHandler;
    private int cmdSend=0;
    private int cmdDone=0;

    private Button switchButton;
    private Button storageButton;
    private Button serviceStartButton;
    private Button serviceStopButton;
    private Button enjoyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_app);
        Log.d(TAG,"Life01: do onCreate()");
        IntentFilter filter = new IntentFilter();
        filter.addAction(SleepRequest);
        mBroadcastReceiver =  new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String actionName = intent.getAction();
                if(SleepRequest.equals(actionName)){
                    Log.d(TAG,"get broadcaset="+SleepRequest);
                }
            }
        };
        this.registerReceiver(mBroadcastReceiver ,filter);

        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        sleepWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
        usbWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG+"Usb");
        mHandler= new Handler();

        switchButton = (Button) findViewById(R.id.button);
        switchButton.setOnClickListener(switchButtonHander);

        storageButton = (Button) findViewById(R.id.btn_storage);
        storageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ComponentName cpn= new ComponentName("com.android.mothership.superapp","com.android.mothership.superapp.StorageInfo");
                intent.setComponent(cpn);
                startActivity(intent);
            }
        });

        serviceStartButton = (Button)findViewById(R.id.btn_service_start);
        serviceStartButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(superAppActivity.this, MyService.class));
            }
        });
        serviceStopButton  = (Button)findViewById(R.id.btn_service_stop);
        serviceStopButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(superAppActivity.this, MyService.class));
            }
        });

        enjoyButton = (Button)findViewById(R.id.btn_enjoy);
        enjoyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                ComponentName cpn= new ComponentName("com.android.mothership.superapp","com.android.mothership.superapp.MediaEnjoy");
                intent.setComponent(cpn);
                startActivity(intent);
            }
        });
    }

    OnClickListener switchButtonHander = new OnClickListener() {
        @Override
        public void onClick(View v){
            Intent intent = new Intent();
            ComponentName cpn= new ComponentName("com.android.mothership.superapp","com.android.mothership.superapp.checkSimStatus");
            intent.setComponent(cpn);
            startActivity(intent);
        }
    };

    private final Runnable mEndCallJob = new Runnable() {
        @Override
        public void run() {
            mHandler.removeCallbacks(mEndCallJob);
            cmdDone++;
            Log.d(TAG,"CMD Hander: cmdSend="+cmdSend+",cmdDone="+cmdDone+" [Runnable]");
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       // Log.d(TAG,"get keyEvent down, keyCode="+ keyCode);
        switch(keyCode){
            case KeyEvent.KEYCODE_F1:
                //仅仅监听按下事件，短按和长按执行结果一样；
                Log.d(TAG,"get keyEvent : KEYCODE_F1");
                //sendBroadcast(new Intent("com.bbt.gotosleep"));
                //usbWakeLock.acquire();
                //Log.d(TAG,"get keyEvent : KEYCODE_F1 add usbWakeLock");

                mHandler.postDelayed(mEndCallJob,1000);
                cmdSend++;
                Log.d(TAG,"CMD Hander: cmdSend="+cmdSend+",cmdDone="+cmdDone);
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                event.startTracking(); //需要监听长按事件
                //Log.d(TAG,"get KEYCODE_VOLUME_DOWN isLongPress="+event.isLongPress());
                return true; //必须返回 true 让长按API捕获该事件
            case KeyEvent.KEYCODE_VOLUME_UP:
                event.startTracking(); //需要监听长按事件
                //Log.d(TAG,"KEYCODE_VOLUME_UP:event.isLongPress()="+event.isLongPress());
                return true; //必须返回 true 让长按API捕获该事件
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event){
        switch(keyCode){
            case KeyEvent.KEYCODE_VOLUME_UP:
                if(VOLUME_UP_LongPressLock){
                    //长按事件已处理完，清空长按事件标志，等待下次长按置位
                    VOLUME_UP_LongPressLock=false;
                    Log.d(TAG,"get LongPress  KeyUp  : KEYCODE_VOLUME_UP");
                }else{
                    //检测到不是长按，则该事件为短按事件
                    Log.d(TAG,"get ShortPress KeyUp  : KEYCODE_VOLUME_UP");
                    addLock();
                }
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if(VOLUME_DOWN_LongPressLock){
                    //长按事件已处理完，清空长按事件标志，等待下次长按置位
                    VOLUME_DOWN_LongPressLock=false;
                    Log.d(TAG,"get LongPress  KeyUp  : KEYCODE_VOLUME_DOWN");
                }else{
                    //检测到不是长按，则该事件为短按事件
                    Log.d(TAG,"get ShortPress KeyUp  : KEYCODE_VOLUME_DOWN");
                    releaseLock();
                }
                break;
            default:
                break;
        }

        return super.onKeyUp(keyCode,event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event){
        //Log.d(TAG,"onKeyLongPress : keyCode"+keyCode);
        switch(keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                VOLUME_UP_LongPressLock =true;
                Log.d(TAG,"get LongPress  KeyDown: KEYCODE_VOLUME_UP");
                break;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                VOLUME_DOWN_LongPressLock=true;
                Log.d(TAG,"get LongPress  KeyDown: KEYCODE_VOLUME_DOWN ");
                break;
            default:
                break;
        }
        return super.onKeyLongPress(keyCode,event);
    }

    void addLock(){
        if(sleepWakeLock != null) {
            sleepWakeLock.acquire();
            addLockNum++;
            Log.d(TAG, "addLockNum="  + addLockNum);
        }else{
            Log.d(TAG, "addLockNum=  --null--" + addLockNum);
        }
    }

    void releaseLock(){
        if(sleepWakeLock != null) {
            sleepWakeLock.release();
            releaseLockNum++;
            Log.d(TAG, "releaseLockNum=" + releaseLockNum);
        }else{
            Log.d(TAG, "releaseLockNum=  --null--" + releaseLockNum);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG,"Life02: do onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        if(sleepWakeLock == null){
            Log.d(TAG,"add sleepWakeLock: waiting app answer");
        }
        Log.d(TAG,"Life03: do onResume()");
    }

    @Override
    public void onPause(){
        super.onPause();
        if(null != sleepWakeLock) {
            //sleepWakeLock.release();
            Log.d(TAG,"get app response: release sleepWakeLock");
        }
        Log.d(TAG,"Life04: do onPause()");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG,"Life05: do onStop()");
    }

    @Override
    public void onDestroy(){
        this.unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
        Log.d(TAG,"Life06: do onDestroy()");
    }

    @Override
    public void onRestart(){
        super.onRestart();
        Log.d(TAG,"Life07: do onRestart()");
    }

    @Override
    public void finish(){
        super.finish();
        Log.d(TAG,"do onfinish()");
    }


}
