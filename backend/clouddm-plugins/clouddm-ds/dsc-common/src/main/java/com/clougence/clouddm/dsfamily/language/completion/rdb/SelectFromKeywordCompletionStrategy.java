/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.dsfamily.language.completion.rdb;

import java.util.List;

import com.clougence.clouddm.dsfamily.language.completion.CompletionContext;
import com.clougence.clouddm.dsfamily.language.completion.CompletionStrategy;
import com.clougence.clouddm.sdk.language.completion.CompletionItem;
import com.clougence.clouddm.sdk.language.completion.CompletionItemKind;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.utils.StringUtils;

public class SelectFromKeywordCompletionStrategy implements CompletionStrategy {

    private static final int SELECT_FROM_WEIGHT = 950;

    @Override
    public int weight() {
        return 850;
    }

    @Override
    public boolean match(CompletionContext context) {
        if (StringUtils.isNotBlank(context.getPrefix()) || context.hasQualifier()) {
            return false;
        }

        boolean inSelectListBeforeFrom = false;
        for (int i = 0; i < context.getTokensBeforeCursor().size(); i++) {
            String token = context.tokenFromEnd(i).toLowerCase();
            switch (token) {
                case "from", "where", "join", "into", "update", "table", "values", "set" -> {
                    return false;
                }
                case "select" -> {
                    inSelectListBeforeFrom = true;
                    break;
                }
                default -> {
                    // continue scanning backwards
                }
            }
            if (inSelectListBeforeFrom) {
                break;
            }
        }
        if (!inSelectListBeforeFrom) {
            return false;
        }

        if (context.getPreviousSignificantChar() == '*') {
            return true;
        }

        String previous = context.previousToken();
        return StringUtils.isNotBlank(previous) && !"select".equalsIgnoreCase(previous);
    }

    @Override
    public List<CompletionItem> complete(CompletionContext context, MetaService metaService) {
        CompletionItem item = new CompletionItem();
        item.setLabel("FROM");
        item.setKind(CompletionItemKind.KEYWORD);
        item.setInsertText("FROM");
        item.setWeight(SELECT_FROM_WEIGHT);
        return List.of(item);
    }
}
