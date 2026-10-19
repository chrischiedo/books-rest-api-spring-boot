package dev.chiedo.bookapi.service;

import dev.chiedo.bookapi.model.dto.*;
import dev.chiedo.bookapi.model.entity.BookEntity;
import dev.chiedo.bookapi.model.entity.UserEntity;
import dev.chiedo.bookapi.repository.BookRepository;
import dev.chiedo.bookapi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BookServiceImpl(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public List<BookResponse> findAll() {
        return StreamSupport.stream(bookRepository.findAll().spliterator(), false).map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public Optional<BookResponse> findById(long bookId) {
        return bookRepository.findById(bookId).map(this::toResponse);
    }

    @Override
    @Transactional
    public Optional<BookResponse> findByTitle(String title) {
        return Optional.ofNullable(bookRepository.findByTitle(title)).map(this::toResponse);
    }

    @Override
    @Transactional
    public BookResponse save(BookCreateRequest request, Long ownerId) {
        UserEntity owner = userRepository.findById(ownerId).orElseThrow(() -> new IllegalArgumentException("User not found"));
        BookEntity book = new BookEntity();
        apply(book, request);
        book.setOwner(owner);
        return toResponse(bookRepository.save(book));
    }

    @Override
    @Transactional
    public void update(long bookId, BookUpdateRequest request, Long authenticatedUserId, boolean admin) {
        BookEntity book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        if (!admin && !book.getOwner().getUserId().equals(authenticatedUserId)) {
            throw new org.springframework.security.access.AccessDeniedException("You can only edit books you created");
        }
        apply(book, request);
        bookRepository.save(book);
    }

    @Override
    @Transactional
    public void delete(long bookId) {
        BookEntity book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
        bookRepository.delete(book);
    }

    private void apply(BookEntity book, BookCreateRequest request) {
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setDescription(request.description());
        book.setIsbn(request.isbn());
    }
    private void apply(BookEntity book, BookUpdateRequest request) {
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setDescription(request.description());
        book.setIsbn(request.isbn());
    }
    private BookResponse toResponse(BookEntity book) {
        UserEntity owner = book.getOwner();
        return new BookResponse(
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getDescription(),
                book.getIsbn(),
                owner.getUserId(),
                owner.getUsername());
    }
}
