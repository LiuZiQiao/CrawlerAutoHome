package com.lxk.crawler.autohome.service.imp;

import com.lxk.crawler.autohome.mapper.CarTestMapper;
import com.lxk.crawler.autohome.pojo.CarTest;
import com.lxk.crawler.autohome.service.CarTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CarTestServiceImpl implements CarTestService {

    @Autowired
    private CarTestMapper carTestMapper;
    @Override
    public List<String> queryTitleByPage(int page, int rows) {
        int start = (page-1)*rows;
        Map<Object,Object> map = new HashMap<>();
        map.put("start",start);
        map.put("rows",rows);
        List<String> list = carTestMapper.queryTitleByPage(map);
        return list;
    }

    @Override
    public void saveCarTest(CarTest carTest) {
        carTestMapper.saveCarTest(carTest);
    }
}
