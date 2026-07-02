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
public class CompletionTableRef {

    private final String catalog;
    private final String schema;
    private final String table;
    private final String alias;

    public String tableName() {
        return table;
    }
}
