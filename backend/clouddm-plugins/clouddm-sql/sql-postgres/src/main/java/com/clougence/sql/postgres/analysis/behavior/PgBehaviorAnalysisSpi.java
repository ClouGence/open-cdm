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
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementBehavior;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.postgres.parser.PgDslProvider;
import com.clougence.sql.postgres.parser.PgSplitAnalysisSpi;
import com.clougence.sql.postgres.parser.PgSplitVisitor;
import com.clougence.sql.postgres.parser.PostgresVersion;

public class PgBehaviorAnalysisSpi implements BehaviorAnalysisSpi {

    private final PgDslProvider                             provider;
    private final PgSplitAnalysisSpi                        splitter;
    private final Predicate<String>                         dialectSystemFunction;
    private final Function<PostgresVersion, PgSplitVisitor> splitVisitorFactory;

    public PgBehaviorAnalysisSpi(PostgresVersion version){
        this(new PgDslProvider(version), new PgSplitAnalysisSpi(version));
    }

    protected PgBehaviorAnalysisSpi(PgDslProvider provider, PgSplitAnalysisSpi splitter){
        this(provider, splitter, name -> false, PgSplitVisitor::new);
    }

    protected PgBehaviorAnalysisSpi(PgDslProvider provider, PgSplitAnalysisSpi splitter, Predicate<String> dialectSystemFunction,
                                    Function<PostgresVersion, PgSplitVisitor> splitVisitorFactory){
        this.provider = provider;
        this.splitter = splitter;
        this.dialectSystemFunction = dialectSystemFunction;
        this.splitVisitorFactory = splitVisitorFactory;
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

    protected List<StatementBehavior> analyzeStatement(Reader queryReader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {

        PgBehaviorParserVisitor[] holder = new PgBehaviorParserVisitor[1];
        DslHelper.doVisitor(provider, queryReader, (lexer, parser) -> {
            holder[0] = new PgBehaviorParserVisitor(parser, provider.version(), levels, baseLine, baseColumn, dialectSystemFunction, splitVisitorFactory);
            return holder[0];
        });
        return holder[0].behaviors();
    }
}
