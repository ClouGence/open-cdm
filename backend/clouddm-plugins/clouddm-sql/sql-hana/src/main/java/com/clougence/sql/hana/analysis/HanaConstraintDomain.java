/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.hana.analysis;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbConstraintDomain;

public final class HanaConstraintDomain extends RdbConstraintDomain {
    @Override
    public List<Map<TargetType, String>> resolveResource() {
        Map<TargetType, String> resource = new EnumMap<>(TargetType.class);
        resource.put(TargetType.Catalog, getTableCatalog());
        resource.put(TargetType.Schema, getTableSchema());
        resource.put(TargetType.Table, getTableName());
        return List.of(resource);
    }
}
