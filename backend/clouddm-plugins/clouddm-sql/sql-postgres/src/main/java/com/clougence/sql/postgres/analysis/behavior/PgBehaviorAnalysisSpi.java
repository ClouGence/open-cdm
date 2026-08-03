/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.postgres.analysis.behavior;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.postgres.parser.PgDslProvider;
import com.clougence.sql.postgres.parser.PostgresVersion;
import com.clougence.utils.StringUtils;

public class PgBehaviorAnalysisSpi implements BehaviorAnalysisSpi {

    private final PgDslProvider provider;

    public PgBehaviorAnalysisSpi(PostgresVersion version){
        this.provider = new PgDslProvider(version);
    }

    public PostgresVersion version() {
        return provider.version();
    }

    @Override
    public List<StatementBehavior> analysisBehavior(String query, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        if (StringUtils.isBlank(query)) {
            return Collections.emptyList();
        }

        PgBehaviorParserVisitor[] holder = new PgBehaviorParserVisitor[1];
        DslHelper.doVisitor(provider, query, (lexer, parser) -> {
            holder[0] = new PgBehaviorParserVisitor(parser, provider.version(), levels, baseLine, baseColumn);
            return holder[0];
        });
        return holder[0].behaviors();
    }
}
