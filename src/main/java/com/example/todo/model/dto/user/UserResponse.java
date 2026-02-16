package com.example.todo.model.dto.user;

import com.example.todo.model.entity.Task;
import com.example.todo.model.enums.Role;

import java.util.List;

public record UserResponse(
        String name,
        String email,
        Role role,
        List<Task> tasks
) {
}
