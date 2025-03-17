package dev.chiedo.bookapi.service;

import dev.chiedo.bookapi.mapper.IMapper;
import dev.chiedo.bookapi.model.dto.BookDto;
import dev.chiedo.bookapi.model.entity.BookEntity;
import dev.chiedo.bookapi.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final IMapper<BookEntity, BookDto> bookMapper;

    // constructor injection
    public BookServiceImpl(BookRepository bookRepository, IMapper<BookEntity, BookDto> bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public List<BookDto> findAll() {
        List<BookEntity> employees = StreamSupport
                .stream(
                        bookRepository.findAll().spliterator(),
                        false)
                .toList();

        return employees.stream()
                .map(bookMapper::mapTo)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<BookDto> findById(long bookId) {

        return bookRepository.findById(bookId).map(bookMapper::mapTo);
    }

    @Override
    public BookDto findByTitle(String title) {
        BookEntity bookEntity = bookRepository.findByTitle(title);
        return bookMapper.mapTo(bookEntity);
    }

    @Override
    @Transactional
    public void save(BookDto bookDto) {
        BookEntity bookEntity = bookMapper.mapFrom(bookDto);
        bookRepository.save(bookEntity);
    }

    @Override
    @Transactional
    public void update(long bookId, BookDto bookDto) {
        // check if book exists
        Optional<BookEntity> optionalBook = bookRepository.findById(bookId);

        if (optionalBook.isEmpty()) {
            throw new RuntimeException("Book not found");
        }

        BookEntity bookEntity = optionalBook.get();

        bookEntity.setBookId(bookId);
        bookEntity.setTitle(bookDto.getTitle());
        bookEntity.setAuthor(bookDto.getAuthor());
        bookEntity.setDescription(bookDto.getDescription());
        bookEntity.setIsbn(bookDto.getIsbn());

        bookRepository.save(bookEntity);
    }

    @Override
    @Transactional
    public void delete(BookDto bookDto) {
        BookEntity bookEntity = bookMapper.mapFrom(bookDto);

        bookRepository.delete(bookEntity);
    }
}
