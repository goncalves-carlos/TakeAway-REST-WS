package com.example.TakeAway.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString

@Document(collection = "Order")
public class Order {
    @Id
    public String id;
    public String customer_id;
    public String dish_id;
    public List<String> dishes_id = new ArrayList<>();
    public int numberOfProducts = 0;
    public double price = 0.0;
    public Address address;
    public boolean finished = false;

    public void addDish(String dish_id) {
        dishes_id.add(dish_id);
    }

    public void removeAll() {
        dishes_id.clear();
        numberOfProducts = 0;
        price = 0.0;
    }

    public void removeDish(String dish_id) {
        dishes_id.remove(dish_id);
        numberOfProducts--;
    }

    public boolean DishExist(String dish_id) {
        return dishes_id.contains(dish_id);
    }

    public double addTotal(double newPrice) {
        this.price += newPrice;
        numberOfProducts++;
        return price;
    }

    public double subTotal(double newPrice) {
        this.price -= newPrice;
        return price;
    }
}
