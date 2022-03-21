package com.automation.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class User {

    // field injection

    @Autowired
    private Salary salary;

    @Autowired
    private Address address;

    // passing values via constructor

    /*
    public User(Salary salary, Address address) {
        this.salary = salary;
        this.address = address;
    }
    */

    //passing values via setter methods

    /*
    @Autowired
    public void setSalary(Salary salary) {
        this.salary = salary;
    }

    @Autowired
    public void setAddress(Address address) {
        this.address = address;
    }
    */

    public void printDetails(){
        System.out.println("address : "+this.address.getStreet());
        System.out.println("salary : "+this.salary.getAmount());
    }
}
