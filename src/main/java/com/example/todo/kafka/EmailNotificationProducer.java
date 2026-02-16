package com.example.todo.kafka;

import com.example.todo.model.dto.email.EmailDto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationProducer {

    @Autowired
    private KafkaTemplate<String, EmailDto> kafkaTemplate;

    public void sendEmailTask(String subject, String text) {
        EmailDto email = new EmailDto( subject, text);
        kafkaTemplate.send("EMAIL_SENDING_TASKS", email);
        // ✅ Отправляем ОБЫЧНЫЙ Java объект
    }
}

