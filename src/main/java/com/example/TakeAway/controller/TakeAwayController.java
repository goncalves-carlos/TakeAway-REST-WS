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

    /*

    * This function send us to the login page where we have to insert the credentials.

    */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /*

    * This function takes the credentials and first check if the user exist.
    * If the user exist then we check if it is a client or an owner. If it does next exist that we stay in the login page.
    * Next part we check if it is a client or an owner. If it is an owner we will be sent to the "/dishes" page.
    * If we are a client then we will be sent to the "/dishesClient" page.
    * If we are a client then we check if an unfinished order exist. If it is the case then we take it and reset it.
    * If it is not the case then we create a new one.

    */
    @GetMapping("/login/insert")
    public String goToWS(@ModelAttribute("user") User user, Model model) {
        String msgLogin = userRepository.findByUsernameEqualsAndPasswordEquals(user.getUsername(), user.getPassword()).toString();
        if (msgLogin.equals("Optional.empty")) {
            return "login";
        }

        User existingUser = userRepository.findByUsernameEqualsAndPasswordEquals(user.getUsername(), user.getPassword()).get();

        String msgOrder = orderRepository.findOrderByFinishedIsFalse().toString();
        if (existingUser.getUserType().equals("client")) {
            model.addAttribute("dishesOpener", repository.findAllByCategoriesEquals("OPENER"));
            model.addAttribute("dishesMain", repository.findAllByCategoriesEquals("MAIN"));
            model.addAttribute("dishesDesert", repository.findAllByCategoriesEquals("DESERT"));
            Order order;
            if (msgOrder.equals("Optional.empty")) {
                order = new Order();
            } else {
                order = orderRepository.findOrderByFinished(false).get();
                order.removeAll();
            }
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

    /*

    * This function makes us go to the html file where the owner can add, update or delete a dish.

    */
    @GetMapping("/dishes")
    public String listDishes(Model model) {
        model.addAttribute("dishes", repository.findAll());
        return "dishes";
    }

    /*

    * This function makes us go to the html file where we add information to add the dish to the collection.

    */
    @GetMapping("/dishes/new")
    public String createDishForm(Model model) {
        Dish dish = new Dish();
        model.addAttribute("dish", dish);
        return "create_dish";
    }

    /*

    * This function stores the added dish into to collection and we will return to the main page of the owner.

    */
    @PostMapping("/dishes")
    public String saveDish(@ModelAttribute("dish") Dish dish) {
        repository.save(dish);
        return "redirect:/dishes";
    }

    /*

    * This function makes us go to the html file where we update/change the existing dish from the collection.

    */
    @GetMapping("/dishes/edit/{id}")
    public String editDishForm(@PathVariable String id, Model model) {
        model.addAttribute("dish", repository.findById(id).get());
        return "edit_dish";
    }

    /*

    * This function stores the updates/changed dish into to collection, and we will return to the main page of the owner.

    */
    @PostMapping("/dishes/{id}")
    public String updateDish(@PathVariable String id, @ModelAttribute("dish") Dish dish) {
        Dish existingDish = repository.findById(id).get();
        existingDish.setName(dish.getName());
        existingDish.setCategories(dish.getCategories());
        existingDish.setPrice(dish.getPrice());
        repository.save(existingDish);
        return "redirect:/dishes";
    }

    /*

    * This function deletes a Dish from the collection.

    */
    @GetMapping("/dishes/{id}")
    public String deleteDish(@PathVariable String id) {
        repository.deleteById(id);
        return "redirect:/dishes";
    }

    /* *********** */
    /* Client View */
    /* *********** */

    /*

    * This function send us to the html where the client can see and order the dishes.

    */
    @GetMapping("/dishesClient")
    public String listDishesClient(Model model) {
        model.addAttribute("dishesOpener", repository.findAllByCategoriesEquals("OPENER"));
        model.addAttribute("dishesMain", repository.findAllByCategoriesEquals("MAIN"));
        model.addAttribute("dishesDesert", repository.findAllByCategoriesEquals("DESERT"));
        return "index";
    }

    /*

    * This function send the dish to the order.
    * First it checks if an unfinished order exists. If not create one and add the dish to the order.
    * If it does then we add the dish to the order.

    */
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
        orderRepository.save(order);
        model.addAttribute("orders", order);
        model.addAttribute("dishesOpener", repository.findAllByCategoriesEquals("OPENER"));
        model.addAttribute("dishesMain", repository.findAllByCategoriesEquals("MAIN"));
        model.addAttribute("dishesDesert", repository.findAllByCategoriesEquals("DESERT"));
        return "index";
    }

    /*

    * This function deletes a dish from an order.
    * First it checks if an order exist and then checks if the dish is in the order.
    * If it is that it will be removed. If not than nothing happens.

    */
    @GetMapping("dishesClient/{id}")
    public String deleteFromCart(@PathVariable String id, Model model) {
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

    /*

    * This function send us to a page where we will finalize the order.

    */
    @GetMapping("/dishesClient/placeOrder")
    public String placeOrder(Model model) {
        Order order = orderRepository.findOrderByFinished(false).get();
        model.addAttribute("order", order);

        return "create_order";
    }

    /*

    * This function takes the information from the page which is the address
    * It will then store the information to the document.
    * To finish of it will then make the order finished by turing the variable isFinished to "true".

    */
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
}
