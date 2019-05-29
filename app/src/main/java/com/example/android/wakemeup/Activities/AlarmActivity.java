package com.example.android.wakemeup.Activities;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TimePicker;

import com.example.android.wakemeup.MainActivity;
import com.example.android.wakemeup.Utilities.AppExecutors;
import com.example.android.wakemeup.Database.Alarm;
import com.example.android.wakemeup.Database.AppDatabase;
import com.example.android.wakemeup.R;
import com.example.android.wakemeup.Utilities.Helper;
import com.example.android.wakemeup.Utilities.AlarmUtilities;

import java.sql.Time;
import java.util.Calendar;

public class AlarmActivity extends AppCompatActivity{

    // Extra for the alarm ID to be received in the intent
    public static final String EXTRA_ALARM_ID = "extraAlarmId";
    // Extra for the alarm ID to be received after rotation
    public static final String INSTANCE_ALARM_ID = "instanceAlarmId";

    public static final String TAG = "logs";

    // Constant for default alarm id to be used when not in update mode
    private static final int DEFAULT_ALARM_ID = -1;
    private int mAlarmId = DEFAULT_ALARM_ID;
    private boolean alarmEnabled = true;

    private Button saveButton;
    private AppDatabase appDatabase;
    private Alarm alarm;
    private Button alarmTimeButton;
    private Switch repeatAlarmSwitch;
    private Switch enableAlarmSwitch;

    private int mHour;
    private int mMinute;

    private Time time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.alarm_toolbar);
        setSupportActionBar(myToolbar);

        appDatabase = AppDatabase.getInstance(getApplicationContext());

        time = new Time(System.currentTimeMillis());

        saveButton = (Button)findViewById(R.id.save_alarm_button);
        repeatAlarmSwitch = (Switch)findViewById(R.id.repeat_alarm_switch);
        enableAlarmSwitch = (Switch)findViewById(R.id.alarm_enabled_switch);


        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_ALARM_ID)) {
            if (mAlarmId == DEFAULT_ALARM_ID) {
                saveButton.setText("Update");
                mAlarmId = intent.getIntExtra(EXTRA_ALARM_ID, DEFAULT_ALARM_ID);
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        alarm = appDatabase.alarmDao().getAlarmById(mAlarmId);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                populateUI(alarm);
                            }
                        });
                    }
                });
            }
        }

        alarmTimeButton = (Button)findViewById(R.id.alarm_time_btn);
        alarmTimeButton.setText(time + "");

        alarmTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(AlarmActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                time = Helper.getTimefromTimePickerDialog(hourOfDay, minute);
                                alarmTimeButton.setText(time + "");
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });


        
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean repeat = repeatAlarmSwitch.isChecked();
                alarm  =  new Alarm(time, repeat, alarmEnabled);

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (mAlarmId == DEFAULT_ALARM_ID) {
                            alarm.setAlarmId((int)appDatabase.alarmDao().insertAlarm(alarm));
                            AlarmUtilities.createAlarmEvent(AlarmActivity.this, alarm);
                        } else {
                            alarm.setAlarmId(mAlarmId);
                            enableAlarmSwitch.setChecked(true);
                            AlarmUtilities.updateAlarmEvent(AlarmActivity.this, alarm);
                            appDatabase.alarmDao().updateAlarm(alarm);
                        }
                        finish();
                    }
                });
            }
        });
    }
    
    private void populateUI(Alarm alarm) {
        if (alarm == null) {
            return;
        }
        Time alarmTime = alarm.getTime();
        alarmTimeButton.setText(alarmTime + "");
        mHour = alarmTime.getHours();
        mMinute = alarmTime.getMinutes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.delete_id:
                AlarmUtilities.deleteAlarmEvent(AlarmActivity.this, alarm);
                appDatabase.alarmDao().deleteAlarm(alarm);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
