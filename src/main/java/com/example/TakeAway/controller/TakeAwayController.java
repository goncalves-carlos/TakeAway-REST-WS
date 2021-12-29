package com.example.TakeAway.controller;

import com.example.TakeAway.model.Dish;
import com.example.TakeAway.model.Order;
import com.example.TakeAway.repository.DishRepository;
import com.example.TakeAway.repository.OrderRepository;
import com.example.TakeAway.repository.UserRepository;
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
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    UserRepository userRepository;

    // Owner View

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

    // Client View

    @GetMapping("/dishesClient")
    public String listDishesClient(Model model) {
        model.addAttribute("dishes", repository.findAll());
        return "index";
    }

    @GetMapping("/dishesClient/newOrder/{id}")
    public String createOrderForm(@PathVariable String id, Model model) {
        System.out.println(orderRepository.findOrderByFinished(false) + " - " + orderRepository.findOrderByFinishedIsFalse());
        String msg = orderRepository.findOrderByFinishedIsFalse().toString();
        System.out.println(msg);
        Order order;
        if (msg.equals("Optional.empty")) {
            order = new Order();
            order.setDish_id(id);
            orderRepository.save(order);
        } else {
            order = orderRepository.findOrderByFinished(false).get();
            order.setDish_id(id);
            orderRepository.save(order);
        }
        model.addAttribute("order", order);
        return "create_order";
    }

    @PostMapping("/dishesClient/{id}")
    public String saveOrder(@PathVariable String id, @ModelAttribute("order") Order order) {
        Order existingOrder = orderRepository.findById(id).get();
        Dish existingDish = repository.findById(existingOrder.getDish_id()).get();
        existingOrder.setCustomer_id(order.getCustomer_id());
        existingOrder.setPrice(existingDish.getPrice());
        existingOrder.setFinished(true);
        orderRepository.save(existingOrder);
        return "redirect:/dishesClient";
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
