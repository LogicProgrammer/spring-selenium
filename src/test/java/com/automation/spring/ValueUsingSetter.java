package com.automation.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ValueUsingSetter {

    private String fruit;

    @Value("${fruit:watermelon}")
    public void setFruit(String fruit){
        this.fruit = fruit;
    }

    public void printFruit(){
        System.out.println(this.fruit);
    }
}
