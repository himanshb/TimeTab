package com.example.pc.notes;

import android.app.DatePickerDialog;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pc.timetab.R;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.pc.timetab.R.id.editText;

public class UpdateNoteActivity extends AppCompatActivity implements View.OnClickListener {

    NotesDatabase notesDatabase;
    EditText editTextTitle,editTextContent;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);
        final String fileName=this.getIntent().getExtras().getString("timeStamp");
        final String fileContent=this.getIntent().getExtras().getString("content");
        final String fileDate=this.getIntent().getExtras().getString("date");
        final String title=this.getIntent().getExtras().getString("title");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Edit Note");

        notesDatabase=new NotesDatabase(this,"NOTES_DB",null,1);

        button=(Button) this.findViewById(R.id.buttonDate);
        button.setOnClickListener(this);
        editTextTitle=(EditText)this.findViewById(R.id.editTextTitle);
        editTextContent=(EditText)this.findViewById(R.id.editTextContent);
        editTextContent.setText(fileContent);
        button.setText(fileDate);
        editTextTitle.setText(title);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabEdit);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date=button.getText().toString();
                String title=editTextTitle.getText().toString();
                if (!title.equals("")&&!date.equals("Select Date")) {
                    updateNote(title,fileName,date);
                }
                else
                    Toast.makeText(UpdateNoteActivity.this, "Enter the Title Or date", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    protected void updateNote(String noteName,String fileName,String fileDate){
        if (!editTextContent.getText().toString().equals("")) {
            notesDatabase.updateNote(noteName,fileName,fileDate);
            notesDatabase.close();
            try {
                String file = fileName + ".txt";
                FileOutputStream fos = openFileOutput(file, MODE_PRIVATE);
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
                bw.write(editTextContent.getText().toString());
                bw.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finish();
        }
        else
            Toast.makeText(this, "Enter the Description", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        if (v == button) {
            Calendar calendar = Calendar.getInstance();
            int curYear = calendar.get(Calendar.YEAR);
            int curMonth = calendar.get(Calendar.MONTH);
            int curDay = calendar.get(Calendar.DAY_OF_MONTH);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                DatePickerDialog dpDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month+=1;
                        if (month<10){
                            if (dayOfMonth<10){

                                button.setText(year+"/"+"0"+month+"/"+"0"+dayOfMonth);
                            }
                            else {
                                button.setText(year+"/"+"0"+month+"/"+dayOfMonth);
                            }
                        }
                        else {
                            if (dayOfMonth<10){
                                button.setText(year+"/"+month+"/"+"0"+dayOfMonth);
                            }
                            else {
                                button.setText(year+"/"+month+"/"+dayOfMonth);
                            }
                        }
                    }
                }, curYear, curMonth, curDay);

                dpDialog.show();
            }
        }
    }
}
