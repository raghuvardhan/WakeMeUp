package com.example.android.wakemeup.ViewHolders;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.wakemeup.Database.Alarm;
import com.example.android.wakemeup.Database.Reminder;
import com.example.android.wakemeup.R;
import com.example.android.wakemeup.Activities.ReminderActivity;
import com.example.android.wakemeup.Utilities.TaskListAdapter;

import java.util.List;

public class ReminderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private TextView reminderTitle, reminderDate, reminderTime;
    private Reminder reminder;
    private List<Alarm> taskList;
    private TaskListAdapter mAdapter;

    public ReminderViewHolder(View itemView, TaskListAdapter adapter, List<Alarm> taskList) {
        super(itemView);
        reminderDate = (TextView)itemView.findViewById(R.id.reminder_date);
        reminderTime = (TextView)itemView.findViewById(R.id.reminder_time);
        reminderTitle = (TextView)itemView.findViewById(R.id.reminder_title);
        this.mAdapter = adapter;
        itemView.setOnClickListener(this);
        this.taskList = taskList;
    }

    public TextView getReminderTitle() {
        return reminderTitle;
    }

    public void setReminderTitle(TextView reminderTitle) {
        this.reminderTitle = reminderTitle;
    }

    public TextView getReminderDate() {
        return reminderDate;
    }

    public void setReminderDate(TextView reminderDate) {
        this.reminderDate = reminderDate;
    }

    public TextView getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(TextView reminderTime) {
        this.reminderTime = reminderTime;
    }

    @Override
    public void onClick(View v) {
        int mPosition = getLayoutPosition();
        reminder = (Reminder) taskList.get(mPosition);
        int reminderId = reminder.getReminderId();
        Intent intent = new Intent(v.getContext(), ReminderActivity.class);
        intent.putExtra(ReminderActivity.EXTRA_REMINDER_ID, reminderId);
        v.getContext().startActivity(intent);
    }
}
