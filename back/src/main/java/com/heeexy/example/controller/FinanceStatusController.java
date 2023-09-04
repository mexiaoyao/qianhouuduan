package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.config.annotation.RequiresPermissions;
import com.heeexy.example.service.FinanceIntroService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.DateUtils;
import com.heeexy.example.util.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/intro")
public class FinanceStatusController {

    @Autowired
    private FinanceIntroService service;

    @RequiresPermissions("intro:import")
    @PostMapping("/import")
    public JSONObject importDo(@RequestPart("file") MultipartFile file) {
        return service.importDo(file);
    }

    @RequiresPermissions("intro:list")
    @PostMapping("/list")
    public JSONObject list(@RequestBody JSONObject requestJson) {
        return service.listMysql(requestJson);
    }

    @RequiresPermissions("intro:add")
    @PostMapping("/add")
    public JSONObject add(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "indexType,codeNumber,sharesName,sharesAlise,sharesTotalNumber,sharesAllowTotalNumber,remarks");
        if(StringTools.isNullOrEmpty(requestJson.get("createTime"))){
            requestJson.put("createTime", DateUtils.getDate());
        }
        if(StringTools.isNullOrEmpty(requestJson.get("status"))){
            requestJson.put("status", 2);
        }
        requestJson.put("id", StringTools.getUUid());
        return service.addMysql(requestJson);
    }

    @RequiresPermissions("intro:update")
    @PostMapping("/update")
    public JSONObject update(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id,indexType,codeNumber,sharesName,sharesAlise,sharesTotalNumber,sharesAllowTotalNumber,remarks");
        if(StringTools.isNullOrEmpty(requestJson.get("updateTime"))){
            requestJson.put("updateTime", DateUtils.getDate());
        }
        return service.updateMysql(requestJson);
    }

    @RequiresPermissions("intro:status")
    @PostMapping("/status")
    public JSONObject status(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id,status");
        return service.updateMysql(requestJson);
    }

    @RequiresPermissions("intro:delete")
    @PostMapping("/delete")
    public JSONObject delete(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id");
        return service.removeMysql(requestJson);
    }
}
