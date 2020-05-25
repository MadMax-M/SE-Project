package com.parse.starter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import static com.parse.starter.MainActivity.user;

public class NoteViewActivity extends AppCompatActivity {
ListView noteListView;
static ArrayList<String> noteDesArrayList = new ArrayList<>();
static ArrayList<String> noteDataArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_view);
        setTitle("Notes");
        noteListView = findViewById(R.id.noteListView);
        ArrayAdapter  arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,noteDesArrayList);
        setNoteList();
        noteListView.setAdapter(arrayAdapter);

        noteListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),PopupNoteView.class);
                intent.putExtra("position", position);
                startActivity(intent);

            }
        });

        noteListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int itemToDelete  = position;
                new AlertDialog.Builder(NoteViewActivity.this)
                        .setIcon(android.R.drawable.ic_delete)
                        .setTitle("Are you sure?")
                        .setMessage("Want to delete this item?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                noteDataArrayList.remove(position);
                                noteDesArrayList.remove(position);
                                saveNoteData();
                                saveNoteDescription();
                                arrayAdapter.notifyDataSetChanged();

                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;
            }
        });
    }

    public void saveNoteDescription() {
        String temp;
        int k;
        temp = "";
        for(String s : noteDesArrayList){
            s = s.substring(0,2)+s.substring(3,5)+s.substring(6,11)+s.substring(12);
            for(k =0 ; k < s.length() ;k++ ){
                if(s.charAt(k) == ' '){
                    s = s.substring(0,k) + "_" + s.substring(k+1);
                }
            }
            temp = temp+" "+s;
        }
        if(temp.length() != 0){
            temp = temp.substring(1);

        }
        user.put("noteDescription",temp);
        user.saveInBackground();
    }

    public void saveNoteData() {
        String temp;
        int k;
        temp = "";
        for(String s : noteDataArrayList){
            for(k =0 ; k < s.length() ;k++ ){
                if(s.charAt(k) == ' '){
                    s = s.substring(0,k) + "_" + s.substring(k+1);
                }
            }
            temp = temp+" "+s;
        }
        if(temp.length() != 0){
            temp = temp.substring(1);
        }
        user.put("note",temp);
        user.saveInBackground();
    }

    private void setNoteList() {
        noteDesArrayList.clear();
        noteDataArrayList.clear();
        String descs, notes;
        descs = user.getString("noteDescription");
        notes = user.getString("note");
        String[] splited1 = descs.split("\\s+");
        String[] splited2 = notes.split("\\s+");
        int i = 0 ;
        int k;
        for(String st: splited1){
            for(k =0 ; k < st.length() ;k++ ){
                if(st.charAt(k) == '_'){
                    st = st.substring(0,k) + " " + st.substring(k+1);
                }
            }
            for(k =0 ; k < splited2[i].length() ;k++ ){
                if(splited2[i].charAt(k) == '_'){
                    splited2[i] = splited2[i].substring(0,k) + " " + splited2[i].substring(k+1);
                }
            }
            if(!st.isEmpty()) {
                String month;
                month = st.substring(2, 4);
                month = Integer.toString(Integer.parseInt(month)+1);
                if(month.length()==1)
                    month = "0"+month;
                noteDesArrayList.add(st.substring(0, 2) + "-" + month + "-" + st.substring(4, 9) + " " + st.substring(9));
                noteDataArrayList.add(splited2[i]);
            }
            i++;
        }
    }
}
