package com.example.android.wakemeup.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.wakemeup.AlarmMedia;
import com.example.android.wakemeup.Database.Alarm;
import com.example.android.wakemeup.Database.AppDatabase;
import com.example.android.wakemeup.R;
import com.example.android.wakemeup.Utilities.AlarmUtilities;

public class CloseAlarmActivity extends AppCompatActivity {

    private static final String TAG = "logs";
    private Button alarmCloseButton;
    private Button alarmSnoozeButton;
    private TextView alarmTimeText;

    private AppDatabase appDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_alarm);

        appDatabase = AppDatabase.getInstance(getApplicationContext());

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        final Alarm alarm = (Alarm) getIntent().getExtras().getBundle("Bundle").getSerializable("Alarm");

        alarmCloseButton = (Button)findViewById(R.id.alarm_close_btn);
        alarmCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmMedia.getInstance(CloseAlarmActivity.this).stopAlarmMedia();
                Log.d(TAG, "onClick: alarm repeat: " + alarm.getRepeat());
                if(!alarm.getRepeat()){
                    alarm.setEnabled(false);
                    Log.d(TAG, "onClick: alarm enabled:" + alarm.getEnabled());
                    appDatabase.alarmDao().updateAlarm(alarm);
                }
                finish();
            }
        });

        alarmSnoozeButton = (Button)findViewById(R.id.alarm_snooze_btn);
        alarmSnoozeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmMedia.getInstance(CloseAlarmActivity.this).stopAlarmMedia();
                AlarmUtilities.snoozeAlarmEvent(CloseAlarmActivity.this, alarm);
                finish();
            }
        });

        alarmTimeText = (TextView)findViewById(R.id.alarm_time_text);
        alarmTimeText.setText(alarm.getTime()+"");
    }
}
