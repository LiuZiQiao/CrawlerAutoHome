package com.lxk.crawler.autohome.service;

import com.lxk.crawler.autohome.pojo.CarTest;

import java.util.List;

public interface CarTestService {
    List<String> queryTitleByPage(int page, int row);

    void saveCarTest(CarTest carTest);
}
