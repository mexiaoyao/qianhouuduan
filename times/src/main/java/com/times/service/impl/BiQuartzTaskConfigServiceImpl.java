package com.times.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.times.mapper.BiQuartzTaskConfigMapper;
import com.times.model.BiQuartzTaskConfig;
import com.times.service.IBiQuartzTaskConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 定时任务配置
 *
 * @author He PanFu
 * @date 2022-04-12 17:51:11
 * @description 针对表【t_bi_quartz_task_config(定时任务配置)】的数据库操作Service实现
 */
@Service
@RequiredArgsConstructor
public class BiQuartzTaskConfigServiceImpl extends ServiceImpl<BiQuartzTaskConfigMapper, BiQuartzTaskConfig> implements IBiQuartzTaskConfigService {

    private final BiQuartzTaskConfigMapper biQuartzTaskConfigMapper;

    @Override
    public List<BiQuartzTaskConfig> listBiQuartzTaskConfigAll() {
        return biQuartzTaskConfigMapper.listBiQuartzTaskConfigAll();
    }

    @Override
    public BiQuartzTaskConfig getBiQuartzTaskConfigByCode(String taskCode) {
        return biQuartzTaskConfigMapper.getBiQuartzTaskConfigByCode(taskCode);
    }
}

