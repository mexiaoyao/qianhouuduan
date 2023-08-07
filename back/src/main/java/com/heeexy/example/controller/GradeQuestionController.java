package com.heeexy.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.heeexy.example.config.annotation.RequiresPermissions;
import com.heeexy.example.dto.session.SessionUserInfo;
import com.heeexy.example.service.GradeQuestionService;
import com.heeexy.example.service.TokenService;
import com.heeexy.example.util.CommonUtil;
import com.heeexy.example.util.DateUtils;
import com.heeexy.example.util.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/question")
public class GradeQuestionController {

    @Autowired
    private GradeQuestionService service;

    @Autowired
    private TokenService tokenService;

    @RequiresPermissions("question:list")
    @PostMapping("/list")
    public JSONObject list(@RequestBody JSONObject requestJson) {
        return service.listMysql(requestJson);
    }

    @RequiresPermissions("question:add")
    @PostMapping("/add")
    public JSONObject add(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "dictTaskPath,dictTaskPathName,dictGradePath,dictGradePathName," +
                "dictSourcePath,dictSourcePathName,dictTypePath,dictTypePathName,intro,question,answers,answerRight,type");
        if(StringTools.isNullOrEmpty(requestJson.get("usedNum"))){
            requestJson.put("usedNum", 0);
        }
        if(StringTools.isNullOrEmpty(requestJson.get("goodNum"))){
            requestJson.put("goodNum", 0);
        }
        if(StringTools.isNullOrEmpty(requestJson.get("poorNum"))){
            requestJson.put("poorNum", 0);
        }
        if(StringTools.isNullOrEmpty(requestJson.get("shareNum"))){
            requestJson.put("shareNum", 0);
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
        return service.addMysql(requestJson);
    }

    @RequiresPermissions("question:update")
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
    @RequiresPermissions("question:delete")
    @PostMapping("/delete")
    public JSONObject delete(@RequestBody JSONObject requestJson) {
        CommonUtil.hasAllRequired(requestJson, "id");
        return service.removeMysql(requestJson);
    }
}
