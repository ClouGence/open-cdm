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
import com.clougence.clouddm.base.metadata.rdp.enumeration.ConnectType;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityType;
import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.clouddm.platform.dal.model.*;
import com.clougence.clouddm.platform.dal.model.approval.*;
import com.clougence.clouddm.platform.dal.model.auth.*;
import com.clougence.clouddm.platform.dal.model.datasource.*;
import com.clougence.clouddm.platform.dal.model.execution.*;
import com.clougence.clouddm.platform.dal.model.monitor.*;
import com.clougence.clouddm.platform.dal.model.project.*;
import com.clougence.clouddm.platform.dal.model.secrule.*;
import com.clougence.clouddm.platform.dal.model.system.*;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvDO;

import lombok.Data;

/**
 * @author wanshao create time is 2019/12/11 10:11 下午 finished
 **/
@Data
@TableName(value = "dm_ds")
public class DmDsDO {

    @TableId(type = IdType.AUTO)
    private Long                   id;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                   gmtCreate;

    @TableField(insertStrategy = FieldStrategy.NOT_NULL, updateStrategy = FieldStrategy.NOT_NULL)
    private Date                   gmtModified;

    private String                 uid;

    private String                 owner;

    private DeployEnvType          deployType;

    private DeployEnvInfoFetchType infoFetchType;

    private DataSourceType         dataSourceType;

    /**
     * ip,port
     */
    private String                 host;

    private String                 privateHost;

    private String                 publicHost;

    private HostType               hostType;

    private String                 instanceDesc;

    private String                 version;

    private String                 driver;

    private String                 instanceId;

    /**
     * if security type is Kerberos, account content is principal. if security type is None ,account content maybe is Hive hadoop user and other. if security type is USER_PASSWORD,account content is
     * username.
     */
    private String                 account;

    private String                 password;

    private String                 accessKey;

    private String                 secretKey;

    private LifeCycleState         lifeCycleState;

    //    @TableField(exist = false)
    //    private DataSourceExtraDO     extraDO;

    @TableField(exist = false)
    private DsExtraConfig          dsExtraConfig;

    // the server security file url, .e.g. krb5.conf, server-ca.crt, client.p12, server-ca.pem...
    private String                 securityFileUrl;

    private String                 securityFilePassword;

    // the client security file url, .e.g. client.crt, client.p12, client.pem...
    private String                 clientSecurityFileUrl;

    private String                 clientSecurityFilePassword;

    // the secret file url, .e.g. xxxx.keytab, client.pk8
    private String                 secretFileUrl;

    // the client secret file decrypt password
    private String                 secretFilePassword;

    /**
     * default is private security type;
     */
    private SecurityType           securityType;

    /**
     * for security type separated by host type, this one public host security type.
     */
    private SecurityType           publicSecurityType;

    private String                 clientTrustStorePassword;

    private SecurityFileStoreType  securityFileStoreType;

    private long                   consoleJobId;

    private Long                   parentDsId;

    private ConnectType            connectType;

    private String                 defaultDbName;

    //    private long                  bindClusterId;

    private Long                   dsEnvId;

    @TableField(exist = false)
    private DmSysEnvDO             dsEnvDO;
}
