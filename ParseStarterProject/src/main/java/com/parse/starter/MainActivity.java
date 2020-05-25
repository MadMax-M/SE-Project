/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.parse.starter.EventsManegerImportant.getSavedEventData;
import static com.parse.starter.SubjectsListActivity.arrayAdapter;
import static com.parse.starter.SubjectsListActivity.noOfSubjects;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener{
    boolean signUpModeActive = true;
    TextView textViewChange;
    Button button;
    EditText userNameEditText;
    EditText passwordEditText;
    ImageView logoImageView;
    ConstraintLayout backgroundLayout;
    static  ParseUser user;
    static ArrayList<String> name = new ArrayList<>();
    int k = 0;

public void goToSubjectList(){
        Intent intent = new Intent(getApplicationContext(),SubjectsListActivity.class);
        startActivity(intent);
    }
    public void goToHome(){
        Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
            buttonClicked(view);

        }
        return false;
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.textViewChange){
            if (signUpModeActive){
                signUpModeActive = false;
                textViewChange.setText("or SIGNUP");
                button.setText("LOGGIN");

            }
            else {
                signUpModeActive = true;
                textViewChange.setText("or LOGGIN");
                button.setText("SIGNUP");
            }
        }
        else if(view.getId() == R.id.LogoImageView || view.getId() == R.id.BackgroundLayout){
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }


    public void buttonClicked (View view){

        if(signUpModeActive) {

            if (userNameEditText.getText().toString().matches("") || passwordEditText.getText().toString().matches("")) {
                Toast.makeText(this, "yousername and password are required", Toast.LENGTH_SHORT).show();
            } else {
                user = new ParseUser();
                user.setUsername(userNameEditText.getText().toString());
                user.setPassword(passwordEditText.getText().toString());
                user.put("noOfSubjects",0);

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("successful  :", "user successfully signed up");
                            user.put("alarm", "off");
                            user.saveInBackground();
                            goToSubjectList();
                        } else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
        else {
            ParseUser.logInInBackground(userNameEditText.getText().toString(), passwordEditText.getText().toString(), (logedInUser, e) -> {
                if (e == null && logedInUser != null){
                    Log.i("successful  :", "user successfully logged in");
                   // getSavedEventData();
                   /* Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                          public void run() {
                            importData();
                        }
                    }, 10000);   //5 seconds*/
                   user = logedInUser;
                    user.put("alarm", "off");
                    user.saveInBackground();
                    Toast.makeText(MainActivity.this, "hello " + user.getUsername(), Toast.LENGTH_LONG).show();
                    k = 1;
                    redirectIfLoggedIn();
                }
                else {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    k = 0;
                }
            });


        }
    }

    private void redirectIfLoggedIn() {
        if(ParseUser.getCurrentUser() != null)
            goToHome();

    }

/*public  void importData(){

    try{
        noOfSubjects = user.getInt("noOfSubjects");
        Log.i("this is the subject :" ," got the value of number of subjects " + Integer.toString(noOfSubjects));
    }
    catch (NullPointerException e){
        if(e != null)
            Log.i("this is the subject :" ,"its a null pointt exception");
        noOfSubjects = 0;
    }
    catch (Exception e){
        Toast.makeText(this, e.getMessage() , Toast.LENGTH_SHORT).show();
        Log.i("this is the subject :" ,e.getMessage());
    }
    if(noOfSubjects == 0){
        //Toast.makeText(getApplicationContext(),"wanna go outt",Toast.LENGTH_LONG).show();
        //name.add("Hello!! Click below to add your subjects");
        Log.i("this is the subject :" ,"please concider meee");
        goToHome();
        return;
    }
    else {
        //Toast.makeText(getApplicationContext(),"i 777777777777",Toast.LENGTH_LONG).show();
        int i;
        String t = user.getString("subjectName");
        Log.i("this is the subject :" ,"here I am trying to collect data");
        Log.i("this is the subject :" ,t);
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
            Log.i("these are the name items :" ,temp);
            name.add(temp);
        }
        SubjectsListActivity.arrayAdapter.notifyDataSetChanged();
        goToHome();
    }




}*/

    @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    logoImageView = findViewById(R.id.LogoImageView);
    backgroundLayout = findViewById(R.id.BackgroundLayout);

    button = (Button)findViewById(R.id.button) ;
    textViewChange =  findViewById(R.id.textViewChange);
    textViewChange.setOnClickListener(this);
    userNameEditText = (EditText)findViewById(R.id.userNameEditText);
    passwordEditText = (EditText)findViewById(R.id.passwordEditText);
    passwordEditText.setOnKeyListener(this);
    backgroundLayout.setOnClickListener(this);
    logoImageView.setOnClickListener(this);
    ParseAnalytics.trackAppOpenedInBackground(getIntent());
    user = ParseUser.getCurrentUser();

    redirectIfLoggedIn();



  }


}