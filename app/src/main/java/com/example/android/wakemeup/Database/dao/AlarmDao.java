package com.example.android.wakemeup.Database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.wakemeup.Database.Alarm;

import java.util.List;

@Dao
public interface AlarmDao {

    @Insert
    public long insertAlarm(Alarm alarm);

    @Delete
    public void deleteAlarm(Alarm alarm);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void updateAlarm(Alarm alarm);

    @Query("SELECT * FROM alarm")
    public LiveData<List<Alarm>> getAlarms();

    @Query("SELECT * FROM alarm WHERE alarmId = :alarmId")
    public Alarm getAlarmById(int alarmId);
}
