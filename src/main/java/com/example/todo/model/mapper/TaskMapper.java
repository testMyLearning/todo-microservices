package com.example.todo.model.mapper;

import com.example.todo.model.dto.task.CreateTaskRequest;
import com.example.todo.model.dto.task.TaskResponse;
import com.example.todo.model.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
@Mapping(target = "id", ignore = true)
@Mapping(target = "user",ignore=true)
@Mapping(target = "status",constant = "ACTIVE")
@Mapping(target = "dateTimeOfCompletion", ignore = true)
    Task toEntity(CreateTaskRequest create);

    TaskResponse toResponse(Task task);

    List<TaskResponse> toList(List<Task> tasks);
}
