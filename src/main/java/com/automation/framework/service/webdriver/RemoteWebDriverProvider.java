package com.automation.framework.service.webdriver;

import com.automation.framework.model.browser.Template;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;

@Component
public class RemoteWebDriverProvider extends WebDriverUtilities {

    public WebDriver getRemoteWebDriver(Template template) throws MalformedURLException {
        String url = "http://" + template.getIp() + ":" + template.getPort() + "/wd/hub";
        String remoteBrowser = template.getRemoteBrowserType();
        Browser browser = Browser.valueOf(remoteBrowser);
        RemoteWebDriver webDriver;
        switch (browser) {
            case chrome:
                webDriver = new RemoteWebDriver(new URL(url), getChromeOptions(template));
                break;
            case firefox:
                webDriver = new RemoteWebDriver(new URL(url), getFirefoxOptions(template));
                break;
            case edge:
                webDriver = new RemoteWebDriver(new URL(url), getEdgeOptions(template));
                break;
            default:
                webDriver = new RemoteWebDriver(new URL(url), new DesiredCapabilities());
        }
        return webDriver;
    }


}
