/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.dsfamily.language.completion.rdb;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.language.completion.CompletionContext;
import com.clougence.clouddm.sdk.language.completion.CompletionItem;
import com.clougence.clouddm.sdk.service.execute.MetaObj;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

public class ExpressionCompletionStrategy extends AbstractColumnCompletionStrategy {

    @Override
    public int weight() {
        return 830;
    }

    @Override
    public boolean match(CompletionContext context) {
        return !context.hasQualifier() && (context.isInSelectList() || context.isInPredicate() || context.isInOrderGroupByClause());
    }

    @Override
    public List<CompletionItem> complete(CompletionContext context, MetaService metaService) {
        List<CompletionItem> items = new ArrayList<>(columnItems(context, metaService));
        items.addAll(functionItems(context, metaService));
        return items;
    }

    private List<CompletionItem> functionItems(CompletionContext context, MetaService metaService) {
        List<CompletionItem> items = new ArrayList<>();
        List<MetaObj> metaObjs = metaService.cachedObjectNames(//
                context.getRequest().getPrimaryUserId(),//
                context.getRequest().getCurrentUserId(),//
                context.getRequest().getDataSourceId(), //
                context.getRequest().getLevels(),       //
                context.getRequest().getLevelsParam());
        for (MetaObj metaObj : metaObjs) {
            if (!isFunction(metaObj) || !context.matchPrefix(metaObj.getName())) {
                continue;
            }

            CompletionItem item = new CompletionItem();
            item.setLabel(metaObj.getName());
            item.setKind(ObjectCompletionStrategy.toCompletionKind(metaObj.getType()));
            item.setUmiType(metaObj.getType());
            item.setIcon(ObjectCompletionStrategy.icon(metaObj.getType()));
            item.setInsertText(metaObj.getName() + "()");
            item.setWeight(700);
            items.add(item);
        }
        return items;
    }

    private boolean isFunction(MetaObj metaObj) {
        return metaObj != null && StringUtils.isNotBlank(metaObj.getName()) && (metaObj.getType() == UmiTypes.Function || metaObj.getType() == UmiTypes.Procedure);
    }
}
