package com.example.pc.notes;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

public class CreateNoteActivity extends AppCompatActivity implements View.OnClickListener {
    NotesDatabase notesDatabase;
    EditText editTextTitle,editTextContent;
    Button button;
    String timeStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Note");

        notesDatabase=new NotesDatabase(this,"NOTES_DB",null,1);

        button=(Button) this.findViewById(R.id.button);
        button.setOnClickListener(this);
        editTextTitle=(EditText)this.findViewById(editText);
        editTextContent=(EditText)this.findViewById(R.id.editText2);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date=button.getText().toString();
                int color=generateColor();
                String title=editTextTitle.getText().toString();
                if (!title.equals("")&&!date.equals("Select Date")) {
                    timeStamp = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss", Locale.getDefault()).format(new Date());
                    addNote(title,timeStamp,date,color);
                }
                else
                    Toast.makeText(CreateNoteActivity.this, "Enter the Title Or date", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    protected int generateColor(){
        List<Integer> list=new ArrayList<>();
        for (int i=1;i<=14;i++){
            list.add(i);
        }
        Collections.shuffle(list);
        switch (list.get(1)){
            case 1:
                return getColor(R.color.colorAccent);
            case 2:
                return getColor(R.color.colorAccent1);
            case 3:
                return getColor(R.color.colorAccent2);
            case 4:
                return getColor(R.color.colorAccent3);
            case 5:
                return getColor(R.color.colorAccent4);
            case 6:
                return getColor(R.color.colorAccent5);
            case 7:
                return getColor(R.color.colorAccent6);
            case 8:
                return getColor(R.color.colorAccent7);
            case 9:
                return getColor(R.color.colorAccent8);
            case 10:
                return getColor(R.color.colorAccent9);
            case 11:
                return getColor(R.color.colorAccent10);
            case 12:
                return getColor(R.color.colorAccent11);
            case 13:
                return getColor(R.color.colorAccent12);
            case 14:
                return getColor(R.color.colorAccent13);
            default:
                return getColor(R.color.colorAccent6);
        }
    }
    protected void addNote(String noteName,String fileName,String fileDate,int color){
        if (!editTextContent.getText().toString().equals("")) {
            notesDatabase.addNote(noteName,fileName,fileDate,color);
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
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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