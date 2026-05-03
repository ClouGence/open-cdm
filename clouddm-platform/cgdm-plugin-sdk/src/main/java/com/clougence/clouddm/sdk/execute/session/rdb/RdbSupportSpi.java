package com.clougence.clouddm.sdk.execute.session.rdb;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.Spi;

/**
 * @author mode 2022/2/21 13:13:09
 */
public interface RdbSupportSpi extends Spi {

    String HINT_FOR_CHANGE_CATALOG     = "SUPPORT_LEVEL_HINT_FOR_CHANGE_CATALOG";
    String HINT_FOR_CHANGE_SCHEMA      = "SUPPORT_LEVEL_HINT_FOR_CHANGE_SCHEMA";
    String HINT_FOR_CHANGE_ISOLATION   = "SUPPORT_LEVEL_HINT_FOR_CHANGE_ISOLATION";
    String HINT_FOR_CHANGE_AUTO_COMMIT = "SUPPORT_LEVEL_HINT_FOR_CHANGE_AUTO_COMMIT";
    String HINT_FOR_CHANGE_READONLY    = "SUPPORT_LEVEL_HINT_FOR_CHANGE_READONLY";
    String HINT_FOR_CANCEL_QUERY       = "SUPPORT_LEVEL_HINT_FOR_CANCEL_QUERY";
    String HINT_FOR_EXPLAIN_QUERY      = "SUPPORT_LEVEL_HINT_FOR_EXPLAIN_QUERY";
    String HINT_FOR_FORMAT_QUERY       = "SUPPORT_LEVEL_HINT_FOR_FORMAT_QUERY";
    String HINT_FOR_ARGS_QUERY         = "SUPPORT_LEVEL_HINT_FOR_ARGS_QUERY";

    /** when RdbSupportLevel is Hint, need i18n : SUPPORT_LEVEL_HINT_FOR_CHANGE_CATALOG */
    RdbSupportLevel supportChangeCatalog(DataSourceConfig dsConfig);

    /** when RdbSupportLevel is Hint, need i18n : SUPPORT_LEVEL_HINT_FOR_CHANGE_SCHEMA */
    RdbSupportLevel supportChangeSchema(DataSourceConfig dsConfig);

    /** when RdbSupportLevel is Hint, need i18n : SUPPORT_LEVEL_HINT_FOR_CHANGE_ISOLATION */
    RdbSupportLevel supportChangeIsolation(DataSourceConfig dsConfig);

    /** when RdbSupportLevel is Hint, need i18n : SUPPORT_LEVEL_HINT_FOR_CHANGE_AUTO_COMMIT */
    RdbSupportLevel supportChangeAutoCommit(DataSourceConfig dsConfig);

    /** when RdbSupportLevel is Hint, need i18n : SUPPORT_LEVEL_HINT_FOR_CHANGE_READONLY */
    RdbSupportLevel supportChangeReadOnly(DataSourceConfig dsConfig);

    /** when RdbSupportLevel is Hint, need i18n : SUPPORT_LEVEL_HINT_FOR_CANCEL_QUERY */
    RdbSupportLevel supportCancelQuery(DataSourceConfig dsConfig);

    /** when RdbSupportLevel is Hint, need i18n : SUPPORT_LEVEL_HINT_FOR_EXPLAIN_QUERY */
    RdbSupportLevel supportExplain(DataSourceConfig dsConfig);

    /** when RdbSupportLevel is Hint, need i18n : SUPPORT_LEVEL_HINT_FOR_FORMAT_QUERY */
    RdbSupportLevel supportFormat(DataSourceConfig dsConfig);

    /** when RdbSupportLevel is Hint, need i18n : SUPPORT_LEVEL_HINT_FOR_ARGS_QUERY */
    RdbSupportLevel supportArgs(DataSourceConfig dsConfig);

    List<RdbIsolation> supportIsolation();

    boolean supportMultiStatement(boolean isDesktop);
}
