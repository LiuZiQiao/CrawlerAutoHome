package com.lxk.crawler.autohome.service;

public interface ApiService {
    /*
     * Get 请求获取页面数据
     *
     */
    public String getHtml(String url);

    /*
     * Get请求下载图片
     *
     */
    public String getImage(String url);
}
