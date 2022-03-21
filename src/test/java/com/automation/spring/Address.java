package com.automation.spring;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Address {

    @Autowired
    Faker faker;

   /* private String street;*/

    /*public Address() {
        this.street = faker.address().streetAddress();
    }*/

    public String getStreet() {
        return faker.address().streetAddress();
    }
}
