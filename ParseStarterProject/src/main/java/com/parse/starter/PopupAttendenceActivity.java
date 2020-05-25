package com.parse.starter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;
import static com.parse.starter.MainActivity.user;
import static com.parse.starter.AttendenceActivity.attendenceArrayList;

import static com.parse.starter.HomeActivity.*;

import static com.parse.starter.EventsManegerImportant.getKey;

public class PopupAttendenceActivity extends AppCompatActivity {
    TextView totalClasses;
    TextView classAttended;
    TextView attendencePercentage;
    TextView subjectName;
    private void setPopupTextView(int position){
        subjectName.setText(attendenceArrayList.get(position));
        String temp = user.getString("totalClasses");
        String[] splited = temp.split("\\s+");
        totalClasses.setText(splited[position]);
        temp = user.getString("totalPresent");
        String[] splited2 = temp.split("\\s+");
        classAttended.setText(splited2[position]);

        int p,t;
        double per;
        p = Integer.parseInt(splited2[position]);
        t = Integer.parseInt(splited[position]);
        if(t != 0) {
            per = (double) p / t;
            per = per * 100.0;
            double roundOff = Math.round(per * 100.0) / 100.0;
            attendencePercentage.setText(Double.toString(roundOff) + "%");
            /*
            if (roundOff > 80) {
                attendencePercentage.setBackgroundColor(Color.GREEN);
            } else if (roundOff < 80 && roundOff > 70)
                attendencePercentage.setBackgroundColor(Color.BLUE);
            else
                attendencePercentage.setBackgroundColor(Color.RED);
            */
        }
        else{
            attendencePercentage.setText("---");
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_attendence);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width* 0.8 ), (int)(height * 0.4));
        totalClasses = (TextView) findViewById(R.id.totalClasses);
        classAttended = (TextView)findViewById(R.id.classesAttended) ;
        attendencePercentage  = (TextView) findViewById(R.id.attendencePercentage);
        subjectName = (TextView) findViewById(R.id.subjectName);



        Intent intent = getIntent();
        int position =  intent.getIntExtra("position",-1);

        if(position < 0){
            Log.i(" error ", " positon value is less than zero");
        }
        else {
            setPopupTextView(position);

        }
    }
}
