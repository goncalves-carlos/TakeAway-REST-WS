package com.example.TakeAway.repository;

import com.example.TakeAway.model.Dish;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DishRepository extends MongoRepository<Dish, String> {
}
