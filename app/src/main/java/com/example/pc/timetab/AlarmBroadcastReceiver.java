package com.example.pc.timetab;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.Calendar;


public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.icon)
                        .setContentTitle("Time Table")
                        .setContentText("...");

        Intent intent1=new Intent(context,MonActivity.class);

        switch (day) {
            case Calendar.MONDAY:
                intent1.setFlags(intent1.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("day","Monday");
                break;
            case Calendar.TUESDAY:
                intent1.setFlags(intent1.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("day","Tuesday");
                break;
            case Calendar.WEDNESDAY:
                intent1.setFlags(intent1.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("day","Wednesday");
                break;
            case Calendar.THURSDAY:
                intent1.setFlags(intent1.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("day","Thursday");
                break;
            case Calendar.FRIDAY:
                intent1.setFlags(intent1.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("day","Friday");
                break;
            case Calendar.SATURDAY:
                intent1.setFlags(intent1.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("day","Saturday");
                break;
            default:
                intent1=new Intent(context,AboutUsActivity.class);
                break;
        }
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent1,
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
