/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.ds.oceanbase.sql.ob4ora.analysis.sysobj;

import com.clougence.clouddm.ds.oceanbase.sql.ob4ora.ObOraSqlEngineSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.sql.common.analysis.sysobj.AbstractSysObjectRegistrySpi;

/** OceanBase Oracle-mode virtual resources. */
public final class ObOraSysObjectRegistrySpi extends AbstractSysObjectRegistrySpi {

    @Override
    public String name() {
        return ObOraSqlEngineSpi.NAME;
    }

    @Override
    protected boolean isRegisteredResource(BehaviorAction action, TargetType targetType, String catalog, String schema, String objectName, String databaseVersion) {
        return action == BehaviorAction.READ && targetType == TargetType.Table && schema == null && "DUAL".equalsIgnoreCase(objectName);
    }
}
