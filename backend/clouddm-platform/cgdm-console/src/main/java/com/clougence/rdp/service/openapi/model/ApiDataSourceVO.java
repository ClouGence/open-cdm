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
package com.clougence.rdp.service.openapi.model;

import java.util.Date;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityType;
import com.clougence.clouddm.console.web.constants.ConsoleTaskState;
import com.clougence.clouddm.platform.dal.model.datasource.DeployEnvType;
import com.clougence.clouddm.platform.dal.model.datasource.HostType;
import com.clougence.clouddm.platform.dal.model.LifeCycleState;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/10/12 10:01
 */
@Getter
@Setter
public class ApiDataSourceVO {

    private boolean          hasPassword;

    private Long             id;

    private Date             gmtCreate;

    private Date             gmtModified;

    private String           uid;

    private String           owner;

    private DeployEnvType    deployType;

    private DataSourceType   dataSourceType;

    private String           privateHost;

    private String           publicHost;

    private HostType         hostType;

    private String           instanceDesc;

    private String           version;

    private String           instanceId;

    private String           autoCreateAccount;

    private String           schemaJson;

    private long             consoleJobId;

    private ConsoleTaskState consoleTaskState;

    private String           accountName;

    private LifeCycleState   lifeCycleState;

    private SecurityType     securityType;

    public void convertFromDsVO(DmDsDO dsDO) {
        this.accountName = dsDO.getAccount();
        // this.autoCreateAccount = dsDO.getAutoCreateAccount();
        this.consoleJobId = dsDO.getConsoleJobId();
        //this.consoleTaskState = dsDO.getConsoleTaskState();
        this.dataSourceType = dsDO.getDataSourceType();
        this.deployType = dsDO.getDeployType();
        this.gmtCreate = dsDO.getGmtCreate();
        this.gmtModified = dsDO.getGmtModified();
        this.privateHost = dsDO.getPrivateHost();
        this.publicHost = dsDO.getPublicHost();
        this.hostType = dsDO.getHostType();
        this.id = dsDO.getId();
        this.instanceDesc = dsDO.getInstanceDesc();
        this.instanceId = dsDO.getInstanceId();
        this.lifeCycleState = dsDO.getLifeCycleState();
        this.owner = dsDO.getOwner();
        this.securityType = dsDO.getSecurityType();
        this.uid = dsDO.getUid();
        this.version = dsDO.getVersion();
    }
}
