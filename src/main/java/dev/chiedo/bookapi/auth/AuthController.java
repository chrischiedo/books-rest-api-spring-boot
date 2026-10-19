package dev.chiedo.bookapi.auth;

import dev.chiedo.bookapi.model.dto.*;
import dev.chiedo.bookapi.model.entity.UserEntity;
import dev.chiedo.bookapi.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final AuthService authenticationService;

    public AuthController(UserService userService, AuthService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user and return a JWT")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid UserRegistrationRequest request) {
        UserEntity user = userService.register(request);
        return ResponseEntity.status(201).body(authenticationService.issueToken(user));
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate a user and return a fresh JWT")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }
}
