package com.times.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.times.model.Api;

public interface ApiService extends IService<Api> {

    Integer apiDelete(String id);
}
