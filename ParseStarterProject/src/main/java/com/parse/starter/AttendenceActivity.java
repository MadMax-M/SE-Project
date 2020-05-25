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
import java.util.ArrayList;
import java.util.List;

public class AttendenceActivity extends AppCompatActivity {
    static ArrayList<String> attendenceArrayList =new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendence);
        setTitle("Your Attendence");
        ListView attendenceListView = (ListView) findViewById(R.id.attendenceListView);

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,attendenceArrayList);
        if(attendenceArrayList.size() == 0)
            fillArrayList();
        for(String st : attendenceArrayList){
            Log.i("Its some thing ", st);
        }
        attendenceListView.setAdapter(arrayAdapter);

        attendenceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(),PopupAttendenceActivity.class);
                intent.putExtra("position", i);
                startActivity(intent);

            }
        });
        
    }

    private void fillArrayList() {
        String data = user.getString("subjectName");

        String[] splited = data.split("\\s+");
        int i;
        for(String st : splited) {
            for(i = 0; i<st.length(); i++) {
                if (st.charAt(i) == '_')
                    st = st.substring(0, i) + " " + st.substring(i + 1);
            }
            attendenceArrayList.add(st);

        }
    }
}
