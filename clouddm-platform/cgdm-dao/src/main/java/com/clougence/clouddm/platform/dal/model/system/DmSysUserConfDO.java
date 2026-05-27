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
package com.clougence.clouddm.platform.dal.model.system;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;
import com.clougence.clouddm.platform.dal.model.system.ConfBelong;
import com.clougence.clouddm.platform.dal.model.system.KvConfValType;
import com.clougence.clouddm.platform.dal.model.system.UserConfigTagType;

import lombok.Data;

/**
 * @author bucketli 2020/11/9 13:23
 */
@Data
@TableName(value = "dm_sys_user_conf")
public class DmSysUserConfDO {

    @TableId(type = IdType.AUTO)
    private Long              id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date              gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date              gmtModified;

    private String            uid;

    private String            configName;

    private String            configValue;

    private String            defaultValue;

    private String            valueRange;

    private boolean           readOnly;

    private UserConfigTagType userConfigTagType;

    private ConfBelong        confBelong;

    private KvConfValType     confValType;

    private boolean           isSecret;

    private String            descKey;
}
