package com.clougence.clouddm.console.web.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.console.web.dal.enumeration.ImType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2019/12/11 10:11 下午 finished
 **/
@Getter
@Setter
@TableName(value = "dm_messenger")
public class DmMessengerDO {

    @TableId(type = IdType.AUTO)
    private Long    id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date    gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date    gmtModified;

    @TableField("owner_uid")
    private String  ownerUid;

    @TableField("im_type")
    private ImType  imType;

    @TableField("im_display")
    private String  imDisplay;

    @TableField("webhook")
    private String  webhook;

    @TableField("secret")
    private String  secret;

    @TableField("enable")
    private boolean enable;
}
