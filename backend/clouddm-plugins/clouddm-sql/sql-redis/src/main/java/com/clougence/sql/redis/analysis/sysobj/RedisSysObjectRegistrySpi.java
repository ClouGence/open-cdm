/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.redis.analysis.sysobj;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.sql.common.analysis.sysobj.AbstractSysObjectRegistrySpi;
import com.clougence.sql.redis.RedisSqlEngineSpi;
import com.clougence.utils.StringUtils;

/** Redis registered virtual commands. */
public final class RedisSysObjectRegistrySpi extends AbstractSysObjectRegistrySpi {

    @Override
    public String name() {
        return RedisSqlEngineSpi.NAME;
    }

    @Override
    protected boolean isRegisteredResource(BehaviorAction action, TargetType targetType, String catalog, String schema, String objectName, String databaseVersion) {
        return action == BehaviorAction.READ && targetType == TargetType.Function && schema == null && StringUtils.equalsIgnoreCase("TIME", objectName);
    }
}
