package dev.chiedo.booksrestapi.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import dev.chiedo.booksrestapi.model.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {

    Book findByTitle(String title);
}
