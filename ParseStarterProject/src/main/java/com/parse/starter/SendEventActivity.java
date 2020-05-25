package com.parse.starter;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


import static com.parse.starter.HomeActivity.*;
import static com.parse.starter.MainActivity.user;

public class SendEventActivity extends AppCompatActivity {
    ParseObject sharedEvents;
    Spinner spinner;
    AutoCompleteTextView inputUserName;
    String events[] = new String[eventTime.size()];
    ArrayList<String> otherUsers = new ArrayList<>();
    ArrayAdapter <String> adapter;
    ArrayAdapter adapterAuto;
    boolean selected = false;
    int num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_event);
        setTitle("Send Event");
        sharedEvents = new ParseObject("SharedEvents");

        Button sendButton = (Button) findViewById(R.id.sendButton);
        Button cancelSharingdButton = (Button) findViewById(R.id.cancelSharingButton);
        spinner = (Spinner) findViewById(R.id.spinner);
        inputUserName = (AutoCompleteTextView) findViewById(R.id.friendUserEditText);
        setEvents();
        adapterAuto = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, otherUsers);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, events);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                num = position;
                selected = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selected = false;
            }
        });
        ParseQuery<ParseUser> query = user.getQuery();
        query.whereNotEqualTo("username",user.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {
                if(e==null){
                    otherUsers.clear();
                    if(objects.size() != 0){
                        for(ParseUser others : objects){
                            otherUsers.add(others.getUsername());
                        }
                    }
                }
            }
        });
        inputUserName.setAdapter(adapterAuto);

        cancelSharingdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!otherUsers.contains(inputUserName.getText().toString())){
                    Toast.makeText(getApplicationContext(), "user not found", Toast.LENGTH_SHORT).show();
                }
                else {
                    sharedEvents.put("sender", user.getCurrentUser().getUsername());
                    sharedEvents.put("receiver", inputUserName.getText().toString());
                    sharedEvents.put("eventTime", eventTime.get(num));
                    sharedEvents.put("eventName", eventsNameHashMap.get(eventTime.get(num)));
                    sharedEvents.put("eventDescription", eventsDescriptionHashMap.get(eventsNameHashMap.get(eventTime.get(num))));
                    sharedEvents.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e == null) {
                                Toast.makeText(SendEventActivity.this, "done!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                                startActivity(intent);
                            }

                            else
                                Toast.makeText(SendEventActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    private void setEvents() {
        int i = 0;
        for(String s : eventTime){
            String month = s.substring(4,6);
            month = Integer.toString(Integer.parseInt(month)+1);
            if(month.length() == 1)
                month = "0"+month;
            events[i] = s.substring(8,10)+ ":"+s.substring(10,12)+ ", " + s.substring(6,8)+ "-" +
                    month + "-" +s.substring(0,4) + ":  "+eventsNameHashMap.get(s);
            i++;
        }
    }
}
