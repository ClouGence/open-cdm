/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.iso.sql92.analysis.behavior;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.iso.sql92.parser.Sql92DslProvider;
import com.clougence.utils.StringUtils;

public class Sql92BehaviorAnalysisSpi implements BehaviorAnalysisSpi {

    @Override
    public List<StatementBehavior> analysisBehavior(String query, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        if (StringUtils.isBlank(query)) {
            return Collections.emptyList();
        }

        Sql92BehaviorParserVisitor[] holder = new Sql92BehaviorParserVisitor[1];
        DslHelper.doVisitor(Sql92DslProvider.INSTANCE, query, (lexer, parser) -> {
            holder[0] = new Sql92BehaviorParserVisitor(levels, baseLine, baseColumn);
            return holder[0];
        });
        return holder[0].behaviors();
    }
}
