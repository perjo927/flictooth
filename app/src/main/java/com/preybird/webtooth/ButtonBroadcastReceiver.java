package com.preybird.webtooth;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

import io.flic.lib.FlicBroadcastReceiver;
import io.flic.lib.FlicButton;

public class ButtonBroadcastReceiver extends FlicBroadcastReceiver {
//    private LocalBroadcastManager mLocalBroadcastManager;
//
//    public BroadcastReceiver(LocalBroadcastManager mLocalBroadcastManager) {
//        this.mLocalBroadcastManager = mLocalBroadcastManager;
//    }

//    public BroadcastReceiver() {
//
//    }

//    MainActivity ma;
//    AlertServiceFragment asf;

//    public BroadcastReceiver(MainActivity maContext) {
//        ma=maContext;
//    }
//    public BroadcastReceiver(AlertServiceFragment asfC) {
//        asf=asfC;
//    }


    @Override
    protected void onRequestAppCredentials(Context context) {
        Config.setFlicCredentials();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onButtonUpOrDown(Context context, FlicButton button, boolean wasQueued,
                                 int timeDiff, boolean isUp, boolean isDown) {
        if (isDown) {
            Intent intent = new Intent();
            intent.setAction("SPINNA_LOSS");
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
    }

    @Override
    public void onButtonRemoved(Context context, FlicButton button) {
        Log.d("yo", "removed");
        Toast.makeText(context, "Button was removed", Toast.LENGTH_SHORT).show();
    }
}
