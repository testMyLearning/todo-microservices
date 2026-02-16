package com.example.todo.repository;

import com.example.todo.model.entity.Task;
import com.example.todo.model.enums.StatusTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    List<Task> findAllByStatus(StatusTask status);
    List<Task> findByUserEmail(String email);
    List<Task> findByUserEmailAndStatus(String email, StatusTask status);

@Query("select t from Task t where t.user.id = :id")
    List<Task> findTasksById(@Param("id") Long id);
}
