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

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.platform.dal.model.secrule.RuleScriptType;
import com.clougence.clouddm.platform.dal.model.secrule.RuleSensitiveMode;

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
