package com.lxk.crawler.autohome.controller;

import com.lxk.crawler.autohome.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
public class TestController {
    @Autowired
    private TestService testService;

    @RequestMapping("/test")
    public String queryDate(){
        String date = testService.queryDate();
        return  date;
    }

    @RequestMapping("/title")
    @ResponseBody
    public BufferedImage queryTitle() throws Exception {
        return ImageIO.read(new FileInputStream(new File("F:/code/IntelljIDE/crawler-autohome/wy.png")));
    }
}

