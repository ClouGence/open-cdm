/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.dsfamily.language.completion.rdb;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.language.completion.CompletionContext;
import com.clougence.clouddm.dsfamily.language.completion.analyzer.CompletionClause;
import com.clougence.clouddm.sdk.language.completion.CompletionItem;
import com.clougence.clouddm.sdk.language.completion.CompletionItemKind;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.utils.StringUtils;

public class AfterFromTableCompletionStrategy extends AbstractColumnCompletionStrategy {

    private static final int WHERE_WEIGHT = 1000;

    @Override
    public int weight() {
        return 850;
    }

    @Override
    public boolean match(CompletionContext context) {
        if (context.getClause() != CompletionClause.FROM_TABLE || StringUtils.isNotBlank(context.getPrefix()) || context.hasQualifier() || context.getTableRefs().isEmpty()) {
            return false;
        }

        String previous = context.previousToken();
        return StringUtils.isNotBlank(previous) && !"from".equalsIgnoreCase(previous) && !"join".equalsIgnoreCase(previous);
    }

    @Override
    public List<CompletionItem> complete(CompletionContext context, MetaService metaService) {
        List<CompletionItem> items = new ArrayList<>();
        items.add(whereItem());
        items.addAll(columnItems(context, metaService));
        return items;
    }

    private static CompletionItem whereItem() {
        CompletionItem item = new CompletionItem();
        item.setLabel("WHERE");
        item.setKind(CompletionItemKind.KEYWORD);
        item.setInsertText("WHERE");
        item.setWeight(WHERE_WEIGHT);
        return item;
    }
}
