package com.parse.starter;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import static com.parse.starter.MainActivity.name;

import static com.parse.starter.MainActivity.user;


public class SubjectsListActivity extends AppCompatActivity {
    ListView listOfSubjects;
    static ArrayAdapter arrayAdapter;
    static int noOfSubjects;
    static ArrayList<String> totalClasssesArrayList = new ArrayList<>();
    static ArrayList<String> totalPresentArrayList = new ArrayList<>();

    public void nextToTimeTableButtonClicked(View view){
        Intent intent1 = new Intent(getApplicationContext(),TimeTableActivity.class);
        startActivity(intent1);
    }

    public void addSubject(View view){
        Intent intent = new Intent(getApplicationContext(),NoteEditorActivity.class);
        startActivity(intent);
    }
    public void convertToString(){
        String temp = "";
        int i;
        for(String s : name) {
            i = s.length();
            for (i = 0;i<s.length();i++){
                if(s.charAt(i) == ' '){
                    s = s.substring(0,i)+"_"+s.substring(i+1);
                }

            }
            temp = temp +" "+s;
        }

        temp = temp.substring(1);

        user.put("subjectName",temp);
        user.saveInBackground();
        addAttendence();

    }
    private  void addAttendence(){
        String temp = "";
        int i=0;
        for(String s : totalClasssesArrayList){
            Log.i("total classes", s );
            i++;
            temp = temp + " "+ s;
        }
        Log.i("value of temp", Integer.toString(i)+"  "+temp  );
        temp = temp.substring(1);
        i = 0;
        user.put("totalClasses", temp);
        temp = "";
        for(String s : totalPresentArrayList){
            i++;
            Log.i("total present", s );
            temp = temp + " "+ s;
        }
        temp = temp.substring(1);
        Log.i("value of temp2", Integer.toString(i)+"  "+temp  );
        user.put("totalPresent",temp);
    }
    private  void attendenceListUpdater(){
        totalClasssesArrayList.clear();
        totalPresentArrayList.clear();
        int i;
        String t = user.getString("totalClasses");
        if(t != null) {
            i = 0;
            String[] splitedC = t.split("\\s+");
            for(String st : splitedC){
                i++;
                Log.i("checking for null  1", st );
                totalClasssesArrayList.add(st);

            }
            Log.i("num of classes", Integer.toString(i)  );
        }
        t = user.getString("totalPresent");
        if(t != null) {
            i = 0;
            String[] splitedA = t.split("\\s+");
            for(String st : splitedA){
                i++;
                Log.i("checking for null  2", st );
                totalPresentArrayList.add(st);

            }
            Log.i("num of present", Integer.toString(i)  );
        }

    }
   public  void listUpdater(){

        if(noOfSubjects == 0){
            //Toast.makeText(getApplicationContext(),"wanna go outt",Toast.LENGTH_LONG).show();
            //name.add("Hello!! Click below to add your subjects");
            return;
        }
        else {
            //Toast.makeText(getApplicationContext(),"i 777777777777",Toast.LENGTH_LONG).show();
            int i;
            String t = user.getString("subjectName");
            String[] splited = t.split("\\s+");
            name.clear();
            for (String temp : splited) {
                i = temp.length();
                for (i = 0; i < temp.length(); i++) {
                    if (temp.charAt(i) == '_') {
                        temp = temp.substring(0, i) + " " + temp.substring(i+1);

                        //Toast.makeText(this,"i am in",Toast.LENGTH_LONG).show();
                    }

                }
                name.add(temp);
            }
        }
        arrayAdapter.notifyDataSetChanged();
        attendenceListUpdater();
        return;


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects_list);
        listOfSubjects  = (ListView) findViewById(R.id.listOfSubject);



        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, name);

        //String temp = user.getString("subjectName");
     try {

         noOfSubjects = user.getInt("noOfSubjects");
         Log.i("fucking integer", Integer.toString(noOfSubjects));
     }
     catch (NullPointerException e){
         e.printStackTrace();
         noOfSubjects = 0;
     }
     catch (Exception e){
         e.printStackTrace();
     }
        listUpdater();

        listOfSubjects.setAdapter(arrayAdapter);
        int k=0;
        for(String s : name) {
            k++;
            Log.i("this is list of names:", s);

        }
        Log.i("this is list of names:", Integer.toString(k));//Log.i("this is list of names  :", Integer.toString(name.get(0).length()));
   //     if(name.get(0).length() == 0 && name.get(1).length()>0){
   //         name.remove(0);
   //     }



        listOfSubjects.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int itemToDelete  = position;
                new AlertDialog.Builder(SubjectsListActivity.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Are you sure?")
                        .setMessage("Want to delete this subject?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                name.remove(itemToDelete);
                                totalClasssesArrayList.remove(itemToDelete);
                                totalPresentArrayList.remove(itemToDelete);
                                arrayAdapter.notifyDataSetChanged();
                                user.put("noOfSubjects",noOfSubjects-1);
                                user.saveInBackground();
                                convertToString();
                                noOfSubjects--;



                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });

    }
}
