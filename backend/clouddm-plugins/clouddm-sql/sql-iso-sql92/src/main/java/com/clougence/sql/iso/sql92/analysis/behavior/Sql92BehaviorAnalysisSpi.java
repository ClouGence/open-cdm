/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.iso.sql92.analysis.behavior;

import java.io.Reader;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.iso.sql92.parser.Sql92DslProvider;

public class Sql92BehaviorAnalysisSpi implements BehaviorAnalysisSpi {

    @Override
    public Stream<StatementBehavior> analysisBehaviorStream(Reader queryReader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        return analysisBehaviorMaterialized(queryReader, levels, baseLine, baseColumn).stream();
    }

    private List<StatementBehavior> analysisBehaviorMaterialized(Reader queryReader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {

        Sql92BehaviorParserVisitor[] holder = new Sql92BehaviorParserVisitor[1];
        DslHelper.doVisitor(Sql92DslProvider.INSTANCE, queryReader, (lexer, parser) -> {
            holder[0] = new Sql92BehaviorParserVisitor(levels, baseLine, baseColumn);
            return holder[0];
        });
        return holder[0].behaviors();
    }
}
