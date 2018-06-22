package com.android.mothership.superapp;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class checkSimStatus extends AppCompatActivity {
    private final String TAG ="checkSimStatus";
    private final String ACTION_SIM_STATE_CHANGED= "android.intent.action.SIM_STATE_CHANGED";
    private TelephonyManager telMgr;
    private Handler mHandler;
    private EditText enterText;

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(ACTION_SIM_STATE_CHANGED)) {
                Log.d(TAG,"onReceive:get ACTION_SIM_STATE_CHANGED");
                mHandler.post(taskJob);
            }
        }
    };

    private final Runnable taskJob=new Runnable(){
        @Override
        public void run(){
            synchronized (checkSimStatus.class){
                checkSim();
            }
        }
    };

    public void gotoMainPage(View view){
        Log.d(TAG,"do gotoMainPage");
        Intent intent = new Intent();
        ComponentName cpn= new ComponentName("com.android.mothership.superapp","com.android.mothership.superapp.superAppActivity");
        intent.setComponent(cpn);
        startActivity(intent);
    }
    public void phoneCall(View view){
        String phoneNum=enterText.getText().toString();
        Log.d(TAG,"ready to call "+phoneNum);
        CallPhone(phoneNum);
    }

    public void CallPhone(String phoneNum){
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:"+phoneNum);
        intent.setData(data);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_sim_status);
        telMgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SIM_STATE_CHANGED);
        this.registerReceiver(mBroadcastReceiver ,filter);

        mHandler = new Handler();
        enterText =(EditText)findViewById(R.id.enterNum);


    }

    private void checkSim(){
        Log.d(TAG,"Sim State: "+getSimState());
        Log.d(TAG,"Sim Phone NO: "+getSimPhoneNumber());
        Log.d(TAG,"Phone State:"+getCallState());
        Log.d(TAG,"Sim Server Provider: "+getSimOperatorName());
        Log.d(TAG,"Sim Location: "+getCellLocation());
    }

    private String getCellLocation(){
        CellLocation location = telMgr.getCellLocation();
        return (location!=null)? (location.toString()):"Null";
    }
    private String getSimOperatorName(){
        return telMgr.getSimOperatorName();
    }
    private String getCallState(){
        StringBuffer result=new StringBuffer("");
        switch(telMgr.getCallState()){
            case TelephonyManager.CALL_STATE_IDLE:
                result.append("无活动");
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                result.append("响铃中");
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                result.append("通话中");
                break;
            default:
                break;
        }
        return result.toString();
    }
    private String getSimState(){
        StringBuffer result=new StringBuffer("");
        switch(telMgr.getSimState()) {
            case TelephonyManager.SIM_STATE_UNKNOWN:
                result.append("SIM_STATE_UNKNOWN");
                break;
            case TelephonyManager.SIM_STATE_ABSENT:
                result.append("SIM_STATE_ABSENT");
                break;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED:
                result.append("SIM_STATE_PIN_REQUIRED");
                break;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED:
                result.append("SIM_STATE_PUK_REQUIRED");
                break;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED:
                result.append("SIM_STATE_NETWORK_LOCKED");
                break;
            case TelephonyManager.SIM_STATE_READY:
                result.append("SIM_STATE_READY");
                break;
            case TelephonyManager.SIM_STATE_NOT_READY:
                result.append("SIM_STATE_NOT_READY");
                break;
            case TelephonyManager.SIM_STATE_PERM_DISABLED:
                result.append("SIM_STATE_PERM_DISABLED");
                break;
            case TelephonyManager.SIM_STATE_CARD_IO_ERROR:
                result.append("SIM_STATE_CARD_IO_ERROR");
                break;
            case TelephonyManager.SIM_STATE_CARD_RESTRICTED:
                result.append("SIM_STATE_CARD_RESTRICTED");
                break;
            default:
                break;
        }
        return result.toString();
    }
    private String getSimPhoneNumber(){
        String phoneNum = "N/A";
        phoneNum = telMgr.getLine1Number();
        return phoneNum!=null ?phoneNum:"N/A";
    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG,"do onResume");
    }
    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG,"do onPause");
    }
}
