package com.automation.pages;

import com.automation.framework.annotations.LazyAutowired;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;

public abstract class BasePage {

    @LazyAutowired
    protected WebDriver driver;

    @LazyAutowired
    protected WebDriverWait wait;

    @PostConstruct
    private void init(){
        PageFactory.initElements(driver,this);
    }

    public abstract boolean isAt();

}
