package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import static com.parse.starter.MainActivity.user;

public class AllEventsRequestsActivity extends AppCompatActivity {
    ListView requestsListView;
    static ArrayAdapter adapter;
    static ArrayList<String> requestEventName = new ArrayList<>();
    static ArrayList<String> requestEventTime = new ArrayList<>();
    static ArrayList<String> requestEventDescription = new ArrayList<>();
    static ArrayList<String> requestedBy = new ArrayList<>();
    static ArrayList<ParseObject> sendersObjectList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_events_requests);
        requestsListView = (ListView) findViewById(R.id.requestsListView);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,requestEventName);
        requestsListView.setAdapter(adapter);
        setTitle("All requests");
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("SharedEvents");
        query.whereEqualTo("receiver", user.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e == null){
                    requestEventDescription.clear();
                    requestEventName.clear();
                    requestedBy.clear();
                    requestEventTime.clear();
                    sendersObjectList.clear();
                    if(objects.size()!= 0){
                        for(ParseObject ob : objects){
                            requestEventTime.add(ob.getString("eventTime"));
                            requestEventDescription.add(ob.getString("eventDescription"));
                            requestEventName.add(ob.getString("eventName"));
                            requestedBy.add(ob.getString("sender"));
                            sendersObjectList.add(ob);

                        }
                        adapter.notifyDataSetChanged();

                    }
                }
                else
                    Toast.makeText(AllEventsRequestsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),PopupEventRequestActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);

            }
        });

    }
}
