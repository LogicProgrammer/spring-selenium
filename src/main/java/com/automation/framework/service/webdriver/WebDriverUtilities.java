package com.automation.framework.service.webdriver;

import com.automation.framework.model.browser.Options;
import com.automation.framework.model.browser.ProxyModel;
import com.automation.framework.model.browser.Template;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.File;
import java.util.Collections;
import java.util.Objects;

@Slf4j
public class WebDriverUtilities {

    public Proxy getProxy(ProxyModel model) {
        Proxy proxy = new Proxy();
        if (model == null) {
            log.info("proxy configuration is ignored");
        } else {
            Proxy.ProxyType type = Proxy.ProxyType.valueOf(model.getProxyType().toUpperCase().trim());
            switch (type) {
                case PAC:
                    proxy.setProxyAutoconfigUrl(model.getPac());
                    break;
                case AUTODETECT:
                case DIRECT:
                    proxy.setProxyType(type);
                    break;
                case MANUAL:
                    if (Objects.nonNull(model.getFtpProxy())) {
                        proxy.setFtpProxy(model.getFtpProxy());
                    } else if (Objects.nonNull(model.getHttpProxy())) {
                        proxy.setHttpProxy(model.getHttpProxy());
                    } else if (Objects.nonNull(model.getSslProxy())) {
                        proxy.setSslProxy(model.getSslProxy());
                    } else if (Objects.nonNull(model.getSock())) {
                        ProxyModel.Sock sock = model.getSock();
                        proxy.setSocksProxy(sock.getSockProxy());
                        proxy.setSocksVersion(sock.getSockVersion());
                        proxy.setSocksUsername(sock.getSockUsername());
                        proxy.setSocksUsername(sock.getSockPassword());
                    } else {
                        log.error("No manual proxy configuration available, skipping the proxy configuration");
                    }
                    break;
                default:
                    proxy.setProxyType(Proxy.ProxyType.SYSTEM);
            }
        }
        return proxy;
    }

    public void setUpDriver(String browserName, String driverDetails) {
        String name = browserName;
        Browser browser = Browser.valueOf(name);
        name = name.equalsIgnoreCase("internetExplorer") ? "ie" : name;
        name = name.equalsIgnoreCase("firefox") ? "gecko" : name;
        String systemKey = "webdriver." + name + ".driver";
        WebDriverManager manager = null;
        switch (browser) {
            case chrome:
                manager = WebDriverManager.chromedriver();
                break;
            case firefox:
                manager = WebDriverManager.firefoxdriver();
                break;
            case edge:
                manager = WebDriverManager.edgedriver();
                break;
            case internetExplorer:
                manager = WebDriverManager.iedriver();
                break;
            case remote :
                manager = null;
            case safari:
                manager = WebDriverManager.safaridriver();
        }
        ;
        if (Objects.nonNull(manager)) {
            if (Objects.nonNull(driverDetails)) {
                if (StringUtils.isNotBlank(driverDetails)) {
                    if (driverDetails.startsWith("@")) {
                        String version = driverDetails.replaceFirst("[@]", "");
                        manager.driverVersion(version).setup();
                    } else {
                        System.setProperty(systemKey, driverDetails);
                    }
                } else {
                    manager.setup();
                }
            } else {
                manager.setup();
            }
        } else {
            System.setProperty(systemKey, driverDetails);
        }
    }

    public ChromeOptions getChromeOptions(Template template) {
        ChromeOptions chromeOptions = new ChromeOptions();
        Options options = template.getOptions();
        if (Objects.nonNull(options)) {
            if (Objects.nonNull(options.getHeadless())) {
                chromeOptions.setHeadless(options.getHeadless());
            }
            if (Objects.nonNull(options.getArgs())) {
                chromeOptions.addArguments(options.getArgs().toArray(new String[0]));
            }
            if (Objects.nonNull(options.getExcludeSwitches())) {
                options.getExcludeSwitches().forEach(arg -> {
                    chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList(arg));
                });
            }
            if (Objects.nonNull(options.getExperimentalOptions())) {
                options.getExperimentalOptions().forEach(chromeOptions::setExperimentalOption);
            }
            if (Objects.nonNull(options.getBinary())) {
                if (new File(options.getBinary()).exists()) {
                    chromeOptions.setBinary(options.getBinary());
                } else {
                    log.error("no binary file is available in the path : {}", options.getBinary());
                }
            }
            if (Objects.nonNull(options.getUnhandledPromptBehaviour())) {
                chromeOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour
                        .valueOf(options.getUnhandledPromptBehaviour().toUpperCase().trim()));
            }
            if (Objects.nonNull(options.getAcceptInsecureCerts())) {
                chromeOptions.setAcceptInsecureCerts(options.getAcceptInsecureCerts());
            }
        }
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        return chromeOptions;
    }

    public EdgeOptions getEdgeOptions(Template template) {
        EdgeOptions edgeOptions = new EdgeOptions();
        Options options = template.getOptions();
        if (Objects.nonNull(options)) {
            if (Objects.nonNull(options.getHeadless())) {
                edgeOptions.setHeadless(options.getHeadless());
            }
            if (Objects.nonNull(options.getArgs())) {
                edgeOptions.addArguments(options.getArgs().toArray(new String[0]));
            }
            if (Objects.nonNull(options.getExcludeSwitches())) {
                options.getExcludeSwitches().forEach(arg -> {
                    edgeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList(arg));
                });
            }
            if (Objects.nonNull(options.getExperimentalOptions())) {
                options.getExperimentalOptions().forEach(edgeOptions::setExperimentalOption);
            }
            if (Objects.nonNull(options.getBinary())) {
                if (new File(options.getBinary()).exists()) {
                    edgeOptions.setBinary(options.getBinary());
                } else {
                    log.error("no binary file is available in the path : {}", options.getBinary());
                }
            }
            if (Objects.nonNull(options.getUnhandledPromptBehaviour())) {
                edgeOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour
                        .valueOf(options.getUnhandledPromptBehaviour().toUpperCase().trim()));
            }
            if (Objects.nonNull(options.getAcceptInsecureCerts())) {
                edgeOptions.setAcceptInsecureCerts(options.getAcceptInsecureCerts());
            }
        }
        edgeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        return edgeOptions;
    }

    public FirefoxOptions getFirefoxOptions(Template template) {
        FirefoxOptions firefoxOptions = new FirefoxOptions();
        Options options = template.getOptions();
        if (Objects.nonNull(options)) {
            if (Objects.nonNull(options.getHeadless())) {
                firefoxOptions.setHeadless(options.getHeadless());
            }
            if (Objects.nonNull(options.getArgs())) {
                firefoxOptions.addArguments(options.getArgs().toArray(new String[0]));
            }
            if (Objects.nonNull(options.getBinary())) {
                if (new File(options.getBinary()).exists()) {
                    firefoxOptions.setBinary(options.getBinary());
                } else {
                    log.error("no binary file is available in the path : {}", options.getBinary());
                }
            }
            if (Objects.nonNull(options.getUnhandledPromptBehaviour())) {
                firefoxOptions.setUnhandledPromptBehaviour(UnexpectedAlertBehaviour
                        .valueOf(options.getUnhandledPromptBehaviour().toUpperCase().trim()));
            }
            if (Objects.nonNull(options.getAcceptInsecureCerts())) {
                firefoxOptions.setAcceptInsecureCerts(options.getAcceptInsecureCerts());
            }
        }
        firefoxOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        return firefoxOptions;
    }

}
