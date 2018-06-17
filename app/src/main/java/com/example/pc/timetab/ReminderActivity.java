package com.example.pc.timetab;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ReminderActivity extends AppCompatActivity implements View.OnClickListener {

    SimpleDateFormat timeFormat;
    Calendar calendar,calendar2;
    String time;
    int id;
    FloatingActionButton fab;
    AlarmManager manager;
    PendingIntent pendingIntent;
    TextView textView,textView2;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        textView=(TextView)this.findViewById(R.id.DatePicker);
        textView.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Reminders");

        textView2=(TextView)this.findViewById(R.id.timePicker);
        textView2.setOnClickListener(this);
        editText=(EditText)this.findViewById(R.id.reminderText);

        time=this.getIntent().getExtras().getString("time");
        id=this.getIntent().getExtras().getInt("id");

        fab=(FloatingActionButton)this.findViewById(R.id.fabReminder);
        fab.setOnClickListener(this);
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    @Override
    public void onClick(View v) {
        if (v == textView) {
            Calendar calendar2 = Calendar.getInstance();
            int curYear = calendar2.get(Calendar.YEAR);
            int curMonth = calendar2.get(Calendar.MONTH);
            int curDay = calendar2.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dpDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month += 1;
                    if (month < 10) {
                        if (dayOfMonth < 10) {

                            textView.setText(year + "/" + "0" + month + "/" + "0" + dayOfMonth);
                        } else {
                            textView.setText(year + "/" + "0" + month + "/" + dayOfMonth);
                        }
                    } else {
                        if (dayOfMonth < 10) {
                            textView.setText(year + "/" + month + "/" + "0" + dayOfMonth);
                        } else {
                            textView.setText(year + "/" + month + "/" + dayOfMonth);
                        }
                    }
                }
            }, curYear, curMonth, curDay);

            dpDialog.show();
        }
        if (v==textView2){

            Calendar calendar2 = Calendar.getInstance();
            int hour = calendar2.get(Calendar.HOUR_OF_DAY);
            int minute = calendar2.get(Calendar.MINUTE);
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    if ((hourOfDay < 10) && (minute < 10)) {
                        textView2.setText("0" + hourOfDay + ":" + "0" + minute);
                    } else if (minute < 10) {
                        textView2.setText(hourOfDay + ":" + "0" + minute);
                    } else if (hourOfDay < 10) {
                        textView2.setText("0" + hourOfDay + ":" + minute);
                    } else
                        textView2.setText(hourOfDay + ":" + minute);
                }
            }, hour, minute, false);
            timePickerDialog.show();
        }
        if (v == fab) {
            String date=textView.getText().toString();
            time=textView2.getText().toString();
            date=date+" "+time;
            calendar=Calendar.getInstance();
            timeFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
            try {
                calendar.setTime(timeFormat.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String description=editText.getText().toString();

            manager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(getApplicationContext(), ReminderReceiver.class);
            intent.putExtra("description",description);
            intent.putExtra("id",id);
            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(this, "Reminder is set", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
