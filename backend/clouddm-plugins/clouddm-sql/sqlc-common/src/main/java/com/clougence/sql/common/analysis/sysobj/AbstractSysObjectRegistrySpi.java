/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.common.analysis.sysobj;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.analysis.sysobj.SysObjectRegistrySpi;

/**
 * Base validation for named SQL-engine resource registries.
 */
public abstract class AbstractSysObjectRegistrySpi implements SysObjectRegistrySpi {

    @Override
    public final boolean isPermissionExempt(BehaviorAction action, TargetType targetType, String catalog, String schema, String objectName, String dbVersion) {
        if (action == null || targetType == null || objectName == null || objectName.isBlank()) {
            return false;
        }
        return isRegisteredResource(action, targetType, catalog, schema, objectName, dbVersion);
    }

    protected boolean isRegisteredResource(BehaviorAction action, TargetType targetType, String catalog, String schema, String objectName, String databaseVersion) {
        return false;
    }
}
