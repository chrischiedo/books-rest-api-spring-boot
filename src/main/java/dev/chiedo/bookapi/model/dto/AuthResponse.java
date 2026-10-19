package dev.chiedo.bookapi.model.dto;

public record AuthResponse(String token, String tokenType, long expiresIn) {
}
