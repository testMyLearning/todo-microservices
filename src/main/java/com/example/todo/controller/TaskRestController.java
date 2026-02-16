package com.example.todo.controller;

import com.example.todo.model.dto.task.CreateTaskRequest;
import com.example.todo.model.dto.task.TaskResponse;
import com.example.todo.model.dto.task.UpdateTaskRequest;
import com.example.todo.model.entity.Task;
import com.example.todo.model.enums.StatusTask;
import com.example.todo.service.TaskRestService;
import com.example.todo.service.TaskService;
import com.example.todo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class TaskRestController {
   private final TaskRestService taskRestService;


    public TaskRestController(TaskRestService taskRestService){
        this.taskRestService = taskRestService;

    }
    //TODO Готов
    @GetMapping("/tasks")
    public List<TaskResponse> getAllTasks(@AuthenticationPrincipal CustomUserDetails userDetails) {
        long id = userDetails.getId();
        return taskRestService.getAll(id);
}
    //TODO Готов
    @PostMapping("/tasks")
    public ResponseEntity<TaskResponse> saveTask(@Valid @RequestBody CreateTaskRequest request,
                                                 @AuthenticationPrincipal CustomUserDetails userDetails){
        Long userid = userDetails.getId();
     TaskResponse response = taskRestService.save(request,userid);
     return ResponseEntity.ok(response);
    }
    //TODO Готов
    @PatchMapping("/tasks")
    public ResponseEntity<?> updateTask(
            @Valid @RequestBody UpdateTaskRequest request,
             @AuthenticationPrincipal CustomUserDetails userDetails){
        long userId = userDetails.getId();
        taskRestService.update(request,userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    //TODO Готов
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<?> delete(@PathVariable(value="id",required=true) UUID id,
                                    @AuthenticationPrincipal CustomUserDetails userDetails){
        long userId = userDetails.getId();
        taskRestService.delete(id,userId);
            return ResponseEntity.noContent().build();
}
    //TODO Готов
    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskResponse> getTask(@PathVariable UUID id,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        long userId = userDetails.getId();
        return ResponseEntity.ok(taskRestService.getTask(id,userId));
    }
}
