package com.example.pc.timetab;

import android.app.TimePickerDialog;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {

    String day,prevSubject,fromTime,toTime;
    MyDatabase myDatabase;
    EditText editText1,editText2,editText3;
    FloatingActionButton fab;
    TextView tvFrom,tvTo;
    TimePickerDialog timePickerDialog;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        prevSubject=this.getIntent().getExtras().getString("subject");
        day=this.getIntent().getExtras().getString("day");
        fromTime=this.getIntent().getExtras().getString("from");
        toTime=this.getIntent().getExtras().getString("to");
        id=this.getIntent().getExtras().getInt("id");


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Edit");

        myDatabase=new MyDatabase(this,"Time_Table_DB",null,1);
        editText1=(EditText)this.findViewById(R.id.editText4);
        editText2=(EditText)this.findViewById(R.id.editText5);
        editText3=(EditText)this.findViewById(R.id.editText6);
        tvFrom=(TextView)findViewById(R.id.textViewFrom);
        tvTo=(TextView)findViewById(R.id.textViewTo);
        Cursor cursor=myDatabase.getCursor(day);
        while (cursor.moveToNext()){
            String subCheck=cursor.getString(cursor.getColumnIndex("subject"));
            String dayCheck=cursor.getString(cursor.getColumnIndex("day"));
            if (dayCheck.equals(day)&&subCheck.equals(prevSubject)) {
                editText1.setText(subCheck);
                editText2.setText(cursor.getString(cursor.getColumnIndex("teacher")));
                editText3.setText("" + cursor.getInt(cursor.getColumnIndex("room")));
            }
        }
        fab=(FloatingActionButton)this.findViewById(R.id.updateFab);
        tvFrom.setOnClickListener(this);
        tvTo.setOnClickListener(this);
        fab.setOnClickListener(this);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        String subject=editText1.getText().toString();
        String teacher=editText2.getText().toString();
        String ed3=editText3.getText().toString();
        int room=0,hour=0,min=0;
        int hour1=0,min1=0;
        if (!ed3.equals("")) {
            room = Integer.parseInt(ed3);
        }
        else
            Toast.makeText(this, "Please Fill All Values", Toast.LENGTH_SHORT).show();


        Calendar calendar,calendar1;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            calendar = Calendar.getInstance();
            SimpleDateFormat timeFormat=new SimpleDateFormat("HH:mm", Locale.getDefault());
            try {
                calendar.setTime(timeFormat.parse(fromTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            min = calendar.get(Calendar.MINUTE);

            calendar1 = Calendar.getInstance();
            try {
                calendar1.setTime(timeFormat.parse(toTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            hour1 = calendar1.get(Calendar.HOUR_OF_DAY);
            min1 = calendar1.get(Calendar.MINUTE);

        }

        if (v == tvFrom) {
            timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if ((hourOfDay < 10) && (minute < 10)) {
                        tvFrom.setText("0" + hourOfDay + ":" + "0" + minute);
                    } else if (minute < 10) {
                        tvFrom.setText(hourOfDay + ":" + "0" + minute);
                    } else if (hourOfDay < 10) {
                        tvFrom.setText("0" + hourOfDay + ":" + minute);
                    } else
                        tvFrom.setText(hourOfDay + ":" + minute);
                }
            }, hour, min, false);
            timePickerDialog.show();
        }
        if (v == tvTo) {
            timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {


                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if ((hourOfDay < 10) && (minute < 10)) {
                        tvTo.setText("0" + hourOfDay + ":" + "0" + minute);
                    }
                    else if (minute < 10) {
                        tvTo.setText(hourOfDay + ":" + "0" + minute);
                    }
                    else if (hourOfDay < 10) {
                        tvTo.setText("0" + hourOfDay + ":" + minute);
                    }
                    else
                        tvTo.setText(hourOfDay + ":" + minute);
                }
            }, hour1, min1, false);
            timePickerDialog.show();
        }

        if (v == fab) {
            String from = tvFrom.getText().toString();
            String to = tvTo.getText().toString();
            if (!subject.equals("") && !teacher.equals("") && !ed3.equals("") && !from.equals("SELECT TIME") && !to.equals("SELECT TIME")) {
                boolean check = myDatabase.updateTable(day, subject, teacher, room,from,to,id);
                if (check) {
                    Toast.makeText(this, "Inserted in DB", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
