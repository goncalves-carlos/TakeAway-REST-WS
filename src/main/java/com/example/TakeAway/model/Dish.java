package com.example.TakeAway.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@ToString

@Document(collection = "Dishes")
public class Dish {
    @Id
    private String id;
    private String name;
    private Categories categories;
    private double price;
}
