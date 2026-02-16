package com.example.todo.service;

import com.example.todo.kafka.EmailNotificationProducer;
import com.example.todo.model.entity.Task;
import com.example.todo.model.enums.StatusTask;
import com.example.todo.repository.TaskRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Transactional
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final RedisTemplate<String,String> redisTemplate;
    private final EmailService emailService;
    private final EmailNotificationProducer producer;

    public TaskService(TaskRepository taskRepository, RedisTemplate<String, String> redisTemplate, EmailService emailService, EmailNotificationProducer producer) {
        this.taskRepository = taskRepository;
        this.redisTemplate = redisTemplate;
        this.emailService = emailService;
        this.producer = producer;
    }

    public List<Task> getAll(String status) {
        if(status==null || status.isEmpty()) {
            // throw new IllegalArgumentException("Статус не может быть пустым");
            return taskRepository.findAll();
        }
        StatusTask statusEnum=StatusTask.valueOf(status.toUpperCase());
        return taskRepository.findAllByStatus(statusEnum);
    }

    public void saveTask(Task task) {
        if (task.getStatus() == null) {
            task.setStatus(StatusTask.ACTIVE);
        }
        taskRepository.save(task);
}

    public void delete(UUID id) {
        taskRepository.deleteById(id);
    }

    public void changeStatus(UUID id, StatusTask status) {
        Task task = taskRepository.findById(id).orElseThrow(()-> new RuntimeException("Not found"));
        task.setStatus(status);
        if(status.equals(StatusTask.COMPLETED)){
            task.setDateTimeOfCompletion();
        }
        if(task.getUser()!=null){
            String userEmail = task.getUser().getEmail();
            String userName = task.getUser().getName();
            countCompletedTask(userEmail);
            producer.sendEmailTask("Тема "+userEmail,"Сообщение"+ userName);
            //emailService.sendMessageWithTaskCompleted(userName,task.getName());
            System.out.println("Отправлено письмо пользователю"+userEmail);
        }
    }
    // 🔥 НОВЫЙ МЕТОД: Считаем выполненную задачу
    private void countCompletedTask(String userEmail) {
        // Создаем ключ для Redis: "completed:email@example.com"
        String redisKey = "completed:" + userEmail;

        // 1. Увеличиваем счетчик на 1
        redisTemplate.opsForValue().increment(redisKey);

        // 2. Ставим время жизни 24 часа
        // (если ключ уже существует, TTL не меняется)
        redisTemplate.expire(redisKey, 24, TimeUnit.HOURS);
    }

    // 🔥 НОВЫЙ МЕТОД: Получить сколько задач выполнено
    public String getCompletedCount(String userEmail) {
        String redisKey = "completed:" + userEmail;
        String count = redisTemplate.opsForValue().get(redisKey);

        // Если еще не выполнено ни одной задачи
        if (count == null) {
            return "0";
        }

        return count;
    }

    public List<Task> getTasksByUserEmail(String userEmail, String status) {
        if(status!=null && !status.isEmpty()){
            StatusTask statusEnum = StatusTask.valueOf(status.toUpperCase());
            return taskRepository.findByUserEmailAndStatus(userEmail, statusEnum);
        }
        return taskRepository.findByUserEmail(userEmail);
    }



}

