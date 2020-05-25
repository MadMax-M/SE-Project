package com.parse.starter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.parse.starter.HomeActivity.eventsArrayList;
import static com.parse.starter.HomeActivity.*;
import static com.parse.starter.MainActivity.user;
import static com.parse.starter.EventsManegerImportant.*;
import static com.parse.starter.EventEditorActivity.*;

public class EditEventsActivity extends AppCompatActivity {



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
        setContentView(R.layout.activity_edit_events);
        setTitle("Events");
        ListView eventsListView = (ListView) findViewById(R.id.eventsListView);
        arrayAdapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, eventsArrayList);
        getSavedEventData();
        checkAndDeletePast();
        eventsListView.setAdapter(arrayAdapter1);

       eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           @Override
           public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               Intent intent = new Intent(getApplicationContext(),PopupEventView.class);
               intent.putExtra("position", i);
               startActivity(intent);

           }
       });


        eventsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int itemToDelete  = position;
                new AlertDialog.Builder(EditEventsActivity.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Are you sure?")
                        .setMessage("Want to delete this subject?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //eventsArrayList.remove(itemToDelete);
                                //arrayAdapter.notifyDataSetChanged();
                                String t,n;
                                n = eventsArrayList.get(itemToDelete);
                                t = getKey(eventsNameHashMap, n);
                                eventsNameHashMap.remove(t);
                                eventsDescriptionHashMap.remove(n);
                                flag = "deleted";
                                setHashToList();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });




        findViewById(R.id.addEventButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),EventEditorActivity.class);
                startActivity(intent);



            }
        });
    }
}
