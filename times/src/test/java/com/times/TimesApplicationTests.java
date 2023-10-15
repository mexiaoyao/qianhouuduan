package com.times;

import com.times.service.ApiService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class TimesApplicationTests {

    @Resource
    ApiService apiService;

    @Test
    void contextLoads() {
        apiService.apiDelete("vsss1");
    }

}
