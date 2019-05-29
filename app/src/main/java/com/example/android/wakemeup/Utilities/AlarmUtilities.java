package com.example.android.wakemeup.Utilities;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.android.wakemeup.Database.Alarm;
import com.example.android.wakemeup.Database.AppDatabase;
import com.example.android.wakemeup.Database.typeConverters.TimeConverter;
import com.example.android.wakemeup.Receivers.AlarmReceiver;


import java.util.ArrayList;

import static android.content.Context.ALARM_SERVICE;

public final class AlarmUtilities {

    public static final String TAG = "Logs";
    private static AppDatabase appDatabase;


    public static void createAlarmEvent(Context context, Alarm alarm) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Alarm", alarm);
        bundle.putString("Task", "Alarm");
        intent.putExtra("Bundle", bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), alarm.getAlarmId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (alarm.getRepeat()) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, TimeConverter.TimetoLong(alarm.getTime()), AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, TimeConverter.TimetoLong(alarm.getTime()), pendingIntent);
        }
    }

    public static void updateAlarmEvent(Context context, Alarm alarm) {
        AlarmUtilities.createAlarmEvent(context, alarm);
    }

    public static void deleteAlarmEvent(Context context, Alarm alarm) {
        if (alarm != null) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), alarm.getAlarmId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
        }
    }

    public static void snoozeAlarmEvent(Context context, Alarm alarm) {

        if (alarm != null) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            intent.putExtra("Alarm", alarm);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), alarm.getAlarmId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 300000, pendingIntent);
            NotificationUtilites.ClearNotifications(context);
        }
    }

    public static void enableAlarmEvent(Context context, Alarm alarm) {
        appDatabase = AppDatabase.getInstance(context);

        Intent intent = new Intent(context, AlarmReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Alarm", alarm);
        bundle.putString("Task", "Alarm");
        intent.putExtra("Bundle", bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), alarm.getAlarmId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (alarm.getRepeat()) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, TimeConverter.TimetoLong(alarm.getTime()), AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, TimeConverter.TimetoLong(alarm.getTime()), pendingIntent);
        }

        alarm.setEnabled(true);
        appDatabase.alarmDao().updateAlarm(alarm);
    }

    public static void disableAlarmEvent(Context context, Alarm alarm) {
        if (alarm != null) {
            appDatabase = AppDatabase.getInstance(context);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context.getApplicationContext(), alarm.getAlarmId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);

            alarm.setEnabled(false);
            appDatabase.alarmDao().updateAlarm(alarm);
        }
    }

}