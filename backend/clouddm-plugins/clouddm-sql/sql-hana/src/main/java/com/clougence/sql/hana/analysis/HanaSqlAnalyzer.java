/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.hana.analysis;

import java.util.Map;
import org.antlr.v4.runtime.CharStreams;
import com.clougence.sql.hana.i18n.HanaSqlI18nKeys;
import com.clougence.sql.hana.parser.HanaDslProvider;
import com.clougence.sql.hana.parser.antlr.HanaParser;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.dslpaser.antlr.AntlerSyntaxException;
import com.clougence.schema.umi.struts.UmiTypes;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class HanaSqlAnalyzer {
    private HanaSqlAnalyzer() {}

    public static HanaAnalysisResult analyze(SplitScript script, Map<UmiTypes, Object> levels) {
        try {
            var lexer = HanaDslProvider.INSTANCE.createLexer(CharStreams.fromString(script.getScript()));
            HanaParser parser = (HanaParser) HanaDslProvider.INSTANCE.createParser(lexer);
            var tree = parser.statementRoot();
            HanaBehaviorParserVisitor visitor = new HanaBehaviorParserVisitor(script, levels);
            visitor.visit(tree);
            return visitor.result();
        } catch (AntlerSyntaxException e) {
            String msg = "Cannot analyze HANA statement at line " + script.getBodyStartCodeLine();
            log.error(msg, e);
            throw ThirdPartyApiException.as().with(e, HanaSqlI18nKeys.HANA_SQL_ANALYSIS_UNSUPPORTED,
                script.getBodyStartCodeLine() + e.getLine() - 1,
                e.getColumn() + (e.getLine() == 1 ? script.getBodyStartCodeColumn() : 0), e.getMessage());
        }
    }
}
