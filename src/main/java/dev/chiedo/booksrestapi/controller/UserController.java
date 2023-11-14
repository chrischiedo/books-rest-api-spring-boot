package dev.chiedo.booksrestapi.controller;

import dev.chiedo.booksrestapi.model.Book;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.chiedo.booksrestapi.repository.UserRepository;
import dev.chiedo.booksrestapi.model.User;
import dev.chiedo.booksrestapi.userdetails.UserRegistrationPayload;


@RestController
//@RequestMapping(value = "api/users", produces = "application/json", consumes = "application/json")
@RequestMapping("/api/users")
@Tag(name = "User Controller", description = "Controller for managing users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    // @Autowired
    private final UserRepository repository;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "Creates a new user")
    @ApiResponse(responseCode = "200", description = "New user successfully registered",
            content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = UserRegistrationPayload.class))})
    @PostMapping("/register")
    public String register(@RequestBody @Valid UserRegistrationPayload requestPayload) {
        var user = new User();
        user.setUsername(requestPayload.username());
        user.setPassword(passwordEncoder.encode(requestPayload.password()));
        user.setAuthority(requestPayload.authority());

        repository.save(user);

        LOGGER.debug("New user registered " + user);

        return "New user successfully registered";
    }

    @Operation(summary = "Returns all the registered users")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = User.class))})
    @GetMapping
    public ResponseEntity<Iterable<User>> getAllUsers() {
        // Iterable<User> users = repository.findAll();
        var users = repository.findAll();

        LOGGER.debug("Total number of registered users " + users);
        return ResponseEntity.ok(users);
    }
}
