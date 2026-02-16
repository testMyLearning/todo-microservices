package com.example.todo.model.dto.email;

public class EmailDto {
    private String subject;
    private String text;

    public EmailDto(String subject, String text) {
        this.subject = subject;
        this.text = text;
    }

    public EmailDto() {
    }

    // геттеры и сеттеры ОБЯЗАТЕЛЬНЫ для Jackson!
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}
