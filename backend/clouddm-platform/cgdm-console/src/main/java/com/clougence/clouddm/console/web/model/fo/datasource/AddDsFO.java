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

import org.springframework.web.multipart.MultipartFile;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.console.web.constants.WhiteListAddType;
import com.clougence.clouddm.console.web.model.fo.InitDsKvBaseConfigFO;
import com.clougence.clouddm.platform.dal.model.LifeCycleState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/28 12:34
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddDsFO {

    private DataSourceType             type;
    private String                     dbName;
    private String                     host;
    private String                     instanceDesc;
    private String                     instanceId;
    private String                     accessKey;
    private String                     secretKey;
    private SecurityType               securityType;
    private LifeCycleState             lifeCycleState;
    private String                     clientTrustStorePassword;
    private String                     secretFilePassword;
    private MultipartFile              securityFile;
    private String                     securityFilePassword;
    private MultipartFile              clientSecurityFile;
    private String                     clientSecurityFilePassword;
    private MultipartFile              secretFile;
    private WhiteListAddType           whiteListAddType;
    private String                     version;
    private String                     driver;
    private List<InitDsKvBaseConfigFO> dsKvConfigs;
    private Long                       bindClusterId;
    private Long                       envId;
}
