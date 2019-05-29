package com.example.android.wakemeup.Database.typeConverters;

import android.arch.persistence.room.TypeConverter;

import java.sql.Time;


public class TimeConverter {

    @TypeConverter
    public static long TimetoLong(Time time){
        return time == null ? null : time.getTime();
    }

    @TypeConverter
    public static Time LongtoTime(Long milliseconds){
        return milliseconds == null ? null : new Time(milliseconds);
    }
}
