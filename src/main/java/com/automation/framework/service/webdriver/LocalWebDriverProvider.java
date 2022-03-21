package com.automation.framework.service.webdriver;

import com.automation.framework.model.browser.GeoLocation;
import com.automation.framework.model.browser.RegisterBasicAuth;
import com.automation.framework.model.browser.Template;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.HasAuthentication;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.UsernameAndPassword;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v96.emulation.Emulation;
import org.openqa.selenium.devtools.v96.log.Log;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Slf4j
public class LocalWebDriverProvider extends WebDriverUtilities{

    public WebDriver getChromeDriver(Template template){
        ChromeOptions options = getChromeOptions(template);
        setUpDriver(template.getBrowserName(),template.getDriver());
        ChromeDriver driver = new ChromeDriver(options);
        return resolveChromiumOptions(driver,template);
    }

    public WebDriver getEdgeDriver(Template template) {
        EdgeOptions options = getEdgeOptions(template);
        setUpDriver(template.getBrowserName(),template.getDriver());
        EdgeDriver driver = new EdgeDriver(options);
        return resolveChromiumOptions(driver,template);
    }

    public WebDriver getFirefoxDriver(Template template){
        FirefoxOptions firefoxOptions = getFirefoxOptions(template);
        setUpDriver(template.getBrowserName(),template.getDriver());
        FirefoxDriver driver =  new FirefoxDriver(firefoxOptions);
        driver.manage().window().maximize();
        return driver;
    }

    public WebDriver resolveChromiumOptions(ChromiumDriver driver, Template template){
        DevTools devTools = driver.getDevTools();
        devTools.createSession();
        if (Objects.nonNull(template.getGeoLocation())) {
            GeoLocation location = template.getGeoLocation();
            log.info("setting up geolocation : {}", location);
            devTools.send(Emulation.setGeolocationOverride(Optional.of(location.getLatitude()),
                    Optional.of(location.getLongitude()), Optional.of(location.getAccuracy())));
        }
        if (Objects.nonNull(template.getCaptureLog())) {
            if (template.getCaptureLog()) {
                devTools.send(Log.enable());
                devTools.addListener(Log.entryAdded(),
                        logEntry -> {
                            log.info("[browser-console] - {}",logEntry.getText());
                        });
            }
        }
        if (Objects.nonNull(template.getCaptureJsExceptions())) {
            if (template.getCaptureJsExceptions()) {
                Consumer<JavascriptException> addEntry = exception->{
                    log.error("[Javascript Exception] - {}", ExceptionUtils.getStackTrace(exception));
                };
                devTools.getDomains().events().addJavascriptExceptionListener(addEntry);
            }
        }
        if (Objects.nonNull(template.getBasicAuths())) {
            List<RegisterBasicAuth> auths = template.getBasicAuths();
            auths.forEach(auth -> {
                Predicate<URI> uriPredicate = uri -> uri.getHost().contains(auth.getUrl());
                ((HasAuthentication) driver).register(uriPredicate,
                        UsernameAndPassword.of(auth.getUsername(), auth.getPassword()));
            });
        }
        return driver;
    }



}
