package com.lxk.crawler.autohome.service.imp;

import com.lxk.crawler.autohome.App;
import com.lxk.crawler.autohome.service.ApiService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
public class ApiServiceImplTest {

    @Autowired
    private ApiService apiService;
    @Test
    public void getHtml() {
        String html = apiService.getHtml("https://www.autohome.com.cn/bestauto/1");
        Document doc = Jsoup.parse(html);
        System.out.println(doc.select("title").first().text());
    }

    @Test
    public void getImage() {
        String Image = apiService.getImage("https://car2.autoimg.cn/cardfs/product/g1/M08/D1/30/800x0_1_q87_autohomecar__ChsEj1yd6jKAeqjwAAUqCgr0sVo779.jpg");
        System.out.println(Image);
    }
}