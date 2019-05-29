package com.example.android.wakemeup;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.example.android.wakemeup.Activities.AlarmActivity;
import com.example.android.wakemeup.Activities.ReminderActivity;
import com.example.android.wakemeup.Activities.TodoActivity;
import com.example.android.wakemeup.Database.Alarm;
import com.example.android.wakemeup.Database.AppDatabase;
import com.example.android.wakemeup.Database.Reminder;
import com.example.android.wakemeup.Database.Todo;
import com.example.android.wakemeup.Utilities.AlarmUtilities;
import com.example.android.wakemeup.Utilities.TaskListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;


//Todo 1.Add filters to navigation view instead of create.
public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TaskListAdapter mAdapter;
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    private AppDatabase appDatabase;
    List<Alarm> taskList;

    private int priority;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar)findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);


        //Todo Get the priority from UI
        priority = 1;

        //Todo Get the priority from UI
        category = "Gym";

        FabSpeedDial fabSpeedDial = (FabSpeedDial) findViewById(R.id.fab_speed_dial);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch(id){
                    case R.id.action_alarm: createAlarm();
                        break;

                    case R.id.action_reminder : createReminder();
                        break;

                    case R.id.action_todo : createTodo();
                        break;
                }
                return true;
            }
        });


        mDrawerLayout = findViewById(R.id.drawer_layout);

        appDatabase = AppDatabase.getInstance(getApplicationContext());

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();
                        int id = menuItem.getItemId();
                        switch(id){
                            case R.id.nav_alarm: createAlarm();
                            break;

                            case R.id.nav_reminder : createReminder();
                            break;

                            case R.id.nav_todo : createTodo();
                            break;

                            case R.id.filter_priority: filterPriority(priority);
                            break;

                            case R.id.filter_category: filterCategory(category);
                            break;
                        }

                        return true;
                    }
                });

        taskList = new ArrayList<Alarm>();


        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_home);
        mAdapter = new TaskListAdapter(this, taskList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        retrieveTasks();
    }

    public void createAlarm(){
        Intent intent = new Intent(MainActivity.this, AlarmActivity.class);
        startActivity(intent);
    }

    public void createReminder(){
        Intent intent = new Intent(MainActivity.this, ReminderActivity.class);
        startActivity(intent);
    }

    public void createTodo(){
        Intent intent = new Intent(MainActivity.this, TodoActivity.class);
        startActivity(intent);    }

    private void retrieveTasks() {
        LiveData<List<Alarm>> alarms = appDatabase.alarmDao().getAlarms();
        alarms.observe(this, new Observer<List<Alarm>>() {
            @Override
            public void onChanged(@Nullable List<Alarm> alarms) {
                mAdapter.setAlarmList(alarms);
            }
        });

        LiveData<List<Reminder>> reminders = appDatabase.reminderDao().getReminders();
        reminders.observe(this, new Observer<List<Reminder>>() {
            @Override
            public void onChanged(@Nullable List<Reminder> reminders) {
                mAdapter.setReminderList(reminders);
            }
        });

        LiveData<List<Todo>> todos = appDatabase.todoDao().getTodos();
        todos.observe(this, new Observer<List<Todo>>() {
            @Override
            public void onChanged(@Nullable List<Todo> todos) {
                mAdapter.setTodoList(todos);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
 //       showFabAdd();
    }

    public void filterPriority(int priority){
        List<Todo> filteredList = appDatabase.todoDao().getTodoByPriority(priority);
        mAdapter.filter(filteredList);
    }

    public void filterCategory(String category){
        List<Todo> filteredList = appDatabase.todoDao().getTodoByCategory(category);
        mAdapter.filter(filteredList);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_action_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_filter:
                filterPriority(priority);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
