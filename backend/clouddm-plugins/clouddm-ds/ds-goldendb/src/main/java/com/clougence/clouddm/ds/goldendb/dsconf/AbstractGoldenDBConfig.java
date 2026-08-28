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
package com.clougence.clouddm.ds.goldendb.dsconf;

import java.util.Properties;

import com.clougence.clouddm.base.metadata.ds.*;
import com.clougence.clouddm.dsfamily.mysql.i18n.MyConfigI18nKeys;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@Getter
@Setter
@FieldNameConstants
public abstract class AbstractGoldenDBConfig extends DataSourceConfig {

    @ConfigDef(name = Fields.defaultSchema, group = DsConfigGroup.GENERAL, labelKey = MyConfigI18nKeys.CONFIG_RDB_DEFAULT_SCHEMA_LABEL, descKey = MyConfigI18nKeys.CONFIG_RDB_DEFAULT_SCHEMA_DESC, readOnly = false)
    private String  defaultSchema;

    @ConfigDef(name = Fields.clientTimeZone, defaultValue = "Asia/Shanghai", group = DsConfigGroup.OPTIONS, labelKey = MyConfigI18nKeys.CONFIG_RDB_CLIENT_TIME_ZONE_LABEL, descKey = MyConfigI18nKeys.CONFIG_RDB_CLIENT_TIME_ZONE_DESC, readOnly = false)
    private String  clientTimeZone;

    @ConfigDef(name = Fields.connectTimeoutMs, defaultValue = "5000", group = DsConfigGroup.ADVANCED, labelKey = MyConfigI18nKeys.CONFIG_RDB_CONN_TIMEOUT_MS_LABEL, descKey = MyConfigI18nKeys.CONFIG_RDB_CONN_TIMEOUT_MS_DESC, readOnly = false)
    private Long    connectTimeoutMs;

    @ConfigDef(name = Fields.soTimeoutSec, defaultValue = "10", group = DsConfigGroup.ADVANCED, labelKey = MyConfigI18nKeys.CONFIG_DS_SO_TIMEOUT_MS_LABEL, descKey = MyConfigI18nKeys.CONFIG_DS_SO_TIMEOUT_MS_DESC, readOnly = false)
    private Integer soTimeoutSec;

    @ConfigDef(name = Fields.connectionCharset, defaultValue = "utf8", group = DsConfigGroup.ADVANCED, labelKey = MyConfigI18nKeys.CONFIG_MY_CONN_CHARSET_LABEL, descKey = MyConfigI18nKeys.CONFIG_MY_CONN_CHARSET_DESC, readOnly = false)
    private String  connectionCharset;

    protected AbstractGoldenDBConfig(DataSourceType dataSourceType){
        setDataSourceType(dataSourceType);
    }

    public Properties asDriverProperties() {
        Properties properties = new Properties();
        properties.setProperty(DsConfigKeys.ID.getConfigKey(), safeStr(getInstanceId()));
        properties.setProperty(DsConfigKeys.HOST.getConfigKey(), safeStr(getHost()));
        properties.setProperty(DsConfigKeys.USER.getConfigKey(), safeStr(getUserName()));
        properties.setProperty(DsConfigKeys.PASSWORD.getConfigKey(), safeStr(getPassword()));
        properties.setProperty(DsConfigKeys.DEFAULT_SCHEMA.getConfigKey(), safeStr(defaultSchema));
        properties.setProperty(DsConfigKeys.AUTO_COMMIT.getConfigKey(), safeStr(StringUtils.toString(getAutoCommit())));
        properties.setProperty(DsConfigKeys.CONNECT_TIMEOUT_MS.getConfigKey(), safeStr(StringUtils.toString(connectTimeoutMs)));
        properties.setProperty(DsConfigKeys.SO_TIMEOUT_SEC.getConfigKey(), safeStr(StringUtils.toString(soTimeoutSec)));
        properties.setProperty(DsConfigKeys.CLIENT_TIME_ZONE.getConfigKey(), safeStr(clientTimeZone));
        properties.setProperty(DsConfigKeys.CLIENT_ENCODING.getConfigKey(), safeStr(connectionCharset));
        properties.setProperty(GoldenDBCompatibilityMode.EXPECTED_MODE_PROPERTY, GoldenDBCompatibilityMode.fromDataSourceType(getDataSourceType()).getServerMode());
        properties.setProperty("serverTimezone", safeStr(clientTimeZone));
        if (getDataSourceType() == DataSourceType.GoldenDBOracle) {
            properties.setProperty("useSSL", "false");
            properties.setProperty("requireSSL", "false");
            properties.setProperty("verifyServerCertificate", "false");
        } else {
            properties.setProperty("sslMode", "DISABLED");
        }
        properties.setProperty("logger", "com.goldendb.jdbc.log.NullLogger");
        properties.setProperty("useUnicode", "true");
        properties.setProperty("useCursorFetch", "false");
        properties.setProperty("cachePrepStmts", "true");
        properties.setProperty("rewriteBatchedStatements", "true");
        return properties;
    }
}
