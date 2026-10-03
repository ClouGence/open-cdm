/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.ds.hana.definition.secrules;

import java.util.List;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.analysis.security.SecRulesSupportSpi;

public class HanaSecRulesSupportSpi implements SecRulesSupportSpi {
    @Override
    public boolean isSupport() { return true; }

    @Override
    public List<TargetType> supportModel() {
        return List.of(TargetType.Query, TargetType.Insert, TargetType.Update, TargetType.Delete, TargetType.Call,
            TargetType.Schema, TargetType.Table, TargetType.Column, TargetType.View, TargetType.Index, TargetType.Constraint, TargetType.Sequence,
            TargetType.Synonym, TargetType.Procedure, TargetType.Function, TargetType.Trigger);
    }

    @Override
    public List<TargetType> exactRangeForQuery() {
        return List.of(TargetType.Schema, TargetType.Table, TargetType.View);
    }

    @Override
    public List<TargetType> prefixRangeForQuery() {
        return List.of(TargetType.Schema, TargetType.Table, TargetType.View);
    }

    @Override
    public List<TargetType> suffixRangeForQuery() {
        return List.of(TargetType.Schema, TargetType.Table, TargetType.View);
    }

    @Override
    public List<TargetType> includeRangeForQuery() {
        return List.of(TargetType.Schema, TargetType.Table, TargetType.View);
    }

    @Override
    public List<TargetType> exactRangeForSen() { return List.of(TargetType.Schema, TargetType.Table, TargetType.View, TargetType.Column); }

    @Override
    public List<TargetType> prefixRangeForSen() { return List.of(TargetType.Schema, TargetType.Table, TargetType.View, TargetType.Column); }

    @Override
    public List<TargetType> suffixRangeForSen() { return List.of(TargetType.Schema, TargetType.Table, TargetType.View, TargetType.Column); }

    @Override
    public List<TargetType> includeRangeForSen() { return List.of(TargetType.Schema, TargetType.Table, TargetType.View, TargetType.Column); }
}
