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
public class CompletionToken {

    private final String text;
    private final int    type;
    private final int    channel;
    private final int    line;
    private final int    column;
    private final int    startIndex;
    private final int    stopIndex;

    public boolean containsOffset(int offset) {
        return startIndex <= offset && offset <= stopIndex + 1;
    }
}
