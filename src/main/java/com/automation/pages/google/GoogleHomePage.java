package com.automation.pages.google;


import com.automation.framework.annotations.PageComponent;
import com.automation.pages.BasePage;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

@PageComponent
public class GoogleHomePage extends BasePage {

    @FindBy(name = "q")
    private WebElement searchBox;

    @FindBy(name = "btnK")
    private List<WebElement> searchButtons;

    public void search(final String keyword) {
        searchBox.sendKeys(keyword);
        searchBox.sendKeys(Keys.TAB);
        this.searchButtons.stream().filter(button -> button.isDisplayed() && button.isEnabled())
                .findFirst().ifPresent(WebElement::click);
    }

    @Override
    public boolean isAt() {
        return this.wait.until((d) -> this.searchBox.isDisplayed());
    }
}
