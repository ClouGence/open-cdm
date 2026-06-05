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
package com.clougence.clouddm.console.web.model.fo;

import org.springframework.web.multipart.MultipartFile;

import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author bucketli 2021/1/28 12:22
 */
@Data
public class UpdateSecurityInfoFO {

    @Min(value = 1, message = "{min.datasourceid}")
    private long          dataSourceId;

    @NotBlank(message = "{notblank.username}")
    private String        userName;

    private String        password;

    private String        accessKey;

    private String        secretKey;

    private String        clientTrustStorePassword;

    private SecurityType  securityType;

    /** like krb5 file,ssl trust store file */
    private MultipartFile securityFile;

    private String        securityFilePassword;

    /** like kerberos file , jaas file */
    private MultipartFile secretFile;

    private String        secretFilePassword;

    // like client ssl ca file
    private MultipartFile clientSecurityFile;

    private String        clientSecurityFilePassword;
}
