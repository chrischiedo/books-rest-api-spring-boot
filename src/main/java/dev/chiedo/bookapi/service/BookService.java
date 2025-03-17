package dev.chiedo.bookapi.service;

import dev.chiedo.bookapi.model.dto.BookDto;

import java.util.List;
import java.util.Optional;


public interface BookService {

    List<BookDto> findAll();

    Optional<BookDto> findById(long bookId);

    BookDto findByTitle(String title);

    void save(BookDto bookDto);

    void update(long bookId, BookDto bookDto);

    void delete(BookDto bookDto);
}
