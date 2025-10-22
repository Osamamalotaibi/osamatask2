

package com.example.taskapi.service.impl;

import com.example.taskapi.aop.Loggable;
import com.example.taskapi.entity.Task;
import com.example.taskapi.repository.TaskRepository;
import com.example.taskapi.service.TaskService;
import io.micronaut.cache.CacheManager;
import io.micronaut.cache.SyncCache;
import io.micronaut.cache.annotation.CacheInvalidate;
import io.micronaut.cache.annotation.Cacheable;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Primary;
import jakarta.inject.Named;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;
@Named("taskServiceImpl")
@Loggable
@Singleton
@Bean(typed = TaskService.class)
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final CacheManager<SyncCache<?>> cacheManager;
    public TaskServiceImpl(TaskRepository taskRepository,
                           CacheManager<SyncCache<?>> cacheManager) {
        this.taskRepository = taskRepository;
        this.cacheManager = cacheManager;
    }

    @Override
//    @Cacheable("tasksCompleted")
    public List<Task> getCompletedTasks() {
        return taskRepository.findByCompletedTrue();
    }

    @Override
//    @Cacheable("tasksCompletedByUser")
    public List<Task> getCompletedTasksByUser(Long userId) {
        return taskRepository.findByCompletedTrue();
    }

    @Override
//    @CacheInvalidate(value = "tasksAll", all = true)
//    @CacheInvalidate(value = "tasksCompleted", all = true)
    public Task createTask(Task task) {
        Task saved = taskRepository.save(task);
        Long userId = saved.getUser().getId();
//
//        Optional.ofNullable(cacheManager.getCache("tasksByUser"))
//                .ifPresent(c -> c.invalidate(userId));
//        Optional.ofNullable(cacheManager.getCache("tasksCompletedByUser"))
//                .ifPresent(c -> c.invalidate(userId));

//        return saved;
        return saved;
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public List<Task> getTasksByUser(Long userId) {
        return taskRepository.findByUserId(userId);
    }
}
