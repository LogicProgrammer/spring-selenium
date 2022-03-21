package com.automation.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class BeanCycle {

    @Value("${name:Vijay}")
    private String name;

    public BeanCycle(){
        System.out.println("I'm in constructor");
        System.out.println("name : "+name);
    }

    @PostConstruct
    private void init(){
        System.out.println("i'm in init..");
        System.out.println("name : "+name);
    }

    public void doesSomething(){
        for(int i=1;i<=10;i++){
            System.out.println("doing something..");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @PreDestroy
    private void close(){
        System.out.println("i'm in close");
    }

}
