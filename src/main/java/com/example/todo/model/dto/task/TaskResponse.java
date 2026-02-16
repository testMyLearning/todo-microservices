package com.example.todo.model.dto.task;

import com.example.todo.model.entity.User;
import com.example.todo.model.enums.StatusTask;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record TaskResponse(
        UUID id,
String name,
StatusTask status,
String description,
LocalDateTime dateTimeOfCompletion,
LocalDate deadline
) {
}
