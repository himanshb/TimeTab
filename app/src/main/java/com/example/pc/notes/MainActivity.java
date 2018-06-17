package com.example.pc.notes;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.pc.timetab.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    NotesDatabase notesDatabase;
    RecyclerView recyclerView;
    NotesAdapter notesAdapter;
    List<NoteBuilder> noteBuilders;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        notesDatabase = new NotesDatabase(this, "NOTES_DB", null, 1);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CreateNoteActivity.class));
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    protected String getNoteContent(String fileName) {
        File file = this.getFileStreamPath(fileName);
        if (file.exists()) {
            StringBuilder buf = new StringBuilder();
            String line;
            try {
                FileInputStream fis = openFileInput(fileName);
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                while ((line = br.readLine()) != null) {
                    buf.append(line).append("\n");
                }
                br.close();
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return buf.toString();
        } else
            return null;
    }

    protected void createCards() {
        noteBuilders = new ArrayList<>();
        cursor = notesDatabase.getCursor();
        while (cursor.moveToNext()) {
            String fileName=cursor.getString(cursor.getColumnIndex("note_file_name"));
            String fileTitle=cursor.getString(cursor.getColumnIndex("note_name"));
            String fileDate=cursor.getString(cursor.getColumnIndex("date"));
            int color=cursor.getInt(cursor.getColumnIndex("color"));
            NoteBuilder noteBuilder=new NoteBuilder(fileTitle,getNoteContent(fileName+".txt"),fileDate);
            noteBuilder.setFileName(fileName);
            noteBuilder.setColor(color);
            noteBuilders.add(noteBuilder);
        }
        recyclerView = (RecyclerView) this.findViewById(R.id.recyclerViewScroll);
        notesAdapter = new NotesAdapter(this, noteBuilders);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(notesAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        createCards();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        notesDatabase.close();
    }
}