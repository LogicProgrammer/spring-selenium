package com.automation.testng;

import com.automation.framework.annotations.LazyAutowired;
import com.automation.pages.google.GoogleApp;
import com.google.common.util.concurrent.Uninterruptibles;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ParallelTest01 extends SpringTestngBase {

    @LazyAutowired
    public GoogleApp google;

    @Test
    public void googleTest() throws IOException {
        google.goTo();
        Assert.assertTrue(google.isAt());
        google.getHomePage().search("selenium");
        Assert.assertTrue(google.getResultsPage().isAt());
        Assert.assertTrue( google.getResultsPage().getResultsCount() > 2);
        Uninterruptibles.sleepUninterruptibly(3, TimeUnit.SECONDS);
        this.screenshotService.takeScreenshot("google-search");
        this.google.close();
    }

}
