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
import com.clougence.clouddm.platform.dal.model.datasource.DeployEnvInfoFetchType;
import com.clougence.clouddm.platform.dal.model.datasource.DeployEnvType;
import com.clougence.clouddm.platform.dal.model.datasource.HostType;
import com.clougence.clouddm.platform.dal.model.LifeCycleState;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020/2/22 11:07
 */
@Getter
@Setter
public class RdpDataSourceVO {

    /** has password means user no need to enter account and pwd */
    private boolean                hasPassword;

    private Long                   id;

    private Date                   gmtCreate;

    private DeployEnvType          deployType;

    private String                 deployTypeI18n;

    private DeployEnvInfoFetchType infoFetchType;

    private DataSourceType         dataSourceType;

    /** ip,port */
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
}
