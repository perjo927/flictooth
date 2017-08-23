package com.preybird.webtooth;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import io.flic.lib.FlicBroadcastReceiver;
import io.flic.lib.FlicButton;

public class ButtonBroadcastReceiver extends FlicBroadcastReceiver {

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
            intent.setAction("SPIN_BUTTON");
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        }
    }

    @Override
    public void onButtonRemoved(Context context, FlicButton button) {
        Toast.makeText(context, "Button was removed", Toast.LENGTH_SHORT).show();
    }
}
