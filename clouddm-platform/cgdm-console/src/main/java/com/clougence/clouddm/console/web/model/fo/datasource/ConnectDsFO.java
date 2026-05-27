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
package com.clougence.clouddm.console.web.model.fo.datasource;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.ConnectType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityType;
import com.clougence.clouddm.console.web.model.fo.InitDsKvBaseConfigFO;
import com.clougence.clouddm.platform.dal.model.datasource.DeployEnvType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConnectDsFO {

    private Long                       bindClusterId;

    private DataSourceType             dataSourceType;

    private DeployEnvType              deployEnvType;

    private String                     privateHost;

    private String                     publicHost;

    private String                     defaultHost;

    private String                     region;

    private String                     instanceDesc;

    private SecurityType               securityType;

    private ConnectType                connectType;

    private Long                       envId;

    private String                     driver;

    private String                     dsPropsJson;

    private List<InitDsKvBaseConfigFO> dsKvConfigs;
}
