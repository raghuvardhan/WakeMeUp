package com.example.android.wakemeup.Database;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Date;

@Entity
public class Reminder extends Alarm{

    @PrimaryKey(autoGenerate = true)
    int reminderId;

    String title;
    Date dueDate;


    public Reminder(int reminderId, String title, Date dueDate, Time time) {
        super(time);
        this.reminderId = reminderId;
        this.title = title;
        this.dueDate = dueDate;

    }

    @Ignore
    public Reminder(String title, Date dueDate, Time time) {
        super(time);
        this.title = title;
        this.dueDate = dueDate;
    }

    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    public String getTitle() {
        return title;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
