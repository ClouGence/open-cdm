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
import com.clougence.clouddm.base.metadata.rdp.enumeration.ConnectType;
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
public class OracleExtraConfig extends DsExtraConfig {

    //    @DsConfigDef(name = "racHosts", descKey = I18nDsConfigMsgKeys.DS_CONFIG_ORACLE_RAC_HOSTS, readOnly = false)
    //    private String      racHosts;

    //    @DsConfigDef(name = "oraLgwrFlushTable", descKey = I18nDsConfigMsgKeys.DS_CONFIG_ORACLE_LGWR_FLUSH_TABLE, readOnly = false)
    //    private String      oraLgwrFlushTable;

    @DsConfigDef(name = "logminerUser", descKey = I18nDsConfigMsgKeys.DS_CONFIG_ORACLE_LOGMINER_USER, readOnly = false)
    private String      logminerUser;

    @DsConfigDef(name = "logminerPasswd", descKey = I18nDsConfigMsgKeys.DS_CONFIG_ORACLE_LOGMINER_PASSWD, readOnly = false, isSecret = true)
    private String      logminerPasswd;

    @DsConfigDef(name = "logminerConnectType", defaultValue = "ORACLE_SID", descKey = I18nDsConfigMsgKeys.DS_CONFIG_ORACLE_LOGMINER_CONNECT_TYPE, valueAdvance = "ORACLE_SID / ORACLE_SERVICE", readOnly = false)
    private ConnectType logminerConnectType;

    @DsConfigDef(name = "logminerSidOrService", descKey = I18nDsConfigMsgKeys.DS_CONFIG_ORACLE_LOGMINER_SID_OR_SERVICE, readOnly = false)
    private String      logminerSidOrService;

    @DsConfigDef(name = "isDataGuard", defaultValue = "false", descKey = I18nDsConfigMsgKeys.DS_CONFIG_ORACLE_DG_IS_DATA_GUARD, valueAdvance = "true / false", readOnly = false, kvConfWebOp = KvConfValType.BOOLEAN)
    private Boolean     isDataGuard;

    @DsConfigDef(name = "dgDicLocation", descKey = I18nDsConfigMsgKeys.DS_CONFIG_ORACLE_DG_DIC_LOCATION, valueAdvance = "eg: ORACLE_BASE", readOnly = false)
    private String      dgDicLocation;
}
