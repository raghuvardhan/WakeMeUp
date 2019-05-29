package com.example.android.wakemeup.Database.typeConverters;

import android.arch.persistence.room.TypeConverter;

import java.sql.Date;

public class DateConverter {

    @TypeConverter
    public static long DatetoLong(Date date){
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Date LongtoDate(Long milliseconds){
        return milliseconds == null ? null : new Date(milliseconds);
    }
}
