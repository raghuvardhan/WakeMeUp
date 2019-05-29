package com.example.android.wakemeup.Utilities;

import android.widget.DatePicker;
import android.widget.TimePicker;
import com.example.android.wakemeup.Database.typeConverters.TimeConverter;
import java.sql.Time;
import java.util.Calendar;
import java.sql.Date;


public final class Helper {


    public static Time getTimefromTimePickerDialog(int hourOfDay,int minute){

        Calendar calendar = Calendar.getInstance();
        int diffHour =  hourOfDay - calendar.get(Calendar.HOUR_OF_DAY);
        int diffMin = minute - calendar.get(Calendar.MINUTE);

        int diffTime = ( diffHour * 60 + diffMin ) * 60 * 1000;

        long alarmTime  = System.currentTimeMillis() + diffTime;
        return new Time(alarmTime);
    }

    public static Date getDatefromDatePickerDialog(int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        Date date = new Date(calendar.getTime().getTime());
        return date;
    }

    public static Time getTimefromTimePicker(TimePicker timePicker){
        Calendar calendar = Calendar.getInstance();
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        int diffHour =  hour - calendar.get(Calendar.HOUR_OF_DAY);
        int diffMin = minute - calendar.get(Calendar.MINUTE);

        int diffTime = ( diffHour * 60 + diffMin ) * 60 * 1000;

        long alarmTime  = System.currentTimeMillis() + diffTime;
        return new Time(alarmTime);
    }

    public static Date getDateFromDatePicker(DatePicker datePicker){
        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        return new Date(calendar.getTime().getTime());
    }

    public static String getTimeinHourMinFormat(Time time){
        return time.getHours() + " : " + time.getMinutes();
    }
}
