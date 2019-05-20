package com.lxk.crawler.autohome.controller;

import com.lxk.crawler.autohome.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private TestService testService;

    @RequestMapping("/test")
    public String queryDate(){
        String date = testService.queryDate();
        return  date;
    }
}

