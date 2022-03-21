package com.automation.bdd;

import com.automation.framework.annotations.LazyAutowired;
import com.automation.pages.google.GoogleApp;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.Assert;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@CucumberContextConfiguration
public class GoogleSteps {

    @LazyAutowired
    GoogleApp app;

    @Given("user is on google home")
    public void userIsOnGoogleHome() {
        app.goTo();
    }

    @When("user searches for {string}")
    public void userSearchesFor(String arg0) {
        app.getHomePage().search(arg0);
    }

    @Then("user gets the results")
    public void userGetsTheResults() {
        Assert.assertTrue(app.getResultsPage().getResultsCount() > 2);
    }
}
