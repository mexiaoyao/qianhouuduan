package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.config.annotation.RequiresPermissions;
import com.heeexy.example.dto.session.SessionUserInfo;
import com.heeexy.example.service.GradeDictService;
import com.heeexy.example.service.TokenService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.DateUtils;
import com.heeexy.example.util.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/dict")
public class GradeDictController {

    @Autowired
    private GradeDictService service;

    @Autowired
    private TokenService tokenService;

    @RequiresPermissions("dict:list")
    @GetMapping("/list")
    public JSONObject list(HttpServletRequest request) {
        return service.listMysql(CommonUtil.request2Json(request));
    }

    @RequiresPermissions("dict:add")
    @PostMapping("/add")
    public JSONObject add(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "type,dictName");
        if(StringTools.isNullOrEmpty(requestJson.get("parentId"))){
            requestJson.put("parentId", 0);
        }
        if(StringTools.isNullOrEmpty(requestJson.get("createTime"))){
            requestJson.put("createTime", DateUtils.getDate());
        }
        if(StringTools.isNullOrEmpty(requestJson.get("updateTime"))){
            requestJson.put("updateTime", DateUtils.getDate());
        }
        SessionUserInfo userInfo = tokenService.getUserInfo();
        if(StringTools.isNullOrEmpty(requestJson.get("createName"))){
            requestJson.put("createName", userInfo.getUsername());
        }
        if(StringTools.isNullOrEmpty(requestJson.get("updateName"))){
            requestJson.put("updateName", userInfo.getUsername());
        }
        return service.addMysql(requestJson);
    }

    @RequiresPermissions("dict:update")
    @PostMapping("/update")
    public JSONObject update(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id,parentId,type,dictName");
        if(StringTools.isNullOrEmpty(requestJson.get("updateTime"))){
            requestJson.put("updateTime", DateUtils.getDate());
        }
        SessionUserInfo userInfo = tokenService.getUserInfo();
        if(StringTools.isNullOrEmpty(requestJson.get("updateName"))){
            requestJson.put("updateName", userInfo.getUsername());
        }
        return service.updateMysql(requestJson);
    }
    @RequiresPermissions("dict:delete")
    @PostMapping("/delete")
    public JSONObject delete(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id");
        return service.removeMysql(requestJson);
    }
}
