package com.lxk.crawler.autohome.config;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConnectManager {
    @Bean
    public PoolingHttpClientConnectionManager poolingHttpClientConnectionManager(){
        //创建连接管理器
        PoolingHttpClientConnectionManager pcm = new PoolingHttpClientConnectionManager();
        //设置最大连接数
        pcm.setMaxTotal(100);
        //设置每个主机的最大连接数
        pcm.setDefaultMaxPerRoute(20);
        return pcm;
    }

}
