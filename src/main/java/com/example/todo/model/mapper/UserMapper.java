package com.example.todo.model.mapper;

import com.example.todo.model.dto.user.CreateUserRequest;
import com.example.todo.model.dto.user.UserResponse;
import com.example.todo.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
@Mapping(target = "role", expression = "java(com.example.todo.model.enums.Role.USER)")
@Mapping(target = "tasks", ignore=true)
    User toEntity(CreateUserRequest create);

    UserResponse toResponse(User user);
    List<UserResponse> toResponseList(List<User> users);
}
