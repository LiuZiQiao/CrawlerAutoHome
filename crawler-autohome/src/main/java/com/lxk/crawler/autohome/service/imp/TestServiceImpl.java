package com.lxk.crawler.autohome.service.imp;

import com.lxk.crawler.autohome.mapper.TestMapper;
import com.lxk.crawler.autohome.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
    @Autowired
    private TestMapper testMapper;
    @Override
    public String queryDate() {
       String date = this.testMapper.queryDate();
        return date;
    }
}
