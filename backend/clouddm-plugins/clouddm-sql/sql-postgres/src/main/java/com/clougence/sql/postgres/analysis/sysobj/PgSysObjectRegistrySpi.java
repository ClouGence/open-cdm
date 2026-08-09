/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.postgres.analysis.sysobj;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.sql.common.analysis.sysobj.AbstractSysObjectRegistrySpi;
import com.clougence.sql.common.registry.RegisteredResourceType;
import com.clougence.sql.postgres.PgSqlEngineSpi;
import com.clougence.sql.postgres.parser.PostgresVersion;

/** PostgreSQL permission-exempt resources backed by the parser resource registry. */
public final class PgSysObjectRegistrySpi extends AbstractSysObjectRegistrySpi {

    private final PgResourceRegistry resources;

    public PgSysObjectRegistrySpi(){
        this.resources = PgResourceRegistry.instance();
    }

    @Override
    public String name() {
        return PgSqlEngineSpi.NAME;
    }

    @Override
    protected boolean isRegisteredResource(BehaviorAction action, TargetType targetType, String catalog, String schema, String objectName, String databaseVersion) {
        PostgresVersion version = requestedVersion(databaseVersion);
        if (version == null) {
            return false;
        }
        if (targetType == TargetType.Function && action == BehaviorAction.CALL) {
            return isRegistered(RegisteredResourceType.FUNCTION, version, catalog, schema, objectName);
        }
        if (targetType == TargetType.Procedure && action == BehaviorAction.CALL) {
            return isRegistered(RegisteredResourceType.PROCEDURE, version, catalog, schema, objectName);
        }
        if ((targetType == TargetType.Table || targetType == TargetType.View || targetType == TargetType.Materialized) && action == BehaviorAction.READ) {
            return isRegistered(RegisteredResourceType.TABLE, version, catalog, schema, objectName);
        }
        if (targetType == TargetType.Type && action == BehaviorAction.READ) {
            return isRegistered(RegisteredResourceType.TYPE, version, catalog, schema, objectName);
        }
        return false;
    }

    private static PostgresVersion requestedVersion(String databaseVersion) {
        PostgresVersion version = PostgresVersion.parse(databaseVersion);
        if (databaseVersion == null || databaseVersion.isBlank()) {
            return version;
        }
        String value = databaseVersion.strip();
        int majorEnd = 0;
        while (majorEnd < value.length() && Character.isDigit(value.charAt(majorEnd))) {
            majorEnd++;
        }
        if (majorEnd == 0) {
            return null;
        }
        try {
            return Integer.parseInt(value.substring(0, majorEnd)) == version.major() ? version : null;
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private boolean isRegistered(RegisteredResourceType type, PostgresVersion version, String catalog, String schema, String objectName) {
        boolean registered;
        if (catalog != null && schema != null) {
            registered = contains(type, catalog, schema, objectName, version);
            if (registered && resources.skipsPermission(type, version, catalog, schema, objectName)) {
                return true;
            }
        }
        if (schema != null) {
            registered = contains(type, schema, objectName, version);
            if (registered && resources.skipsPermission(type, version, schema, objectName)) {
                return true;
            }
        }
        return contains(type, objectName, version) && resources.skipsPermission(type, version, objectName);
    }

    private boolean contains(RegisteredResourceType type, String objectName, PostgresVersion version) {
        return switch (type) {
            case FUNCTION -> resources.isSystemFunction(objectName, version);
            case PROCEDURE -> resources.isSystemProcedure(objectName, version);
            case TABLE -> resources.isSystemRelation(objectName, version);
            case TYPE -> resources.isSystemType(objectName, version);
        };
    }

    private boolean contains(RegisteredResourceType type, String schema, String objectName, PostgresVersion version) {
        return switch (type) {
            case FUNCTION -> resources.isSystemFunction(schema, objectName, version);
            case PROCEDURE -> resources.isSystemProcedure(schema, objectName, version);
            case TABLE -> resources.isSystemRelation(schema, objectName, version);
            case TYPE -> resources.isSystemType(schema, objectName, version);
        };
    }

    private boolean contains(RegisteredResourceType type, String catalog, String schema, String objectName, PostgresVersion version) {
        return switch (type) {
            case FUNCTION -> resources.isSystemFunction(catalog, schema, objectName, version);
            case PROCEDURE -> resources.isSystemProcedure(catalog, schema, objectName, version);
            case TABLE -> resources.isSystemRelation(catalog, schema, objectName, version);
            case TYPE -> resources.isSystemType(catalog, schema, objectName, version);
        };
    }
}
