package com.todo.task.client;

import com.todo.common.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service", url = "${user-service.url:http://localhost:8081}")
public interface UserServiceClient {

    /**
     * Получить пользователя по ID
     */
    @GetMapping("/api/users/{id}")
    UserDto getUserById(@PathVariable("id") Long id);

    /**
     * Проверить существование пользователя
     */
    @GetMapping("/api/users/exists")
    Boolean userExists(@RequestParam("id") Long id);
}