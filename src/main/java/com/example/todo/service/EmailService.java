package com.example.todo.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public void sendSimpleMessage(String themes,String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("swalka1995@yandex.ru");
        message.setSubject(themes);
        message.setTo("radionov1-va@yandex.ru");
        message.setText(text);
        mailSender.send(message);
        System.out.println("✅ Простое письмо отправлено на: " + "radionov1-va@yandex.ru");
    }
public void sendWelcomeEmail(String name){
        sendSimpleMessage(
                "Приветственное письмо",
                "Добро пожаловать в наш планировщик задач!"+ name);
}
public void sendMessageWithTaskCompleted(String userName,String taskName){
    String text = String.format(
            "Привет, %s! Ты выполнил задачу: %s\n" +
                    "Так держать!",
            userName, taskName);
        sendSimpleMessage("Задача выполнена", text);
}


}
