package com.preybird.webtooth;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import java.util.Date;

import io.flic.lib.FlicBroadcastReceiver;
import io.flic.lib.FlicButton;
import android.widget.Toast;

public class BroadcastReceiver extends FlicBroadcastReceiver {
    @Override
    protected void onRequestAppCredentials(Context context) {
        Config.setFlicCredentials();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onButtonUpOrDown(Context context, FlicButton button, boolean wasQueued, int timeDiff, boolean isUp, boolean isDown) {
        if (isDown) {

            Notification notification = new Notification.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Button was pressed")
                    .setContentText("Pressed last time at " + new Date())
                    .build();
            ((NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(1, notification);
        }
    }

    @Override
    public void onButtonRemoved(Context context, FlicButton button) {
        Log.d("yo", "removed");
        Toast.makeText(context, "Button was removed", Toast.LENGTH_SHORT).show();
    }
}
