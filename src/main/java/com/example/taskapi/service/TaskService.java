package com.example.taskapi.service;

import com.example.taskapi.entity.Task;

import java.util.List;

public interface TaskService {
	Task createTask(Task task);
	List<Task> getAllTasks();
    List<Task> getTasksByUser(Long userId);
    List<Task> getCompletedTasks();
    List<Task> getCompletedTasksByUser(Long userId); 

}