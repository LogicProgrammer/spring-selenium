package com.automation.spring;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class SpringSeleniumApplicationTests {

	@Autowired
	User user;

	@Autowired
	ValueUsingSetter valueUsingSetter;

	@Autowired
	Faker faker;

	@Autowired
	BeanCycle beanCycle;

	@Value("${path}")
	private String path;

	@Value("${fruits}")
	private List<String> fruits;

	@Test
	void contextLoads() {
		/*Salary salary = new Salary();
		Address address = new Address();
		User user = new User(salary,address);*/
		user.printDetails();
	}

	@Test
	void fakerTest(){
		System.out.println(faker.name().firstName());
	}

	@Test
	void valueTest() {
		System.out.println(this.path);
		System.out.println(this.fruits);
		valueUsingSetter.printFruit();
	}

	@Test
	void beanCycleTest() {
		beanCycle.doesSomething();
	}

}
