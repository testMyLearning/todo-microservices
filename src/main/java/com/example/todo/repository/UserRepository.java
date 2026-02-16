package com.example.todo.repository;

import com.example.todo.model.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    boolean existsByEmail(String email);

    Optional<User> findUserByEmail(String email);

    void deleteByEmail(String email);
    @Query("SELECT DISTINCT u FROM User u JOIN u.tasks t WHERE t.status='ACTIVE'")
    List<User> findUsersWithActiveTasks();
    @Query("SELECT DISTINCT u FROM User u " +
            "JOIN Task t ON u.id = t.user.id " +
            "WHERE t.status = 'COMPLETED' " +
            "AND t.dateTimeOfCompletion BETWEEN :startDate AND :endDate")
    List<User> findUsersWithTasksCompletedInPeriod(
            @Param("startDate") LocalDateTime start,
            @Param("endDate") LocalDateTime end
    );
}
