package com.example.TakeAway.controller;

import com.example.TakeAway.model.Dish;
import com.example.TakeAway.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TakeAwayController {
    @Autowired
    DishRepository repository;

    @PostMapping("/addDish")
    public String saveDish(@RequestBody Dish dish) {
        repository.save(dish);
        return "Added Dish with id: " + dish.getId();
    }

    @GetMapping("/findAllDishes")
    public List<Dish> getDishes() {
        return repository.findAll();
    }

    @DeleteMapping("/delete/{id}")
    public String deleteDish(@PathVariable String id) {
        repository.deleteById(id);
        return "Dish deleted with id: " + id;
    }
}
