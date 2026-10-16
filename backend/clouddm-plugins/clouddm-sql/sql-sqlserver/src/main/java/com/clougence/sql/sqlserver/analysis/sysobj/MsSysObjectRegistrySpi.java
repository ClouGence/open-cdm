/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.sqlserver.analysis.sysobj;

import java.util.List;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.sql.common.analysis.sysobj.AbstractSysObjectRegistrySpi;
import com.clougence.sql.sqlserver.MsSqlSqlEngineSpi;
import com.clougence.sql.sqlserver.parser.SqlServerVersion;
import com.clougence.sql.sqlserver.analysis.reference.MsSqlResourceRegistry;

/** SQL Server system procedures. */
public final class MsSysObjectRegistrySpi extends AbstractSysObjectRegistrySpi {

    @Override
    public String name() {
        return MsSqlSqlEngineSpi.NAME;
    }

    @Override
    protected boolean isRegisteredResource(BehaviorAction action, TargetType targetType, String catalog, String schema, String objectName, String databaseVersion) {
        if (action != BehaviorAction.CALL) {
            return false;
        }
        int version = Integer.parseInt(SqlServerVersion.parse(databaseVersion).versionString());
        MsSqlResourceRegistry registry = MsSqlResourceRegistry.instance();
        if (targetType == TargetType.Function && catalog == null && schema == null) {
            return registry.isBuiltinFunction(objectName, version);
        }
        return targetType == TargetType.Procedure && schema != null
               && (registry.isSystemProcedure(List.of(schema, objectName)) || registry.isSessionProcedure(schema, objectName, version));
    }
}
