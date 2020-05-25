package com.parse.starter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.Calendar;

import static com.parse.starter.MainActivity.user;

    public  class TaskReceiver extends BroadcastReceiver {




    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar al = Calendar.getInstance();
        String temp= "";
        Log.i("Calender set" , "i m in for attendenceee");
        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.DAY_OF_WEEK);
        switch (week){
            case 2:
                temp = "monday";
                break;
            case 3:
                temp = "tuesday";
                break;
            case 4:
                temp = "wednesday";
                break;
            case 5:
                temp = "thursday";
                break;
            case 6:
                temp = "friday";
                break;
            default:
                return;
        }
        temp = temp + "SlotsSlots";
        ParseUser user1 = ParseUser.getCurrentUser();
        Log.i("temp", temp);
        String data = "";
        try {
            data = user1.getString(temp);
        }
        catch (Exception e){
            e.printStackTrace();
            return;
        }
        String stored = "";
        String[] splited = data.split("\\s+");
        for(int i = 0; i < 9 ; i ++){
            splited[i] = Integer.toString(i)+splited[i];
        }
        String st = "";
        for(int i = 0; i< 9 ; i++){
            st = st + " " + splited[i];
        }
    st = st.substring(1);
        Log.i("Calender set" , st);

        try {
            stored = user1.getString("AskingAtttendence");
        }
        catch (NullPointerException e){
            stored = "";
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(stored.equals("") || stored == null || stored.equals("null")) {
            Log.i("Calender set" , "stored is null");
            stored = st;
        }
        else {
            Log.i("Calender set" , "stored is not null");
            stored = stored + " " + st;
        }
        if(stored.charAt(0) == ' ')
            stored = stored.substring(1);
        user1.put("AskingAtttendence",stored);
        user1.saveInBackground();
        Toast.makeText(context, "data Recieved", Toast.LENGTH_SHORT).show();


    }

}
