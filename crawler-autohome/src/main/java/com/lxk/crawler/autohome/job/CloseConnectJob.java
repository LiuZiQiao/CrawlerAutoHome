package com.lxk.crawler.autohome.job;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

//同步执行任务(单线程)，当前任务未完成的情况下，不会启动新的任务
@DisallowConcurrentExecution
public class CloseConnectJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
       //获取Spring容器
        ApplicationContext applicationContext = (ApplicationContext) context.getJobDetail().getJobDataMap().get("context");
        //从Spring容器中获取HTTPClinet的连接管理器，关闭无效连接
//        applicationContext.getBean(PoolingHttpClientConnectionManager.class).closeExpiredConnections();
        PoolingHttpClientConnectionManager pcm = applicationContext.getBean(PoolingHttpClientConnectionManager.class);
        pcm.closeExpiredConnections();
        System.out.println("关闭连接");
    }
}

