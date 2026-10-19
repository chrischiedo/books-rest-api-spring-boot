package dev.chiedo.bookapi.service;

import dev.chiedo.bookapi.model.dto.UserResponse;
import dev.chiedo.bookapi.model.dto.UserRegistrationRequest;
import dev.chiedo.bookapi.model.entity.UserEntity;

import java.util.List;

public interface UserService {
    UserEntity register(UserRegistrationRequest request);
    List<UserResponse> findAll();
}

