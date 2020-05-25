package com.parse.starter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.parse.starter.MainActivity.user;
import static com.parse.starter.EventEditorActivity.contextEventEditor;
import static com.parse.starter.HomeActivity.*;
public class EventsManegerImportant {
    static  String flag = "none";
    static ArrayAdapter arrayAdapter1;
    static int numberOfEvents;
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <K, V> K getKey(Map<K, V> map, V value) {
        return map.entrySet()
                .stream()
                .filter(entry -> value.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst().get();
    }
    public   static void checkAndDeletePast(){
        String date,n;
        if(eventTime.isEmpty()||eventsArrayList.isEmpty())
            return;
        n = eventsArrayList.get(0);
        date = eventTime.get(0);
        Calendar cal = Calendar.getInstance();
        int month,year,day,hour,minut;
        year = Integer.parseInt(date.substring(0,4));
        month = Integer.parseInt(date.substring(4,6));
        day = Integer.parseInt(date.substring(6,8));
        hour = Integer.parseInt(date.substring(8,10));
        minut = Integer.parseInt(date.substring(10));
        cal.set(year,month,day,hour,minut);

        if(cal.getTimeInMillis() < Calendar.getInstance().getTimeInMillis() + 1000*60){
            eventsNameHashMap.remove(date);
            setHashToList();
            eventsDescriptionHashMap.remove(n);
        }

    }


    public static void getSavedEventData(){
        String[] splited;
        int i,k;
        try{
            numberOfEvents = user.getInt("numberOfEvents");
           // Toast.makeText(,Integer.toString(numberOfEvents), Toast.LENGTH_SHORT).show();
        }
        catch (NullPointerException e){
            numberOfEvents = 0;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        Log.i("finding problem : " , "    i am waiting for approval");
        String timeToBeDecoded, descriptionToBeDecoded, nameToBeDecoded;
        if(numberOfEvents != 0 && eventsNameHashMap.isEmpty() ){
            Log.i("finding problem : " , "    i am in for getting saved items");
            timeToBeDecoded = user.getString("timeOfEvents");
            nameToBeDecoded = user.getString("nameOfEvents");
            descriptionToBeDecoded = user.getString("descriptionOfEvents");

            splited = timeToBeDecoded.split("\\s+");

            for(String st : splited) {
                for(i = 0; i < st.length() ; i++){
                    if(st.charAt(i) == '_'){
                        st = st.substring(0,i) + st.substring(i+1);
                    }
                }
                Log.i("these are the times : " , st);
                eventTime.add(st);
            }
            k = 0;
            splited = nameToBeDecoded.split("\\s+");
            for(String st : splited) {

                for(i = 0 ; i < st.length() ; i++)
                    if(st.charAt(i) == '_')
                        st = st.substring(0,i) +' '+ st.substring(i+1);
                Log.i("theses are the names : " , st);
                Log.i("integers which name: " , eventTime.get(k));
                eventsNameHashMap.put(eventTime.get(k),st);
                eventsArrayList.add(st);
                k++;
            }
            k = 0;
            //arrayAdapter1.notifyDataSetChanged();
            splited = descriptionToBeDecoded.split("\\s+");
            for(String st : splited) {

                for(i = 0 ; i < st.length() ; i++) {
                    if (st.charAt(i) == '_')
                        st = st.substring(0, i) + ' ' + st.substring(i + 1);
                    else if (st.charAt(i) == '#')
                        st = st.substring(0, i) + '\n' + st.substring(i + 1);
                }

                Log.i("description: " , st);
                Log.i("where is the error : " , eventsArrayList.get(k));
                eventsDescriptionHashMap.put(eventsArrayList.get(k), st);
                k++;
            }

        }

    }



    public static void setHashToList() {
        int c = eventsArrayList.size();
        eventTime.clear();
        eventsArrayList.clear();
        if (eventsNameHashMap.isEmpty()) {
            Log.i("events Name hash map", " empty");
            saveDataInParse();
        }
        Log.i("events Name hash map", " non-empty");


        // TreeMap to store values of HashMap

        TreeMap<String, String> sorted = new TreeMap<>();

        // Copy all data from hashMap into TreeMap
        sorted.putAll(eventsNameHashMap);

        // Display the TreeMap which is naturally sorted
        for ( Map.Entry<String, String> entry : sorted.entrySet() ){//202003271746    202510012240  202003271746
            eventTime.add(entry.getKey());
            Log.i("this is the key ", entry.getKey());
            eventsArrayList.add(entry.getValue());
            Log.i("this is the value ", entry.getValue());
          //  arrayAdapter1.notifyDataSetChanged();
        }
        numberOfEvents = eventTime.size();
        if(flag.equals("deleted"))
            arrayAdapter1.notifyDataSetChanged();
     //   arrayAdapter1.notifyDataSetChanged();
        saveDataInParse();
    }
    private   static  void  saveDataInParse(){
        String  timeToBeCoded, nameToBeCoded , descriptionToBeCoded;
        //timeToBeCoded = newKey.substring(0,4)+"_"+newKey.substring(4,6)+"_"+newKey.substring(6,8)+"_"+newKey.substring(8,10)+"_"+newKey.substring(10);
        Log.i("finding problem : " , " about to cross a huge barrier  ");
        if(eventTime.isEmpty() || eventsArrayList.isEmpty() || eventsNameHashMap.isEmpty() || eventsDescriptionHashMap.isEmpty() || numberOfEvents == 0) {
            user.put("numberOfEvents",0 );
            user.put("nameOfEvents", "");
            user.put("timeOfEvents", "");
            user.put("descriptionOfEvents","");
            return;
        }
        Log.i("finding problem : " , "   i got off the barrier");
        timeToBeCoded = "";
        nameToBeCoded = "";
        descriptionToBeCoded = "";
        String newDescription;
        numberOfEvents = 0;
        for(String newKey: eventTime){

            numberOfEvents ++;
            timeToBeCoded = timeToBeCoded + " " + newKey;
        }
        for(String newNames: eventsArrayList){
            Log.i("heyyyyy do you see this : " , newNames);
            newDescription = eventsDescriptionHashMap.get(newNames);
            for(int i = 0; i < newNames.length() ; i++){
                if(newNames.charAt(i) == ' ')
                    newNames = newNames.substring(0,i) + "_" + newNames.substring(i+1);
            }
            for(int i = 0; i < newDescription.length() ; i++){
                if(newDescription.charAt(i) == ' ')
                    newDescription = newDescription.substring(0,i) + "_" + newDescription.substring(i+1);
                else if(newDescription.charAt(i) == '\n'){
                    newDescription = newDescription.substring(0,i) + "#" + newDescription.substring(i+1);
                }
            }

            nameToBeCoded = nameToBeCoded + " " +newNames;
            descriptionToBeCoded = descriptionToBeCoded + " "+ newDescription;

        }
        /*
        try{
            numberOfEvents = user.getInt("numberOfEvents");
            Toast.makeText(getApplicationContext(),Integer.toString(numberOfEvents), Toast.LENGTH_SHORT).show();
            timeOfEvents = user.getString("timeOfEvents");
            nameOfEvents = user.getString("nameOfEvents");
            descriptionOfEvents = user.getString("descriptionOfEvents");

        }
        catch (NullPointerException e){
            numberOfEvents = 0;
            timeOfEvents = "";
            nameOfEvents = "";
            descriptionOfEvents = "";
        }
        catch (Exception e){
            e.printStackTrace();
        }
        */
        nameToBeCoded = nameToBeCoded.substring(1);
        timeToBeCoded = timeToBeCoded.substring(1);
        descriptionToBeCoded = descriptionToBeCoded.substring(1);
        Log.i("number of events are  :", Integer.toString(numberOfEvents));
        user.put("numberOfEvents",numberOfEvents );
        user.put("nameOfEvents", nameToBeCoded);
        user.put("timeOfEvents", timeToBeCoded);
        user.put("descriptionOfEvents", descriptionToBeCoded);
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                   Log.i("event data status :", " saved in parse");
                }
                else{
                    Log.i("event data status :", " didnt save any thing");
                    Toast.makeText(contextEventEditor ,e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



}
