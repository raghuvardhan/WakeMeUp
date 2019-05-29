package com.example.android.wakemeup.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.android.wakemeup.Activities.CloseAlarmActivity;
import com.example.android.wakemeup.AlarmMedia;
import com.example.android.wakemeup.Database.Alarm;
import com.example.android.wakemeup.Utilities.NotificationUtilites;



public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        AlarmMedia.getInstance(context).startAlarmMedia();
//        NotificationUtilites.sendNotification(context, intent);
        Alarm alarm = (Alarm) intent.getExtras().getBundle("Bundle").getSerializable("Alarm");

        Intent closeIntent = new Intent(context.getApplicationContext(),CloseAlarmActivity.class);
        closeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        Bundle bundle = new Bundle();
        bundle.putSerializable("Alarm", alarm);
        bundle.putString("Task", "Alarm");
        closeIntent.putExtra("Bundle", bundle);
        context.startActivity(closeIntent);
    }
}
