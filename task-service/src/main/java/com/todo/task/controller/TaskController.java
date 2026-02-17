package com.todo.task.controller;

import com.todo.common.dto.CreateTaskRequest;
import com.todo.common.dto.TaskDto;
import com.todo.common.dto.UpdateTaskRequest;
import com.todo.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    /**
     * Получить все задачи текущего пользователя
     * GET /api/tasks
     */
    @GetMapping
    public ResponseEntity<List<TaskDto>> getUserTasks(
            @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(taskService.getUserTasks(userId));
    }

    /**
     * Получить задачу по ID
     * GET /api/tasks/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTask(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(taskService.getTask(id, userId));
    }

    /**
     * Создать новую задачу
     * POST /api/tasks
     */
    @PostMapping
    public ResponseEntity<TaskDto> createTask(
            @Valid @RequestBody CreateTaskRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(taskService.createTask(request, userId));
    }

    /**
     * Обновить задачу
     * PATCH /api/tasks/{id}
     */
    @PatchMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTaskRequest request,
            @RequestHeader("X-User-Id") Long userId) {
        return ResponseEntity.ok(taskService.updateTask(id, request, userId));
    }

    /**
     * Удалить задачу
     * DELETE /api/tasks/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable UUID id,
            @RequestHeader("X-User-Id") Long userId) {
        taskService.deleteTask(id, userId);
        return ResponseEntity.noContent().build();
    }
}
