package com.clougence.clouddm.console.web.dal.model;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.console.web.dal.enumeration.RuleScriptType;
import com.clougence.clouddm.console.web.dal.enumeration.RuleSensitiveMode;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Ekko
 * @date 2024/7/10 15:08
*/
@Getter
@Setter
@TableName(value = "dm_sec_sensitive")
public class DmSecSensitiveDO {

    @TableId(type = IdType.AUTO)
    private Long              id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date              gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date              gmtModified;

    private String            ownerUid;

    @TableField("sen_id")
    private String            senId;

    @TableField("sen_name")
    private String            name;

    @TableField("sen_desc")
    private String            description;

    @TableField("sen_type")
    private RuleScriptType    scriptType;

    @TableField("sen_def")
    private String            scriptDef;

    @TableField("sen_content")
    private String            scriptContent;

    @TableField("sen_md5")
    private String            scriptMD5;

    private boolean           innerShare;

    private boolean           deprecated;

    // for SENSITIVE

    private RuleSensitiveMode senMode;
}
