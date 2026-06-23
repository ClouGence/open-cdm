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
    @ConfigDef(group = DsConfigGroup.GENERAL, readOnly = false, name = Fields.connectType, descKey = ConfigI18nKey.CONFIG_ORACLE_CONNECT_TYPE_DESCRIPTION)
    private OraConnectType connectType;
    @ConfigDef(group = DsConfigGroup.GENERAL, readOnly = false, name = Fields.sid, descKey = ConfigI18nKey.CONFIG_ORACLE_SID_DESCRIPTION)
    private String         sid;
    @ConfigDef(group = DsConfigGroup.GENERAL, readOnly = false, name = Fields.serviceName, descKey = ConfigI18nKey.CONFIG_ORACLE_SERVICE_DESCRIPTION)
    private String         serviceName;
    @ConfigDef(group = DsConfigGroup.GENERAL, readOnly = false, name = Fields.pdbName, descKey = ConfigI18nKey.CONFIG_ORACLE_PDB_DESCRIPTION)
    private String         pdbName;
    @ConfigDef(group = DsConfigGroup.GENERAL, readOnly = false, name = Fields.tnsAdmin, descKey = ConfigI18nKey.CONFIG_ORACLE_TNS_ADMIN_DESCRIPTION)
    private String         tnsAdmin;
    @ConfigDef(group = DsConfigGroup.GENERAL, readOnly = false, name = Fields.tnsName, descKey = ConfigI18nKey.CONFIG_ORACLE_TNS_NAME_DESCRIPTION)
    private String         tnsName;
    // ------------------------------------------------------------------------------------------------------------------------ OPTIONS
    @ConfigDef(group = DsConfigGroup.OPTIONS, readOnly = false, name = Fields.autoCommit, defaultValue = "true", descKey = ConfigI18nKey.CONFIG_RDB_TRANSACTION_DESCRIPTION)
    private Boolean        autoCommit;
    // ------------------------------------------------------------------------------------------------------------------------ ADVANCED
    @ConfigDef(group = DsConfigGroup.ADVANCED, readOnly = false, name = Fields.connectTimeoutMs, defaultValue = "5000", descKey = ConfigI18nKey.CONFIG_RDB_CONN_TIMEOUT_MS_DESCRIPTION)
    private Long           connectTimeoutMs;
    @ConfigDef(group = DsConfigGroup.ADVANCED, readOnly = false, name = Fields.soTimeoutSec, defaultValue = "10", descKey = ConfigI18nKey.CONFIG_DS_SO_TIMEOUT_MS_DESCRIPTION)
    private Integer        soTimeoutSec;
    @ConfigDef(group = DsConfigGroup.ADVANCED, readOnly = false, name = Fields.excludeOraMaintainedSchemas, defaultValue = "false", descKey = ConfigI18nKey.CONFIG_ORACLE_EXCLUDE_ORA_MAINTAINED_SCHEMAS_DESCRIPTION)
    private Boolean        excludeOraMaintainedSchemas;

    public OraConfig(){
        setDataSourceType(DataSourceType.Oracle);
    }

    public Properties asDriverProperties() {
        String ipStr = "";
        String portStr = "1521";
        if (StringUtils.isNotBlank(getHost())) {
            String[] ipPort = getHost().split(":");
            if (ipPort.length == 3) {
                if (this.connectType == OraConnectType.SID) {
                    ipStr = ipPort[0];
                    if (StringUtils.isNotBlank(ipPort[1])) {
                        portStr = ipPort[1];
                    }
                    this.sid = ipPort[2];
                } else if (this.connectType == OraConnectType.SERVICE) {
                    ipStr = ipPort[0];
                    if (StringUtils.isNotBlank(ipPort[1])) {
                        portStr = ipPort[1];
                    }
                    this.serviceName = ipPort[2];
                } else if (this.connectType == OraConnectType.PDB) {
                    ipStr = ipPort[0];
                    if (StringUtils.isNotBlank(ipPort[1])) {
                        portStr = ipPort[1];
                    }
                    this.pdbName = ipPort[2];
                } else {
                    throw new IllegalArgumentException("unsupported Oracle connect type:" + this.connectType);
                }
            } else {
                throw new IllegalArgumentException("unsupported Oracle host format:" + getHost());
            }
        }

        Properties properties = new Properties();
        properties.setProperty(DsConfigKeys.ID.getConfigKey(), safeStr(this.getInstanceId()));
        properties.setProperty(DsConfigKeys.HOST.getConfigKey(), safeStr(ipStr + ":" + portStr));
        properties.setProperty(DsConfigKeys.USER.getConfigKey(), safeStr(this.getUserName()));
        properties.setProperty(DsConfigKeys.PASSWORD.getConfigKey(), safeStr(this.getPassword()));
        properties.setProperty(DsConfigKeys.ORA_ORACLE_CONNECT_TYPE.getConfigKey(), safeStr(this.getConnectType().getDriverTypeCode()));
        properties.setProperty(DsConfigKeys.ORA_SID.getConfigKey(), safeStr(this.getSid()));
        properties.setProperty(DsConfigKeys.ORA_PDB.getConfigKey(), safeStr(this.getPdbName()));
        properties.setProperty(DsConfigKeys.ORA_SERVICE_NAME.getConfigKey(), safeStr(this.getServiceName()));
        properties.setProperty(DsConfigKeys.ORA_TNS_ADMIN.getConfigKey(), safeStr(this.getTnsAdmin()));
        properties.setProperty(DsConfigKeys.ORA_TNS_NAME.getConfigKey(), safeStr(this.getTnsName()));
        properties.setProperty(DsConfigKeys.AUTO_COMMIT.getConfigKey(), safeStr(StringUtils.toString(this.getAutoCommit())));
        properties.setProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey(), safeStr(StringUtils.toString(this.getConnectTimeoutMs())));
        properties.setProperty(DsConfigKeys.SO_TIMEOUT_SEC.getConfigKey(), safeStr(StringUtils.toString(this.getSoTimeoutSec())));
        return properties;
    }
}
