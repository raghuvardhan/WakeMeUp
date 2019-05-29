package com.example.android.wakemeup.Utilities;

import android.annotation.TargetApi;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.example.android.wakemeup.Database.Alarm;
import com.example.android.wakemeup.Database.AppDatabase;
import com.example.android.wakemeup.Database.Reminder;
import com.example.android.wakemeup.Database.Todo;
import com.example.android.wakemeup.R;
import com.example.android.wakemeup.ViewHolders.AlarmViewHolder;
import com.example.android.wakemeup.ViewHolders.ReminderViewHolder;
import com.example.android.wakemeup.ViewHolders.TodoViewHolder;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.example.android.wakemeup.Utilities.AlarmUtilities.TAG;

public class TaskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public List<Alarm> taskList = new ArrayList<>();
    private List<Alarm> alarms;
    private List<Reminder> reminders;
    private List<Todo> todos;
    private final int ALARM = 0, REMINDER = 1, TODO = 2;


    public TaskListAdapter(Context context, List<Alarm> taskList) {
        this.taskList = taskList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        Log.d(TAG, "onCreateViewHolder: viewType : " + viewType);
        switch (viewType) {
            case ALARM:
                View alarmView = inflater.inflate(R.layout.alarm_list_item, parent, false);
                viewHolder = new AlarmViewHolder(alarmView, this, taskList);
                break;
            case REMINDER:
                View reminderView = inflater.inflate(R.layout.reminder_list_item, parent, false);
                viewHolder = new ReminderViewHolder(reminderView, this, taskList);
                break;
            case TODO:
                View todoView = inflater.inflate(R.layout.todo_list_item, parent, false);
                viewHolder = new TodoViewHolder(todoView, this,  taskList);
                break;

        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case ALARM:
                AlarmViewHolder alarmViewHolder = (AlarmViewHolder) viewHolder;
                configureAlarmViewHolder(alarmViewHolder, position);
                break;
            case REMINDER:
                 ReminderViewHolder reminderViewHolder = (ReminderViewHolder) viewHolder;
                configureReminderViewHolder(reminderViewHolder, position);
                break;
            case TODO:
                 TodoViewHolder todoViewHolder = (TodoViewHolder) viewHolder;
                 configureTodoViewHolder(todoViewHolder, position);
                break;
        }
    }

    private void configureReminderViewHolder(ReminderViewHolder reminderViewHolder, int position) {
        Reminder reminder = (Reminder) taskList.get(position);
        if(reminder != null) {
            reminderViewHolder.getReminderDate().setText(reminder.getDueDate() + "");
            reminderViewHolder.getReminderTime().setText(reminder.getTime() + "");
            reminderViewHolder.getReminderTitle().setText(reminder.getTitle());
        }
    }

    private void configureAlarmViewHolder(final AlarmViewHolder alarmViewHolder, int position) {
        final Alarm alarm = (Alarm) taskList.get(position);
        if (alarm != null) {
            alarmViewHolder.getAlarmTime().setText("Time: " + Helper.getTimeinHourMinFormat(alarm.getTime()));
            final Switch alarmEnabledSwitch = alarmViewHolder.getAlarmEnabledSwitch();
            alarmEnabledSwitch.setChecked(alarm.getEnabled());
            alarmEnabledSwitch.setText(alarm.getEnabled() ? "Alarm Enabled" : "Alarm Disabled");
            alarmEnabledSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(alarmEnabledSwitch.isChecked()){
                        AlarmUtilities.enableAlarmEvent(alarmEnabledSwitch.getContext(), alarm);
                        alarmEnabledSwitch.setText("Alarm Enabled");
                    }
                    else{
                        AlarmUtilities.disableAlarmEvent(alarmEnabledSwitch.getContext(), alarm);
                        alarmEnabledSwitch.setText("Alarm Disabled");
                    }
                }
            });
        }
    }

    private void configureTodoViewHolder(TodoViewHolder todoViewHolder, int position) {
        Todo todo = (Todo) taskList.get(position);
        todoViewHolder.getTodoDate().setText(todo.getDate() + "");
        todoViewHolder.getTodoDescription().setText(todo.getDescription() );
        todoViewHolder.getTodoTime().setText(todo.getTime()+"");
    }

    @Override
    public int getItemViewType(int position) {
        if (taskList.get(position) instanceof Alarm && !(taskList.get(position) instanceof Reminder) && !(taskList.get(position) instanceof Todo) ) {
            return ALARM;
        } else if (taskList.get(position) instanceof Todo) {
            return TODO;
        } else if (taskList.get(position) instanceof Reminder){
            return REMINDER;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void setAlarmList(List<Alarm> alarmList) {
        try {
            if (taskList != null && taskList.containsAll(this.alarms)) {
                taskList.removeAll(this.alarms);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.alarms = alarmList;
            taskList.addAll(this.alarms);
            notifyAdapterDataSetChanged();
        }
    }

    public void setReminderList(List<Reminder> reminderList) {
        try{
            if(taskList != null && taskList.containsAll(this.reminders)) {
                taskList.removeAll(this.reminders);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.reminders = reminderList;
            taskList.addAll(this.reminders);
            notifyAdapterDataSetChanged();
        }
    }

    public void setTodoList(List<Todo> todoList) {
        try {
            if (taskList != null && taskList.containsAll(this.todos)) {
                taskList.removeAll(this.todos);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            this.todos = todoList;
            taskList.addAll(this.todos);
            notifyAdapterDataSetChanged();
        }
    }

    @TargetApi(24)
    public void notifyAdapterDataSetChanged() {
        /* do your sorting here */
        taskList.sort(new Comparator<Alarm>() {
            @Override
            public int compare(Alarm o1, Alarm o2) {
                return o1.getTime().compareTo(o2.getTime());
            }
        });

        notifyDataSetChanged();
    }

    public void filter(List<Todo> filteredList){
        taskList.clear();
        taskList.addAll(filteredList);
        notifyAdapterDataSetChanged();
    }
}
