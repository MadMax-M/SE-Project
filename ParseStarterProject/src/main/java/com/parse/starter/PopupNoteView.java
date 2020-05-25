package com.parse.starter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

import static com.parse.starter.NoteViewActivity.noteDataArrayList;
import static com.parse.starter.NoteViewActivity.noteDesArrayList;

public class PopupNoteView extends AppCompatActivity {
    TextView titleTextView;
    TextView bodyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_note_view);
        titleTextView = (TextView)findViewById(R.id.titleTextView);
        bodyTextView = (TextView) findViewById( R.id.bodyTextView);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width* 0.8 ), (int)(height * 0.4));
        Intent intent = getIntent();
        int position =  intent.getIntExtra("position",-1);
        setDataView(position);



    }

    private void setDataView(int position) {
        titleTextView.setText(noteDesArrayList.get(position));
        bodyTextView.setText(noteDataArrayList.get(position));
    }
}
