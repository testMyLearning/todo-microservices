package com.example.todo.model.dto.task;

import com.example.todo.model.enums.StatusTask;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateTaskRequest(
        UUID id,
        @Nullable
        @Size(min = 1,max = 100, message = "Длина заголовка от 1 до 100 символов")
        String name,
        @Nullable
        @Size(min = 1,max = 100, message = "Длина заголовка от 1 до 100 символов")
        String description,
        @Nullable
        StatusTask status,
        @Nullable
        LocalDate deadline


) {
}
