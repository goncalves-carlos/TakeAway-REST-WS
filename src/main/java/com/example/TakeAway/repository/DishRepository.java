package com.example.TakeAway.repository;

import com.example.TakeAway.model.Dish;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface DishRepository extends MongoRepository<Dish, String> {
    List<Dish> findAllByCategoriesEquals(String categories);
}
