package com.todo.task.repository;

import com.todo.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findByUserId(Long userId);

    List<Task> findByUserIdAndStatus(Long userId, String status);

    boolean existsByIdAndUserId(UUID id, Long userId);
}