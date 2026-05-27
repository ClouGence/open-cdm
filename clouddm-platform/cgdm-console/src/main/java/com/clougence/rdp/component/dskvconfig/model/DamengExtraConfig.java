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

import com.clougence.clouddm.base.metadata.ds.DsExtraConfig;
import com.clougence.rdp.constant.DsConfigDef;
import com.clougence.clouddm.console.web.global.i18n.I18nDsConfigMsgKeys;
import com.clougence.clouddm.platform.dal.model.system.KvConfValType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@FieldNameConstants
@JsonIgnoreProperties(ignoreUnknown = true)
public class DamengExtraConfig extends DsExtraConfig {

    @DsConfigDef(name = "isDscNode", defaultValue = "false", valueAdvance = "true/false", descKey = I18nDsConfigMsgKeys.DS_CONFIG_DAMENG_IS_DCS_NODE, readOnly = false, kvConfWebOp = KvConfValType.BOOLEAN)
    private Boolean isDscNode;

    @DsConfigDef(name = "dscHosts", descKey = I18nDsConfigMsgKeys.DS_CONFIG_DAMENG_DSC_HOSTS, readOnly = false, kvConfWebOp = KvConfValType.TEXT)
    private String  dscHosts;

    @DsConfigDef(name = "dscSyncLsnTable", defaultValue = "SYS.CC_DSC_SYNC_TABLE", descKey = I18nDsConfigMsgKeys.DS_CONFIG_DAMENG_SYNC_LSN_TABLE, readOnly = false, kvConfWebOp = KvConfValType.TEXT)
    private String  dscSyncLsnTable;
}
