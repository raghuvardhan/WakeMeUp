package com.example.android.wakemeup.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.sql.Time;
import java.sql.Date;

@Entity
public class Todo extends Alarm{

    @PrimaryKey(autoGenerate = true)
    int todoId;

    Date date;
    String description;
    int priority;
    String category;
    boolean state;

    public Todo(int todoId, Date date, String description, Time time, int priority, String category, boolean state) {
        super(time);
        this.todoId = todoId;
        this.date = date;
        this.description = description;
        this.priority = priority;
        this.category = category;
        this.state = state;
    }

    public int getTodoId() {
        return todoId;
    }

    public void setTodoId(int todoId) {
        this.todoId = todoId;
    }

    @Ignore
    public Todo(Date date, Time time, String description,int priority, String category) {
        super(time);
        this.date = date;
        this.description = description;
        this.priority = priority;
        this.category = category;
        this.state = false;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
