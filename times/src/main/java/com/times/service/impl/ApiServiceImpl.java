package com.times.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.times.mapper.ApiMapper;
import com.times.model.Api;
import com.times.service.ApiService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ApiServiceImpl extends ServiceImpl<ApiMapper, Api> implements ApiService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer apiDelete(String id){
        return baseMapper.deleteById(id);
    }

}
