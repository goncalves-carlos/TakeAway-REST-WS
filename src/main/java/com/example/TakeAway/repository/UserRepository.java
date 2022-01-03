package com.example.TakeAway.repository;

import com.example.TakeAway.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsernameEqualsAndPasswordEquals(String username, String password);
}
