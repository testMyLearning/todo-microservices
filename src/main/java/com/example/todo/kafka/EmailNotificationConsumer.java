package com.example.todo.kafka;

import com.example.todo.model.dto.email.EmailDto;
import com.example.todo.service.EmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailNotificationConsumer {

    private final EmailService emailService;
    public EmailNotificationConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics ="EMAIL_SENDING_TASKS", groupId = "email-sender-group")
    public void consumeEmailSendingTask(EmailDto email) {
        emailService.sendSimpleMessage(email.getSubject(),email.getText());

}



}
