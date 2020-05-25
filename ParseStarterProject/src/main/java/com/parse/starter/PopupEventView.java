package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;
import static com.parse.starter.EventsManegerImportant.*;
import static com.parse.starter.HomeActivity.*;


public class PopupEventView extends AppCompatActivity {
    TextView nameDataView;
    TextView dateDataView;
    TextView timeDataView;
    TextView descriptionDataView;

    private void setPopupTextView(int position){
        String n,t,d;
        n = eventsArrayList.get(position);
        t = getKey(eventsNameHashMap, n);
        d = eventsDescriptionHashMap.get(n);
        nameDataView.setText(n);
        String month;
        month = Integer.toString(Integer.parseInt(t.substring(4,6))+1);
        if (month.length()==1)
            month = "0"+month;
        dateDataView.setText(t.substring(6,8) + "-"+month+ "-" + t.substring(0,4));
        timeDataView.setText(t.substring(8,10)+ ":" + t.substring(10));
        descriptionDataView.setText(eventsDescriptionHashMap.get(n));



    }
    /*    public void setHashToList(){
        String day,month,year,minut,hour;
        Set<Long> keys = eventsMap.keySet();
        events.clear();
        for(long i : keys){
            events.add(eventsMap.get(i));
            //y0y1y2y3m4m5d6d7h8h8m9m10
            year = Long.toString(i).substring(0,4);
            month = Long.toString(i).substring(4,6);
            day = Long.toString(i).substring(6,8);
            hour = Long.toString(i).substring(8,10);
            minut = Long.toString(i).substring(10);



        }


    }*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_event_view);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width* 0.8 ), (int)(height * 0.4));


        nameDataView = (TextView) findViewById(R.id.data1);
        dateDataView = (TextView) findViewById(R.id.data2);
        timeDataView = (TextView) findViewById(R.id.data3);
        descriptionDataView = (TextView) findViewById(R.id.data4);
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
