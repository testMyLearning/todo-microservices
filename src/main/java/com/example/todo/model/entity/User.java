package com.example.todo.model.entity;

import com.example.todo.model.enums.Role;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(schema = "public",name = "user")
public class User {


    @Id
@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
@Column(nullable = false,length = 20)
    private String name;
@Column(nullable = false,unique = true,length = 50)
    private String email;
@Column(nullable = false,length = 200)
    private String password;
@OneToMany(mappedBy = "user",cascade = CascadeType.ALL)
    private final List<Task> tasks = new ArrayList<>();
    @Enumerated(EnumType.STRING)  // Храним в БД как строку "USER"/"ADMIN"
    @Column()
    private Role role;

    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = Role.USER;
    }
    public Role getRole() {
        return role;
    }
public void setRole(Role role){
this.role=role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Long getId() {
        return id;
    }
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
