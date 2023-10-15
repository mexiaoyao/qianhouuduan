package com.times.controller;

import com.times.util.QuartzManagerUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test/quartz")
@RestController
public class QuartzController {

    @GetMapping("/task/{taskCode}/{type}")
    public String task(@PathVariable("taskCode") String taskCode, @PathVariable("type") Integer type) throws Exception {
        //https://blog.csdn.net/apple_csdn/article/details/124131892
        if (1 == type) {
            QuartzManagerUtil.initAllJob();
        } else if (2 == type) {
            QuartzManagerUtil.stopJob(taskCode);
        } else if (3 == type) {
            QuartzManagerUtil.resumeJob(taskCode);
        } else if (4 == type) {
            QuartzManagerUtil.removeJob(taskCode);
        } else if (5 == type) {
            QuartzManagerUtil.reload(taskCode);
        } else if (6 == type) {
            QuartzManagerUtil.addNewJob(taskCode);
        }

        return "task";
    }
}