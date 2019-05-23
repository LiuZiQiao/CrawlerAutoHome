package com.lxk.crawler.autohome.utils;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/*
 * 用于下载页面作为过程测试
 */

public class JsoupUrlTest {
    public static void main(String[] args) throws Exception {
        Document document = Jsoup.parse(new URL("https://www.autohome.com.cn/bestauto/1"), 10000);
        FileUtils.writeStringToFile(new File("G:/test.html"),document.html(),"UTF-8");
        String title = document.getElementsByTag("title").first().text();
        System.out.println(title);
    }
}
