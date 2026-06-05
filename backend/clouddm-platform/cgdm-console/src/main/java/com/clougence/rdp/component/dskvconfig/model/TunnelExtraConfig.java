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
import com.clougence.clouddm.base.metadata.rdp.enumeration.SchemaLessValueFormat;
import com.clougence.clouddm.base.metadata.rdp.enumeration.TunnelProtocol;
import com.clougence.clouddm.base.metadata.rdp.enumeration.TunnelSrcType;
import com.clougence.rdp.constant.DsConfigDef;
import com.clougence.clouddm.console.web.global.i18n.I18nDsConfigMsgKeys;
import com.clougence.clouddm.platform.dal.model.system.KvConfValType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 * @author bucketli 2022/8/10 09:33:48
 */
@Getter
@Setter
@FieldNameConstants
@JsonIgnoreProperties(ignoreUnknown = true)
public class TunnelExtraConfig extends DsExtraConfig {

    @DsConfigDef(name = "dbsJson", defaultValue = "[{\"db\":\"cc_virtual_db\",\"schemas\":[{\"schema\":\"cc_virtual_schema\",\"tables\":[]}]}]", descKey = I18nDsConfigMsgKeys.DS_CONFIG_VIRTUAL_DBS, readOnly = false, kvConfWebOp = KvConfValType.JSON)
    private String                dbsJson;

    @DsConfigDef(name = "uriPrefix", descKey = I18nDsConfigMsgKeys.DS_CONFIG_TUNNEL_URI_PREFIX, readOnly = false)
    private String                uriPrefix;

    @DsConfigDef(name = "valueFormat", defaultValue = "CLOUDCANAL_JSON_FOR_TUNNEL", descKey = I18nDsConfigMsgKeys.DS_CONFIG_TUNNEL_VALUES_FORMAT, readOnly = false)
    private SchemaLessValueFormat valueFormat;

    @DsConfigDef(name = "tunnelSrcType", defaultValue = "HOST", descKey = I18nDsConfigMsgKeys.DS_CONFIG_TUNNEL_SRC_TYPE, readOnly = false)
    private TunnelSrcType         tunnelSrcType;

    @DsConfigDef(name = "tunnelProtocol", defaultValue = "HTTP", descKey = I18nDsConfigMsgKeys.DS_CONFIG_TUNNEL_PROTOCOL)
    private TunnelProtocol        tunnelProtocol;
}
