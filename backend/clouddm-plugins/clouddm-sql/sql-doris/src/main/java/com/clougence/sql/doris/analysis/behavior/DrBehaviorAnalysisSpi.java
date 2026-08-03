/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.doris.analysis.behavior;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.doris.parser.DrDslProvider;
import com.clougence.utils.StringUtils;

public class DrBehaviorAnalysisSpi implements BehaviorAnalysisSpi {

    private final DrDslProvider provider;

    public DrBehaviorAnalysisSpi(DrDslProvider provider){
        this.provider = provider;
    }

    @Override
    public List<StatementBehavior> analysisBehavior(String query, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        if (StringUtils.isBlank(query)) {
            return Collections.emptyList();
        }

        DrBehaviorParserVisitor[] holder = new DrBehaviorParserVisitor[1];
        DslHelper.doVisitor(provider, query, (lexer, parser) -> {
            holder[0] = new DrBehaviorParserVisitor(parser, levels, baseLine, baseColumn);
            return holder[0];
        });
        return holder[0].behaviors();
    }
}
