/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.oceanbase.sql.ob4my.analysis.behavior;

import java.io.Reader;
import java.util.Map;
import java.util.stream.Stream;

import com.clougence.clouddm.ds.oceanbase.sql.ob4my.parser.ObMyDslProvider;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.schema.umi.struts.UmiTypes;

public class ObMyBehaviorAnalysisSpi implements BehaviorAnalysisSpi {
    @Override
    public Stream<StatementBehavior> analysisBehaviorStream(Reader queryReader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        ObMyBehaviorParserVisitor[] holder = new ObMyBehaviorParserVisitor[1];
        DslHelper.doVisitor(ObMyDslProvider.INSTANCE, queryReader, (lexer, parser) -> {
            holder[0] = new ObMyBehaviorParserVisitor(parser, levels, baseLine, baseColumn);
            return holder[0];
        });
        return holder[0].behaviors().stream();
    }
}
