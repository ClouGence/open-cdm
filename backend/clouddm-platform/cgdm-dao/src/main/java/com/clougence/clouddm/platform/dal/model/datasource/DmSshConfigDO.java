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
package com.clougence.clouddm.platform.dal.model.datasource;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName(value = "dm_ssh_config")
public class DmSshConfigDO {

    @TableId(type = IdType.AUTO)
    private Long    id;
    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date    gmtCreate;
    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date    gmtModified;
    private String  name;
    private String  host;
    private Integer port;
    private String  username;
    private String  authType;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String  password;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String  privateKeyData;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String  privateKeyPassphrase;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String  conFeatures;
    private String  proxyType;
    @TableField(updateStrategy = FieldStrategy.ALWAYS)
    private String  proxyFeatures;
    private boolean deleted;
}
