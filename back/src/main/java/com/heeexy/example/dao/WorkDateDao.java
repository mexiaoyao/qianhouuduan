package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.util.model.FinanceIntro;

import java.util.List;

public interface WorkDateDao {

    int addMysql(JSONObject jsonObject);

    int countMysql(JSONObject jsonObject);

    List<JSONObject> listMysql(JSONObject jsonObject);

    List<JSONObject> listAllMysql();

    int updateMysql(JSONObject jsonObject);

    int removeMysql(JSONObject jsonObject);
}
