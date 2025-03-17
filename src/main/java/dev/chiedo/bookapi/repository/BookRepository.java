package dev.chiedo.bookapi.repository;

import dev.chiedo.bookapi.model.entity.BookEntity;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<BookEntity, Long> {

    BookEntity findByTitle(String title);
}
