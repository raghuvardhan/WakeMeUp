package com.example.android.wakemeup.ViewHolders;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.example.android.wakemeup.Activities.AlarmActivity;
import com.example.android.wakemeup.Database.Alarm;
import com.example.android.wakemeup.R;
import com.example.android.wakemeup.Utilities.TaskListAdapter;

import java.util.List;

import static com.example.android.wakemeup.Activities.AlarmActivity.TAG;

public class AlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private TextView alarmTime;
    private Alarm alarm;
    private List<Alarm> taskList;
    private TaskListAdapter mAdapter;
    private Switch alarmEnabledSwitch;


    public AlarmViewHolder(View itemView, TaskListAdapter adapter, List<Alarm> taskList) {
        super(itemView);
        alarmTime = (TextView)itemView.findViewById(R.id.alarm_time);
        alarmEnabledSwitch = (Switch)itemView.findViewById(R.id.alarm_enabled_switch);
        this.mAdapter = adapter;
        itemView.setOnClickListener(this);
        this.taskList = taskList;
    }

    public TextView getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(TextView alarmTime) {
        this.alarmTime = alarmTime;
    }

    public Switch getAlarmEnabledSwitch() {
        return alarmEnabledSwitch;
    }

    public void setAlarmEnabledSwitch(Switch alarmEnabledSwitch) {
        this.alarmEnabledSwitch = alarmEnabledSwitch;
    }

    @Override
    public void onClick(View v) {
        int mPosition = getLayoutPosition();
        alarm = (Alarm)taskList.get(mPosition);
        int alarmId = alarm.getAlarmId();
        Intent intent = new Intent(v.getContext(), AlarmActivity.class);
        intent.putExtra(AlarmActivity.EXTRA_ALARM_ID, alarmId);
        v.getContext().startActivity(intent);
    }
}
