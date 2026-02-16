package com.example.todo.exception;

public class DeletedFailedException extends RuntimeException {
    public DeletedFailedException(String email){
        super("Не удалось удалить пользователя с таким email "+email);
    }
}
