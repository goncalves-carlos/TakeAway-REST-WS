package com.example.TakeAway.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Address {
    private String country;
    private String city;
    private String street;
    private int number;
}
