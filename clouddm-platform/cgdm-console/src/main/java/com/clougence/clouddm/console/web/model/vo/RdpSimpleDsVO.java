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
package com.clougence.clouddm.console.web.model.vo;

import java.util.Date;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityType;
import com.clougence.clouddm.console.web.dal.enumeration.DeployEnvInfoFetchType;
import com.clougence.clouddm.console.web.dal.enumeration.DeployEnvType;
import com.clougence.clouddm.console.web.dal.enumeration.HostType;
import com.clougence.clouddm.console.web.dal.enumeration.LifeCycleState;
import com.clougence.clouddm.console.web.dal.model.RdpDataSourceDO;
import com.clougence.clouddm.console.web.util.DmI18nUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020/2/22 11:07
 */
@Getter
@Setter
public class RdpSimpleDsVO {

    private Long                   id;

    private Date                   gmtCreate;

    private DeployEnvType          deployType;

    private String                 deployTypeI18n;

    private DeployEnvInfoFetchType infoFetchType;

    private DataSourceType         dataSourceType;

    private String                 host;

    private String                 privateHost;

    private String                 publicHost;

    private HostType               hostType;

    private String                 instanceDesc;

    private String                 version;

    private String                 instanceId;

    private String                 accountName;

    private LifeCycleState         lifeCycleState;

    private SecurityType           securityType;

    private Long                   dsEnvId;

    private String                 dsEnvName;

    public void convertFromDO(RdpDataSourceDO dsDO) {
        this.id = dsDO.getId();
        this.gmtCreate = dsDO.getGmtCreate();
        this.host = dsDO.getHost();
        this.privateHost = dsDO.getPrivateHost();
        this.publicHost = dsDO.getPublicHost();
        this.hostType = dsDO.getHostType();
        this.accountName = dsDO.getAccount();
        this.lifeCycleState = dsDO.getLifeCycleState();
        this.securityType = dsDO.getSecurityType();
        this.dsEnvId = dsDO.getDsEnvId();
        if (dsDO.getDsEnvDO() != null) {
            this.dsEnvName = dsDO.getDsEnvDO().getEnvName();
        }
        this.instanceId = dsDO.getInstanceId();
        this.instanceDesc = dsDO.getInstanceDesc();
        this.dataSourceType = dsDO.getDataSourceType();
        if (dsDO.getDeployType() != null) {
            this.deployType = dsDO.getDeployType();
            this.deployTypeI18n = DmI18nUtils.getMessage(dsDO.getDeployType().name());
        }

        this.infoFetchType = dsDO.getInfoFetchType();

        this.version = dsDO.getVersion();
    }
}
