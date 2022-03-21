package com.automation.framework.service.webdriver;

import com.automation.framework.model.browser.Template;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;

public class WebDriverProvider {

    public WebDriver createDriver(Template template) throws MalformedURLException {
        LocalWebDriverProvider localWebDriverProvider = new LocalWebDriverProvider();
        RemoteWebDriverProvider remoteWebDriverProvider = new RemoteWebDriverProvider();
        Browser browser = Browser.valueOf(template.getBrowserName());
        WebDriver driver;
        switch (browser) {
            case chrome:
                driver = localWebDriverProvider.getChromeDriver(template);
                break;
            case firefox:
                driver = localWebDriverProvider.getFirefoxDriver(template);
                break;
            case edge:
                driver = localWebDriverProvider.getEdgeDriver(template);
                break;
            case remote:
                driver = remoteWebDriverProvider.getRemoteWebDriver(template);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + browser);
        }
        return driver;
    }

}
