package com.parse.starter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int data = intent.getIntExtra("name",9);
        Toast.makeText(context, Integer.toString(data), Toast.LENGTH_SHORT).show();
        MediaPlayer mediaPlayer = MediaPlayer.create(context, Settings.System.DEFAULT_RINGTONE_URI);
        mediaPlayer.start();
    }
}
