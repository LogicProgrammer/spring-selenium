package com.automation.testng;

import com.automation.framework.annotations.LazyAutowired;
import com.automation.framework.service.ScreenshotService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@SpringBootTest
public class SpringTestngBase extends AbstractTestNGSpringContextTests {

    @LazyAutowired
    protected ScreenshotService screenshotService;

}
