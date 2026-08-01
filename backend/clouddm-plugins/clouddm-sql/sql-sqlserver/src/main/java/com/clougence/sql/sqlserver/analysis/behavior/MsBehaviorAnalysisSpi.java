/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.sqlserver.analysis.behavior;

import java.io.Reader;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.sqlserver.parser.MsSqlDslProvider;

public class MsBehaviorAnalysisSpi implements BehaviorAnalysisSpi {

    @Override
    public List<StatementBehavior> analysisBehavior(Reader queryReader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {

        MsBehaviorParserVisitor[] holder = new MsBehaviorParserVisitor[1];
        DslHelper.doVisitor(MsSqlDslProvider.INSTANCE, queryReader, (lexer, parser) -> {
            holder[0] = new MsBehaviorParserVisitor(parser, levels, baseLine, baseColumn);
            return holder[0];
        });
        return holder[0].behaviors();
    }
}
