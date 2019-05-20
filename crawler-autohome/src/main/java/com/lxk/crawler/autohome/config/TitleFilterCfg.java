package com.lxk.crawler.autohome.config;

import com.lxk.crawler.autohome.service.CarTestService;
import com.lxk.crawler.autohome.utils.TitleFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class TitleFilterCfg {
    @Autowired
    private CarTestService carTestService;
    @Bean
    public TitleFilter titleFilter(){
        //创建汽车标题的去重过滤器
        TitleFilter titleFilter = new TitleFilter();
        int page = 1,pageSzie = 0;
        do {
            //查询数据库中title数据，防止数据过大，分页查询
            List<String> titles = carTestService.queryTitleByPage(page,500);
            for (String title: titles) {
                titleFilter.add(title);
            }
            page++;
            pageSzie = titles.size();
        }while(pageSzie == 500);
        return titleFilter;
    }
}
