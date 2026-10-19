package dev.chiedo.bookapi.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BookCreateRequest(
        @NotBlank @Size(max = 50) String title,
        @NotBlank @Size(max = 50) String author,
        @NotBlank @Size(max = 1000) String description,
        @NotBlank @Size(max = 50) String isbn) {
}
