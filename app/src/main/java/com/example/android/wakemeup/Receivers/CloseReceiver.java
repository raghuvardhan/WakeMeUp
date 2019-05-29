package com.example.android.wakemeup.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.android.wakemeup.AlarmMedia;
import com.example.android.wakemeup.Database.Alarm;
import com.example.android.wakemeup.Utilities.NotificationUtilites;

public class CloseReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmMedia.getInstance(context).stopAlarmMedia();
        NotificationUtilites.ClearNotifications(context);
    }
}
