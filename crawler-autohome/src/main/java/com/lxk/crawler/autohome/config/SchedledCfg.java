package com.lxk.crawler.autohome.config;

import com.lxk.crawler.autohome.job.CloseConnectJob;
import com.lxk.crawler.autohome.job.CrawlerAutohomeJob;
import org.quartz.CronTrigger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class SchedledCfg {

    // 定义关闭无效连接任务
    @Bean("crawlerAutohomeJobBean")
    public JobDetailFactoryBean crawlerAutohomeJobBean() {
        //创建任务描述的工厂bean
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        //设置spring容器的key，任务中可以根据这个key获取spring容器
        jobDetailFactoryBean.setApplicationContextJobDataKey("context");
        //设置任务
        jobDetailFactoryBean.setJobClass(CrawlerAutohomeJob.class);
        //设置当前没有触发器和任务绑定，不会删除任务
        jobDetailFactoryBean.setDurability(true);

        return jobDetailFactoryBean;
    }

    // 定义关闭无效连接触发器
    @Bean("crawlerAutohomeJobTrigger")
    // @Qualifier注解通过名字注入bean
    public CronTriggerFactoryBean crawlerAutohomeJobTrigger(
            @Qualifier(value = "crawlerAutohomeJobBean") JobDetailFactoryBean itemJobBean) {
        //创建表达式触发器工厂bean
        CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
        //设置任务描述到触发器
        tigger.setJobDetail(itemJobBean.getObject());
        //设置七子表达式，从0开始每隔5s执行一次，后面*是时分秒，年省略
        //                         s  m h d m
        tigger.setCronExpression("0/5 * * * * ? ");
        return tigger;
    }

    // 定义调度器
    @Bean
    public SchedulerFactoryBean schedulerFactory(CronTrigger[] cronTriggerImpl) {
        //创建任务调度器工厂bean
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        //给任务调度器设置触发器
        bean.setTriggers(cronTriggerImpl);
        return bean;
    }
}
