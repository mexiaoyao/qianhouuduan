package com.heeexy.example.util.model;

import lombok.Data;

import java.util.Map;

/**
 * @Author: zl
 * @Package: com.heeexy.example.util.model
 * @Name: ApiJson
 * @Date: 2023/9/1 10:34
 **/
@Data
public class FinanceMap {

    //账号
    private String account;

    //返回码
    private String code;

    //返回数据
    private Map<String, Object> map;

}
