package com.example.myproject.common.Scheduled;

import com.example.myproject.common.DataSource.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

/**
 * @Program: myproject
 * @Author: haozai
 * @Create: 2019-10-12 11:50
 **/
@Slf4j
@Configuration
public class SchedulerConfig implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ApplicationContext applicationContext;
    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("定时任务已经启动..."+event.getSource());
    }
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(JobFactory jobFactory) throws IOException {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setDataSource(DynamicDataSourceContextHolder.dataSourceList.get("quartz"));
        factory.setJobFactory(jobFactory);
        factory.setQuartzProperties(quartzProperties());
       // factory.setSchedulerName("MyScheduler");//数据库中存储的名字
        //QuartzScheduler 延时启动，应用启动5秒后 QuartzScheduler 再启动
        factory.setStartupDelay(1);
        factory.setApplicationContextSchedulerContextKey("applicationContextKey");
        //可选，QuartzScheduler 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
        factory.setOverwriteExistingJobs(true);
        factory.setWaitForJobsToCompleteOnShutdown(true);//这样当spring关闭时，会等待所有已经启动的quartz job结束后spring才能完全shutdown。
        //设置自动启动，默认为true
        factory.setAutoStartup(true);
        return factory;
    }
    /*
     * 通过SchedulerFactoryBean获取Scheduler的实例
     */
    @Bean(name="scheduler")
    public Scheduler scheduler() throws IOException{
        return schedulerFactoryBean(jobFactory(applicationContext)).getScheduler();
    }
//    @Bean
//    public QuartzInitializerListener executorListener() {
//        return new QuartzInitializerListener();
//    }
    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }
}
