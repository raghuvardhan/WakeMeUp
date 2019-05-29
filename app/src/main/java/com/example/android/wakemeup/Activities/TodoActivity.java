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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.android.wakemeup.Database.Todo;
import com.example.android.wakemeup.Database.typeConverters.TimeConverter;
import com.example.android.wakemeup.Receivers.AlarmReceiver;
import com.example.android.wakemeup.Receivers.TodoReceiver;
import com.example.android.wakemeup.Utilities.AppExecutors;
import com.example.android.wakemeup.Database.AppDatabase;
import com.example.android.wakemeup.Database.Todo;
import com.example.android.wakemeup.R;
import com.example.android.wakemeup.Utilities.Helper;

import java.sql.Date;
import java.sql.Time;
import java.util.Calendar;

public class TodoActivity extends AppCompatActivity {

    public static final String EXTRA_TODO_ID = "extraTodoId";
    public static final String INSTANCE_TODO_ID = "instanceTodoId";

    private static final int DEFAULT_TODO_ID = -1;
    private int mTodoId = DEFAULT_TODO_ID;

    private static final int CRITICAL_PRIORITY = 1;
    private static final int HIGH_PRIORITY = 2;
    private static final int MEDIUM_PRIORITY = 3;
    private static final int LOW_PRIORITY = 4;

    private AppDatabase appDatabase;

    private CheckBox todoState;
    private TextView todoTitle;
    private EditText todoDescription;
    private Button todoTimeButton;
    private Button todoDateButton;
    private Button saveButton;
    private Todo todo;

    private int mHour;
    private int mMinute;
    private int mYear;
    private int mMonth;
    private int mDay;

    private Time time;
    private Date date;

    private int priority;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.todo_toolbar);
        setSupportActionBar(myToolbar);

        todoState = (CheckBox)findViewById(R.id.todo_state);

        appDatabase = AppDatabase.getInstance(getApplicationContext());
        time = new Time(System.currentTimeMillis());
        date = new Date(System.currentTimeMillis());

        //TODO: Get priority from drop down from ui
        priority = 1;

        //Todo: Get Category from UI
        category = "Gym";

        todoDescription  = (EditText) findViewById(R.id.todo_description);

        saveButton = (Button)findViewById(R.id.save_todo_button);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_TODO_ID)) {
            if (mTodoId == DEFAULT_TODO_ID) {
                saveButton.setText("Update");

                mTodoId = intent.getIntExtra(EXTRA_TODO_ID, DEFAULT_TODO_ID);

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        todo = appDatabase.todoDao().getTodoById(mTodoId);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                populateUI(todo);
                            }
                        });
                    }
                });
            }
        }

        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        todoTimeButton = (Button)findViewById(R.id.todo_time_btn);

        todoTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(TodoActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                time = Helper.getTimefromTimePickerDialog(hourOfDay, minute);
                                todoTimeButton.setText(time + "");
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        mDay = c.get(Calendar.DAY_OF_MONTH);
        mMonth = c.get(Calendar.MONTH);
        mYear = c.get(Calendar.YEAR);

        todoDateButton = (Button)findViewById(R.id.todo_date_btn);

        todoDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(TodoActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                date = Helper.getDatefromDatePickerDialog(year, month, dayOfMonth);
                                todoDateButton.setText(time + "");
                            }

                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todo = new Todo(date, time, todoDescription.getText().toString(), priority, category);

                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (mTodoId == DEFAULT_TODO_ID) {
                            createTodoEvent(todo);
                            appDatabase.todoDao().insertTodo(todo);
                        } else {
                            todo.setTodoId(mTodoId);
                            updateTodoEvent(todo);
                            appDatabase.todoDao().updateTodo(todo);
                        }
                        finish();
                    }
                });
            }
        });
    }

    private void populateUI(Todo todo) {
        if (todo == null) {
            return;
        }
        Toast.makeText(getApplicationContext(), "Im called", Toast.LENGTH_SHORT);
        todoDescription.setText(todo.getDescription().toString());
        todoState.setVisibility(View.VISIBLE);
        todoState.setChecked(todo.isState());
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
                deleteTodoEvent(todo);
                appDatabase.todoDao().deleteTodo(todo);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void createTodoEvent(Todo todo){
        Log.d("Todo", "createTodo Eent");
        Intent intent = new Intent(TodoActivity.this, TodoReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), todo.getTodoId(), intent, 0);

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, TimeConverter.TimetoLong(todo.getTime()), pendingIntent);
        Log.d("Todo", "Todo Manager created at" + todo.getTime());
    }

    public void updateTodoEvent(Todo todo){
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(TodoActivity.this, TodoReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), todo.getTodoId(), intent, 0);
        alarmManager.cancel(pendingIntent);

        Intent myIntent = new Intent(TodoActivity.this, TodoReceiver.class);
        PendingIntent myPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), todo.getTodoId(), intent, 0);
        AlarmManager myAlarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        myAlarmManager.set(AlarmManager.RTC_WAKEUP, TimeConverter.TimetoLong(todo.getTime()), pendingIntent);
    }

    public void deleteTodoEvent(Todo todo){
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(TodoActivity.this, TodoReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), todo.getTodoId(), intent, 0);
        alarmManager.cancel(pendingIntent);

    }
}
