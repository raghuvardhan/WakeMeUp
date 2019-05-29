package com.example.android.wakemeup.Utilities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.android.wakemeup.Activities.CloseAlarmActivity;
import com.example.android.wakemeup.Database.Alarm;
import com.example.android.wakemeup.Database.Reminder;
import com.example.android.wakemeup.Database.typeConverters.TimeConverter;
import com.example.android.wakemeup.MainActivity;
import com.example.android.wakemeup.R;
import com.example.android.wakemeup.Receivers.CloseReceiver;
import com.example.android.wakemeup.Receivers.SnoozeReceiver;

import java.sql.Time;

import static android.content.Context.NOTIFICATION_SERVICE;

public final class NotificationUtilites {

    private static final int REMINDER_NOTIFICATION_ID = 1;
    private static final int TODO_NOTIFICATION_ID = 2;

    public static void sendReminderNotification(Context context, Intent intent){

        String task = intent.getBundleExtra("Bundle").getString("Task");
        PendingIntent pendingNotificationIntent = getReminderNotificationIntent(context);
        PendingIntent  pendingSnoozeIntent = getReminderSnoozeIntent(context, intent);
        PendingIntent pendingCloseIntent = getReminderCloseIntent(context);

        String notificationContent = getReminderNotificationContent(intent);
        int  notificationId = REMINDER_NOTIFICATION_ID;

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(context)
                .setContentTitle("Reminder!")
                .setContentText(notificationContent)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentIntent(pendingNotificationIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .addAction(R.drawable.ic_snooze, "Remind me in 5 Minutes", pendingSnoozeIntent)
                .addAction(R.drawable.ic_close, "Close", pendingCloseIntent)
                .setAutoCancel(true);


        Notification notification = notifyBuilder.build();
        notificationManager.notify(notificationId, notification);
    }

    public static void ClearNotifications(Context context){
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    private static PendingIntent getReminderNotificationIntent(Context context){
        Intent reminderNotificationIntent = new Intent(context, MainActivity.class);
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(context,
                REMINDER_NOTIFICATION_ID, reminderNotificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        return notificationPendingIntent;
    }

    private static PendingIntent getReminderCloseIntent(Context context){
        Intent closeIntent = new Intent(context, CloseReceiver.class);
        PendingIntent closePendingIntent = PendingIntent.getBroadcast(context, 0, closeIntent, 0);
        return closePendingIntent;
    }

    private static PendingIntent getReminderSnoozeIntent(Context context, Intent intent){
        Reminder reminder = (Reminder) intent.getExtras().getBundle("Bundle").getSerializable("Reminder");
        int reminderId = reminder.getReminderId();

        Intent snoozeIntent = new Intent(context, SnoozeReceiver.class);
        snoozeIntent.putExtra("Reminder", reminder);
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(context, reminderId, snoozeIntent, 0);
        return snoozePendingIntent;
    }

    private static String getReminderNotificationContent(Intent intent){
        Reminder reminder = (Reminder) intent.getExtras().getBundle("Bundle").getSerializable("Reminder");
        String reminderMessage = reminder.getTitle();
        String reminderTime = Helper.getTimeinHourMinFormat(reminder.getTime());
        return reminderMessage + "\n" +  reminderTime;
    }

    public static void sendTodoNotification(){

    }

}
