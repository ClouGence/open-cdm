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
package com.clougence.rdp.component.dskvconfig.model;

import com.clougence.rdp.constant.DsConfigDef;
import com.clougence.clouddm.console.web.global.i18n.I18nDsConfigMsgKeys;
import com.clougence.clouddm.platform.dal.model.system.KvConfValType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 * @author chunlin create time is 2025/6/13
 */
@Getter
@Setter
@FieldNameConstants
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleDriveExtraConfig extends FileExtraConfig {

    @DsConfigDef(name = "projectId", descKey = I18nDsConfigMsgKeys.DS_CONFIG_GOOGLE_CLOUD_PROJECT_ID, readOnly = false, kvConfWebOp = KvConfValType.TEXT)
    private String projectId;

    @DsConfigDef(name = "privateKeyId", descKey = I18nDsConfigMsgKeys.DS_CONFIG_GOOGLE_CLOUD_SERVICE_ACCOUNT_PRIVATE_KEY_ID, readOnly = false, kvConfWebOp = KvConfValType.TEXT)
    private String privateKeyId;

    @DsConfigDef(name = "privateKey", descKey = I18nDsConfigMsgKeys.DS_CONFIG_GOOGLE_CLOUD_SERVICE_ACCOUNT_PRIVATE_KEY, isSecret = true, readOnly = false, kvConfWebOp = KvConfValType.TEXT)
    private String privateKey;

    @DsConfigDef(name = "clientEmail", descKey = I18nDsConfigMsgKeys.DS_CONFIG_GOOGLE_CLOUD_SERVICE_ACCOUNT_EMAIL, readOnly = false, kvConfWebOp = KvConfValType.TEXT)
    private String clientEmail;

    @DsConfigDef(name = "clientId", descKey = I18nDsConfigMsgKeys.DS_CONFIG_GOOGLE_CLOUD_SERVICE_ACCOUNT_ID, readOnly = false, kvConfWebOp = KvConfValType.TEXT)
    private String clientId;
}
