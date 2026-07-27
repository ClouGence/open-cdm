/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.mysql.analysis.sysobj;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.sql.common.analysis.sysobj.AbstractSysObjectRegistrySpi;
import com.clougence.sql.mysql.MySqlEngineSpi;
import com.clougence.sql.mysql.analysis.reference.MySqlResourceRegistry;
import com.clougence.sql.mysql.parser.MySqlVersion;

/** MySQL permission-exempt resources backed by the parser resource registry. */
public final class MySysObjectRegistrySpi extends AbstractSysObjectRegistrySpi {

    private final MySqlResourceRegistry resources = MySqlResourceRegistry.instance();

    @Override
    public String name() {
        return MySqlEngineSpi.NAME;
    }

    @Override
    protected boolean isRegisteredResource(BehaviorAction action, TargetType targetType, String catalog, String schema, String objectName, String databaseVersion) {
        MySqlVersion version = MySqlVersion.parse(databaseVersion);
        if (targetType == TargetType.Function && action == BehaviorAction.CALL) {
            return schema == null ? !resources.isUserDefinedFunction(objectName, false, version) : resources.isSystemFunction(schema, objectName, version);
        }
        if (targetType == TargetType.Procedure && action == BehaviorAction.CALL) {
            return schema != null && resources.isSystemProcedure(schema, objectName, version);
        }
        if (targetType == TargetType.Table && action == BehaviorAction.READ) {
            return schema == null && "DUAL".equalsIgnoreCase(objectName) || schema != null && resources.isMetadataTable(schema, objectName, version);
        }
        return false;
    }
}
