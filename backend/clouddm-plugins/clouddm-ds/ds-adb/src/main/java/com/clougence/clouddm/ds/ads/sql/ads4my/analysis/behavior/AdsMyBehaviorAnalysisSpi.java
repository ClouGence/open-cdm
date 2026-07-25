/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.ads.sql.ads4my.analysis.behavior;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.ads.sql.ads4my.parser.AdsMyDslProvider;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

public class AdsMyBehaviorAnalysisSpi implements BehaviorAnalysisSpi {

    @Override
    public List<StatementBehavior> analysisBehavior(String query, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        if (StringUtils.isBlank(query)) {
            return Collections.emptyList();
        }

        AdsMyBehaviorParserVisitor[] holder = new AdsMyBehaviorParserVisitor[1];
        DslHelper.doVisitor(AdsMyDslProvider.INSTANCE, query, (lexer, parser) -> {
            holder[0] = new AdsMyBehaviorParserVisitor(parser, levels, baseLine, baseColumn);
            return holder[0];
        });
        return holder[0].behaviors();
    }
}
