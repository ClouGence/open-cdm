/* Copyright 2026 杭州开云集致科技有限公司. Licensed under the Apache License, Version 2.0. */
package com.clougence.clouddm.ds.hologres.sql.analysis;

import com.clougence.clouddm.ds.hologres.sql.HgSqlEngineSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.sql.common.analysis.sysobj.AbstractSysObjectRegistrySpi;
import com.clougence.sql.postgres.analysis.sysobj.PgSysObjectRegistrySpi;
import com.clougence.sql.postgres.parser.PostgresVersion;

public final class HgSysObjectRegistrySpi extends AbstractSysObjectRegistrySpi {
    private final PgSysObjectRegistrySpi postgres = new PgSysObjectRegistrySpi();

    @Override
    public String name() {
        return HgSqlEngineSpi.NAME;
    }

    @Override
    protected boolean isRegisteredResource(BehaviorAction action, TargetType targetType, String catalog, String schema, String objectName, String databaseVersion) {
        return postgres.isPermissionExempt(action, targetType, catalog, schema, objectName, PostgresVersion.POSTGRES_12.versionString());
    }
}
