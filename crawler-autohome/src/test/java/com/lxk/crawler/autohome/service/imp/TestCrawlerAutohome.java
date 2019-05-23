package com.lxk.crawler.autohome.service.imp;

import com.lxk.crawler.autohome.App;
import com.lxk.crawler.autohome.pojo.CarTest;
import com.lxk.crawler.autohome.service.ApiService;
import com.lxk.crawler.autohome.service.CarTestService;
import com.lxk.crawler.autohome.utils.TitleFilter;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.runner.RunWith;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class)
public class TestCrawlerAutohome {

    @Autowired
    private ApiService apiService;
    @Autowired
    private CarTestService carTestService;
    @Autowired
    private TitleFilter titleFilter;

    @Test
    public void queryTitle(){
        int total = 10;
        int i = 0;
        List<List<String>> lists = new ArrayList<>();
        do {
            List<String> list = carTestService.queryTitleByPage(1,i);
            i++;
            lists.add(list);
            System.out.println(list);
        }while (i <= total);
        System.out.println(lists.size());
    }

    @Test
    public void testCrawler() throws Exception {
        //声明爬取的页面url
        String url = "https://www.autohome.com.cn/bestauto/1";
        //使用apiService 爬取页面
        //这里做测试，因此下载页面到  "G:/test.html",这里就直接拿来用
        //jsoup解析页面
        Document domcument = Jsoup.parse(new File("G:/test.html"),"UTF-8");
        Elements divs = domcument.select("#bestautocontent div.uibox");
//        System.out.println(divs.html().toString());
        for(Element div : divs){
            //去重过滤，重复的数据不需要再处理
            String title =div.select("div.uibox-title").first().text();
            if (titleFilter.contains(title)){
                continue;       //如果重复，就遍历下一个
            }
            //页面
            CarTest carTest = this.getCarTest(div);
            //图片
            String Image = this.getCarImage(div);
            carTest.setImage(Image);
            System.out.println(Image);
            this.carTestService.saveCarTest(carTest);
        }
    }

    private String getCarImage(Element div) {
        List<String> images = new ArrayList<>();
       //获取图片的url
        Elements page = div.select("ul.piclist02 li");
        //遍历评测图片的元素
        for (Element element: page) {
//            System.out.println(element.select("a").attr("href"));
            //获取图片展示地址
            String imagePage = "https:"+element.select("a").attr("href");
            //获取图片展示页面
            String html = this.apiService.getHtml(imagePage);
            //解析图片展示页面
            Document doc = Jsoup.parse(html);
            //获取图片的url地址
            String imageUrl ="https:" + doc.getElementById("img").attr("src");
            //下载
            String image = this.apiService.getImage(imageUrl);
            images.add(image);
//            System.out.println(image);
        }
        //图片名称返回，吧集合转为字符串，多个元素用，分隔
        System.out.println("下载图片总数:"+images.size());
        return StringUtils.join(images,",");
    }

    private CarTest getCarTest(Element div) {
        CarTest carTest = new CarTest();
        // 评测车辆标题
        carTest.setTitle(div.select("div.uibox-title").first().text());

        // 评测项目-加速(0-100公里/小时),单位毫秒
//        #bestautocontent > div:nth-child(6) > div > div > div.uibox-con > div:nth-child(1) > div.cartype-l > div.tabbox1 > dl > dd:nth-child(2) > div.dd-div2
        String speed = div.select(".tabbox1 dd:nth-child(2) div.dd-div2").first().text();
        carTest.setTest_speed(this.StrToNum(speed));
        //评测项目-刹车(100-0公里/小时),单位毫米
        String brake = div.select(".tabbox1 dd:nth-child(3) div.dd-div2").first().text();
        carTest.setTest_brake(this.StrToNum(brake));
        // 评测项目-实测油耗(升/100公里),单位毫升
        String oil = div.select(".tabbox1 dd:nth-child(4) div.dd-div2").first().text();
        carTest.setTest_oil(this.StrToNum(oil));

        // 评测编辑1
        carTest.setEditor_name1(div.select(".tabbox2 dd:nth-child(2) > div.dd-div1").first().text());
        // 点评内容1
        carTest.setEditor_remark1(div.select(".tabbox2 dd:nth-child(2) > div.dd-div3").first().text());

        // 评测编辑2
        carTest.setEditor_name2(div.select(".tabbox2 dd:nth-child(3) > div.dd-div1").first().text());
        // 点评内容2
        carTest.setEditor_remark2(div.select(".tabbox2 dd:nth-child(3) > div.dd-div3").first().text());

        // 评测编辑3
        carTest.setEditor_name3(div.select(".tabbox2 dd:nth-child(4) > div.dd-div1").first().text());
        // 点评内容3
        carTest.setEditor_remark3(div.select(".tabbox2 dd:nth-child(4) > div.dd-div3").first().text());

        // 设置时间
        carTest.setCreated(new Date());
        carTest.setUpdated(carTest.getCreated());
        System.out.println(carTest);
        return carTest;
    }

    private int StrToNum(String str) {
        //-- 是因为有的汽车没有说明汽车油耗，是--表示的，因此过滤下
        if (!str.equals("--")){
            str = str.substring(0,str.length()-1);
            System.out.println(str);
            Number num = Float.parseFloat(str)*1000;
            return num.intValue();
        }
        return 0;
    }
}
