package com.clougence.clouddm.console.web.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.console.web.dal.enumeration.ScmType;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2019/12/11 10:11 下午 finished
 **/
@Getter
@Setter
@TableName(value = "dm_project_scm")
public class DmProjectScmDO {

    @TableId(type = IdType.AUTO)
    private Long    id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date    gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date    gmtModified;

    @TableField("owner_uid")
    private String  ownerUid;

    @TableField("scm_type")
    private ScmType scmType;

    @TableField("scm_display")
    private String  scmDisplay;

    @TableField("scm_service_url")
    private String  scmServiceUrl;

    @TableField("scm_access_token")
    private String  scmAccessToken;

}
