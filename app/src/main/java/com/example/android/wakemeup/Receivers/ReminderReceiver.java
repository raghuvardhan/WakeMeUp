package com.example.android.wakemeup.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.android.wakemeup.AlarmMedia;
import com.example.android.wakemeup.Utilities.NotificationUtilites;

public class ReminderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        Log.d("Remainder", "onReceive: Remainder started");
        AlarmMedia.getInstance(context).startAlarmMedia();
        NotificationUtilites.sendReminderNotification(context, intent);

    }
}
