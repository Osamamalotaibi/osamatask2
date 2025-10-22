////// src/main/java/com/example/taskapi/repository/TaskRepository.java
package com.example.taskapi.repository;

import com.example.taskapi.entity.Task;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUserId(Long userId);

    List<Task> findByUserIdAndCompleted(Long userId, boolean completed);

    List<Task> findByCompletedTrue();
}

