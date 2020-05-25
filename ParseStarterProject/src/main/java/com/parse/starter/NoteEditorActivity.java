package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static com.parse.starter.MainActivity.user;
import static com.parse.starter.MainActivity.name;

public class NoteEditorActivity extends AppCompatActivity {
    EditText editTextSubjectName;
    Button buttonSaveSubjectName;

    public void goToSubjectList(){
        Intent intent = new Intent(getApplicationContext(),SubjectsListActivity.class);
        startActivity(intent);
    }
    private  void setAttendenceSlot(){
        String totalClasses,totalPresent;
        Log.i("attendence status", "in setAttendence method");
        try{
            totalClasses = user.getString("totalClasses");
            totalPresent = user.getString("totalPresent");
            Log.i("attendence status", totalClasses);

        }
        catch (NullPointerException e){
            totalClasses = "";
            totalPresent = "";
            Log.i("attendence status", "its a null point exception");
        }
        catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.i("attendence status", "its some other exception");
            return;
        }
        Log.i("attendence status", "all is well");
        if(totalClasses == "" ){
            user.put("totalClasses" , "000");
            user.put("totalPresent" , "000");
        }
        else {

            user.put("totalClasses", totalClasses + " " + "000");
            user.put("totalPresent", totalPresent + " " + "000");
        }
        user.saveInBackground();
    }



    public void saveSubjectButtonClickecd(View view){
        Log.i("enterd text is   : ",editTextSubjectName.getText().toString());
        int totalSub = 0;
        try {
            totalSub = user.getInt("noOfSubjects");
        }
        catch (NullPointerException e){
            totalSub = 0;

        }
        catch ( Exception e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            return;
        }
        String temp = editTextSubjectName.getText().toString();
        if(temp.length() == 0) {
            Toast.makeText(getApplicationContext(), "no zero length  string", Toast.LENGTH_LONG).show();
            goToSubjectList();
            return;
        }


        int i;
        for (i = 0;i<temp.length();i++){
            if(temp.charAt(i) == '_'){
                Toast.makeText(getApplicationContext(),"Error, do not use '_'",Toast.LENGTH_LONG).show();
                goToSubjectList();
                return;
            }
            else if(temp.charAt(i) == ' '){
                temp = temp.substring(0,i)+"_"+temp.substring(i+1);
            }
        }
        if (totalSub == 0) {
            try {
                Log.i("there is a problem",temp);
                user.put("subjectName", temp);
                user.put("noOfSubjects",totalSub+1);
            }
            catch (Exception e){
                e.printStackTrace();
                Log.i("there is a problem"," cant save data in parse");
                return;
            }

        }
        else {
            user.put("subjectName", user.getString("subjectName")+" "+temp);
            user.put("noOfSubjects",totalSub+1);
        }
        user.saveInBackground();
        setAttendenceSlot();
        goToSubjectList();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width* 0.8 ), (int)(height * 0.4));

        editTextSubjectName = (EditText) findViewById(R.id.editTextSubjectName);
        buttonSaveSubjectName = (Button) findViewById(R.id.buttonSaveSubjectName);
    }
}
