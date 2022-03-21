package com.automation.pages.google;

import com.automation.framework.annotations.PageComponent;
import com.automation.pages.BasePage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@PageComponent
public class GoogleResultsPage extends BasePage {

    @FindBy(css = "div[data-async-context*='query']  a[href] h3")
    private List<WebElement> results;

    public int getResultsCount(){
        return this.results.size();
    }

    @Override
    public boolean isAt() {
        return this.wait.until((d)->!this.results.isEmpty());
    }
}
