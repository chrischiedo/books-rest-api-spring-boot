package dev.chiedo.bookapi.service;

import dev.chiedo.bookapi.model.dto.UserRegistrationRequest;
import dev.chiedo.bookapi.model.dto.UserResponse;
import dev.chiedo.bookapi.model.entity.UserEntity;
import dev.chiedo.bookapi.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService {
    private static final String ROLE_USER = "ROLE_USER";
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public UserEntity register(UserRegistrationRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username is already registered");
        }
        UserEntity user = new UserEntity();
        user.setUsername(request.username());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setAuthority(ROLE_USER);
        try {
            return userRepository.save(user);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Username is already registered", ex);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .map(user -> new UserResponse(user.getUserId(), user.getUsername(), user.getAuthority()))
                .toList();
    }
}
