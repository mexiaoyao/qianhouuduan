package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.config.annotation.RequiresPermissions;
import com.heeexy.example.service.FinanceApiService;
import com.heeexy.example.service.FinanceIntroService;
import com.heeexy.example.service.FinanceStatusService;
import com.heeexy.example.service.WorkDateService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.DateUtils;
import com.heeexy.example.util.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.heeexy.example.util.constants.ErrorEnum.G_update001;

@RestController
@RequestMapping("/intro")
public class FinanceIntroController {

    @Autowired
    private FinanceIntroService service;

    @Autowired
    private WorkDateService workService;

    @Autowired
    private FinanceStatusService statusService;

    @Autowired
    private FinanceApiService apiService;

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
        CommonUtil.hasAllRequired(requestJson, "id,status,codeNumber");
        String codeNumber = requestJson.getString("codeNumber");
        String status = requestJson.getString("status");
        int result = service.statusMysql(requestJson);
        if(result>0){
            String tablesName = "t_shares_"+codeNumber;
            if(status.equals("1")){
                service.createShreas(tablesName);
            }else if(status.equals("2")){
                service.deleteTable(tablesName);
            }
            return CommonUtil.successJson();
        }else{
            return CommonUtil.errorJson(G_update001);
        }
    }

    @RequiresPermissions("intro:delete")
    @PostMapping("/delete")
    public JSONObject delete(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id");
        return service.removeMysql(requestJson);
    }

    @RequiresPermissions("intro:workadd")
    @PostMapping("/workadd")
    public JSONObject workadd(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "workDate, holiday");
        requestJson.put("id", StringTools.getUUid());
        return workService.addMysql(requestJson);
    }

    @RequiresPermissions("intro:workList")
    @PostMapping("/workList")
    public JSONObject workList() {
        return workService.listAllMysql();
    }

    @RequiresPermissions("intro:remove")
    @PostMapping("/remove")
    public JSONObject remove(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id");
        return workService.removeMysql(requestJson);
    }

    @RequiresPermissions("intro:statusList")
    @PostMapping("/statusList")
    public JSONObject statusList(@RequestBody JSONObject requestJson) {
        return statusService.listMysql(requestJson);
    }

    @RequiresPermissions("intro:statusDelete")
    @PostMapping("/statusDelete")
    public JSONObject statusDelete(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id");
        return statusService.removeMysql(requestJson);
    }

    @RequiresPermissions("intro:apiList")
    @PostMapping("/apiList")
    public JSONObject apiList(@RequestBody JSONObject requestJson) {
        return apiService.listMysql(requestJson);
    }

    @RequiresPermissions("intro:apiAdd")
    @PostMapping("/apiAdd")
    public JSONObject apiAdd(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "name,url,maxNumber,spaceNumber,remarks");
        requestJson.put("id", StringTools.getUUid());
        if(StringTools.isNullOrEmpty(requestJson.get("createTime"))){
            requestJson.put("createTime", DateUtils.getDate());
        }
        return apiService.addMysql(requestJson);
    }

    @RequiresPermissions("intro:apiUpdate")
    @PostMapping("/apiUpdate")
    public JSONObject apiUpdate(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id,name,url,maxNumber,spaceNumber,remarks");
        return apiService.updateMysql(requestJson);
    }

    @RequiresPermissions("intro:apiDelete")
    @PostMapping("/apiDelete")
    public JSONObject apiDelete(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id");
        return apiService.removeMysql(requestJson);
    }



}
