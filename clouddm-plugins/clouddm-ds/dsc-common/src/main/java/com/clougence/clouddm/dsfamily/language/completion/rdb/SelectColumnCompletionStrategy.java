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
import com.clougence.utils.StringUtils;

public class SelectColumnCompletionStrategy extends AbstractColumnCompletionStrategy {

    @Override
    public int weight() {
        return 700;
    }

    @Override
    public boolean match(CompletionContext context) {
        int offset = StringUtils.isBlank(context.getPrefix()) ? 0 : 1;
        if ("select".equalsIgnoreCase(context.tokenFromEnd(offset))) {
            return true;
        }

        for (int i = 0; i < context.getTokensBeforeCursor().size(); i++) {
            String token = context.tokenFromEnd(i).toLowerCase();
            switch (token) {
                case "from", "where", "join", "into", "update", "table", "values", "set" -> {
                    return false;
                }
                case "select" -> {
                    return true;
                }
                default -> {
                    // continue scanning backwards
                }
            }
        }
        return false;
    }

    @Override
    public List<CompletionItem> complete(CompletionContext context, MetaService metaService) {
        return columnItems(context, metaService);
    }
}
