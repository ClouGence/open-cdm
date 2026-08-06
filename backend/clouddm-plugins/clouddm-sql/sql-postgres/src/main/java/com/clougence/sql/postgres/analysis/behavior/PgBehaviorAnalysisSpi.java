/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.postgres.analysis.behavior;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.postgres.parser.PgDslProvider;
import com.clougence.sql.postgres.parser.PgSplitAnalysisSpi;
import com.clougence.sql.postgres.parser.PostgresVersion;

public class PgBehaviorAnalysisSpi implements BehaviorAnalysisSpi {

    private final PgDslProvider      provider;
    private final PgSplitAnalysisSpi splitter;

    public PgBehaviorAnalysisSpi(PostgresVersion version){
        this.provider = new PgDslProvider(version);
        this.splitter = new PgSplitAnalysisSpi(version);
    }

    @Override
    public Stream<StatementBehavior> analysisBehaviorStream(Reader queryReader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        var scripts = this.splitter.splitScriptStream(queryReader, List.of(), baseLine, baseColumn);
        return scripts.flatMap(script -> {
            StringReader reader = new StringReader(script.getScript());
            int codeLine = script.getBodyStartCodeLine();
            int codeColumn = script.getBodyStartCodeColumn();

            return analyzeStatement(reader, levels, codeLine, codeColumn).stream();
        }).onClose(scripts::close);
    }

    private List<StatementBehavior> analyzeStatement(Reader queryReader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {

        PgBehaviorParserVisitor[] holder = new PgBehaviorParserVisitor[1];
        DslHelper.doVisitor(provider, queryReader, (lexer, parser) -> {
            holder[0] = new PgBehaviorParserVisitor(parser, provider.version(), levels, baseLine, baseColumn);
            return holder[0];
        });
        return holder[0].behaviors();
    }
}
