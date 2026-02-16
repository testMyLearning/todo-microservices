package com.example.todo.model.dto.user;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @Nullable
        @Size(max = 30,min=3,message = "Имя пользователя должно быть от 3 до 30 символов")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Имя пользователя может содержать только буквы, цифры и подчеркивание")
        String name,
        @Size(min=8,max=30,message = "Пароль должен быть от 8 до 30 символов")
        @Pattern( regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$",
                message = "Пароль должен содержать хотя бы одну цифру, одну строчную и одну заглавную букву"
        )
        @Nullable
        String password


) {
}
