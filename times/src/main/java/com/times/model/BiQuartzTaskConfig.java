package com.times.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@TableName(value ="t_quartz_task_config")
@Data
public class BiQuartzTaskConfig implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 配置Id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 任务编码
     */
    @TableField(value = "task_code")
    private String taskCode;

    /**
     * cron表达式
     */
    @TableField(value = "cron")
    private String cron;

    /**
     * job引用地址
     */
    @TableField(value = "class_name")
    private String className;

    /**
     * 描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 删除状态(0:正常; 1:已删除)
     */
    @TableField(value = "del_flag")
    private Integer delFlag;

    /**
     * 创建人
     */
    @TableField(value = "create_by")
    private String createBy;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新人
     */
    @TableField(value = "update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

}
