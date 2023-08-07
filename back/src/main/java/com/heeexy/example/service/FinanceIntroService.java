package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.FinanceIntroDao;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.heeexy.example.util.constants.ErrorEnum.*;

/**
 * @author: heeexy
 * @date: 2017/10/24 16:07
 */
@Service
public class FinanceIntroService {

    @Autowired
    private FinanceIntroDao mysqlDao;

    @Transactional(rollbackFor = Exception.class)
    public JSONObject addMysql(JSONObject jsonObject) {
        int result = mysqlDao.addMysql(jsonObject);
        if(result>0){
            return CommonUtil.successJson();
        }else{
            return CommonUtil.errorJson(G_add001);
        }
    }

    public JSONObject listMysql(JSONObject jsonObject) {
        CommonUtil.fillPageParam(jsonObject);
        int count = mysqlDao.countMysql(jsonObject);
        List<JSONObject> list = mysqlDao.listMysql(jsonObject);
        return CommonUtil.successPage(jsonObject, list, count);
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject updateMysql(JSONObject jsonObject) {
        int result = mysqlDao.updateMysql(jsonObject);
        if(result>0){
            return CommonUtil.successJson();
        }else{
            return CommonUtil.errorJson(G_update001);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public JSONObject removeMysql(JSONObject jsonObject) {
        int result = mysqlDao.removeMysql(jsonObject);
        if(result>0){
            return CommonUtil.successJson();
        }else{
            return CommonUtil.errorJson(G_delete001);
        }
    }
}
