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

import com.clougence.clouddm.console.web.global.i18n.I18nDsConfigMsgKeys;

import static com.clougence.clouddm.console.web.global.i18n.I18nDsConfigMsgKeys.CONFIG_DATA_SOURCE_SSL_MODE;

import com.clougence.rdp.constant.DsConfigDef;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@FieldNameConstants
@JsonIgnoreProperties(ignoreUnknown = true)
public class MySQLExtraConfig extends CommonDsExtraConfig {

    @DsConfigDef(name = "sslMode", defaultValue = "DISABLED", valueAdvance = "DISABLED/PREFERRED/REQUIRED/VERIFY_CA/VERIFY_IDENTITY", descKey = CONFIG_DATA_SOURCE_SSL_MODE, readOnly = false)
    private String sslMode;
}
