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

public class WhereColumnCompletionStrategy extends AbstractColumnCompletionStrategy {

    @Override
    public int weight() {
        return 800;
    }

    @Override
    public boolean match(CompletionContext context) {
        int offset = StringUtils.isBlank(context.getPrefix()) ? 0 : 1;
        String previous = context.tokenFromEnd(offset);
        return "where".equalsIgnoreCase(previous) ||    //
               "having".equalsIgnoreCase(previous) ||  //
               "on".equalsIgnoreCase(previous) ||      //
               "set".equalsIgnoreCase(previous);
    }

    @Override
    public List<CompletionItem> complete(CompletionContext context, MetaService metaService) {
        return columnItems(context, metaService);
    }
}
