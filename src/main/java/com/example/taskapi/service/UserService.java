package com.example.taskapi.service;

import com.example.taskapi.entity.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    List<User> getAllUsers();
}