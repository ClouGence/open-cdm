/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.sqlserver.analysis.behavior;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.sqlserver.parser.MsSqlDslProvider;
import com.clougence.sql.sqlserver.parser.MsSqlSplitAnalysisSpi;

public class MsBehaviorAnalysisSpi implements BehaviorAnalysisSpi {

    private final boolean showPlan;

    public MsBehaviorAnalysisSpi(){
        this(false);
    }

    public MsBehaviorAnalysisSpi(boolean showPlan){
        this.showPlan = showPlan;
    }

    @Override
    public Stream<StatementBehavior> analysisBehaviorStream(Reader queryReader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        var scripts = new MsSqlSplitAnalysisSpi(this.showPlan).splitScriptStream(queryReader, List.of(), baseLine, baseColumn);
        return scripts.flatMap(script -> {
            StringReader reader = new StringReader(script.getScript());
            int codeLine = script.getBodyStartCodeLine();
            int codeColumn = script.getBodyStartCodeColumn();

            List<StatementBehavior> behaviors = analyzeStatement(reader, levels, codeLine, codeColumn);
            if (this.showPlan) {
                behaviors.forEach(behavior -> {
                    behavior.setStatementType(SplitQueryType.SELECT);
                    behavior.getRelations().forEach(relation -> relation.setAction(BehaviorAction.READ));
                });
            }
            return behaviors.stream();
        }).onClose(scripts::close);
    }

    private List<StatementBehavior> analyzeStatement(Reader queryReader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {

        MsBehaviorParserVisitor[] holder = new MsBehaviorParserVisitor[1];
        DslHelper.doVisitor(MsSqlDslProvider.INSTANCE, queryReader, (lexer, parser) -> {
            holder[0] = new MsBehaviorParserVisitor(parser, levels, baseLine, baseColumn);
            return holder[0];
        });
        return holder[0].behaviors();
    }
}
