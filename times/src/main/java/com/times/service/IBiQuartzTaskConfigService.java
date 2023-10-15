package com.times.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.times.model.BiQuartzTaskConfig;

import java.util.List;

/**
 * 定时任务配置
 * @author He PanFu
 * @date 2022-04-12 17:51:11
 * @description 针对表【t_quartz_task_config(定时任务配置)】的数据库操作Service
 */
public interface IBiQuartzTaskConfigService extends IService<BiQuartzTaskConfig> {

    /**
     * 获取所有定时任务配置
     * @return 定时任务配置集合
     */
    List<BiQuartzTaskConfig> listBiQuartzTaskConfigAll();

    /**
     * 根据任务编码获取任务配置
     *
     * @param taskCode 任务编码
     * @return 任务配置
     */
    BiQuartzTaskConfig getBiQuartzTaskConfigByCode(String taskCode);
}

