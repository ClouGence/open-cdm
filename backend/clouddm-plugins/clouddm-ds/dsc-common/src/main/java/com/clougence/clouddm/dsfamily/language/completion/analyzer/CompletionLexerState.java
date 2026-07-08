/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.dsfamily.language.completion.analyzer;

import java.util.Collections;
import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CompletionLexerState {

    @Builder.Default
    private final List<CompletionToken> tokens             = Collections.emptyList();
    @Builder.Default
    private final List<String>          tokensBeforeCursor = Collections.emptyList();
    @Builder.Default
    private final List<String>          operatorsBeforeCursor = Collections.emptyList();
    private final CompletionToken       tokenBeforeCursor;
    private final CompletionToken       tokenAfterCursor;
    private final CompletionToken       currentToken;
    private final String                functionName;
    private final int                   functionParameterIndex;
    private final String                operatorBeforeCursor;
    private final String                prefix;
    private final String                qualifier;
    private final CompletionNamePath    namePath;
    private final char                  previousSignificantChar;
    private final int                   cursorOffset;
}
