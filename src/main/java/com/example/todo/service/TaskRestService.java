package com.example.todo.service;

import com.example.todo.model.dto.task.CreateTaskRequest;
import com.example.todo.model.dto.task.TaskResponse;
import com.example.todo.model.dto.task.UpdateTaskRequest;
import com.example.todo.model.entity.Task;
import com.example.todo.model.entity.User;
import com.example.todo.model.enums.StatusTask;
import com.example.todo.model.mapper.TaskMapper;
import com.example.todo.repository.TaskRepository;
import com.example.todo.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class TaskRestService {

    private final TaskRepository taskRepository;
    private final TaskMapper mapper;
    private final UserRepository userRepository;

    public TaskRestService(TaskRepository taskRepository, TaskMapper mapper, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    //TODO готов!
    public List<TaskResponse> getAll(long id) {

        List<Task> tasks = taskRepository.findTasksById(id);
        if (tasks.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Задач у пользователя с таким Id нет");
        return mapper.toList(tasks);
    }


    public TaskResponse save(CreateTaskRequest request, Long id) {
        Task savedEntity = mapper.toEntity(request);
        User user = userRepository.findById(id).orElseThrow();
        savedEntity.setUser(user);
        taskRepository.save(savedEntity);
        return mapper.toResponse(savedEntity);
    }

    public void update(UpdateTaskRequest request, long userId) {
        UUID id = request.id();
        User user = userRepository.findById(userId).orElseThrow();
        if (userHasTask(user, id)) {
            Task task = taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Задачи по данному ID не существует"));
            if (request.name() != null && !request.name().isBlank()) {
                task.setName(request.name());
            }
            if (request.description() != null && !request.description().isBlank()) {
                task.setDescription(request.description());
            }
            if (request.status() != null) {
                task.setStatus(request.status());
            }
            if (request.deadline() != null && request.deadline().isAfter(LocalDate.now())) {
                task.setDeadline(request.deadline());
            }
            taskRepository.save(task);

        } else {
            throw new IllegalArgumentException("Задача не принадлежит пользователю");
        }
    }

    public boolean userHasTask(User user, UUID taskUUID) {
        return user.getTasks().stream()
                .anyMatch(task -> task.getId().equals(taskUUID));
        // anyMatch вернет true/false без исключений
    }

    public void delete(UUID id, long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        if (userHasTask(user, id)) {
            Task task = taskRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Задачи по данному ID не существует"));
            taskRepository.delete(task);
        } else {
            throw new IllegalArgumentException("Задача не принадлежит пользователю");
        }
    }

    public TaskResponse getTask(UUID id, long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Задача не найдена"));
        if (userHasTask(user, id)) {
            return mapper.toResponse(task);
        } else {
            throw new IllegalArgumentException("Задача не принадлежит пользователю");
        }
    }
}
