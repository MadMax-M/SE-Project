package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import static com.parse.starter.MainActivity.user;

public class ShareEventsActivity extends AppCompatActivity {
    int noOfRequest;

    public  void viewAllRequestClicked(View view){
        Intent intent = new Intent(getApplicationContext(),AllEventsRequestsActivity.class);
        startActivity(intent);
    }
    public  void sendRequestClicked(View view){

        Intent intent = new Intent(getApplicationContext(),SendEventActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_events);

        setTitle("share events");
        TextView textView = findViewById(R.id.requestTextView);

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("SharedEvents");
        query.whereEqualTo("receiver", user.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    noOfRequest = objects.size();
                    textView.setText("you have "+Integer.toString(noOfRequest)+" new event requests");
                }

            }

        });

    }
}
