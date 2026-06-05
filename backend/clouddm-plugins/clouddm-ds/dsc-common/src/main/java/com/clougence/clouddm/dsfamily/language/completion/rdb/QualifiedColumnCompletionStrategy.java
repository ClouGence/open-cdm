/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.dsfamily.language.completion.rdb;

import java.util.List;

import com.clougence.clouddm.dsfamily.language.completion.CompletionContext;
import com.clougence.clouddm.sdk.language.completion.CompletionItem;
import com.clougence.clouddm.sdk.service.execute.MetaService;

public class QualifiedColumnCompletionStrategy extends AbstractColumnCompletionStrategy {

    @Override
    public int weight() {
        return 1000;
    }

    @Override
    public boolean match(CompletionContext context) {
        return context.hasQualifier();
    }

    @Override
    public List<CompletionItem> complete(CompletionContext context, MetaService metaService) {
        return columnItems(context, metaService);
    }
}
