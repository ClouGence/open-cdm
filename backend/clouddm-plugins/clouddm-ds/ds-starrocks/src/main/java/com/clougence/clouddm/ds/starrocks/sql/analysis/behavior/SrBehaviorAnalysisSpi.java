/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.starrocks.sql.analysis.behavior;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.clougence.clouddm.ds.starrocks.sql.parser.SrDslProvider;
import com.clougence.clouddm.ds.starrocks.sql.parser.SrSplitAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.schema.umi.struts.UmiTypes;

public class SrBehaviorAnalysisSpi implements BehaviorAnalysisSpi {
    @Override
    public Stream<StatementBehavior> analysisBehaviorStream(Reader queryReader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        var scripts = new SrSplitAnalysisSpi().splitScriptStream(queryReader, List.of(), baseLine, baseColumn);
        return scripts.flatMap(script -> {
            StringReader reader = new StringReader(script.getScript());
            int codeLine = script.getBodyStartCodeLine();
            int codeColumn = script.getBodyStartCodeColumn();

            SplitQueryType statementType = script.getType().stream().findFirst().orElse(SplitQueryType.UNKNOWN);
            return analyzeStatement(reader, levels, codeLine, codeColumn, statementType).stream();
        }).onClose(scripts::close);
    }

    private List<StatementBehavior> analyzeStatement(Reader queryReader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn, SplitQueryType statementType) {

        SrBehaviorParserVisitor[] holder = new SrBehaviorParserVisitor[1];
        DslHelper.doVisitor(SrDslProvider.INSTANCE, queryReader, (lexer, parser) -> {
            holder[0] = new SrBehaviorParserVisitor(parser, levels, baseLine, baseColumn, statementType);
            return holder[0];
        });
        return holder[0].behaviors();
    }
}
