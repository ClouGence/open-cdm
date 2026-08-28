/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.ds.dameng.sql.analysis.sysobj;

import com.clougence.clouddm.ds.dameng.sql.DmSqlEngineSpi;
import com.clougence.clouddm.ds.dameng.sql.analysis.reference.DmResourceRegistry;
import com.clougence.clouddm.ds.dameng.sql.parser.DmVersion;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.sql.common.analysis.sysobj.AbstractSysObjectRegistrySpi;

/** Dameng permission-exempt resources backed by the parser resource registry. */
public final class DmSysObjectRegistrySpi extends AbstractSysObjectRegistrySpi {

    private final DmResourceRegistry resources = DmResourceRegistry.instance();

    @Override
    public String name() {
        return DmSqlEngineSpi.NAME;
    }

    @Override
    protected boolean isRegisteredResource(BehaviorAction action, TargetType targetType, String catalog, String schema, String objectName, String databaseVersion) {
        DmVersion parserVersion = DmVersion.parse(databaseVersion);
        if (!DmVersion.ge(parserVersion, DmVersion.DM_8)) {
            return false;
        }

        int version = DmResourceRegistry.DM8;
        if (targetType == TargetType.Function) {
            BehaviorAction expectedAction = resources.functionBehavior(objectName, version);
            boolean registered = schema == null && resources.isSystemFunction(objectName, version);
            if (schema != null) {
                expectedAction = resources.functionBehavior(schema, objectName);
                registered = false;
                if (catalog != null) {
                    registered = resources.isSystemFunction(catalog, schema, objectName, version);
                }
                if (!registered) {
                    registered = resources.isSystemFunction(schema, objectName, version);
                }
            }
            return registered && action == expectedAction;
        }
        if (targetType == TargetType.Procedure && action == BehaviorAction.CALL) {
            if (catalog != null && schema != null && resources.isSystemProcedure(catalog, schema, objectName, version)) {
                return true;
            }
            if (schema != null && resources.isSystemProcedure(schema, objectName, version)) {
                return true;
            }
            return schema == null && resources.isSystemProcedure(objectName, version);
        }
        if ((targetType == TargetType.Table || targetType == TargetType.View) && action == BehaviorAction.READ) {
            if (catalog != null && schema != null && resources.isSystemView(catalog, schema, objectName, version)) {
                return true;
            }
            if (schema != null && resources.isSystemView(schema, objectName, version)) {
                return true;
            }
            return schema == null && resources.isSystemView(objectName, version);
        }
        if (targetType == TargetType.Type && action == BehaviorAction.READ) {
            if (catalog != null && schema != null && resources.isSystemType(catalog, schema, objectName, version)) {
                return true;
            }
            if (schema != null && resources.isSystemType(schema, objectName, version)) {
                return true;
            }
            return schema == null && resources.isSystemType(objectName, version);
        }
        return false;
    }
}
