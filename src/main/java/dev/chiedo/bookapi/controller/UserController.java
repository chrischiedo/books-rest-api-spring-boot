package dev.chiedo.bookapi.controller;

import dev.chiedo.bookapi.model.entity.UserEntity;
import dev.chiedo.bookapi.repository.UserRepository;
import dev.chiedo.bookapi.user.UserRegistrationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    // constructor injection
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    @Operation(summary = "Creates a new user")
    @ApiResponse(responseCode = "200", description = "New user successfully registered",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserRegistrationDto.class))})
    public String registerNewUser(@RequestBody @Valid UserRegistrationDto registrationDto) {
        UserEntity user = new UserEntity();
        user.setUsername(registrationDto.username());
        user.setPassword(passwordEncoder.encode(registrationDto.password()));
        user.setAuthority(registrationDto.authority());

        userRepository.save(user);

        LOGGER.info("New user registered {}", user);

        return "New user successfully registered";
    }

    @Operation(summary = "Returns all the registered users")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = UserEntity.class))})
    @GetMapping
    public ResponseEntity<Iterable<UserEntity>> getAllUsers() {
        Iterable<UserEntity> users = userRepository.findAll();

        LOGGER.info("Total number of registered users {}", users);

        return ResponseEntity.ok(users);
    }
}
