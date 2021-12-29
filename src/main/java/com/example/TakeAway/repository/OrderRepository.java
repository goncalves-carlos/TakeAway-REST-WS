package com.example.TakeAway.repository;

import com.example.TakeAway.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OrderRepository extends MongoRepository<Order, String> {
    Optional<Order> findOrderByFinished(Boolean boo);
    Optional<Order> findOrderByFinishedIsFalse();
}
