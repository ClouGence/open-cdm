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
import java.util.List;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.platform.dal.model.secrule.RuleScriptType;
import com.clougence.clouddm.platform.dal.model.secrule.RuleTarget;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode create time is 2023/05/21 13:27
 **/
@Getter
@Setter
@TableName(value = "dm_sec_rules")
public class DmSecRuleDO {

    @TableId(type = IdType.AUTO)
    private Long                 id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                 gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                 gmtModified;

    private String               ownerUid;

    @TableField("rule_id")
    private String               ruleId;

    @TableField("rule_name")
    private String               name;

    @TableField("rule_desc")
    private String               description;

    @TableField("rule_type")
    private RuleScriptType       scriptType;

    @TableField("rule_def")
    private String               scriptDef;

    @TableField("rule_content")
    private String               scriptContent;

    @TableField("rule_md5")
    private String               scriptMD5;

    private boolean              innerShare;

    private boolean              deprecated;

    // for QUERY
    @TableField(value = "ds_range", typeHandler = JacksonTypeHandler.class)
    private List<DataSourceType> ruleDsRange;

    @TableField("rule_target")
    private RuleTarget           ruleTarget;
}
