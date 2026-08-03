/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.maxcompute.sql.analysis.behavior;

import java.io.Reader;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.maxcompute.sql.parser.McSqlDslProvider;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.schema.umi.struts.UmiTypes;

public class McBehaviorAnalysisSpi implements BehaviorAnalysisSpi {
    @Override
    public List<StatementBehavior> analysisBehavior(Reader queryReader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {

        McBehaviorParserVisitor[] holder = new McBehaviorParserVisitor[1];
        DslHelper.doVisitor(McSqlDslProvider.INSTANCE, queryReader, (lexer, parser) -> {
            holder[0] = new McBehaviorParserVisitor(parser, levels, baseLine, baseColumn);
            return holder[0];
        });
        return holder[0].behaviors();
    }
}
