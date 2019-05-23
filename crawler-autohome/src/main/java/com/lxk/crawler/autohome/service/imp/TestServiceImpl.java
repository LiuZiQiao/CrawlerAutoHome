package com.lxk.crawler.autohome.service.imp;

import com.lxk.crawler.autohome.mapper.TestMapper;
import com.lxk.crawler.autohome.service.CarTestService;
import com.lxk.crawler.autohome.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private TestMapper testMapper;
    @Autowired
    private CarTestService carTestService;
    @Override
    public String queryDate() {
       String date = this.testMapper.queryDate();
        return date;
    }

}
