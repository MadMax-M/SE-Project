package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import static com.parse.starter.MainActivity.user;
import static com.parse.starter.SubjectsListActivity.noOfSubjects;
import static com.parse.starter.TimeTableActivity.dayKey;

import java.util.ArrayList;
import static com.parse.starter.MainActivity.name;
import static com.parse.starter.GettingSlotsActivity.slots;

public class SelectSubjectActivity extends AppCompatActivity {
    ArrayList<String> copiedName = new ArrayList<>(name);
    String temp;
    static String[] mondaySlots = new String[9];
    static String[] tuesdaySlots = new String[9];
    static String[] wednesdaySlots = new String[9];
    static String[] thursdaySlots = new String[9];
    static String[] fridaySlots = new String[9];

    private static void arrayToStingOfSlot(String[] dayName, String temp){
        temp = temp + "Slots";
        int k,j;
        String st,data;
        data = "";
        for(k=0;k<9;k++){
            st = dayName[k];
            for(j=0;j<st.length();j++)
                if(st.charAt(j) == ' ')
                    st = st.substring(0,j)+ "_" + st.substring(j+1);
            data = data +" "+ st;
        }
        data =data.substring(1);
        Log.i("this is string",data);
        user.put(temp, data);
        user.saveInBackground();


    }
    private void fillEmptySpots(String[] temp){
        for(int i= 0 ; i<9 ; i++){
            if(temp[i] == null){
                temp[i] = "Break Time";
            }
        }
    }
    private  void fillSlotInArray(String[] dayName, int slotID, int i){
        dayName[slotID] = copiedName.get(i);
        fillEmptySpots(dayName);
        String temp;
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
            default:
                temp = "";
        }
        temp = temp + "Slots";
        arrayToStingOfSlot(dayName,temp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_subject);
        Intent intent = getIntent();

        final int slotId =  intent.getIntExtra("slotId",-1);
        final String slotTemp = slots.get(slotId);
        ListView subjectsListView = (ListView) findViewById(R.id.subjectListView);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.select_dialog_singlechoice,copiedName);
        if(copiedName.size() == noOfSubjects)
            copiedName.add("Break Time");
        subjectsListView.setAdapter(arrayAdapter);
        subjectsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GettingSlotsActivity.slots.set(slotId,slotTemp + ":     "+ copiedName.get(i));
                GettingSlotsActivity.arrayAdapter.notifyDataSetChanged();
                /*switch (dayKey) {
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
                switch (slotId){
                    case 0:
                        temp = temp + "Slot1";
                        break;
                    case 1:
                        temp = temp + "Slot2";
                        break;
                    case 2:
                        temp = temp + "Slot3";
                        break;
                    case 3:
                        temp = temp + "Slot4";
                        break;
                    case 4:
                        temp = temp + "Slot5";
                        break;
                    case 5:
                        temp = temp + "Slot6";
                        break;
                    case 6:
                        temp = temp + "Slot7";
                        break;

                    case 7:
                        temp = temp + "Slot8";
                        break;
                    case 8:
                        temp = temp + "Slot9";
                        break;

                }*/
                switch (dayKey) {
                    case 0:
                        temp = "monday";
                        fillSlotInArray(mondaySlots,slotId,i);
                        break;
                    case 1:
                        temp = "tuesday";
                        fillSlotInArray(tuesdaySlots,slotId,i);
                        break;
                    case 2:
                        temp = "wednesday";
                        fillSlotInArray(wednesdaySlots,slotId,i);
                        break;
                    case 3:
                        temp = "thursday";
                        fillSlotInArray(thursdaySlots, slotId,i);
                        break;
                    case 4:
                        temp = "friday";
                        fillSlotInArray(fridaySlots,slotId,i);
                        break;
                }
                //user.put( temp, copiedName.get(i));
                //user.saveInBackground();
                Toast.makeText(getApplicationContext(),temp,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),GettingSlotsActivity.class);
                intent.putExtra("slotId", i);

                startActivity(intent);

            }
        });



    }
}
