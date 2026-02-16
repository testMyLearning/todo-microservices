package com.todo.task.service;

import com.todo.common.dto.CreateTaskRequest;
import com.todo.common.dto.TaskDto;
import com.todo.common.dto.UpdateTaskRequest;
import com.todo.common.dto.UserDto;
import com.todo.task.client.UserServiceClient;
import com.todo.task.entity.Task;
import com.todo.task.mapper.TaskMapper;
import com.todo.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы с задачами
 * Теперь с использованием маппера!
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserServiceClient userServiceClient;
    private final TaskMapper taskMapper;  // ← инжектим маппер

    /**
     * Получить все задачи пользователя
     */
    public List<TaskDto> getUserTasks(Long userId) {
        log.info("Getting tasks for user: {}", userId);

        // Проверяем, что пользователь существует
        UserDto user = userServiceClient.getUserById(userId);
        log.debug("User found: {}", user);

        List<Task> tasks = taskRepository.findByUserId(userId);
        return taskMapper.toDtoList(tasks);  // ← используем маппер для списка
    }

    /**
     * Получить задачу по ID
     */
    public TaskDto getTask(UUID taskId, Long userId) {
        Task task = findTaskForUser(taskId, userId);
        return taskMapper.toDto(task);  // ← используем маппер
    }

    /**
     * Создать новую задачу
     */
    @Transactional
    public TaskDto createTask(CreateTaskRequest request, Long userId) {
        log.info("Creating task for user: {}", userId);

        // Проверяем, что пользователь существует
        UserDto user = userServiceClient.getUserById(userId);

        // Используем маппер для создания Entity
        Task task = taskMapper.toEntity(request, userId);
        task = taskRepository.save(task);

        log.info("Task created with id: {}", task.getId());
        return taskMapper.toDto(task);
    }

    /**
     * Обновить задачу
     */
    @Transactional
    public TaskDto updateTask(UUID taskId, UpdateTaskRequest request, Long userId) {
        Task task = findTaskForUser(taskId, userId);

        // Используем маппер для обновления полей
        taskMapper.updateTaskFromRequest(request, task);

        // Отдельно обрабатываем статус (с бизнес-логикой)
        if (request.status() != null) {
            taskMapper.updateTaskStatus(task, request.status());
        }

        task = taskRepository.save(task);
        return taskMapper.toDto(task);
    }

    /**
     * Удалить задачу
     */
    @Transactional
    public void deleteTask(UUID taskId, Long userId) {
        Task task = findTaskForUser(taskId, userId);
        taskRepository.delete(task);
        log.info("Task deleted: {}", taskId);
    }

    /**
     * Найти задачу и проверить принадлежность пользователю
     */
    private Task findTaskForUser(UUID taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found: " + taskId));

        if (!task.getUserId().equals(userId)) {
            throw new RuntimeException("Task does not belong to user: " + userId);
        }

        return task;
    }
}