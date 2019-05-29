package com.example.android.wakemeup.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.android.wakemeup.Database.dao.AlarmDao;
import com.example.android.wakemeup.Database.dao.ReminderDao;
import com.example.android.wakemeup.Database.dao.TodoDao;
import com.example.android.wakemeup.Database.typeConverters.DateConverter;
import com.example.android.wakemeup.Database.typeConverters.TimeConverter;

@Database(entities = {Todo.class, Reminder.class, Alarm.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class, TimeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase ourInstance;
    private static final Object lock = new Object();
    private static final String DatabaseName = "Tasks";

    public static AppDatabase getInstance(Context context) {
        if(ourInstance == null){
            synchronized (lock){
                ourInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, AppDatabase.DatabaseName)
                        .allowMainThreadQueries()
                        .build();
            }
        }
        return ourInstance;
    }

    public abstract TodoDao todoDao();
    public abstract ReminderDao reminderDao();
    public abstract AlarmDao alarmDao();
}
