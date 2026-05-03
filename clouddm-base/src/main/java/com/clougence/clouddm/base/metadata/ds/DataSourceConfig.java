package com.clougence.clouddm.base.metadata.ds;

import java.util.Objects;
import java.util.Properties;

import com.clougence.clouddm.base.metadata.rdp.enumeration.DsConfigGroup;
import com.clougence.clouddm.base.metadata.rdp.enumeration.SecurityType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2020/11/6 18:52
 */
@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataSourceConfig implements DeserializeAble, ConfigKeys {

    // ------------------------------------------------------------------------------------------------------------------------ basic
    @ConfigDef(name = "instanceId", descKey = ConfigI18nKey.CONFIG_DS_INSTANCE_ID_DESCRIPTION)
    private String         instanceId;

    /** DataSourceType */
    @ConfigDef(name = DM_DS_KEY_DS_TYPE, descKey = ConfigI18nKey.CONFIG_DS_TYPE_DESCRIPTION)
    private DataSourceType dataSourceType;

    @ConfigDef(name = DM_DS_KEY_VERSION, descKey = ConfigI18nKey.CONFIG_RDB_VERSION_DESCRIPTION)
    private String         version;

    @ConfigDef(name = DM_DS_KEY_DRIVER_VERSION, descKey = ConfigI18nKey.CONFIG_RDB_VERSION_DESCRIPTION, readOnly = false)
    private String         driverVersion;

    @ConfigDef(name = DM_DS_KEY_SEC_TYPE, descKey = ConfigI18nKey.CONFIG_DS_SECURITY_TYPE_DESCRIPTION, readOnly = false, valueAdvance = "NONE / USER_PASSWD / ONLY_PASSWD / ONLY_USER / USER_PASSWD_WITH_TLS / KERBEROS")
    private SecurityType   securityType;

    // ------------------------------------------------------------------------------------------------------------------------ default session config
    @ConfigDef(name = "soTimeoutSec", defaultValue = "10", valueRequire = false, descKey = ConfigI18nKey.CONFIG_DS_SO_TIMEOUT_MS_DESCRIPTION, readOnly = false, valueAdvance = "10 - 60", group = DsConfigGroup.OPTIONS)
    private Integer        soTimeoutSec;
    @ConfigDef(name = "maxIdleTimeSec", defaultValue = "300", descKey = ConfigI18nKey.CONFIG_DS_MAX_IDLE_TIME_SEC_DESCRIPTION, readOnly = false, valueAdvance = "value is second.", group = DsConfigGroup.OPTIONS)
    private Integer        maxIdleTimeSec;
    @ConfigDef(name = "readOnly", defaultValue = "false", valueAdvance = "true or false", descKey = ConfigI18nKey.CONFIG_DS_READONLY_DESCRIPTION, readOnly = false)
    private Boolean        readOnly;

    // console
    @ConfigDef(name = "onlineMaxConnections", defaultValue = "100", descKey = ConfigI18nKey.CONFIG_DS_ONLINE_MAX_CONNECTIONS_DESCRIPTION, readOnly = false)
    private Integer        onlineMaxConnections;
    @ConfigDef(name = "onlineMaxQueryTimeoutSec", defaultValue = "30", descKey = ConfigI18nKey.CONFIG_DS_ONLINE_MAX_QUERY_TIMEOUT_SEC_DESCRIPTION, readOnly = false)
    private Integer        onlineMaxQueryTimeoutSec;

    // background
    @ConfigDef(name = "exportMaxConnections", defaultValue = "50", descKey = ConfigI18nKey.CONFIG_DS_EXPORT_MAX_CONCURRENT_DESCRIPTION, readOnly = false)
    private Integer        exportMaxConnections;
    @ConfigDef(name = "exportMaxQueryTimeoutSec", defaultValue = "300", descKey = ConfigI18nKey.CONFIG_DS_EXPORT_MAX_QUERY_TIMEOUT_SEC_DESCRIPTION, readOnly = false)
    private Integer        exportMaxQueryTimeoutSec;

    // jdbc config
    // ---------------------------------------------------------------------------------------------------

    @ConfigDef(name = DM_DS_KEY_USERNAME, descKey = ConfigI18nKey.CONFIG_RDB_USERNAME_DESCRIPTION, readOnly = false)
    private String         userName;

    @ConfigDef(name = DM_DS_KEY_PASSWORD, descKey = ConfigI18nKey.CONFIG_RDB_PASSWORD_DESCRIPTION, isSecret = true, readOnly = false)
    private String         password;

    @ConfigDef(name = "defaultDataBase", valueRequire = false, descKey = ConfigI18nKey.CONFIG_RDB_DEFAULT_DB_DESCRIPTION, readOnly = false)
    private String         defaultDataBase;

    @ConfigDef(name = "defaultSchema", valueRequire = false, descKey = ConfigI18nKey.CONFIG_RDB_DEFAULT_SCHEMA_DESCRIPTION, readOnly = false)
    private String         defaultSchema;

    /** ip:port */
    @ConfigDef(name = DM_DS_KEY_HOST, descKey = ConfigI18nKey.CONFIG_RDB_CONN_HOST_DESCRIPTION, readOnly = false)
    private String         host;

    @ConfigDef(name = "connectTimeoutMs", defaultValue = "5000", valueRequire = false, descKey = ConfigI18nKey.CONFIG_RDB_CONN_TIMEOUT_MS_DESCRIPTION, readOnly = false, valueAdvance = "2000 - 100000", group = DsConfigGroup.OPTIONS)
    private Long           connectTimeoutMs;

    @ConfigDef(name = "isolation", defaultValue = "DEFAULT", valueAdvance = "DEFAULT/READ_UNCOMMITTED/READ_COMMITTED/REPEATABLE_READ/SERIALIZABLE", descKey = ConfigI18nKey.CONFIG_RDB_ISOLATION_DESCRIPTION, readOnly = false)
    private String         isolation;

    @ConfigDef(name = "autoCommit", defaultValue = "true", valueAdvance = "true or false", descKey = ConfigI18nKey.CONFIG_RDB_TRANSACTION_DESCRIPTION, readOnly = false)
    private Boolean        autoCommit;

    //    @ConfigDef(name = "schemaPattern", defaultValue = ".*:.*", valueAdvance = "using regex pattern. database and schema", descKey = ConfigI18nKey.CONFIG_RDB_TRANSACTION_DESCRIPTION, readOnly = false)
    //    private Boolean schemaPattern;

    @ConfigDef(name = DM_DS_KEY_STORE_PASSWORD, descKey = ConfigI18nKey.CONFIG_RDB_STORE_PASSWORD_DESCRIPTION, isSecret = true, readOnly = false)
    private String         storePassword;

    // ----------------------------------------------------------------------------------------------- config version  UUID,for ssl file update
    @ConfigDef(name = "configVersion", descKey = ConfigI18nKey.CONFIG_RDB_CONFIG_VERSION_DESCRIPTION, defaultValue = "1")
    private Long           configVersion;

    // aliyun config
    // ---------------------------------------------------------------------------------------------------
    @Deprecated // should be used enum to represent the platform.
    @ConfigDef(name = "aliyunInstanceId", descKey = ConfigI18nKey.CONFIG_DS_DEPLOY_ALIYUN_INSTANCE_ID_DESCRIPTION, group = DsConfigGroup.CLOUD)
    private String         aliyunInstanceId;

    public void setDataSourceType(DataSourceType dataSourceType) {
        if (this.dataSourceType == null) {
            this.dataSourceType = Objects.requireNonNull(dataSourceType, "dataSourceType can not be null.");
            return;
        }

        if (dataSourceType != this.dataSourceType) {
            throw new UnsupportedOperationException("different values can only be initialized once.");
        }
    }

    @Override
    public void deserialize() {
    }

    protected String safeStr(String value) {
        return value == null ? "" : value;
    }

    public Properties asDriverProperties() {
        return new Properties();
    }
}
