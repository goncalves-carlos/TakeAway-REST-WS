package com.example.TakeAway.controller;

import com.example.TakeAway.model.Address;
import com.example.TakeAway.model.Dish;
import com.example.TakeAway.model.Order;
import com.example.TakeAway.model.User;
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

    /* ***** */
    /* Login */
    /* ***** */

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/login/insert")
    public String goToWS(@ModelAttribute("user") User user, Model model) {
        System.out.println(userRepository.findByUsernameEqualsAndPasswordEquals(user.getUsername(), user.getPassword()));
        String msg = userRepository.findByUsernameEqualsAndPasswordEquals(user.getUsername(), user.getPassword()).toString();
        if (msg.equals("Optional.empty")) {
            return "login";
        }

        User existingUser = userRepository.findByUsernameEqualsAndPasswordEquals(user.getUsername(), user.getPassword()).get();

        if (existingUser.getUserType().equals("client")) {
            System.out.println(repository.findAllByCategoriesEquals("OPENER"));
            System.out.println("new line baby");
            System.out.println(repository.findAll());
            model.addAttribute("dishesOpener", repository.findAllByCategoriesEquals("OPENER"));
            model.addAttribute("dishesMain", repository.findAllByCategoriesEquals("MAIN"));
            model.addAttribute("dishesDesert", repository.findAllByCategoriesEquals("DESERT"));
            Order order = new Order();
            order.setCustomer_id(existingUser.getId());
            orderRepository.save(order);
            return "index";
        } else if (existingUser.getUserType().equals("owner")) {
            model.addAttribute("dishes", repository.findAll());
            return "dishes";
        }

        return "login";
    }

    /* ********** */
    /* Owner View */
    /* ********** */

    // this function makes us go to the html file where the owner can add, update or delete a dish
    @GetMapping("/dishes")
    public String listDishes(Model model) {
        model.addAttribute("dishes", repository.findAll());
        return "dishes";
    }

    // this function makes us go to the html file where we add information to add the dish to the collection
    @GetMapping("/dishes/new")
    public String createDishForm(Model model) {
        Dish dish = new Dish();
        model.addAttribute("dish", dish);
        return "create_dish";
    }

    // this function stores the added dish into to collection and we will return to the main page of the owner
    @PostMapping("/dishes")
    public String saveDish(@ModelAttribute("dish") Dish dish) {
        repository.save(dish);
        return "redirect:/dishes";
    }

    // this function makes us go to the html file where we update/change the existing dish from the collection
    @GetMapping("/dishes/edit/{id}")
    public String editDishForm(@PathVariable String id, Model model) {
        model.addAttribute("dish", repository.findById(id).get());
        return "edit_dish";
    }

    // this function stores the updates/changed dish into to collection, and we will return to the main page of the owner
    @PostMapping("/dishes/{id}")
    public String updateDish(@PathVariable String id, @ModelAttribute("dish") Dish dish) {
        Dish existingDish = repository.findById(id).get();
        existingDish.setName(dish.getName());
        existingDish.setCategories(dish.getCategories());
        existingDish.setPrice(dish.getPrice());
        repository.save(existingDish);
        return "redirect:/dishes";
    }

    // this function deletes a Dish from the collection
    @GetMapping("/dishes/{id}")
    public String deleteDish(@PathVariable String id) {
        repository.deleteById(id);
        return "redirect:/dishes";
    }

    /* *********** */
    /* Client View */
    /* *********** */

    // this function send us to the html where the client can see and order the dishes
    @GetMapping("/dishesClient")
    public String listDishesClient(Model model) {
        model.addAttribute("dishesOpener", repository.findAllByCategoriesEquals("OPENER"));
        model.addAttribute("dishesMain", repository.findAllByCategoriesEquals("MAIN"));
        model.addAttribute("dishesDesert", repository.findAllByCategoriesEquals("DESERT"));
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
        } else {
            order = orderRepository.findOrderByFinished(false).get();
        }
        order.setDish_id(id);
        orderRepository.save(order);
        model.addAttribute("order", order);
        return "create_order";
    }

    @GetMapping("/dishesClient/addToCart/{id}")
    public String addToCart(@PathVariable String id, Model model) {
        String msg = orderRepository.findOrderByFinishedIsFalse().toString();
        Order order;
        if (msg.equals("Optional.empty")) {
            order = new Order();
        } else {
            order = orderRepository.findOrderByFinished(false).get();
        }
        order.setDish_id(id);
        order.addDish(id);
        order.addTotal(repository.findById(id).get().getPrice());
        //order.setPrice(order.setTotal(repository.findById(id).get().getPrice()));
        orderRepository.save(order);
        model.addAttribute("orders", order);
        model.addAttribute("dishesOpener", repository.findAllByCategoriesEquals("OPENER"));
        model.addAttribute("dishesMain", repository.findAllByCategoriesEquals("MAIN"));
        model.addAttribute("dishesDesert", repository.findAllByCategoriesEquals("DESERT"));
        return "index";
    }

    @GetMapping("dishesClient/{id}")
    public String deleteFromCart(@PathVariable String id, Model model) {
        String msg = orderRepository.findOrderByFinishedIsFalse().toString();
        Order order = orderRepository.findOrderByFinished(false).get();
        System.out.println(order.DishExist(id));
        if (order.DishExist(id)) {
            order.removeDish(id);
            order.subTotal(repository.findById(id).get().getPrice());
            orderRepository.save(order);
        }
        model.addAttribute("orders", order);
        model.addAttribute("dishesOpener", repository.findAllByCategoriesEquals("OPENER"));
        model.addAttribute("dishesMain", repository.findAllByCategoriesEquals("MAIN"));
        model.addAttribute("dishesDesert", repository.findAllByCategoriesEquals("DESERT"));
        return "/index";
    }

    @GetMapping("/dishesClient/placeOrder")
    public String placeOrder(Model model) {
        String msg = orderRepository.findOrderByFinishedIsFalse().toString();
        Order order = orderRepository.findOrderByFinished(false).get();
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

        Address address = order.getAddress();
        existingOrder.setAddress(address);

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
