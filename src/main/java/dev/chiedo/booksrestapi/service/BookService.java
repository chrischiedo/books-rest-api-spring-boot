package dev.chiedo.booksrestapi.service;

import java.util.Optional;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dev.chiedo.booksrestapi.model.Book;
import dev.chiedo.booksrestapi.repository.BookRepository;

@Service
@Transactional(Transactional.TxType.REQUIRED)
public class BookService {
    
//    @Autowired
//    private BookRepository bookRepository;

    @Autowired
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public Book findBookByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public Iterable<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book saveNewBook(@Valid Book newBook) {
        return bookRepository.save(newBook);
    }

    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }
}
