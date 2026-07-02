/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.dsfamily.language.completion.analyzer;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CompletionSyntaxError {

    private final int    line;
    private final int    column;
    private final String message;
    private final String offendingText;
}
