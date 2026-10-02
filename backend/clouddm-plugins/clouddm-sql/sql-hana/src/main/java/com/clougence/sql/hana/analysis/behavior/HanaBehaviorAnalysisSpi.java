/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.hana.analysis.behavior;

import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.clouddm.sdk.sql.analysis.security.ContextInfo;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.hana.analysis.HanaSqlAnalyzer;
import com.clougence.sql.hana.parser.HanaSplitAnalysisSpi;

import java.io.Reader;
import java.util.Map;
import java.util.stream.Stream;

public class HanaBehaviorAnalysisSpi implements BehaviorAnalysisSpi {
    private final MetaService metaService;

    public HanaBehaviorAnalysisSpi(){
        this(null);
    }

    public HanaBehaviorAnalysisSpi(MetaService metaService){
        this.metaService = metaService;
    }

    @Override
    public Stream<StatementBehavior> analysisBehaviorWithContextStream(Reader reader, ContextInfo context, int baseLine, int baseColumn) {
        if (metaService == null) {
            return analysisBehaviorStream(reader, context.getLevelsParam(), baseLine, baseColumn);
        }

        return new HanaSplitAnalysisSpi().splitScriptStream(reader, null, baseLine, baseColumn)
            .map(script -> HanaSqlAnalyzer
                .analyze(script, context.getLevelsParam(), (levels, index) -> metaService.fetchIndexedObject(context.getCuid(), context.getDsId(), levels, index))
                .getBehavior());
    }

    @Override
    public Stream<StatementBehavior> analysisBehaviorStream(Reader reader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        return new HanaSplitAnalysisSpi().splitScriptStream(reader, null, baseLine, baseColumn).map(script -> HanaSqlAnalyzer.analyze(script, levels).getBehavior());
    }
}
