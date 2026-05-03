package com.clougence.clouddm.console.web.dal.model;

import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.clougence.clouddm.console.web.dal.enumeration.RuleKind;
import com.clougence.clouddm.console.web.dal.enumeration.RuleSensitiveMode;
import com.clougence.clouddm.console.web.dal.enumeration.WarnLevel;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2023/05/21 13:27
 **/
@Getter
@Setter
@TableName(value = "dm_sec_referer")
public class DmSecRefererDO {

    @TableId(type = IdType.AUTO)
    private Long                id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                gmtModified;

    private String              ownerUid;

    private Long                refRule;

    @TableField("ref_kind")
    private RuleKind            refRuleKind;

    private Long                refSpec;

    private boolean             enable;

    @TableField(typeHandler = JacksonTypeHandler.class)
    private Map<String, String> ruleParam;

    @TableField("ref_md5")
    private String              refMD5;

    private WarnLevel           warnLevel;

    private RuleSensitiveMode   senMode;
}
