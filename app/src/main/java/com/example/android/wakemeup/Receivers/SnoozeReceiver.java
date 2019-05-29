package com.example.android.wakemeup.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.android.wakemeup.AlarmMedia;
import com.example.android.wakemeup.Database.Alarm;
import com.example.android.wakemeup.Database.Reminder;
import com.example.android.wakemeup.Utilities.AlarmUtilities;
import com.example.android.wakemeup.Utilities.ReminderUtilities;

public class SnoozeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Reminder reminder = (Reminder)intent.getSerializableExtra("Reminder");
        AlarmMedia.getInstance(context).stopAlarmMedia();
        ReminderUtilities.snoozeReminderEvent(context, reminder);
    }
}
