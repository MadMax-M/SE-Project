package com.parse.starter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


public  class AlertReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            String data = intent.getStringExtra("data");
            Log.i("i am in", data);
            NotificationHelper notificationHelper = new NotificationHelper(context, data);
            NotificationCompat.Builder nb = notificationHelper.getChannelNotification();
            notificationHelper.getManager().notify(1, nb.build());
        }
    }



