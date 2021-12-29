package com.example.TakeAway.controller;

import com.example.TakeAway.model.Dish;
import com.example.TakeAway.repository.DishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class TakeAwayController {
    @Autowired
    DishRepository repository;

    @GetMapping("/dishes")
    public String listDishes(Model model) {
        model.addAttribute("dishes", repository.findAll());
        return "dishes";
    }

    @GetMapping("/dishes/new")
    public String createDishForm(Model model) {
        Dish dish = new Dish();
        model.addAttribute("dish", dish);
        return "create_dish";
    }

    @PostMapping("/dishes")
    public String saveDish(@ModelAttribute("dish") Dish dish) {
        repository.save(dish);
        return "redirect:/dishes";
    }

    @GetMapping("/dishes/edit/{id}")
    public String editDishForm(@PathVariable String id, Model model) {
        model.addAttribute("dish", repository.findById(id).get());
        return "edit_dish";
    }

    @PostMapping("/dishes/{id}")
    public String updateDish(@PathVariable String id, @ModelAttribute("dish") Dish dish) {
        Dish existingDish = repository.findById(id).get();
        existingDish.setName(dish.getName());
        existingDish.setCategories(dish.getCategories());
        existingDish.setPrice(dish.getPrice());
        repository.save(existingDish);
        return "redirect:/dishes";
    }

    @GetMapping("/dishes/{id}")
    public String deleteDish(@PathVariable String id) {
        repository.deleteById(id);
        return "redirect:/dishes";
    }

    /*
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

     */
}
