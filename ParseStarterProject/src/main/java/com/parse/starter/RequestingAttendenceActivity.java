package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.Calendar;

import static com.parse.starter.HomeActivity.completedSlots;
import static com.parse.starter.HomeActivity.arrayAdapter;

import static com.parse.starter.HomeActivity.slotsValue;
import static com.parse.starter.MainActivity.user;

public class RequestingAttendenceActivity extends AppCompatActivity {
    int num;
    String shortNote;
    TextView subjectTextView;
    RadioGroup radioGroup;
    RadioButton radioButton;
    EditText shortNoteEditText;
    String name = "";
    public void onRadioButtonClicked(View view){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        Toast.makeText(this, "you were " + radioButton.getText()  , Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requesting_attendence);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        subjectTextView = (TextView)findViewById(R.id.textView5);
        shortNoteEditText = (EditText) findViewById(R.id.editText) ;
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width* 0.8 ), (int)(height * 0.4));

        Intent intent = getIntent();
        num = intent.getIntExtra("clicekItem",-1);
        try {
            name = completedSlots.get(num);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        subjectTextView.setText(name.substring(13));
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                String key = radioButton.getTag().toString();
                Toast.makeText(RequestingAttendenceActivity.this, key, Toast.LENGTH_SHORT).show();
                name = completedSlots.get(num);
                name = name.substring(13);
                String t = user.getString("subjectName");
                String[] splited = t.split("\\s+");
                int i,j;
                j = 0;
                shortNote = shortNoteEditText.getText().toString();
                if(shortNote.length() != 0) {
                    for (i = 0; i < shortNote.length(); i++) {
                        if (shortNote.charAt(i) == ' ')
                            shortNote = shortNote.substring(0, i) + "_" + shortNote.substring(i + 1);
                        else if (shortNote.charAt(i) == '\n') {
                            shortNote = shortNote.substring(0, i) + "#" + shortNote.substring(i + 1);
                        }
                    }
                    Calendar calendar = Calendar.getInstance();
                    String day, month;
                    day =  Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
                    month = Integer.toString(calendar.get(Calendar.MONTH));
                    if(day.length() == 1)
                        day = "0"+day;
                    if(month.length()==1)
                        month = "0"+month;
                    String time =day + month + Integer.toString(calendar.get(Calendar.YEAR)) + ":";
                    String noteDescription = name;
                    for (i = 0; i < noteDescription.length(); i++) {
                        if (noteDescription.charAt(i) == ' ')
                            noteDescription = noteDescription.substring(0, i) + "_" + noteDescription.substring(i + 1);

                    }
                    noteDescription = time + noteDescription;
                    String desc,note;
                    desc = "";
                    note = "";
                    try {
                        note = user.getString("note");
                        desc = user.getString("noteDescription");
                    }
                    catch (NullPointerException e){
                        note = "";
                        desc = "";
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    if(desc.equals("null") || note.equals("null")) {
                        desc = "";
                        note = "";
                    }
                    note = note +" "+ shortNote;
                    desc = desc +" "+ noteDescription;
                    if(desc.charAt(0) == ' '){
                        note = note.substring(1);
                        desc = desc.substring(1);
                    }
                    user.put("note", note);
                    user.put("noteDescription", desc);
                    user.saveInBackground();
                }

                Log.i("name is", name + Integer.toString(name.length()));
                for (String temp : splited) {
                    for (i = 0; i < temp.length(); i++) {
                        if (temp.charAt(i) == '_') {
                            temp = temp.substring(0, i) + " " + temp.substring(i+1);
                            splited[j] = temp;
                            //Toast.makeText(this,"i am in",Toast.LENGTH_LONG).show();
                        }

                    }
                    Log.i("values of splited j", splited[j]+ Integer.toString(splited[j].length()));
                    if(splited[j].equals(name)){
                        Log.i("i am in", splited[j]);
                        break;
                    }
                    j++;

                }

                String temp = user.getString("totalClasses");
                String[] splited1 = temp.split("\\s+");
                temp = user.getString("totalPresent");
                String[] splited2 = temp.split("\\s+");
                int totalClasses = Integer.parseInt(splited1[j] ) +1;
                int attended = Integer.parseInt(splited2[j]);
                if(key .equals("1") )
                    attended++;
                splited1[j] = Integer.toString(totalClasses);
                splited2[j] = Integer.toString(attended);
                while (splited1[j].length() < 3 ){
                    splited1[j] = "0" +splited1[j];
                }
                while (splited2[j].length() < 3 ){
                    splited2[j] = "0" +splited2[j];
                }
                j = 0;
                String temp1 , temp2;
                temp1 = "";
                temp2 ="";
                for(String st : splited1){
                    temp1 = temp1 +" "+ st;
                    temp2 = temp2 +" "+ splited2[j];
                    j++;
                }
                temp1  = temp1.substring(1);
                temp2  = temp2.substring(1);
                user.put("totalClasses", temp1);
                user.put("totalPresent",temp2);
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null){

                            completedSlots.remove(num);
                            slotsValue.remove(num);
                            arrayAdapter.notifyDataSetChanged();
                            String temp = "";
                            String temp1;
                            int i,j;
                            for(i = 0; i < completedSlots.size(); i++){
                                temp1 = completedSlots.get(i).substring(13);
                                for(j = 0; j< temp1.length() ; j++){
                                    if(temp1.charAt(j) == ' '){
                                        temp1 = temp1.substring(0,j) + "_" + temp1.substring(j+1);
                                    }
                                }

                                temp = temp + " " +slotsValue.get(i)+temp1;
                            }
                            if(temp.length() == 0){
    //                            temp = "null";
                            }
                            else {
                                temp = temp.substring(1);
                            }
                            user.put("AskingAtttendence", temp);
                            user.saveInBackground();
                        }
                        else
                            Log.i("some thing went wrong", e.getMessage());
                    }

                });

                finish();
                //Intent intent2 = new Intent(getApplicationContext(),HomeActivity.class);
                //startActivity(intent2);


            }
        });

    }
}

