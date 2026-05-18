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

import com.clougence.clouddm.base.metadata.ds.ConfigDef;
import com.clougence.clouddm.base.metadata.ds.ConfigI18nKey;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.base.metadata.rdp.enumeration.DsConfigGroup;
import com.clougence.clouddm.sdk.execute.dsconf.Serialization;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2020/11/6 10:23
 */
@Getter
@Setter
@Serialization(provider = OraSerializationSpi.PROVIDER_NAME)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OraConfig extends DataSourceConfig {

    @ConfigDef(name = "connectType", valueRequire = false, descKey = ConfigI18nKey.CONFIG_ORACLE_CONNECT_TYPE_DESCRIPTION, valueAdvance = "SID / SERVICE / TNS", readOnly = false)
    private OraConnectType connectType;

    @ConfigDef(name = "sid", valueRequire = false, descKey = ConfigI18nKey.CONFIG_ORACLE_SID_DESCRIPTION, readOnly = false)
    private String         sid;

    @ConfigDef(name = "serviceName", valueRequire = false, descKey = ConfigI18nKey.CONFIG_ORACLE_SERVICE_DESCRIPTION, readOnly = false)
    private String         serviceName;

    @ConfigDef(name = "pdbName", valueRequire = false, descKey = ConfigI18nKey.CONFIG_ORACLE_PDB_DESCRIPTION, readOnly = false)
    private String         pdbName;

    @ConfigDef(name = "tnsAdmin", valueRequire = false, descKey = ConfigI18nKey.CONFIG_ORACLE_TNS_ADMIN_DESCRIPTION, readOnly = false)
    private String         tnsAdmin;

    @ConfigDef(name = "tnsName", valueRequire = false, descKey = ConfigI18nKey.CONFIG_ORACLE_TNS_NAME_DESCRIPTION, readOnly = false)
    private String         tnsName;

    @ConfigDef(name = "excludeOraMaintainedSchemas", defaultValue = "false", valueRequire = false, descKey = ConfigI18nKey.CONFIG_ORACLE_EXCLUDE_ORA_MAINTAINED_SCHEMAS_DESCRIPTION, readOnly = false, valueAdvance = "true - false", group = DsConfigGroup.OPTIONS)
    private Boolean        excludeOraMaintainedSchemas = true;

    public OraConfig(){
        setDataSourceType(DataSourceType.Oracle);
    }

    @Override
    public void deserialize() {
        super.deserialize();
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
        properties.setProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey(), safeStr(StringUtils.toString(this.getConnectTimeoutMs())));
        properties.setProperty(DsConfigKeys.SO_TIMEOUT_SEC.getConfigKey(), safeStr(StringUtils.toString(this.getSoTimeoutSec())));

        properties.setProperty(DsConfigKeys.ORA_ORACLE_CONNECT_TYPE.getConfigKey(), safeStr(this.getConnectType().getDriverTypeCode()));
        properties.setProperty(DsConfigKeys.ORA_SID.getConfigKey(), safeStr(this.getSid()));
        properties.setProperty(DsConfigKeys.ORA_PDB.getConfigKey(), safeStr(this.getPdbName()));
        properties.setProperty(DsConfigKeys.ORA_SERVICE_NAME.getConfigKey(), safeStr(this.getServiceName()));
        properties.setProperty(DsConfigKeys.ORA_TNS_ADMIN.getConfigKey(), safeStr(this.getTnsAdmin()));
        properties.setProperty(DsConfigKeys.ORA_TNS_NAME.getConfigKey(), safeStr(this.getTnsName()));
        return properties;
    }
}
