package com.example.android.wakemeup.Database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;
import java.sql.Time;

@Entity
public class Alarm implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int alarmId;

    Boolean repeat;

    Boolean enabled;

    Time time;

    public Alarm(int alarmId, Time time, Boolean repeat, Boolean enabled) {
        this.alarmId = alarmId;
        this.time = time;
        this.repeat = repeat;
        this.enabled = enabled;
    }

    public int getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(int alarmId) {
        this.alarmId = alarmId;
    }

    @Ignore
    public Alarm(Time time, Boolean repeat, Boolean enabled) {
        this.time = time;
        this.repeat = repeat;
        this.enabled = enabled;
    }

    @Ignore
    public Alarm(Time time){
        this.time = time;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public Boolean getRepeat() {
        return repeat;
    }

    public void setRepeat(Boolean repeat) {
        this.repeat = repeat;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
