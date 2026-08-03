/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.iso.sql99.analysis.behavior;

import java.io.Reader;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.iso.sql99.parser.Sql99DslProvider;

public class Sql99BehaviorAnalysisSpi implements BehaviorAnalysisSpi {

    @Override
    public List<StatementBehavior> analysisBehavior(Reader queryReader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {

        Sql99BehaviorParserVisitor[] holder = new Sql99BehaviorParserVisitor[1];
        DslHelper.doVisitor(Sql99DslProvider.INSTANCE, queryReader, (lexer, parser) -> {
            holder[0] = new Sql99BehaviorParserVisitor(levels, baseLine, baseColumn);
            return holder[0];
        });
        return holder[0].behaviors();
    }
}
