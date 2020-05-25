package com.parse.starter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import static com.parse.starter.EventsManegerImportant.*;
import static com.parse.starter.HomeActivity.*;
import static com.parse.starter.MainActivity.user;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;



public class EventEditorActivity extends AppCompatActivity {
    int day,month,year,minut,hour;
    DatePicker datePicker;
    TimePicker timePicker ;
    TextView nameEditText;
    TextView descriptionEditText;
    String nameOfEvent,descriptionOfEvent;
    static Context contextEventEditor;
    static String newKey;
    public void addToHashMap(){
        String dayString,monthString,hourString, minutString;
        dayString = Integer.toString(day);
        monthString = Integer.toString(month);
        hourString = Integer.toString(hour);
        minutString = Integer.toString(minut);
        if (dayString.length() == 1){
            dayString = "0"+dayString;
        }
        if (monthString.length() == 1){
            monthString = "0" + monthString;
        }
        if (hourString.length() == 1)
                hourString = "0" + hourString;

        if(minutString.length() == 1)
            minutString = "0" + minutString;


        descriptionOfEvent = descriptionEditText.getText().toString();
        nameOfEvent = nameEditText.getText().toString();
        if(nameOfEvent.length() == 0 ||  descriptionOfEvent.length() == 0) {
            Toast.makeText(this, "name or description can not be empty!!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),EditEventsActivity.class);
            startActivity(intent);
            return;
        }
        int i = 1;
        String s = "";
        newKey = Integer.toString(year)+monthString+dayString+hourString+minutString;
        Log.i("time and name  together " ,newKey + "    "+ nameOfEvent + "     " + descriptionOfEvent );
        while(eventsDescriptionHashMap.get(nameOfEvent + s) != null) {
            s = "(" + Integer.toString(i) +")";
            i++;
        }
        nameOfEvent = nameOfEvent + s;
        eventsNameHashMap.put(newKey, nameOfEvent);
        eventsDescriptionHashMap.put(nameOfEvent, descriptionOfEvent);
        setHashToList();

    }


    public  void onButtonClicked(View view){
        day = datePicker.getDayOfMonth();
        month = datePicker.getMonth();
        year = datePicker.getYear();
        minut = timePicker.getMinute();
        hour = timePicker.getHour();
        addToHashMap();
        Intent intent = new Intent(getApplicationContext(),EditEventsActivity.class);
        startActivity(intent);


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_editor);
        contextEventEditor = this;

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        nameEditText = (TextView) findViewById(R.id.nameEditText);
        descriptionEditText = (TextView) findViewById(R.id.descriptionEditText);
        timePicker.setIs24HourView(true);
        datePicker.setMinDate(System.currentTimeMillis() - 1000);



    }
}
