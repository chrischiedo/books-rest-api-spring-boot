package dev.chiedo.booksrestapi.repository;


import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import dev.chiedo.booksrestapi.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findAppUserByUsername(String username);
}
