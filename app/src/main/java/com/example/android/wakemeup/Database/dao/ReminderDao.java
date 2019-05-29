package com.example.android.wakemeup.Database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.wakemeup.Database.Reminder;

import java.util.List;

@Dao
public interface ReminderDao {

    @Insert
    public long insertReminder(Reminder reminder);

    @Delete
    public void deleteReminder(Reminder reminder);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void updateReminder(Reminder reminder);

    @Query("SELECT * FROM reminder")
    public LiveData<List<Reminder>> getReminders();

    @Query("SELECT * FROM reminder WHERE reminderId = :reminderId")
    public Reminder getReminderById(int reminderId);


}
