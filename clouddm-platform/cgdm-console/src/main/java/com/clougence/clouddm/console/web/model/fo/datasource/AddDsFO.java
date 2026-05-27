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
import com.clougence.clouddm.base.metadata.rdp.enumeration.ConnectType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityType;
import com.clougence.clouddm.console.web.constants.WhiteListAddType;
import com.clougence.clouddm.console.web.model.fo.InitDsKvBaseConfigFO;
import com.clougence.clouddm.platform.dal.model.datasource.DeployEnvInfoFetchType;
import com.clougence.clouddm.platform.dal.model.datasource.DeployEnvType;
import com.clougence.clouddm.platform.dal.model.datasource.HostType;
import com.clougence.clouddm.platform.dal.model.LifeCycleState;
import com.clougence.utils.StringUtils;
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

    private DeployEnvType              deployType;

    private DataSourceType             type;

    private String                     dbName;

    private String                     host;

    private String                     privateHost;

    private String                     publicHost;

    private HostType                   hostType;

    private String                     instanceDesc;

    private String                     instanceId;

    //    private boolean                    autoCreateAccount;

    private String                     account;

    private String                     password;

    private String                     accessKey;

    private String                     secretKey;

    private SecurityType               securityType;

    //    TODO,remove
    //    private DataSourceExtraData        extraData;

    //    private List<Long>                 clusterIds;

    private LifeCycleState             lifeCycleState;

    private String                     clientTrustStorePassword;

    private String                     secretFilePassword;

    /**
     * like krb5 file,ssl trust store file(.jks)
     */
    private MultipartFile              securityFile;

    private String                     securityFilePassword;

    /**
     * like client ssl ca file, ssl keystore file(.jks)
     */
    private MultipartFile              clientSecurityFile;

    private String                     clientSecurityFilePassword;

    /**
     * like kerberos file , jaas file, client.pk8
     */
    private MultipartFile              secretFile;

    private WhiteListAddType           whiteListAddType;

    private Long                       parentDsId;

    private String                     version;

    private String                     driver;

    private ConnectType                connectType;

    private List<InitDsKvBaseConfigFO> dsKvConfigs;

    // for CloudDM,change to optional
    private Long                       bindClusterId;

    // for CloudDM,change to optional
    private Long                       envId;

    private DeployEnvInfoFetchType     infoFetchType;

    public void manualValidAndTrim() {
        if (deployType == null) {
            throw new IllegalArgumentException("deploy env type can not be empty.");
        }

        if (type == null) {
            throw new IllegalArgumentException("data source type can not be empty.");
        }

        if (StringUtils.isNotBlank(host)) {
            host = host.trim();
        }

        if (StringUtils.isBlank(privateHost) && StringUtils.isBlank(publicHost)) {
            throw new IllegalArgumentException("private host and public host can not be both blank.");
        }

        if (StringUtils.isBlank(privateHost) && StringUtils.isNotBlank(publicHost)) {
            hostType = HostType.PUBLIC;
        } else {
            hostType = HostType.PRIVATE;
        }

        if (StringUtils.isNotBlank(privateHost)) {
            privateHost = privateHost.trim();
        }

        if (StringUtils.isNotBlank(publicHost)) {
            publicHost = publicHost.trim();
        }

        if (securityType == null) {
            throw new IllegalArgumentException("security type can not be empty.");
        }

        // instanceId is not necessary, e.g., OSS
        //        if (deployType == DeployEnvType.ALIBABA_CLOUD_HOSTED && (infoFetchType == null || infoFetchType == DeployEnvInfoFetchType.OPENAPI) && StringUtils.isBlank(instanceId)) {
        //            throw new IllegalArgumentException("data source is Aliyun RDS ,but instance id is blank.");
        //        }

        if (deployType == DeployEnvType.ALIBABA_CLOUD_HOSTED && (infoFetchType == null || infoFetchType == DeployEnvInfoFetchType.OPENAPI) && type == DataSourceType.MySQL
            && (StringUtils.isBlank(accessKey) || StringUtils.isBlank(secretKey))) {
            throw new IllegalArgumentException("data source is Aliyun RDS ,but accessKey and secretKey is blank.");
        }

        if (securityType == SecurityType.KERBEROS) {
            if (securityFile == null || secretFile == null) {
                throw new IllegalArgumentException("security file or secret file can not be null.");
            }
        } else if (securityType == SecurityType.USER_PASSWD_WITH_TLS) {
            if (securityFile == null) {
                throw new IllegalArgumentException("security file can not be null.");
            }

            if (clientTrustStorePassword == null && securityFilePassword == null) {
                throw new IllegalArgumentException("client trust store password can not be null.");
            }
        } else if (securityType == SecurityType.CA_CERTIFICATE) {
            if (securityFile == null) {
                throw new IllegalArgumentException("security file can not be null.");
            }
        }
    }
}
