package com.heeexy.example.service;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.FinanceApiDao;
import com.heeexy.example.dao.FinanceStatusDao;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.DateUtils;
import com.heeexy.example.util.StringTools;
import com.heeexy.example.util.constants.Constants;
import com.heeexy.example.util.excel.ExcelUtils;
import com.heeexy.example.util.model.FinanceIntro;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.heeexy.example.util.constants.ErrorEnum.*;

/**
 * @author: heeexy
 * @date: 2017/10/24 16:07
 */
@Service
@Slf4j
public class FinanceApiService {

    @Autowired
    private FinanceApiDao mysqlDao;

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
