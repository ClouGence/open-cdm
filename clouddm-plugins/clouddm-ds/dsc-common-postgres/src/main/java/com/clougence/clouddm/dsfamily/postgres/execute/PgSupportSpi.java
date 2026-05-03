package com.clougence.clouddm.dsfamily.postgres.execute;

import java.util.Arrays;
import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbSupportLevel;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbSupportSpi;

import lombok.extern.slf4j.Slf4j;

/**
 * https://www.postgresql.org/docs/current/transaction-iso.html
 * https://www.postgresql.org/docs/current/sql-set-transaction.html
 * @author mode 2022/11/03 16:48
 **/
@Slf4j
public class PgSupportSpi implements RdbSupportSpi {

    //    private final List<RdbIsolation> isolationDef = Arrays.asList(RdbIsolation.values());
    private final List<RdbIsolation> isolationDef = Arrays.asList(RdbIsolation.DEFAULT);

    @Override
    public RdbSupportLevel supportChangeCatalog(DataSourceConfig dsConfig) {
        return RdbSupportLevel.Hint;
    }

    @Override
    public RdbSupportLevel supportChangeSchema(DataSourceConfig dsConfig) {
        return RdbSupportLevel.Allow;
    }

    @Override
    public RdbSupportLevel supportChangeIsolation(DataSourceConfig dsConfig) {
        return RdbSupportLevel.Allow;
    }

    @Override
    public RdbSupportLevel supportChangeAutoCommit(DataSourceConfig dsConfig) {
        return RdbSupportLevel.Allow;
    }

    @Override
    public RdbSupportLevel supportChangeReadOnly(DataSourceConfig dsConfig) {
        return RdbSupportLevel.No;
    }

    @Override
    public RdbSupportLevel supportCancelQuery(DataSourceConfig dsConfig) {
        return RdbSupportLevel.Hint;
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
        return this.isolationDef;
    }

    @Override
    public boolean supportMultiStatement(boolean isDesktop) {
        return true;
    }
}
