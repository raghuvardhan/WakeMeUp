package com.example.android.wakemeup.Activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.android.wakemeup.Database.Reminder;
import com.example.android.wakemeup.Database.typeConverters.TimeConverter;
import com.example.android.wakemeup.Receivers.AlarmReceiver;
import com.example.android.wakemeup.Receivers.ReminderReceiver;
import com.example.android.wakemeup.Utilities.AlarmUtilities;
import com.example.android.wakemeup.Utilities.AppExecutors;
import com.example.android.wakemeup.Database.Reminder;
import com.example.android.wakemeup.Database.AppDatabase;
import com.example.android.wakemeup.R;
import com.example.android.wakemeup.Utilities.Helper;
import com.example.android.wakemeup.Utilities.ReminderUtilities;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity {


    // Extra for the reminder ID to be received in the intent
    public static final String EXTRA_REMINDER_ID = "extraReminderId";
    // Extra for the reminder ID to be received after rotation
    public static final String INSTANCE_REMINDER_ID = "instanceReminderId";

    // Constant for default reminder id to be used when not in update mode
    private static final int DEFAULT_REMINDER_ID = -1;
    private int mReminderId = DEFAULT_REMINDER_ID;

    private Reminder reminder;
    private TextView reminderTitle;

    private Button saveButton;
    private AppDatabase appDatabase;
    private Button reminderTimeButton;
    private Button reminderDateButton;
    private EditText reminderMessage;
    private int mHour;
    private int mMinute;
    private int mYear;
    private int mMonth;
    private int mDay;

    private Time time;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.reminder_toolbar);
        setSupportActionBar(myToolbar);


        appDatabase = AppDatabase.getInstance(getApplicationContext());
        time = new Time(System.currentTimeMillis());
        date = new Date(System.currentTimeMillis());

        reminderTitle = (TextView)findViewById(R.id.edit_reminder_title);
        saveButton = (Button)findViewById(R.id.save_reminder_button);
        reminder = (Reminder)getIntent().getSerializableExtra("reminder");


        reminderMessage = (EditText)findViewById(R.id.reminder_message_text);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_REMINDER_ID)) {
            if (mReminderId == DEFAULT_REMINDER_ID) {
                saveButton.setText("Update");

                mReminderId = intent.getIntExtra(EXTRA_REMINDER_ID, DEFAULT_REMINDER_ID);

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        reminder = appDatabase.reminderDao().getReminderById(mReminderId);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                populateUI(reminder);
                            }
                        });
                    }
                });
            }
        }

        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        reminderTimeButton = (Button)findViewById(R.id.reminder_time_btn);
        reminderTimeButton.setText(time + "");

        reminderTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(ReminderActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                time = Helper.getTimefromTimePickerDialog(hourOfDay, minute);
                                reminderTimeButton.setText(time + "");
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        mDay = c.get(Calendar.DAY_OF_MONTH);
        mMonth = c.get(Calendar.MONTH);
        mYear = c.get(Calendar.YEAR);

        reminderDateButton = (Button)findViewById(R.id.reminder_date_btn);

        reminderDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(ReminderActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                date = Helper.getDatefromDatePickerDialog(year, month, dayOfMonth);
                                reminderDateButton.setText(time + "");
                            }

                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminder = new Reminder(reminderMessage.getText().toString(), date, time);

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (mReminderId == DEFAULT_REMINDER_ID) {
                            reminder.setReminderId((int)appDatabase.reminderDao().insertReminder(reminder));
                            ReminderUtilities.createReminderEvent(ReminderActivity.this, reminder);

                        } else {
                            reminder.setReminderId(mReminderId);
                            appDatabase.reminderDao().updateReminder(reminder);
                            ReminderUtilities.updateReminderEvent(ReminderActivity.this,reminder);
                        }
                        finish();
                    }
                });
            }
        });
    }

    private void populateUI(Reminder reminder) {
        if (reminder == null) {
            return;
        }
        reminderMessage.setText(reminder.getTitle());
        reminderTimeButton.setText(reminder.getTime() + "");
        reminderDateButton.setText(reminder.getDueDate() + "");
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
                ReminderUtilities.deleteReminderEvent(ReminderActivity.this, reminder);
                appDatabase.reminderDao().deleteReminder(reminder);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
