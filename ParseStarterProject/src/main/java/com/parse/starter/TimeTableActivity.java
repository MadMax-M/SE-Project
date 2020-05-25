package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.*;
import android.widget.Toast;

public class TimeTableActivity extends AppCompatActivity {
    Button mondayButton;
    Button tuesdayButton;
    Button wednesdayButton;
    Button thursdayButton;
    Button fridayButton;
    Button saveTimeTable;
    Button backTobackToSubjectListButton;
    static int dayKey;

    public void setBackTobackToSubjectListButton(View view){
        Intent intent = new Intent(getApplicationContext(),SubjectsListActivity.class);
        startActivity(intent);
        return;
    }
    public void goToHome(View view){
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
    }


    public void onDayButtonClicked (View view){
        int id = view.getId();
        switch (id){
            case R.id.mondayButton:
                dayKey = 0;
                break;
            case R.id.tuesdayButton:
                dayKey = 1;
                break;
            case R.id.wednesdayButton:
                dayKey = 2;
                break;
            case R.id.thursdayButton:
                dayKey = 3;
                break;
            case R.id.fridayButton:
                dayKey = 4;
                break;

        }
        Toast.makeText(this,"key is "+Integer.toString(dayKey), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),GettingSlotsActivity.class);
        startActivity(intent);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);
        mondayButton = (Button) findViewById(R.id.mondayButton);
        tuesdayButton = (Button) findViewById(R.id.tuesdayButton);
        wednesdayButton = (Button) findViewById(R.id.wednesdayButton);
        thursdayButton = (Button) findViewById(R.id.thursdayButton);
        fridayButton = (Button) findViewById(R.id.fridayButton);
        saveTimeTable = (Button) findViewById(R.id.saveTimeTable);
        backTobackToSubjectListButton = (Button) findViewById(R.id.backToSubjectListButton);

    }
}
