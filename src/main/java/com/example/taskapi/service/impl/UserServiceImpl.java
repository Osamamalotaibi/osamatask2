package com.example.taskapi.service.impl;

import com.example.taskapi.constants.Constants;
import com.example.taskapi.entity.User;
import com.example.taskapi.errors.Messageerrors;
import com.example.taskapi.repository.UserRepository;
import com.example.taskapi.service.UserService;

import java.util.List;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new IllegalArgumentException(Messageerrors.USERNAME_ALREADY_EXISTS);
        }

        if (!user.getUsername().matches(Constants.alhpanumbricRegax)) {
            throw new IllegalArgumentException(Messageerrors.INVALID_USERNAME);
        }

        if (!user.getPassword().matches(Constants.alhpanumbricRegax)) {
            throw new IllegalArgumentException(Messageerrors.INVALID_PASSWORD);
        }

        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}