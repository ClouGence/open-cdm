/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.sdk.sql.analysis.sysobj;

import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;

/**
 * Named SQL-engine registry for resources which do not require object authorization.
 *
 * <p>Implementations are registered by SQL parser plugins under the same name as their
 * {@code SqlEngineSpi}. The registry classifies derived resource requests and does not modify
 * behavior analysis results.</p>
 */
public interface SysObjectRegistrySpi extends Spi {

    boolean isPermissionExempt(BehaviorAction action, TargetType targetType,    //
                               String catalog, String schema, String objectName,//
                               String dbVersion);
}
