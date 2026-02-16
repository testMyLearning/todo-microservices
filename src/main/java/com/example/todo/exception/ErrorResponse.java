package com.example.todo.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
    private int status;
    private String error;
    private String message;
    private String path;

    // 1. Пустой конструктор
    public ErrorResponse() {}

    // 2. Конструктор со всеми полями
    public ErrorResponse(LocalDateTime time, int status, String error, String message, String path) {
        this.time = time;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    // 3. Статический фабричный метод ВМЕСТО билдера
    public static ErrorResponse of(LocalDateTime time, int status, String error, String message, String path) {
        return new ErrorResponse(time, status, error, message, path);
    }

    // 4. Геттеры (обязательно для JSON)
    public LocalDateTime getTime() { return time; }
    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getMessage() { return message; }
    public String getPath() { return path; }

    // 5. Сеттеры (обязательно для JSON)
    public void setTime(LocalDateTime time) { this.time = time; }
    public void setStatus(int status) { this.status = status; }
    public void setError(String error) { this.error = error; }
    public void setMessage(String message) { this.message = message; }
    public void setPath(String path) { this.path = path; }
}
