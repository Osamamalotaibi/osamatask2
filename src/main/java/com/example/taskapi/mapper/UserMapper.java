package com.example.taskapi.mapper;

import com.example.taskapi.dto.RegisterRequest;
import com.example.taskapi.entity.User;

public class UserMapper {

    public static User toEntity(RegisterRequest dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRole("USER");
        return user;
    }
}
