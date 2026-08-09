/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.hana.sql.analysis.sysobj;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.hana.sql.HanaSqlEngineSpi;
import com.clougence.clouddm.ds.hana.sql.parser.HanaVersion;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.sql.common.analysis.sysobj.AbstractSysObjectRegistrySpi;
import com.clougence.sql.common.registry.RegisteredResourceType;

/** HANA permission-exempt system objects backed by the datasource resource registry. */
public final class HanaSysObjectRegistrySpi extends AbstractSysObjectRegistrySpi {

    private final HanaResourceRegistry resources;

    public HanaSysObjectRegistrySpi(){
        this.resources = HanaResourceRegistry.instance();
    }

    @Override
    public String name() {
        return HanaSqlEngineSpi.NAME;
    }

    @Override
    protected boolean isRegisteredResource(BehaviorAction action, TargetType targetType, String catalog, String schema, String objectName, String databaseVersion) {
        HanaVersion version = HanaVersion.parse(databaseVersion);
        List<String> name = qualifiedName(catalog, schema, objectName);
        if (targetType == TargetType.Function && action == BehaviorAction.CALL) {
            return resources.isSystemFunction(name, version) && resources.shouldSkipPermissionCheck(RegisteredResourceType.FUNCTION, name, version);
        }
        if (targetType == TargetType.Procedure && action == BehaviorAction.CALL) {
            return resources.isSystemProcedure(name, version) && resources.shouldSkipPermissionCheck(RegisteredResourceType.PROCEDURE, name, version);
        }
        if ((targetType == TargetType.Table || targetType == TargetType.View || targetType == TargetType.Materialized) && action == BehaviorAction.READ) {
            return resources.isSystemView(name, version) && resources.shouldSkipPermissionCheck(RegisteredResourceType.TABLE, name, version);
        }
        return false;
    }

    private static List<String> qualifiedName(String catalog, String schema, String objectName) {
        List<String> name = new ArrayList<>(3);
        if (catalog != null && !catalog.isBlank()) {
            name.add(catalog);
        }
        if (schema != null && !schema.isBlank()) {
            name.add(schema);
        }
        name.add(objectName);
        return name;
    }
}
