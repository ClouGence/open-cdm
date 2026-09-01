/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.tidb.sql.analysis.sysobj;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.clougence.clouddm.ds.tidb.sql.TiSqlEngineSpi;
import com.clougence.clouddm.ds.tidb.sql.parser.TiDBVersion;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.sql.common.analysis.sysobj.AbstractSysObjectRegistrySpi;
import com.clougence.sql.common.registry.RegisteredResourceType;

/** TiDB permission-exempt system objects backed by the datasource resource registry. */
public final class TiDBSysObjectRegistrySpi extends AbstractSysObjectRegistrySpi {

    private static final Pattern       TIDB_VERSION     = Pattern.compile("(?i)\\bTiDB(?:\\s+(?:SERVER|VERSION))?[\\s:_-]*v?(\\d+)");
    private static final Pattern       LEADING_VERSION  = Pattern.compile("(?i)^\\s*v?(\\d+)");

    private final TiDBResourceRegistry resources;

    public TiDBSysObjectRegistrySpi(){
        this.resources = TiDBResourceRegistry.instance();
    }

    @Override
    public String name() {
        return TiSqlEngineSpi.NAME;
    }

    @Override
    protected boolean isRegisteredResource(BehaviorAction action, TargetType targetType, String catalog, String schema, String objectName, String databaseVersion) {
        TiDBVersion version = parserVersion(databaseVersion);
        if (version == null) {
            return false;
        }
        RegisteredResourceType type = resourceType(targetType);
        if (type == null || !supportsAction(type, action, objectName, version)) {
            return false;
        }
        int major = version.major();
        if (catalog != null && schema != null && resources.contains(type, major, catalog, schema, objectName)) {
            return resources.skipsPermission(type, major, catalog, schema, objectName);
        }
        if (schema != null && resources.contains(type, major, schema, objectName)) {
            return resources.skipsPermission(type, major, schema, objectName);
        }
        return schema == null && resources.contains(type, major, objectName) && resources.skipsPermission(type, major, objectName);
    }

    private boolean supportsAction(RegisteredResourceType type, BehaviorAction action, String objectName, TiDBVersion version) {
        if (type == RegisteredResourceType.FUNCTION) {
            return action == resources.functionBehavior(objectName, version.major());
        }
        if (type == RegisteredResourceType.PROCEDURE) {
            return action == BehaviorAction.CALL;
        }
        return action == BehaviorAction.READ;
    }

    private static RegisteredResourceType resourceType(TargetType targetType) {
        if (targetType == TargetType.Function) {
            return RegisteredResourceType.FUNCTION;
        }
        if (targetType == TargetType.Procedure) {
            return RegisteredResourceType.PROCEDURE;
        }
        if (targetType == TargetType.Table || targetType == TargetType.View || targetType == TargetType.Materialized) {
            return RegisteredResourceType.TABLE;
        }
        if (targetType == TargetType.Type) {
            return RegisteredResourceType.TYPE;
        }
        return null;
    }

    private static TiDBVersion parserVersion(String databaseVersion) {
        if (databaseVersion == null || databaseVersion.isBlank()) {
            return TiDBVersion.LATEST;
        }
        Matcher matcher = TIDB_VERSION.matcher(databaseVersion);
        boolean found = matcher.find();
        if (!found) {
            matcher = LEADING_VERSION.matcher(databaseVersion);
            found = matcher.find();
        }
        if (!found) {
            return null;
        }
        int major = Integer.parseInt(matcher.group(1));
        if (major < TiDBVersion.TIDB_5.major() || major > TiDBVersion.TIDB_9.major()) {
            return null;
        }
        return TiDBVersion.parse(Integer.toString(major));
    }
}
