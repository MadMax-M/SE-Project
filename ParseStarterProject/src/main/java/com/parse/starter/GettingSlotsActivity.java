package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.*;
import android.widget.Toast;
import static com.parse.starter.MainActivity.user;
import java.util.ArrayList;
import static com.parse.starter.SelectSubjectActivity.*;

import static com.parse.starter.TimeTableActivity.dayKey;

public class GettingSlotsActivity extends AppCompatActivity {
    public static ArrayAdapter arrayAdapter;
    static ArrayList<String>slots = new ArrayList<>();
    public void addSlots(){
        int i;
        String current;
        String temp = "";
        switch (dayKey) {
            case 0:
                temp = "monday";
                break;
            case 1:
                temp = "tuesday";
                break;
            case 2:
                temp = "wednesday";
                break;
            case 3:
                temp = "thursday";
                break;
            case 4:
                temp = "friday";
                break;
        }

        try {
            current = user.getString(temp +"SlotsSlots");
            Log.i("this is what i got",temp+current);
            String[] splited = new String[9];
            int k,j;
            j = 0;
            if(current != null) {
                splited = current.split("\\s+");
                for(String st : splited){
                    for(k=0;k < st.length();k++){
                        if(st.charAt(k) == '_')
                            st = st.substring(0,k) + " " + st.substring(k+1);

                    }
                    splited[j] = st;
                    Log.i("this is what i got",Integer.toString(j) + " "+ st);
                    j++;
                }
            }
            else{
                for(k = 0; k < 9 ; k++)
                    splited[k] = "Break Time";
            }
            switch (dayKey) {
                case 0:
                    mondaySlots = splited;
                    break;
                case 1:
                    tuesdaySlots = splited;
                    break;
                case 2:
                    wednesdaySlots = splited;
                    break;
                case 3:
                    thursdaySlots = splited;
                    break;
                case 4:
                    fridaySlots = splited;
                    break;
            }
            slots.clear();

            for (i = 0; i < 9; i++) {
                current = splited[i];

                if (i < 4){
                    slots.add(Integer.toString(i + 8)+"am to " + Integer.toString(i + 8)+":55am" + "  "+ current);
                }
                else if (i == 4){
                    slots.add(Integer.toString(i + 8)+"pm to " + Integer.toString(i + 8)+":55pm"  + "  "+ current);
                }
                else {
                    slots.add(Integer.toString(i - 3)+"pm to " + Integer.toString(i - 3)+":55pm"  + "  "+ current);
                }
            }
        }
        catch (Exception e){
            Log.i("I got an exception  ", e.getMessage());
            e.printStackTrace();
            if (e != null){
                slots.clear();
                for (i = 8; i < 12; i++){
                    current = Integer.toString(i);
                    slots.add(current+"am to " + current+":55am");
                }
                current = Integer.toString(12);
                slots.add(current+"pm to " + current+":55pm");

                for(i = 2; i < 6 ; i++ ){
                    current = Integer.toString(i);
                    slots.add(current+"pm to " + current+":55pm");
                }
            }
        }

    }
    public void backButtonClicked(View  view){
        Intent intent = new Intent(getApplicationContext(),TimeTableActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_slots);
        ListView slotsListView = (ListView) findViewById(R.id.slotsListView);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,slots);
        addSlots();
        slotsListView.setAdapter(arrayAdapter);
        slotsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),SelectSubjectActivity.class);
                if(slots.get(i).length()>16){
                    if (i < 4){
                        slots.set(i,Integer.toString(i + 8)+"am to " + Integer.toString(i + 8)+":55am");
                    }
                    else if (i == 4){
                        slots.set(i,Integer.toString(i + 8)+"pm to " + Integer.toString(i + 8)+":55pm");
                    }
                    else {
                        slots.set(i,Integer.toString(i - 3)+"pm to " + Integer.toString(i - 3)+":55pm");
                    }
                }
                intent.putExtra("slotId", i);
                Toast.makeText(getApplicationContext(),Integer.toString(i),Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

    }
}
