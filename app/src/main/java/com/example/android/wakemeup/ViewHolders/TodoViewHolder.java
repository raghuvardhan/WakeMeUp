package com.example.android.wakemeup.ViewHolders;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.android.wakemeup.Database.Alarm;
import com.example.android.wakemeup.Database.Todo;
import com.example.android.wakemeup.Activities.TodoActivity;
import com.example.android.wakemeup.R;
import com.example.android.wakemeup.Utilities.TaskListAdapter;


import java.util.List;

public class TodoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private TextView todoDescription,todoDate,todoTime;
    private CheckBox todoIsCompleted;
    private Todo todo;
    private List<Alarm> taskList;
    private TaskListAdapter mAdapter;

    public TodoViewHolder(View itemView, TaskListAdapter adapter, List<Alarm> taskList) {
        super(itemView);
        todoTime = (TextView)itemView.findViewById(R.id.todo_time);
        todoDescription = (TextView)itemView.findViewById(R.id.todo_description);
        todoDate = (TextView)itemView.findViewById(R.id.todo_date);
        todoIsCompleted = (CheckBox)itemView.findViewById(R.id.todo_isCompleted);
        this.mAdapter = adapter;
        itemView.setOnClickListener(this);
        this.taskList = taskList;
    }

    public TextView getTodoDescription() {
        return todoDescription;
    }

    public void setTodoDescription(TextView todoDescription) {
        this.todoDescription = todoDescription;
    }

    public TextView getTodoDate() {
        return todoDate;
    }

    public void setTodoDate(TextView todoDate) {
        this.todoDate = todoDate;
    }

    public TextView getTodoTime() {
        return todoTime;
    }

    public void setTodoTime(TextView todoTime) {
        this.todoTime = todoTime;
    }

    public CheckBox getTodoIsCompleted() {
        return todoIsCompleted;
    }

    public void setTodoIsCompleted(CheckBox todoIsCompleted) {
        this.todoIsCompleted = todoIsCompleted;
    }


    @Override
    public void onClick(View v) {
        int mPosition = getLayoutPosition();
        todo = (Todo)taskList.get(mPosition);
        int todoId = todo.getTodoId();
        Intent intent = new Intent(v.getContext(), TodoActivity.class);
        intent.putExtra(TodoActivity.EXTRA_TODO_ID, todoId);
        v.getContext().startActivity(intent);
    }
}
