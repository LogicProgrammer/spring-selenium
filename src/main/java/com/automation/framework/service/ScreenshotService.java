package com.automation.framework.service;

import com.automation.framework.utilities.DateUtilities;
import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Lazy
@Service
public class ScreenshotService {

    @Autowired
    private ApplicationContext ctx;

    @Value("${screenshot.path}")
    private Path path;

    @PostConstruct
    public void init() throws InterruptedException {
        File dir = path.toFile();
        if(!dir.exists()){
            dir.mkdirs();
        }
    }

    public void takeScreenshot(final String name) throws IOException {
        File file = this.ctx.getBean(TakesScreenshot.class).getScreenshotAs(OutputType.FILE);
        String dateTime = DateUtilities.getCurrentDate("MM-dd-yyyy'T'hh-mm-ss");
        FileCopyUtils.copy(file,this.path.resolve(name+"-"+
                RandomStringUtils.randomAlphabetic(2) +"-"+dateTime+".png").toFile());
    }

}
