package com.automation.pages.google;

import com.automation.framework.annotations.LazyAutowired;
import com.automation.framework.annotations.Page;
import com.automation.pages.BasePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@Page
public class GoogleApp extends BasePage {

    @LazyAutowired
    private GoogleHomePage homePage;

    @LazyAutowired
    private GoogleResultsPage resultsPage;

    @Value("${app.url:http://ww.google.com}")
    private String url;

    public void goTo(){
        this.driver.get(url);
    }

    public GoogleResultsPage getResultsPage(){
        return this.resultsPage;
    }

    public GoogleHomePage getHomePage(){
        return this.homePage;
    }

    @Override
    public boolean isAt() {
        return this.homePage.isAt();
    }

    public void close(){
        this.driver.quit();
    }
}
