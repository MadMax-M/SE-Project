package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.ParseException;

import static com.parse.starter.AllEventsRequestsActivity.*;
import static com.parse.starter.HomeActivity.*;

import static com.parse.starter.EventsManegerImportant.getSavedEventData;
import static com.parse.starter.EventsManegerImportant.setHashToList;

public class PopupEventRequestActivity extends AppCompatActivity {
    TextView nameDataView;
    TextView dateDataView;
    TextView timeDataView;
    TextView descriptionDataView;
    TextView sender;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_event_request);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width* 0.8 ), (int)(height * 0.4));

        sender = (TextView) findViewById(R.id.data1);
        nameDataView = (TextView) findViewById(R.id.data2);
        dateDataView = (TextView) findViewById(R.id.data3);
        timeDataView = (TextView) findViewById(R.id.data4);
        descriptionDataView = (TextView) findViewById(R.id.data5);

        Intent intent = getIntent();
        int position =  intent.getIntExtra("position",-1);

        if(position < 0){
            Log.i(" error ", " positon value is less than zero");
            Toast.makeText(this, "positon value is less than zero", Toast.LENGTH_SHORT).show();
        }
        else {
            setPopupTextView(position);

        }
        i = position;
        findViewById(R.id.ignoreButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendersObjectList.get(i).deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            requestEventDescription.remove(i);
                            requestEventName.remove(i);
                            requestedBy.remove(i);
                            requestEventTime.remove(i);
                            sendersObjectList.remove(i);
                            Intent intent = new Intent(getApplicationContext(),AllEventsRequestsActivity.class);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(PopupEventRequestActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),AllEventsRequestsActivity.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSavedEventData();
                int j = 1;

                String s = "";
                String nameOfEvent = requestEventName.get(position);
                while(eventsDescriptionHashMap.get(nameOfEvent + s) != null) {
                    s = "(" + Integer.toString(j) +")";
                    j++;
                }
                nameOfEvent = nameOfEvent + s;
                eventsNameHashMap.put(requestEventTime.get(position), nameOfEvent);
                eventsDescriptionHashMap.put(nameOfEvent, requestEventDescription.get(position));
                setHashToList();

                /*eventsNameHashMap.put(requestEventTime.get(position),requestEventName.get(position));
                eventsDescriptionHashMap.put(requestEventName.get(position),requestEventDescription.get(position));
                setHashToList();*/
                Intent intent = new Intent(getApplicationContext(),AllEventsRequestsActivity.class);
                startActivity(intent);
                sendersObjectList.get(i).deleteInBackground(new DeleteCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null){
                            requestEventDescription.remove(i);
                            requestEventName.remove(i);
                            requestedBy.remove(i);
                            requestEventTime.remove(i);
                            sendersObjectList.remove(i);
                            adapter.notifyDataSetChanged();

                        }
                        else{
                            Toast.makeText(PopupEventRequestActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                });


            }
        });
    }

    private void setPopupTextView(int position) {
        String date,time;
        date = requestEventTime.get(position);
        String month = date.substring(4,6);
        month = Integer.toString(Integer.parseInt(month)+1);
        if(month.length() == 1)
            month = "0"+month;
        time =  date.substring(8,10)+ ":"+date.substring(10,12);
        date =  date.substring(6,8)+ "-" + month + "-" +date.substring(0,4);
        sender.setText(requestedBy.get(position));
        nameDataView.setText(requestEventName.get(position));
        dateDataView.setText(date);
        timeDataView.setText(time);
        descriptionDataView.setText(requestEventDescription.get(position));
    }
}
