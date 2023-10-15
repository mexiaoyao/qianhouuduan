package com.times.config;

import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.annotation.Resource;

/**
 *
 *
 * @author He PanFu
 * @date 2022-03-24 13:48:47
 */
@Configuration
public class QuartzScheduleJobConfig {

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    /**
     * 配置SchedulerFactoryBean，将一个方法产生为Bean并交给Spring容器管理
     * @param jobFactory 任务工厂
     * @return SchedulerFactoryBean
     */
    @Bean("schedulerFactoryBean")
    public SchedulerFactoryBean createFactoryBean(JobFactory jobFactory) {
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        factoryBean.setJobFactory(jobFactory);
        factoryBean.setTaskExecutor(threadPoolTaskExecutor);
        factoryBean.setOverwriteExistingJobs(true);
        return factoryBean;
    }

    /**
     * 通过这个类对定时任务进行操作
     *
     * @param factoryBean 工厂
     * @return 定时任务
     */
    @Bean
    public Scheduler scheduler(@Qualifier("schedulerFactoryBean") SchedulerFactoryBean factoryBean) {
        return factoryBean.getScheduler();
    }
}