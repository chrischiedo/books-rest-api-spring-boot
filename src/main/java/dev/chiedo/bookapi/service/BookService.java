package dev.chiedo.bookapi.service;

import dev.chiedo.bookapi.model.dto.BookCreateRequest;
import dev.chiedo.bookapi.model.dto.BookResponse;
import dev.chiedo.bookapi.model.dto.BookUpdateRequest;

import java.util.List;
import java.util.Optional;


public interface BookService {

    List<BookResponse> findAll();
    Optional<BookResponse> findById(long bookId);
    Optional<BookResponse> findByTitle(String title);
    BookResponse save(BookCreateRequest request, Long ownerId);
    void update(long bookId, BookUpdateRequest request, Long authenticatedUserId, boolean admin);
    void delete(long bookId);
}
