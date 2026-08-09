/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.doris.analysis.sysobj;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.sql.common.analysis.sysobj.AbstractSysObjectRegistrySpi;
import com.clougence.sql.common.registry.RegisteredResourceType;
import com.clougence.sql.doris.DrSqlEngineSpi;
import com.clougence.sql.doris.parser.DorisVersion;

/** Doris permission-exempt resources backed by the parser resource registry. */
public final class DrSysObjectRegistrySpi extends AbstractSysObjectRegistrySpi {

    private final DrResourceRegistry resources;

    public DrSysObjectRegistrySpi(){
        this.resources = DrResourceRegistry.instance();
    }

    @Override
    public String name() {
        return DrSqlEngineSpi.NAME;
    }

    @Override
    protected boolean isRegisteredResource(BehaviorAction action, TargetType targetType, String catalog, String schema, String objectName, String databaseVersion) {
        DorisVersion version = DorisVersion.parse(databaseVersion);
        if (targetType == TargetType.Function && action == BehaviorAction.CALL) {
            return isFunction(catalog, schema, objectName, version) && skipsPermission(RegisteredResourceType.FUNCTION, catalog, schema, objectName, version);
        }
        if (targetType == TargetType.Procedure && action == BehaviorAction.CALL) {
            return isProcedure(catalog, schema, objectName, version) && skipsPermission(RegisteredResourceType.PROCEDURE, catalog, schema, objectName, version);
        }
        if ((targetType == TargetType.Table || targetType == TargetType.View || targetType == TargetType.Materialized) && action == BehaviorAction.READ) {
            return isRelation(catalog, schema, objectName, version) && skipsPermission(RegisteredResourceType.TABLE, catalog, schema, objectName, version);
        }
        return false;
    }

    private boolean isFunction(String catalog, String schema, String objectName, DorisVersion version) {
        if (catalog != null && schema != null && resources.isSystemFunction(catalog, schema, objectName, version)) {
            return true;
        }
        if (schema != null && resources.isSystemFunction(schema, objectName, version)) {
            return true;
        }
        return schema == null && resources.isSystemFunction(objectName, version);
    }

    private boolean isProcedure(String catalog, String schema, String objectName, DorisVersion version) {
        if (catalog != null && schema != null && resources.isSystemProcedure(catalog, schema, objectName, version)) {
            return true;
        }
        if (schema != null && resources.isSystemProcedure(schema, objectName, version)) {
            return true;
        }
        return schema == null && resources.isSystemProcedure(objectName, version);
    }

    private boolean isRelation(String catalog, String schema, String objectName, DorisVersion version) {
        if (catalog != null && schema != null && resources.isSystemRelation(catalog, schema, objectName, version)) {
            return true;
        }
        return schema != null && resources.isSystemRelation(schema, objectName, version);
    }

    private boolean skipsPermission(RegisteredResourceType type, String catalog, String schema, String objectName, DorisVersion version) {
        if (catalog != null && schema != null && resources.skipsPermission(type, version, catalog, schema, objectName)) {
            return true;
        }
        if (schema != null && resources.skipsPermission(type, version, schema, objectName)) {
            return true;
        }
        return schema == null && resources.skipsPermission(type, version, objectName);
    }
}
