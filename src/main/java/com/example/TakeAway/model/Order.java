package com.example.TakeAway.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString

@Document(collection = "Order")
public class Order {
    @Id
    public String id;
    public String customer_id;
    public String dish_id;
    public double price;
    public Address address;
    public boolean finished = false;
}
