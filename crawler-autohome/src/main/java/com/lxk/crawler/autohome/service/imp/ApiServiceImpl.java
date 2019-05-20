package com.lxk.crawler.autohome.service.imp;

import com.lxk.crawler.autohome.service.ApiService;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

@Service
public class ApiServiceImpl implements ApiService {

    //注入HTTPCLient连接管理器
    @Autowired
    private PoolingHttpClientConnectionManager pcm;
    @Override
    public String getHtml(String url) {
        //获取HTTPClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(pcm).build();
        //声明HttpGet请求对象
        HttpGet httpGet = new HttpGet(url);
        //设置用户代理信息
        httpGet.setHeader("User-Agent","");
        //设置请求参数
        httpGet.setConfig(this.getConfig());
        //使用HttpClient发起请求，返回response
        CloseableHttpResponse response = null;
        try {
         response = httpClient.execute(httpGet);
        //解析response数据
            if (response.getStatusLine().getStatusCode() == 200){
                String html = "";
                //如果response.getEntity获取结果是空，执行EntityUtils.toString会报错
                if(response.getEntity() != null){
                   html = EntityUtils.toString(response.getEntity(),"UTF-8");
                }
                return html;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
                try {
                    if (response != null){
                        response.close();
                    }
//                    httpClient.close();  这里不需要在关闭了，连接器已经管理
                } catch (IOException e) {
                    e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String getImage(String url) {
        //获取HTTPClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(pcm).build();
        //声明HttpGet请求对象
        HttpGet httpGet = new HttpGet(url);
        //设置用户代理信息
        httpGet.setHeader("User-Agent","");
        //设置请求参数
        httpGet.setConfig(this.getConfig());
        //使用HttpClient发起请求，返回response
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            //解析response下载图片
            if (response.getStatusLine().getStatusCode() == 200){
                String contentType = response.getEntity().getContentType().getValue();
                //获取文件类型
                String extName = "."+contentType.split("/")[1];
                //使用UUID生成图片名称
                String imageName = UUID.randomUUID().toString()+extName;
                String path = "G:/images/"+imageName;
                OutputStream outputStream = new FileOutputStream(new File(path));
                response.getEntity().writeTo(outputStream);
                return imageName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (response != null){
                    response.close();
                }
//                    httpClient.close();  这里不需要在关闭了，连接器已经管理
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /*
     * 获取请求参数对象
     */

    private RequestConfig getConfig(){
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(1000)    //设置创建连接超时时间
                .setConnectionRequestTimeout(500)        //设置获取连接超时时间
                .setSocketTimeout(10000)                ///设置连接超时时间
                .build();
        return requestConfig;
    }
}
