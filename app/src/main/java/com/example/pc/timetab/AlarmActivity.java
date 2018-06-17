package com.example.pc.timetab;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity implements View.OnClickListener {

    AlarmManager manager;
    PendingIntent pendingIntent;
    FloatingActionButton fab;
    TimePicker timePicker;
    Calendar calendar;
    TextView textView;
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Alarm");

        manager=(AlarmManager)this.getSystemService(ALARM_SERVICE);
        timePicker=(TimePicker)this.findViewById(R.id.timePicker);
        fab=(FloatingActionButton)this.findViewById(R.id.floatingActionButton2);
        textView=(TextView)this.findViewById(R.id.textViewCancel);
        fab.setOnClickListener(this);
        textView.setOnClickListener(this);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                calendar=Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
            }
        });

        SharedPreferences preferences=this.getSharedPreferences("my_preferences",MODE_PRIVATE);
        final SharedPreferences.Editor editor=preferences.edit();

        if (!preferences.contains("first_time")) {
            builder = new AlertDialog.Builder(this);
            builder.setTitle("Please notice");
            builder.setMessage("This alarm will be repeating daily");
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editor.putBoolean("first_time",true);
                    editor.apply();
                }
            });
            builder.show();
        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        if (v==fab) {
            if (calendar!=null) {
                if(calendar.getTimeInMillis()<(System.currentTimeMillis()-5000)){
                    long updatedTime=calendar.getTimeInMillis()+86400000;
                    calendar.setTimeInMillis(updatedTime);
                }
                Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
                manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000, pendingIntent);
                Toast.makeText(this, "Alarm rings at: " + calendar.get(Calendar.HOUR_OF_DAY) + " : " + calendar.get(Calendar.MINUTE), Toast.LENGTH_SHORT).show();
                finish();
            }
            else {
                Toast.makeText(this, "Set Time First", Toast.LENGTH_SHORT).show();
            }
        }
        if (v==textView){

            Intent intent = new Intent(this, AlarmBroadcastReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
            manager.cancel(pendingIntent);
            Toast.makeText(this, "Canceled", Toast.LENGTH_SHORT).show();
        }
    }
}
