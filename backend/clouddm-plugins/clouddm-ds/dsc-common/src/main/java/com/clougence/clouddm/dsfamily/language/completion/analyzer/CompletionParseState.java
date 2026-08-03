/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.dsfamily.language.completion.analyzer;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CompletionParseState {

    private final boolean                     parsed;
    private final boolean                     hasSyntaxError;
    private final CompletionClause            clause;
    private final List<CompletionTableRef>    tableRefs;
    private final List<CompletionColumnRef>   columnRefs;
    private final List<CompletionSyntaxError> syntaxErrors;
}
