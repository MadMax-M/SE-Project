package com.parse.starter;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.parse.starter.EventsManegerImportant.*;
import static com.parse.starter.MainActivity.name;
import static com.parse.starter.MainActivity.user;
import static com.parse.starter.SettingsActivity.*;
import static com.parse.starter.EditEventsActivity.*;
public class HomeActivity extends AppCompatActivity {
    static Map<String, String> eventsNameHashMap = new HashMap<>();
    static Map<String, String> eventsDescriptionHashMap = new HashMap<>();
    static ArrayList<String> eventsArrayList = new ArrayList<>();
    static ArrayList<String> eventsDescription = new ArrayList<>();
    static ArrayList<String> eventTime = new ArrayList<>();
    static ArrayList<String> completedSlots = new ArrayList<>();
    static ArrayList<String> slotsValue = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    ConstraintLayout constraintLayout;
    public void setAlarmAndNotifStatus(){
        int a,n;
        try {
            a = user.getInt("alarmStatus");
            n = user.getInt("notificationStatus");
        }
        catch (NullPointerException e){
            a = 0;
            n = 0;
        }
        switch (a){
            case 0:
                alarmStatus = true;
                break;
            case 1:
                alarmStatus = false;
                break;
            case 2:
                alarmStatus = true;
                break;
        }
        switch (n){
            case 0:
                notificationStatus = true;
                break;
            case 1:
                notificationStatus = false;
                break;
            case 2:
                notificationStatus = true;
                break;

        }
    }
    public void goToSubjectList(){
        Intent intent = new Intent(getApplicationContext(),SubjectsListActivity.class);
        startActivity(intent);
    }
    public void goToSettings(){
        Intent intent = new Intent(getApplicationContext(),SettingsActivity.class);
        startActivity(intent);
    }
    public void goToShareEvents(){
        Intent intent = new Intent(getApplicationContext(),ShareEventsActivity.class);
        startActivity(intent);
    }
    public void goToAttendence(){
        Intent intent = new Intent(getApplicationContext(),AttendenceActivity.class);
        startActivity(intent);
    }
    public void goToAddEvents(){
        Intent intent = new Intent(getApplicationContext(),EditEventsActivity.class);
        startActivity(intent);
    }
    public void goToLoggin(){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
    }
    public  void  goToNoteViewActivity(){
        Intent intent = new Intent(getApplicationContext(),NoteViewActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu_view,menu);
        return super.onCreateOptionsMenu(menu);
    }
    public void saveCompletedInParse(){

    }
    public void updateCompletedSlots(){
        Log.i("updateCompletedSlotsFN","I am in");
        completedSlots.clear();
        slotsValue.clear();
        try {
            String temp = user.getString("AskingAtttendence");
            if(temp.equals("null")){
                return;
            }
            String[] splited = temp.split("\\s+");
            int i,j,key;
            j = 0;
            String time = "";
            String slot;
            for (String st : splited) {
                slot = st.substring(0,1);
                key = Integer.parseInt(slot);
                switch (key) {
                    case 0:
                        time = "8 am class:  ";
                        break;
                    case 1:
                        time = "9 am class:  ";
                        break;
                    case 2:
                        time = "10 am class: ";
                        break;
                    case 3:
                        time = "11 am class: ";
                        break;
                    case 4:
                        time = "12 pm class: ";
                        break;
                    case 5:
                        time = "2 pm class:  ";
                        break;
                    case 6:
                        time = "3 pm class:  ";
                        break;
                    case 7:
                        time = "4 pm class:  ";
                        break;
                    case 8:
                        time = "5 pm class:  ";
                        break;
                }
                st = st.substring(1);
                for (i = 0; i < st.length(); i++) {
                    if (st.charAt(i) == '_') {
                        st = st.substring(0, i) + " " + st.substring(i + 1);
                    }
                }
                if(!st.equals("Break Time")) {
                    Log.i("these are the times :", st);
                    slotsValue.add(slot);
                    completedSlots.add(time + st);
                }
                j++;

            }
            Log.i("these are the times :", Integer.toString(j));
        }
        catch (NullPointerException e){
            Log.i("updateCompletedSlotsFN","null point exception");
            return;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);



        switch (item.getItemId()) {
            case R.id.settingsOption:
                goToSettings();
                return true;
            case R.id.shareOption:
                goToShareEvents();
                return true;
            case R.id.editTimeTableOption:
                goToSubjectList();
                return true;
            case R.id.attendenceOption:
                if(user.getString("subjectName") == null){
                    Toast.makeText(this, "please enter subjects first", Toast.LENGTH_SHORT).show();
                }
                else {
                    goToAttendence();
                }
                return true;
            case R.id.noteOption:
                if(user.getString("subjectName") == null){
                    Toast.makeText(this, "please enter subjects first", Toast.LENGTH_SHORT).show();
                }
                else {
                    goToNoteViewActivity();
                }
                return true;
            case R.id.addEventsOption:

                goToAddEvents();

                return true;
            case R.id.logoutOption:

                user.logOut();
                user.saveInBackground();
                goToLoggin();
                name.clear();
                eventsNameHashMap.clear();
                eventsDescriptionHashMap.clear();
                eventsArrayList.clear();
            default:
                return false;
            }

        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setTitle("Unmarked Classes");
        ///user.get("MondayS")
        getSavedEventData();
        setAlarmAndNotifStatus();
        if(!user.getString("alarm").equals("on") || alarmStatus == true ) {
            setMorningAlarm();
        }
        if(alarmStatus == false && user.getString("alarm").equals("on")){
            stopMorningAlarm();
        }
       // setTCalForAttendence();
        constraintLayout = (ConstraintLayout) findViewById(R.id.constraintLayout);

        checkAndDeletePast();
        updateCompletedSlots();
        Calendar calendar = Calendar.getInstance();
        ListView completedSlotsList = (ListView) findViewById(R.id.completedSlotsList);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, completedSlots);
        completedSlotsList.setAdapter(arrayAdapter);
        Calendar today = Calendar.getInstance();
       // getListFromParse(calendar);
        setTCalForAttendence();
        if(numberOfEvents != 0 ){
            setCalender();
        }
        completedSlotsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),RequestingAttendenceActivity.class);

                intent.putExtra("clicekItem", position);
                startActivity(intent);
            }
        });

    }



    public  void setTCalForAttendence(){
        Calendar cal = Calendar.getInstance();
        int i;
        cal.set(Calendar.HOUR_OF_DAY,18);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        if(Calendar.getInstance().getTimeInMillis()>cal.getTimeInMillis()){
            cal.setTimeInMillis(cal.getTimeInMillis() + 24*60*60*1000);
        }

        startTask(cal);
    }
    private  void startTask(Calendar cal){
        long i = cal.getTimeInMillis();
        long j = Calendar.getInstance().getTimeInMillis();
        Log.i("cal", Long.toString(cal.getTimeInMillis()));
        Log.i("cal", Long.toString(Calendar.getInstance().getTimeInMillis()));
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), TaskReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),0,intent,0);
        int ALARM_TYPE = AlarmManager.RTC;
        alarmManager.setRepeating(ALARM_TYPE,i, AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void getListFromParse(Calendar calendar) {
        int week = calendar.get(Calendar.DAY_OF_WEEK);

    }


    private void setCalender() {
        Calendar calendar = Calendar.getInstance();
        String date = eventTime.get(0);
        int month,year,day,hour,minut;
        year = Integer.parseInt(date.substring(0,4));
        month = Integer.parseInt(date.substring(4,6));
        day = Integer.parseInt(date.substring(6,8));
        hour = Integer.parseInt(date.substring(8,10));
        minut = Integer.parseInt(date.substring(10));
        calendar.set(year,month,day,hour,minut);
        String nameOfeve = eventsNameHashMap.get(date);
        String descOfeve = eventsDescriptionHashMap.get(nameOfeve);
         if(notificationStatus){
            startAlarm(calendar, nameOfeve , descOfeve);
        }
        else{
            cancelAlarm();
        }



    }

    private void startAlarm(Calendar cal, String nameOfEve , String descOfEve) {
        String mix = nameOfEve + "_" + descOfEve;
        Log.i("data is", mix);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);
        intent.putExtra("data" , mix);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,intent,0);
        int ALARM_TYPE = AlarmManager.RTC_WAKEUP;
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
         //   Log.i("  its me   :", "in 1");
         //   alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
       // }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.i("  its me   :", "in 2");
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        }
        else {
            Log.i("  its me   :", "in 3");
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        }

    }
    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
        //Toast.makeText(this, "alarm cancelled!!", Toast.LENGTH_SHORT).show();
    }



    public  void  setMorningAlarm() {



        if (user.getInt("alarmStatus") == 1) {
            return;
        }
        AlarmManager[] alarmManagers = new AlarmManager[5];
        String temp;
        int day;
        for(day = 2; day < 7 ; day++ ) {
            switch (day) {
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
            String data = user.getString(temp);
            String stored = "";
            String[] splited = data.split("\\s+");
            int i = 0;
            while (splited[i] == "Break_Time") {
                i++;
            }
            int hour;
            switch (i) {
                case 0:
                    hour = 7;
                    break;
                case 1:
                    hour = 8;
                    break;
                case 2:
                    hour = 9;
                    break;
                case 3:
                    hour = 10;
                    break;
                default:
                    hour = 10;

            }

            activateAlarmManager(alarmManagers, hour, day);
        }
    }

    private void activateAlarmManager(AlarmManager[] alarmManagers, int hour, int day) {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, day);
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        long timeInMills = cal.getTimeInMillis();
        cal.setTimeInMillis( timeInMills + 24*7*60*60*1000);
        String show=  Integer.toString(cal.get(Calendar.DAY_OF_MONTH))+ " " + Integer.toString(cal.get(Calendar.MONTH))+ " "+ Integer.toString(day);
        Log.i("day :", show );

        alarmManagers[day - 2] = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlarmReceiver.class);
        user.put("alarm", "on");
        user.saveInBackground();
        intent.putExtra("name", day);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,day,intent,PendingIntent.FLAG_UPDATE_CURRENT);

       //long timeInMills = cal.getTimeInMillis();
        alarmManagers[day - 2].setRepeating(AlarmManager.RTC_WAKEUP,timeInMills, AlarmManager.INTERVAL_DAY*7, pendingIntent);
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Log.i("  its me   :", "in 2");
            alarmManagers[i].setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        }
        else {
            Log.i("  its me   :", "in 3");
            alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);
        }*/
    }
    private void stopMorningAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        int i;
        for(i = 2; i < 7 ; i++) {
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
        }
        user.put("alarm", "off");
        user.saveInBackground();
        Toast.makeText(this, "Alarm turned off", Toast.LENGTH_SHORT).show();
    }


}





