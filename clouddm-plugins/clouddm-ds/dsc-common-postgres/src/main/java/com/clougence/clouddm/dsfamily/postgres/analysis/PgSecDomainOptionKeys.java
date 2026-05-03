package com.clougence.clouddm.dsfamily.postgres.analysis;

public interface PgSecDomainOptionKeys {

    String OPT_CATALOG_OWNER             = "owner";
    String OPT_CATALOG_TABLESPACE        = "tableSpace";
    String OPT_CATALOG_RCV               = "refreshCollationVersion"; //refreshCollationVersion
    String OPT_CATALOG_RESET             = "reset";
    String OPT_CATALOG_ALLOW_CONNECTIONS = "allowConnections";
    String OPT_CATALOG_CONNECTION_LIMIT  = "connectionLimit";
    String OPT_CATALOG_IS_TEMPLATE       = "isTemplate";
    String OPT_CATALOG_CONF_SET          = "configSet";
    String OPT_CATALOG_CONF_NAME         = "configName";
    String OPT_CATALOG_CONF_VALUE        = "configValue";

    String OPT_JOIN_NATURAL              = "naturalJoin";
}
