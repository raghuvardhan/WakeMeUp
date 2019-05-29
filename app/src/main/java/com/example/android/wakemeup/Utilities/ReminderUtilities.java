package com.example.android.wakemeup.Utilities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.android.wakemeup.Database.Reminder;
import com.example.android.wakemeup.Database.typeConverters.TimeConverter;
import com.example.android.wakemeup.Receivers.ReminderReceiver;

import static android.content.Context.ALARM_SERVICE;

public final class ReminderUtilities {

    public static final String TAG = "Logs";

    public static void createReminderEvent(Context context, Reminder reminder) {
        Log.d(TAG, "createReminderEvent: reminder id is : " + reminder.getReminderId());
        Intent intent = new Intent(context, ReminderReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Reminder", reminder);
        bundle.putString("Task", "Reminder");
        intent.putExtra("Bundle", bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), reminder.getReminderId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, TimeConverter.TimetoLong(reminder.getTime()), pendingIntent);
    }

    public static void updateReminderEvent(Context context, Reminder reminder) {
        ReminderUtilities.createReminderEvent(context, reminder);
    }

    public static void deleteReminderEvent(Context context, Reminder reminder) {
        Log.d(TAG, "deleteReminderEvent: reminder is : " + reminder.getReminderId());
        if (reminder != null) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(context, ReminderReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), reminder.getReminderId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
        }
    }

    public static void snoozeReminderEvent(Context context, Reminder reminder) {

        if (reminder != null){
            Intent intent = new Intent(context, ReminderReceiver.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("Reminder", reminder);
            bundle.putString("Task", "Reminder");
            intent.putExtra("Bundle", bundle);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), reminder.getReminderId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager reminderManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            reminderManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 300000, pendingIntent);
            NotificationUtilites.ClearNotifications(context);
        }
    }
    
}
