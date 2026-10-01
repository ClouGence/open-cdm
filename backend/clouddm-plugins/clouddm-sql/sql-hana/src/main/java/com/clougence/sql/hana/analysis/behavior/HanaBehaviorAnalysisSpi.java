/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.hana.analysis.behavior;

import java.io.Reader;
import java.util.Map;
import java.util.stream.Stream;
import com.clougence.sql.hana.analysis.HanaSqlAnalyzer;
import com.clougence.sql.hana.parser.HanaSplitAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.schema.umi.struts.UmiTypes;

public class HanaBehaviorAnalysisSpi implements BehaviorAnalysisSpi {
    @Override
    public Stream<StatementBehavior> analysisBehaviorStream(Reader reader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        return new HanaSplitAnalysisSpi().splitScriptStream(reader, null, baseLine, baseColumn)
            .map(script -> HanaSqlAnalyzer.analyze(script, levels).getBehavior());
    }
}
