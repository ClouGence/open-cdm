/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.dsfamily.language.completion.analyzer;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CompletionParseState {

    private final boolean                 parsed;
    private final boolean                 hasSyntaxError;
    @Builder.Default
    private final SecQueryType            statementType = SecQueryType.UNKNOWN;
    @Builder.Default
    private final CompletionClause        clause        = CompletionClause.UNKNOWN;
    @Builder.Default
    private final List<CompletionTableRef> tableRefs    = Collections.emptyList();
    @Builder.Default
    private final List<CompletionColumnRef> columnRefs   = Collections.emptyList();
    @Builder.Default
    private final List<CompletionSyntaxError> syntaxErrors = Collections.emptyList();
}
