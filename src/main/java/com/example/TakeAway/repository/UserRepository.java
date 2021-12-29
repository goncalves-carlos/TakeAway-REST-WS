package com.example.TakeAway.repository;

import com.example.TakeAway.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
