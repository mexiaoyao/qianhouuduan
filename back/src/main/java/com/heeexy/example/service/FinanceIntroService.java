package com.heeexy.example.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.FinanceIntroDao;
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
import java.util.Map;

import static com.heeexy.example.util.constants.ErrorEnum.*;

/**
 * @author: heeexy
 * @date: 2017/10/24 16:07
 */
@Service
@Slf4j
public class FinanceIntroService {

    @Autowired
    private FinanceIntroDao mysqlDao;

    @Transactional(rollbackFor = Exception.class)
    public JSONObject importDo(MultipartFile file) {
        int result = 0;
        try {
            List<FinanceIntro> list = ExcelUtils.readMultipartFile(file, FinanceIntro.class);
            for(FinanceIntro financeIntro : list){
                 financeIntro.setId(StringTools.getUUid());
                financeIntro.setCreateTime(DateUtils.getDate());
                financeIntro.setStatus(Constants.TWO_BYTE);
                log.info(financeIntro.toString());
            };
            result = mysqlDao.batchInsert(list);
        }catch (Exception e){
            log.error("导入失败："+e.getMessage());
        }
        if(result>0){
            return CommonUtil.successJson();
        }else{
            return CommonUtil.errorJson(G_add001);
        }
    }

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
    public int statusMysql(JSONObject jsonObject) {
        return mysqlDao.statusMysql(jsonObject);
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
    @Transactional(rollbackFor = Exception.class)
    public int createShreas(String tableName) {
        try {
            return mysqlDao.createShreas(tableName);
        }catch (Exception e){
            log.warn(e.getMessage());
            return 0;
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public int deleteTable(String tableName) {
        return mysqlDao.deleteTable(tableName);
    }
}
