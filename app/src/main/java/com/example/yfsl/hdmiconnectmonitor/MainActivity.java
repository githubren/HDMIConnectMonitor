package com.example.yfsl.hdmiconnectmonitor;

import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.drm.DrmStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.util.Log;

import java.io.File;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    private static boolean isHdmiConnect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean isConnect = isHdmiSwitchSet();
        Log.e("TAG","isConnect:"+isConnect);

        MyReceiver myReceiver = new MyReceiver();
        Intent intent = new Intent();
        intent.setAction("android.intent.action.HDMI_PLUGGED");
        myReceiver.onReceive(this,intent);
    }

    public static class MyReceiver extends android.content.BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals("android.intent.action.HDMI_PLUGGED")) {
                boolean state = intent.getBooleanExtra("state", false);
                if (state) {
//                    isHdmiConnect = true;
                    Log.e("TAG","connect");
                } else {
//                    isHdmiConnect = false;
                    Log.e("TAG","disconnect");
                }
            }
        }
    }

    private static boolean isHdmiSwitchSet() {
        // The file '/sys/devices/virtual/switch/hdmi/state' holds an int -- if it's 1 then an HDMI device is connected.
        // An alternative file to check is '/sys/class/switch/hdmi/state' which exists instead on certain devices.
        File switchFile = new File("/sys/devices/virtual/switch/hdmi/state");
        if (!switchFile.exists()) {
            switchFile = new File("/sys/class/switch/hdmi/state");
        }
        try {
            Scanner switchFileScanner = new Scanner(switchFile);
            int switchValue = switchFileScanner.nextInt();
            Log.e("TAG","content："+switchValue);
            switchFileScanner.close();
            return switchValue > 0;
        } catch (Exception e) {
            Log.e("TAG","err:"+e);
            return false;
        }
    }
}
