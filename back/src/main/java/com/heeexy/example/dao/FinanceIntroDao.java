package com.heeexy.example.dao;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.util.model.FinanceIntro;

import java.util.List;

public interface FinanceIntroDao {

    int addMysql(JSONObject jsonObject);

    int batchInsert(List<FinanceIntro> list);

    int countMysql(JSONObject jsonObject);

    List<JSONObject> listMysql(JSONObject jsonObject);

    int updateMysql(JSONObject jsonObject);

    int removeMysql(JSONObject jsonObject);
}
