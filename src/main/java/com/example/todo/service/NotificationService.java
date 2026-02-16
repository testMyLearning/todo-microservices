package com.example.todo.service;

import com.example.todo.kafka.EmailNotificationProducer;
import com.example.todo.model.entity.User;
import com.example.todo.model.enums.StatusTask;
import com.example.todo.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final EmailService emailService;
    private final EmailNotificationProducer producer;
    private final UserRepository userRepository;

    public NotificationService(EmailService emailService, EmailNotificationProducer producer, UserRepository userRepository) {
        this.emailService = emailService;
        this.producer = producer;
        this.userRepository = userRepository;
    }

    @Scheduled(cron ="0 0 18 * * *")
    public void sendNotification(){
        producer.sendEmailTask(
                "ежедневное уведомление",
                "Это ежедневное уведомление");
    }
    @Scheduled(cron ="0 59 23 * * *")
    public void sendMessageUsersWithActiveTasks(){
        List<User> users = userRepository.findUsersWithActiveTasks();
        if(!users.isEmpty()){
            users.forEach(user->{
                long countActiveTasks = user.getTasks().stream().filter(task -> task.getStatus() == StatusTask.ACTIVE).count();
                String message = String.format("У вас %d активных задач", countActiveTasks);
                producer.sendEmailTask("Оповещение об оставшихся задачах", message);

});
    }else{
               System.out.println("Нет пользователей с не выполненными заданиями");
}
        }
    @Scheduled(cron ="0 59 23 * * *")
        public void sendMessageUsersWithCompletedTasksInPeriod(){
            LocalDateTime startDate = LocalDateTime.now().minusHours(24);
            LocalDateTime endDate=LocalDateTime.now();
        List<User> users = userRepository.findUsersWithTasksCompletedInPeriod(startDate,endDate);
        users.forEach(user->{
               long completedCount=user.getTasks().stream().filter(date->date.getDateTimeOfCompletion()!=null &&
                       date.getDateTimeOfCompletion().isAfter(startDate)&&
                        date.getDateTimeOfCompletion().isBefore(endDate)).count();
            String message = String.format("выполнили в течение последних суток %d задач ",completedCount);
            if (completedCount >= 1) {
                producer.sendEmailTask("Оповещение о выполненных задачах",message);
            }else{
                System.out.println("нет завершённые задачи за прошлые сутки");
            }


        });
        }

}
