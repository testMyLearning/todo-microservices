package com.example.todo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code= HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id){
        super("Пользователь с таким id не найден"+id);
    }
    public UserNotFoundException(String email){
        super("Пользователь с таким email не найден"+email);
    }
}
