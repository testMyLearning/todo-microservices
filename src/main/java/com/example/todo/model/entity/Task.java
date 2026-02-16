package com.example.todo.model.entity;

import com.example.todo.model.enums.StatusTask;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
//import org.jspecify.annotations.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(schema = "public",name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID", updatable = false, nullable = false,unique = true)
    private UUID id;

    @Column(nullable = false,length = 20)
    private String name;

    @Column(length = 100)
    private String description;

    @Enumerated(value= EnumType.STRING)
    @Column(nullable = false)  // Добавьте это!
    private StatusTask status;

    @Column(nullable=true)
    private LocalDate deadline;

    @Column(nullable=true)
    private LocalDateTime dateTimeOfCompletion;

    @ManyToOne(optional = true)
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;

    public Task(String name, String description,LocalDate deadline) {
        this.name = name;
        this.description = description;
        this.status = StatusTask.ACTIVE;
        this.deadline = deadline;
    }

    public Task() {
    }
    public LocalDate getDeadline() {
        return deadline;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public LocalDateTime getDateTimeOfCompletion() {
        return dateTimeOfCompletion;
    }

    public void setDateTimeOfCompletion() {
        this.dateTimeOfCompletion = LocalDateTime.now();
    }
    public UUID getId(){
        return id;
}
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusTask getStatus() {
        return status;
    }

    public void setStatus(StatusTask status) {
        this.status = status;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
