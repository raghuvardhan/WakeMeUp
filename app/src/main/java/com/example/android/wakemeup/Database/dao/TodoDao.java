package com.example.android.wakemeup.Database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.wakemeup.Database.Todo;

import java.util.List;

@Dao
public interface TodoDao {

    @Insert
    public long insertTodo(Todo todo);

    @Delete
    public void deleteTodo(Todo todo);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void updateTodo(Todo todo);

    @Query("SELECT * FROM todo")
    public LiveData<List<Todo>> getTodos();

    @Query("SELECT * FROM todo WHERE todoId = :todoId")
    public Todo getTodoById(int todoId);

    @Query("SELECT * FROM todo WHERE priority = :priority")
    public List<Todo> getTodoByPriority(int priority);

    @Query("SELECT * FROM todo WHERE category = :category")
    public List<Todo> getTodoByCategory(String category);

    @Query("SELECT * FROM todo WHERE state = :state")
    public List<Todo> getTodoByState(boolean state);

}
