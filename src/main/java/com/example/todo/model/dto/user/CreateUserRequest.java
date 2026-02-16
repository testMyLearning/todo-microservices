package com.example.todo.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank(message = "Имя не может быть пустым")
        @Size(max = 30,min=3,message = "Имя пользователя должно быть от 3 до 30 символов")
        @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Имя пользователя может содержать только буквы, цифры и подчеркивание")
        String name,
        @NotBlank(message = "Email обязателен")
        @Email(message = "Некорректный формат email")
        @Size(max = 100, message = "Email не должен превышать 100 символов")
        String email,
        @NotBlank(message = "Пароль обязателен")
        @Size(min=8,max=30,message = "Пароль должен быть от 8 до 30 символов")
        @Pattern( regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$",
                        message = "Пароль должен содержать хотя бы одну цифру, одну строчную и одну заглавную букву"
                )
        String password
) {
}
