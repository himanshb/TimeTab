package com.example.pc.timetab;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;


public class ReminderReceiver extends BroadcastReceiver {
    int id;
    String description,title;
    AlertDialog.Builder alertDialog;
    MyDatabase myDatabase;
    @Override
    public void onReceive(Context context, Intent intent) {
        //connect to database for the title
        myDatabase=new MyDatabase(context,"Time_Table_DB",null,1);
        //get the description of reminder

        description=intent.getExtras().getString("description");
        id=intent.getExtras().getInt("id");
        Cursor cursor=myDatabase.getCursorByID(id);
        cursor.moveToNext();
        title=cursor.getString(cursor.getColumnIndex("subject"));

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.icon)
                        .setContentTitle("Reminder")
                        .setContentText(title);

        Intent notificationIntent = new Intent(context, Reminder2Activity.class);
        notificationIntent.putExtra("title",title);
        notificationIntent.putExtra("description",description);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        builder.setAutoCancel(true);

        // Add as notification
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification=builder.build();
        notification.defaults|=Notification.DEFAULT_SOUND;
        notification.defaults|=Notification.DEFAULT_VIBRATE;
        manager.notify(0, notification);
    }

}
