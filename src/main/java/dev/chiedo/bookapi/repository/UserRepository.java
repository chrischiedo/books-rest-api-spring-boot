package dev.chiedo.bookapi.repository;

import dev.chiedo.bookapi.model.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {

    Optional<UserEntity> findAppUserByUsername(String username);
}
