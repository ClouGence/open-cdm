package com.clougence.clouddm.console.web.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.console.web.dal.enumeration.DmEventType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wanshao create time is 2020/4/13
 **/
@Getter
@Setter
@TableName(value = "alert_config_detail")
public class AlertConfigDetailDO {

    @TableId(type = IdType.AUTO)
    private Long        id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date        gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date        gmtModified;

    private DmEventType eventType;

    private String      uid;

    private boolean     phone;

    private boolean     email;

    private boolean     dingding;

    private boolean     sms;

    private boolean     duplicated;

    private String      ruleName;

    private String      expression;

    private boolean     sendAdmin;

    private boolean     sendSystem;

    private Long        workerId;
}
