package com.example.pc.timetab;


import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FormActivity extends AppCompatActivity implements View.OnClickListener {
    String day;
    MyDatabase myDatabase;
    EditText editText1, editText2, editText3;
    FloatingActionButton fab;
    TextView txt1, txt2, txt3, txt4;
    TimePickerDialog timePickerDialog;
    String from, to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        day = this.getIntent().getExtras().getString("day");
        myDatabase = new MyDatabase(this, "Time_Table_DB", null, 1);
        editText1 = (EditText) this.findViewById(R.id.editText);
        editText2 = (EditText) this.findViewById(R.id.editText2);
        editText3 = (EditText) this.findViewById(R.id.editText3);
        txt1 = (TextView) this.findViewById(R.id.textView4);
        txt2 = (TextView) this.findViewById(R.id.textView5);
        txt3 = (TextView) this.findViewById(R.id.textView6);
        txt4 = (TextView) this.findViewById(R.id.textView7);
        txt2.setOnClickListener(this);
        txt4.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Schedule");

        fab = (FloatingActionButton) this.findViewById(R.id.floatingActionButton9);
        fab.setOnClickListener(this);
    }
//Method to generate a random color for a card
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        String subject = editText1.getText().toString();
        String teacher = editText2.getText().toString();
        String ed3 = editText3.getText().toString();
        int color=generateColor();
        int room = 0;
        int hour = 0, min = 0;
        if (!ed3.equals("")) {
            room = Integer.parseInt(ed3);
        }
        Calendar calendar;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            calendar = Calendar.getInstance();

            hour = calendar.get(Calendar.HOUR_OF_DAY);
            min = calendar.get(Calendar.MINUTE);

        }

        if (v == txt2) {
            timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if ((hourOfDay < 10) && (minute < 10)) {
                        txt2.setText("0" + hourOfDay + ":" + "0" + minute);
                    } else if (minute < 10) {
                        txt2.setText(hourOfDay + ":" + "0" + minute);
                    } else if (hourOfDay < 10) {
                        txt2.setText("0" + hourOfDay + ":" + minute);
                    } else
                        txt2.setText(hourOfDay + ":" + minute);
                }
            }, hour, min, false);
            timePickerDialog.show();
        }
        if (v == txt4) {
            timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {


                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if ((hourOfDay < 10) && (minute < 10)) {
                        txt4.setText("0" + hourOfDay + ":" + "0" + minute);
                    } else if (minute < 10) {
                        txt4.setText(hourOfDay + ":" + "0" + minute);
                    } else if (hourOfDay < 10) {
                        txt4.setText("0" + hourOfDay + ":" + minute);
                    } else
                        txt4.setText(hourOfDay + ":" + minute);
                }
            }, hour, min, false);
            timePickerDialog.show();
        }

        if (v == fab) {
            from = txt2.getText().toString();
            to = txt4.getText().toString();
            if (!subject.equals("") && !teacher.equals("") && !ed3.equals("") && !from.equals("SELECT TIME") && !to.equals("SELECT TIME")) {
                boolean check = myDatabase.enterInDb(day, subject, teacher, room, from, to,color);
                if (check) {
                    Toast.makeText(this, "Inserted in DB", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(this, "Time is unique", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        }
    }
}