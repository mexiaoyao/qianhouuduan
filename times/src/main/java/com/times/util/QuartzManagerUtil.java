package com.times.util;

import com.times.model.BiQuartzTaskConfig;
import com.times.service.IBiQuartzTaskConfigService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Quartz管理 工具类
 *
 * @author He PanFu
 * @date 2022-03-23 16:19:33
 */
@Slf4j
@Component
public class QuartzManagerUtil implements ApplicationContextAware {

    private static Scheduler scheduler;
    private static IBiQuartzTaskConfigService biQuartzTaskConfigService;

    /**
     * 程序启动开始加载所有定时任务
     */
    public static void initAllJob() {
        log.info("程序启动开始加载所有定时任务");
        final List<BiQuartzTaskConfig> taskConfigAll = biQuartzTaskConfigService.listBiQuartzTaskConfigAll();

        if (CollectionUtils.isEmpty(taskConfigAll)) {
            log.info("定时任务加载数据为空");
            return;
        }

        for (BiQuartzTaskConfig taskConfig : taskConfigAll) {
            CronTrigger cronTrigger = null;
            JobDetail jobDetail = null;
            try {
                cronTrigger = getCronTrigger(taskConfig);
                jobDetail = getJobDetail(taskConfig);
                scheduler.scheduleJob(jobDetail, cronTrigger);
                log.info("编码：{}定时任务加载成功", taskConfig.getTaskCode());
            } catch (Exception e) {
                log.error("编码：{}定时任务加载失败", taskConfig.getTaskCode());
            }

        }
    }

    /**
     * 停止任务
     *
     * @param taskCode 任务编码
     */
    public static void stopJob(String taskCode) throws SchedulerException {
        log.info("停止任务：{}", taskCode);
        scheduler.pauseJob(JobKey.jobKey(taskCode));
    }

    /**
     * 恢复任务
     *
     * @param taskCode 任务编码
     * @throws SchedulerException 异常
     */
    public static void resumeJob(String taskCode) throws SchedulerException {
        log.info("恢复任务：{}", taskCode);
        scheduler.resumeJob(JobKey.jobKey(taskCode));
    }

    /**
     * 移除任务
     *
     * @param taskCode 任务编码
     */
    public static void removeJob(String taskCode) throws SchedulerException {
        log.info("移除任务：{}", taskCode);
        // 获取以前的触发器
        TriggerKey triggerKey = TriggerKey.triggerKey(taskCode);
        // 停止触发器
        scheduler.pauseTrigger(triggerKey);
        // 删除触发器
        scheduler.unscheduleJob(triggerKey);
        // 删除任务
        scheduler.deleteJob(JobKey.jobKey(taskCode));
    }

    /**
     * 重新加载任务
     *
     * @param taskCode 任务编码
     * @throws Exception 异常
     */
    public static void reload(String taskCode) throws Exception {
        log.info("重新加载任务：{}", taskCode);
        final BiQuartzTaskConfig taskConfig = biQuartzTaskConfigService.getBiQuartzTaskConfigByCode(taskCode);
        if (taskConfig == null) {
            log.error("未找到相关Job配置，任务编码：{}", taskCode);
            throw new SchedulerConfigException("未找到相关Job配置");
        }
        // 获取以前的触发器
        TriggerKey triggerKey = TriggerKey.triggerKey(taskCode);
        // 停止触发器
        scheduler.pauseTrigger(triggerKey);
        // 删除触发器
        scheduler.unscheduleJob(triggerKey);
        // 删除原来的job
        scheduler.deleteJob(JobKey.jobKey(taskCode));

        JobDetail jobDetail = getJobDetail(taskConfig);
        CronTrigger cronTrigger = getCronTrigger(taskConfig);
        // 重新加载job
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    /**
     * 添加新任务
     *
     * @param taskCode 任务编码
     * @throws SchedulerConfigException 异常
     */
    public static void addNewJob(String taskCode) throws SchedulerConfigException {
        log.info("添加新任务：{}", taskCode);
        final BiQuartzTaskConfig taskConfig = biQuartzTaskConfigService.getBiQuartzTaskConfigByCode(taskCode);
        if (taskConfig == null) {
            log.error("未找到相关Job配置，任务编码：{}", taskCode);
            throw new SchedulerConfigException("未找到相关Job配置");
        }
        try {
            JobDetail jobDetail = getJobDetail(taskConfig);
            CronTrigger cronTrigger = getCronTrigger(taskConfig);
            scheduler.scheduleJob(jobDetail, cronTrigger);
        } catch (Exception e) {
            log.error("加载定时任务异常", e);
            throw new SchedulerConfigException("加载定时任务异常", e);
        }
    }

    /**
     * 获取任务是否存在
     * <p>
     * STATE_BLOCKED 4 阻塞
     * STATE_COMPLETE 2 完成
     * STATE_ERROR 3 错误
     * STATE_NONE -1 不存在
     * STATE_NORMAL 0 正常
     * STATE_PAUSED 1 暂停
     *
     * @param taskCode 任务编码
     */
    public static Boolean notExists(String taskCode) {
        log.info("获取任务是否存在：{}", taskCode);
        try {
            return scheduler.getTriggerState(TriggerKey.triggerKey(taskCode)) == Trigger.TriggerState.NONE;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 组装JobDetail
     *
     * @param taskConfig 任务配置
     * @return CronTrigger
     * @throws ClassNotFoundException 异常
     */
    private static JobDetail getJobDetail(BiQuartzTaskConfig taskConfig) throws ClassNotFoundException {
        Class<? extends Job> aClass = Class.forName(taskConfig.getClassName()).asSubclass(Job.class);
        return JobBuilder.newJob()
                .withIdentity(JobKey.jobKey(taskConfig.getTaskCode()))
                .withDescription(taskConfig.getDescription())
                .ofType(aClass).build();
    }

    /**
     * 组装CronTrigger
     *
     * @param taskConfig 任务配置
     * @return CronTrigger
     */
    private static CronTrigger getCronTrigger(BiQuartzTaskConfig taskConfig) {
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(taskConfig.getCron());
        return TriggerBuilder.newTrigger()
                .withIdentity(TriggerKey.triggerKey(taskConfig.getClassName()))
                .withSchedule(cronScheduleBuilder)
                .build();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        scheduler = (Scheduler) applicationContext.getBean("scheduler");
        biQuartzTaskConfigService = applicationContext.getBean(IBiQuartzTaskConfigService.class);
    }
}