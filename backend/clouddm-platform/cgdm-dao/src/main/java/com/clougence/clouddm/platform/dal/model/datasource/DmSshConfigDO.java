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
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.clougence.clouddm.base.metadata.ds.SshAuthType;
import com.clougence.clouddm.base.metadata.ds.SshConFeatures;
import com.clougence.clouddm.base.metadata.ds.SshProxyFeatures;
import com.clougence.clouddm.base.metadata.ds.SshProxyType;
import com.clougence.clouddm.platform.dal.handler.encrypt.SshProxyFeaturesTypeHandler;
import com.clougence.clouddm.platform.dal.handler.encrypt.StrSecretTypeHandler;
import com.clougence.clouddm.platform.dal.handler.enums.SshAuthTypeHandler;
import com.clougence.clouddm.platform.dal.handler.enums.SshProxyTypeHandler;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName(value = "dm_ssh_config", autoResultMap = true)
public class DmSshConfigDO {

    @TableId(type = IdType.AUTO)
    private Long             id;
    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date             gmtCreate;
    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date             gmtModified;
    private String           name;
    private String           host;
    private Integer          port;
    private String           username;
    @TableField(typeHandler = SshAuthTypeHandler.class)
    private SshAuthType      authType;
    @TableField(updateStrategy = FieldStrategy.ALWAYS, typeHandler = StrSecretTypeHandler.class)
    private String           password;
    @TableField(updateStrategy = FieldStrategy.ALWAYS, typeHandler = StrSecretTypeHandler.class)
    private String           privateKeyData;
    @TableField(updateStrategy = FieldStrategy.ALWAYS, typeHandler = StrSecretTypeHandler.class)
    private String           privateKeyPassphrase;
    @TableField(updateStrategy = FieldStrategy.ALWAYS, typeHandler = JacksonTypeHandler.class)
    private SshConFeatures   conFeatures;
    @TableField(typeHandler = SshProxyTypeHandler.class)
    private SshProxyType     proxyType;
    @TableField(updateStrategy = FieldStrategy.ALWAYS, typeHandler = SshProxyFeaturesTypeHandler.class)
    private SshProxyFeatures proxyFeatures;
}
