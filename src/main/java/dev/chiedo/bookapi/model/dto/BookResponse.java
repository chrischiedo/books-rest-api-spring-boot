package dev.chiedo.bookapi.model.dto;

public record BookResponse(
        Long bookId,
        String title,
        String author,
        String description,
        String isbn,
        Long ownerId,
        String ownerUsername) {
}
