package dev.chiedo.bookapi.auth;

import dev.chiedo.bookapi.model.dto.AuthResponse;
import dev.chiedo.bookapi.model.dto.LoginRequest;
import dev.chiedo.bookapi.model.entity.UserEntity;
import dev.chiedo.bookapi.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(request.username(), request.password()));
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        UserEntity user = userRepository.findAppUserByUsername(principal.getUsername()).orElseThrow();
        String token = jwtService.generateToken(user.getUserId(), user.getUsername(), List.of(user.getAuthority()));
        Duration expiration = jwtService.getExpiration();
        return new AuthResponse(token, "Bearer", expiration.toSeconds());
    }

    public AuthResponse issueToken(UserEntity user) {
        String token = jwtService.generateToken(user.getUserId(), user.getUsername(), List.of(user.getAuthority()));
        return new AuthResponse(token, "Bearer", jwtService.getExpiration().toSeconds());
    }
}

