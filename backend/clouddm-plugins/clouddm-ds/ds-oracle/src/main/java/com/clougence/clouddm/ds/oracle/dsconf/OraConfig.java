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
package com.clougence.clouddm.ds.oracle.dsconf;

import java.util.Properties;

import com.clougence.clouddm.base.metadata.ds.*;
import com.clougence.clouddm.ds.oracle.i18n.OraConfigI18nKeys;
import com.clougence.clouddm.sdk.execute.dsconf.Serialization;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

/**
 * @author mode 2020/11/6 10:23
 */
@Getter
@Setter
@FieldNameConstants
@Serialization(provider = OraSerializationSpi.PROVIDER_NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OraConfig extends DataSourceConfig {
    // ------------------------------------------------------------------------------------------------------------------------ GENERAL
    @ConfigDef(name = Fields.connectType, //
            group = DsConfigGroup.GENERAL, labelKey = OraConfigI18nKeys.CONFIG_ORACLE_CONNECT_TYPE_LABEL, descKey = OraConfigI18nKeys.CONFIG_DESCRIPTION_EMPTY, readOnly = false)
    private OraConnectType connectType;
    @ConfigDef(name = Fields.sid, //
            group = DsConfigGroup.GENERAL, labelKey = OraConfigI18nKeys.CONFIG_ORACLE_SID_LABEL, descKey = OraConfigI18nKeys.CONFIG_DESCRIPTION_EMPTY, readOnly = false)
    private String         sid;
    @ConfigDef(name = Fields.serviceName, //
            group = DsConfigGroup.GENERAL, labelKey = OraConfigI18nKeys.CONFIG_ORACLE_SERVICE_LABEL, descKey = OraConfigI18nKeys.CONFIG_DESCRIPTION_EMPTY, readOnly = false)
    private String         serviceName;
    @ConfigDef(name = Fields.pdbName, //
            group = DsConfigGroup.GENERAL, labelKey = OraConfigI18nKeys.CONFIG_ORACLE_PDB_LABEL, descKey = OraConfigI18nKeys.CONFIG_DESCRIPTION_EMPTY, readOnly = false)
    private String         pdbName;
    @ConfigDef(name = Fields.tnsAdmin, //
            group = DsConfigGroup.GENERAL, labelKey = OraConfigI18nKeys.CONFIG_ORACLE_TNS_ADMIN_LABEL, descKey = OraConfigI18nKeys.CONFIG_DESCRIPTION_EMPTY, readOnly = false)
    private String         tnsAdmin;
    @ConfigDef(name = Fields.tnsName, //
            group = DsConfigGroup.GENERAL, labelKey = OraConfigI18nKeys.CONFIG_ORACLE_TNS_NAME_LABEL, descKey = OraConfigI18nKeys.CONFIG_DESCRIPTION_EMPTY, readOnly = false)
    private String         tnsName;
    // ------------------------------------------------------------------------------------------------------------------------ ADVANCED
    @ConfigDef(name = Fields.connectTimeoutMs, defaultValue = "5000", //
            group = DsConfigGroup.ADVANCED, labelKey = OraConfigI18nKeys.CONFIG_RDB_CONN_TIMEOUT_MS_LABEL, descKey = OraConfigI18nKeys.CONFIG_RDB_CONN_TIMEOUT_MS_DESC, readOnly = false)
    private Long           connectTimeoutMs;
    @ConfigDef(name = Fields.soTimeoutSec, defaultValue = "10", //
            group = DsConfigGroup.ADVANCED, labelKey = OraConfigI18nKeys.CONFIG_DS_SO_TIMEOUT_MS_LABEL, descKey = OraConfigI18nKeys.CONFIG_DS_SO_TIMEOUT_MS_DESC, readOnly = false)
    private Integer        soTimeoutSec;
    @ConfigDef(name = Fields.excludeOraMaintainedSchemas, defaultValue = "false", //
            group = DsConfigGroup.ADVANCED, labelKey = OraConfigI18nKeys.CONFIG_ORACLE_EXCLUDE_ORA_MAINTAINED_SCHEMAS_LABEL, descKey = OraConfigI18nKeys.CONFIG_ORACLE_EXCLUDE_ORA_MAINTAINED_SCHEMAS_DESC, readOnly = false)
    private Boolean        excludeOraMaintainedSchemas;

    public OraConfig(){
        setDataSourceType(DataSourceType.Oracle);
    }

    public Properties asDriverProperties() {
        String ipStr = "";
        String portStr = "1521";
        if (StringUtils.isNotBlank(getHost())) {
            String[] ipPort = getHost().split(":");
            if (ipPort.length == 2 || ipPort.length == 3) {
                ipStr = ipPort[0];
                if (StringUtils.isNotBlank(ipPort[1])) {
                    portStr = ipPort[1];
                }
            }
            if (ipPort.length == 3) {
                if (this.connectType == OraConnectType.SID) {
                    this.sid = ipPort[2];
                } else if (this.connectType == OraConnectType.SERVICE) {
                    this.serviceName = ipPort[2];
                } else if (this.connectType == OraConnectType.PDB) {
                    this.pdbName = ipPort[2];
                } else {
                    throw new IllegalArgumentException("unsupported Oracle connect type:" + this.connectType);
                }
            } else if (ipPort.length != 2) {
                throw new IllegalArgumentException("unsupported Oracle host format:" + getHost());
            }
        }

        OraConnectType connectType = this.getConnectType();
        String serviceName = this.getServiceName();
        if (connectType == OraConnectType.PDB) {
            connectType = OraConnectType.SERVICE;
            if (StringUtils.isBlank(serviceName)) {
                serviceName = this.getPdbName();
            }
        }

        Properties properties = new Properties();
        properties.setProperty(DsConfigKeys.ID.getConfigKey(), safeStr(this.getInstanceId()));
        properties.setProperty(DsConfigKeys.HOST.getConfigKey(), safeStr(ipStr + ":" + portStr));
        properties.setProperty(DsConfigKeys.USER.getConfigKey(), safeStr(this.getUserName()));
        properties.setProperty(DsConfigKeys.PASSWORD.getConfigKey(), safeStr(this.getPassword()));
        properties.setProperty(DsConfigKeys.ORA_ORACLE_CONNECT_TYPE.getConfigKey(), safeStr(connectType.getDriverTypeCode()));
        properties.setProperty(DsConfigKeys.ORA_SID.getConfigKey(), safeStr(this.getSid()));
        properties.setProperty(DsConfigKeys.ORA_PDB.getConfigKey(), safeStr(this.getPdbName()));
        properties.setProperty(DsConfigKeys.ORA_SERVICE_NAME.getConfigKey(), safeStr(serviceName));
        properties.setProperty(DsConfigKeys.ORA_TNS_ADMIN.getConfigKey(), safeStr(this.getTnsAdmin()));
        properties.setProperty(DsConfigKeys.ORA_TNS_NAME.getConfigKey(), safeStr(this.getTnsName()));
        properties.setProperty(DsConfigKeys.AUTO_COMMIT.getConfigKey(), safeStr(StringUtils.toString(this.getAutoCommit())));
        properties.setProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey(), safeStr(StringUtils.toString(this.getConnectTimeoutMs())));
        properties.setProperty(DsConfigKeys.SO_TIMEOUT_SEC.getConfigKey(), safeStr(StringUtils.toString(this.getSoTimeoutSec())));
        return properties;
    }
}
