package com.parse.starter;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import static com.parse.starter.MainActivity.user;
import android.view.*;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SaveCallback;

public class SettingsActivity extends AppCompatActivity {
    TextView usernameTextView;
    EditText newUserNameEditText;
    TextView textView7;
    Switch activateNotificationSwitch;
    Switch activateAlarmSwitch;
    static boolean alarmStatus;
    static boolean notificationStatus;

    public void changeUserName(View view){
        final String newUserName;
        newUserName = newUserNameEditText.getText().toString();

        if(newUserName.length() == 0){
            Toast.makeText(getApplicationContext(),"new user name can not be empty!", Toast.LENGTH_SHORT).show();
            textView7.setTextColor(Color.RED);
            return;
        }
        else if(newUserName == usernameTextView.getText().toString()){
            Toast.makeText(getApplicationContext(),"no changes made", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
                user.setUsername(newUserName);
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null){
                            usernameTextView.setText("welcome " + newUserName);
                            Toast.makeText(getApplicationContext(), "user name changed to " + newUserName, Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        }
    }
    protected  void setSwitches(){
        int alarmIsOn,notificationIsOn;
        try {
            alarmIsOn = user.getInt("alarmStatus");
            notificationIsOn = user.getInt("notificationStatus");
        }
        catch (NullPointerException e){
            alarmIsOn = 0;
            notificationIsOn = 0;
        }
        catch (Exception e){
            e.printStackTrace();
            return;
        }
        switch (alarmIsOn){
            case 1:
                activateAlarmSwitch.setChecked(false);
                break;
            case 2:
                activateAlarmSwitch.setChecked(true);
                break;
            case 0:
                activateAlarmSwitch.setChecked(true);
                break;
        }
        switch (notificationIsOn){
            case 1:
                activateNotificationSwitch.setChecked(false);
                break;
            case 2:
                activateNotificationSwitch.setChecked(true);
                break;
            case 0:
                activateNotificationSwitch.setChecked(true);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");
        usernameTextView = (TextView) findViewById(R.id.usernameTextView);
        usernameTextView.setText("welcome "+user.getUsername());
        newUserNameEditText = (EditText) findViewById(R.id.newUserNameEditText);
        newUserNameEditText.setText(user.getUsername());
        textView7 = (TextView) findViewById(R.id.textView7);
        textView7.setTextColor(Color.BLACK);
        activateAlarmSwitch = (Switch) findViewById( R.id.activateAlarmSwitch);
        activateNotificationSwitch = (Switch) findViewById( R.id.activateNotificationSwitch);
        setSwitches();

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activateAlarmSwitch.isChecked()){
                    user.put("alarmStatus", 2);
                }
                else
                    user.put("alarmStatus",1);
                if(activateNotificationSwitch.isChecked())
                    user.put("notificationStatus",2);
                else{
                    user.put("notificationStatus",1);
                    Log.i("notifi switch is ", " off ");
                }
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null) {
                            Toast.makeText(getApplicationContext(),"notification settings saved", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(getApplicationContext(),"some thing went wrong", Toast.LENGTH_SHORT).show();;
                    }
                });
            }
        });



    }
}
