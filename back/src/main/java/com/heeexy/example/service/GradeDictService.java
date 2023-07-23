package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.GradeDictDao;
import com.heeexy.example.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.heeexy.example.util.constants.ErrorEnum.G_add001;
import static com.heeexy.example.util.constants.ErrorEnum.G_update001;

/**
 * @author: heeexy
 * @date: 2017/10/24 16:07
 */
@Service
public class GradeDictService {

    @Autowired
    private GradeDictDao mysqlDao;

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
}
