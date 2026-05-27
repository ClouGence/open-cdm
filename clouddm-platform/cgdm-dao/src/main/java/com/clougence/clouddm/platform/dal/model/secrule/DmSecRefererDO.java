/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.platform.dal.model.secrule;

import java.util.Date;
import java.util.Map;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.clougence.clouddm.platform.dal.model.secrule.RuleKind;
import com.clougence.clouddm.platform.dal.model.secrule.RuleSensitiveMode;
import com.clougence.clouddm.platform.dal.model.secrule.WarnLevel;

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
