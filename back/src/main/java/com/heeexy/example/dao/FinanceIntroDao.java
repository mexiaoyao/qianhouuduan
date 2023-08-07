package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public interface FinanceIntroDao {

    int addMysql(JSONObject jsonObject);

    int countMysql(JSONObject jsonObject);

    List<JSONObject> listMysql(JSONObject jsonObject);

    int updateMysql(JSONObject jsonObject);

    int removeMysql(JSONObject jsonObject);
}
