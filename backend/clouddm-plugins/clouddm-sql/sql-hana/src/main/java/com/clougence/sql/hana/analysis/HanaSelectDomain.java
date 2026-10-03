/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.hana.analysis;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbSelectDomain;
import com.clougence.detectrule.lang.reflect.RuleIgnore;

public final class HanaSelectDomain extends RdbSelectDomain {
    @Getter
    @Setter
    private boolean hasSubQuery;
    @RuleIgnore
    private final List<Map<TargetType, String>> resources;

    HanaSelectDomain(List<Map<TargetType, String>> resources) {
        this.resources = resources;
    }

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        if (resources.isEmpty()) {
            return super.resolveResource();
        }
        return resources;
    }
}
