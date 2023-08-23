package com.heeexy.example.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.dao.FinanceIntroDao;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.excel.ExcelUtils;
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
        try {
            Map<String, JSONArray> map = ExcelUtils.readFileManySheet(file);
            map.forEach((key, value) -> {
                System.out.println("Sheet名称：" + key);
                System.out.println("Sheet数据：" + value);
                System.out.println("----------------------");
            });
        }catch (Exception e){
            log.error("导入失败："+e.getMessage());
        }
        return CommonUtil.successJson();
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
    public JSONObject removeMysql(JSONObject jsonObject) {
        int result = mysqlDao.removeMysql(jsonObject);
        if(result>0){
            return CommonUtil.successJson();
        }else{
            return CommonUtil.errorJson(G_delete001);
        }
    }
}
