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

import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020/12/30 14:26
 */
@Getter
@Setter
@Builder
public class DsSecurityOption {

    private SecurityType securityType;

    private String       securityTypeI18nName;

    private boolean      hasAutoGenAccountPasswdOption;

    /**
     * if hasAutoGenAccountPasswdOption is true,this option is significant
     */
    private boolean      checkAutoGenAccountPasswdOption;

    private boolean      hasAutoCreateAccountOption;

    private String       defaultHost;

    /**
     * if hasAutoCreateAccountOption is true, this option is significant
     */
    private boolean      checkAutoCreateAccountOption;

    private boolean      needUserName;

    private boolean      needPassword;

    private boolean      needApiKey;

    private boolean      needAkSk;

    private boolean      needExtraAliyunAkSk;

    private boolean      isAccountAliyunAkSk;

    private boolean      isUserNamePrinciple;

    /*
        CA Security files related options
        caFile - CA certificate file, e.g., .crt, .cer, .pem
        clientCaFile - Client CA certificate file, e.g., .crt, .cer, .pem
        clientKeyFile - Client private key file, e.g., .key, .secret, .env, .pk8
     */
    private boolean      needCaFile;

    private boolean      needClientCaFile;

    private boolean      needClientKeyFile;

    private boolean      needSecretFilePassword;

    private boolean      needKrb5File;

    private boolean      needKeyTabFile;

    private boolean      defaultCheck;

    private boolean      needDbName;

    private boolean      needInstanceId;

    private String       dbNameLabel;

    /*
        TLS/SSL related files
        truststore(.jks) - Store public key certificate
        keystore(.jks) - Store private key certificate
     */
    private boolean      needTlsTrustStoreFile;

    private boolean      needTlsTrustStoreFilePassword;

    private boolean      needTlsKeyStoreFile;

    private boolean      needTlsKeyStoreFilePassword;

    private boolean      needKeystoreFile;

    private boolean      needKeystoreFilePassword;
}
