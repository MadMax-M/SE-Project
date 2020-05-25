package com.parse.starter;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.parse.ParseUser;

import static com.parse.starter.HomeActivity.*;
import static com.parse.starter.MainActivity.*;
import static com.parse.starter.EventsManegerImportant.getSavedEventData;

class NotificationHelper extends ContextWrapper {
    String nameOfEvent;
    String descriptionOfEvent;
    public static final String channelID = "channelID";
    public static final String channelName = "event notification";

    private NotificationManager mManager;

    public NotificationHelper(Context base , String data) {
        super(base);
        int i = 0;
        for(i = 0 ; i < data.length() ; i ++){
            if(data.charAt(i) == '_')
                break;
        }
        nameOfEvent = data.substring(0,i);
        descriptionOfEvent = data.substring(i+1);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_HIGH);

        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification() {
        ParseUser tempUser = ParseUser.getCurrentUser();

        String names[] = tempUser.getString("nameOfEvents").split("\\s+");
        String descrip[] = tempUser.getString("descriptionOfEvents").split("\\s+");


        return new NotificationCompat.Builder(getApplicationContext(), channelID)
                .setContentTitle(names[0])
                .setContentText(descrip[0])
                .setSmallIcon(R.drawable.check);
    }
}
