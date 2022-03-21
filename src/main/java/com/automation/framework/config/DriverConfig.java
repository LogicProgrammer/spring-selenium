package com.automation.framework.config;

import com.automation.framework.annotations.LazyConfiguration;
import com.automation.framework.annotations.ThreadScopeBean;
import com.automation.framework.model.browser.BrowserOptions;
import com.automation.framework.model.browser.Template;
import com.automation.framework.utilities.YamlUtilities;
import com.automation.framework.service.webdriver.WebDriverProvider;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;

@LazyConfiguration
public class DriverConfig {

    @Value("${timeout:30}")
    private int timeout;

    @Value("classpath:browsers.yaml")
    private Resource browserTemplates;

    @Value("${browser:default}")
    private String templateName;

    @ThreadScopeBean
    public WebDriver webDriver() throws IOException {
        if (!templateName.equals("default")) {
            Template template = getBrowserTemplate();
            return new WebDriverProvider().createDriver(template);
        } else {
            WebDriverManager.chromedriver().setup();
            return new ChromeDriver();
        }
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public WebDriverWait webDriverWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeout));
    }

    public Template getBrowserTemplate() throws IOException {
        BrowserOptions options = YamlUtilities.loadAs(browserTemplates.getFile().getPath(), BrowserOptions.class);
        if (Objects.isNull(options)) {
            throw new IllegalStateException("unable to load browser-config.yaml file, please make sure values are proper");
        }
        if (Objects.isNull(options.getTemplates())) {
            throw new IllegalStateException("No templates available in the file : browser-config.yaml");
        }
        return options.getTemplates().stream().filter(template -> template.getName().equals(templateName))
                .findAny().orElseThrow(() -> new IllegalArgumentException("No template with name : " + templateName));
    }

}
