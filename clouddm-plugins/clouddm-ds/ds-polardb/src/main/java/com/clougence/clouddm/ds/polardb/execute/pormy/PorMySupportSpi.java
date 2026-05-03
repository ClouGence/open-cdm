package com.clougence.clouddm.ds.polardb.execute.pormy;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbSupportLevel;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbSupportSpi;

/**
 * @Author: Ekko
 * @Date: 2024-06-18 11:31
 */
public class PorMySupportSpi implements RdbSupportSpi {

    @Override
    public RdbSupportLevel supportChangeCatalog(DataSourceConfig dsConfig) {
        return RdbSupportLevel.No;
    }

    @Override
    public RdbSupportLevel supportChangeSchema(DataSourceConfig dsConfig) {
        return RdbSupportLevel.No;
    }

    @Override
    public RdbSupportLevel supportChangeIsolation(DataSourceConfig dsConfig) {
        return RdbSupportLevel.No;
    }

    @Override
    public RdbSupportLevel supportChangeAutoCommit(DataSourceConfig dsConfig) {
        return RdbSupportLevel.No;
    }

    @Override
    public RdbSupportLevel supportChangeReadOnly(DataSourceConfig dsConfig) {
        return RdbSupportLevel.No;
    }

    @Override
    public RdbSupportLevel supportCancelQuery(DataSourceConfig dsConfig) {
        return RdbSupportLevel.No;
    }

    @Override
    public RdbSupportLevel supportExplain(DataSourceConfig dsConfig) {
        return RdbSupportLevel.Allow;
    }

    @Override
    public RdbSupportLevel supportFormat(DataSourceConfig dsConfig) {
        return RdbSupportLevel.Allow;
    }

    @Override
    public RdbSupportLevel supportArgs(DataSourceConfig dsConfig) {
        return RdbSupportLevel.No;
    }

    @Override
    public List<RdbIsolation> supportIsolation() {
        return Collections.emptyList();
    }

    @Override
    public boolean supportMultiStatement(boolean isDesktop) {
        return true;
    }
}
