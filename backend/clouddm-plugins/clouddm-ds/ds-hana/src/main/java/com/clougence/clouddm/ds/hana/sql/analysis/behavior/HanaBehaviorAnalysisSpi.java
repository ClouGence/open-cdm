/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.hana.sql.analysis.behavior;

import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

public class HanaBehaviorAnalysisSpi implements BehaviorAnalysisSpi {

    private final SplitAnalysisSpi splitAnalysisSpi;

    public HanaBehaviorAnalysisSpi(SplitAnalysisSpi splitAnalysisSpi){
        this.splitAnalysisSpi = splitAnalysisSpi;
    }

    @Override
    public List<StatementBehavior> analysisBehavior(Reader queryReader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        return splitAnalysisSpi.splitScript(queryReader, Collections.emptyList(), baseLine, baseColumn).stream().map(script -> {
            StatementBehavior behavior = new StatementBehavior();
            behavior.setStatementType(script.getType().stream().findFirst().orElse(SplitQueryType.UNKNOWN));
            return behavior;
        }).toList();
    }
}
