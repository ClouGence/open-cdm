/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.ds.hana.language;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import com.clougence.clouddm.ds.hana.dialect.HanaDialect;
import com.clougence.sql.hana.parser.HanaDslProvider;
import com.clougence.sql.hana.parser.antlr.HanaLexer;
import com.clougence.clouddm.dsfamily.language.completion.CompletionContext;
import com.clougence.clouddm.dsfamily.language.completion.CompletionStrategy;
import com.clougence.clouddm.dsfamily.language.completion.analyzer.CompletionAnalyzer;
import com.clougence.clouddm.dsfamily.language.completion.rdb.*;
import com.clougence.clouddm.sdk.language.completion.CompletionItem;
import com.clougence.clouddm.sdk.language.completion.CompletionItemKind;
import com.clougence.clouddm.sdk.language.completion.CompletionRequest;
import com.clougence.clouddm.sdk.service.execute.MetaObj;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.umi.struts.UmiTypes;

public class HanaCompletionStrategyCenter {

    private final List<CompletionStrategy> strategies = List.of(new QualifiedColumnCompletionStrategy(), new SelectFromKeywordCompletionStrategy(),
        new SelectColumnCompletionStrategy(), new WhereColumnCompletionStrategy(), new PredicateColumnCompletionStrategy(),
        new OrderGroupByColumnCompletionStrategy(), new ObjectCompletionStrategy(), new AfterFromTableCompletionStrategy(), new ExpressionCompletionStrategy());

    public List<CompletionItem> complete(CompletionRequest request, MetaService metaService) {
        if (request == null || request.getDataSourceId() == null || metaService == null) {
            return List.of();
        }
        CompletionContext context = CompletionAnalyzer.INSTANCE.analyze(request, HanaDialect.INSTANCE);
        int cursor = request.getSqlText().codePointCount(0, context.getCursorOffset());
        for (Token token : HanaDslProvider.INSTANCE.createLexer(CharStreams.fromString(request.getSqlText())).getAllTokens()) {
            if (token.getStartIndex() >= cursor) {
                break;
            }
            boolean textToken = switch (token.getType()) {
                case HanaLexer.STRING, HanaLexer.LINE_COMMENT, HanaLexer.BLOCK_COMMENT -> true;
                default -> false;
            };
            if (textToken && token.getStopIndex() + 1 >= cursor) {
                return List.of();
            }
            if (token.getType() == HanaLexer.UNTERMINATED_COMMENT ||
                (token.getType() == HanaLexer.INVALID_CHARACTER && "'".equals(token.getText()))) {
                return List.of();
            }
        }
        // Suggestions are scoped to the editor's selected schema, including aliased column lookups.
        for (var table : context.getTableRefs()) {
            if ((table.getSchema() != null && !Objects.equals(table.getSchema(), request.getLevelsParam().get(UmiTypes.Schema))) ||
                (table.getCatalog() != null && !Objects.equals(table.getCatalog(), request.getLevelsParam().get(UmiTypes.Catalog)))) {
                return List.of();
            }
        }
        MetaService authorized = new HanaCompletionMetaService(metaService, request);
        List<CompletionItem> items;
        if ("CALL".equalsIgnoreCase(context.previousToken())) {
            items = new ArrayList<>();
            for (MetaObj object : authorized.cachedObjectNames(request.getPrimaryUserId(), request.getCurrentUserId(), request.getDataSourceId(), request.getLevels(), request.getLevelsParam())) {
                if (object.getType() == UmiTypes.Procedure && context.matchPrefix(object.getName())) {
                    CompletionItem item = new CompletionItem();
                    item.setLabel(object.getName());
                    item.setInsertText(HanaDialect.INSTANCE.fmtName(true, object.getName()) + "()");
                    item.setKind(CompletionItemKind.PROCEDURE);
                    item.setUmiType(UmiTypes.Procedure);
                    items.add(item);
                }
            }
            return items;
        }
        items = strategies.stream().filter(strategy -> strategy.match(context)).max(Comparator.comparingInt(CompletionStrategy::weight))
            .map(strategy -> strategy.complete(context, authorized)).orElseGet(List::of);
        items = new ArrayList<>(items);
        items.removeIf(item -> item.getUmiType() == UmiTypes.Procedure);
        for (CompletionItem item : items) {
            if (item.getUmiType() == UmiTypes.Function) {
                item.setInsertText(HanaDialect.INSTANCE.fmtName(true, item.getLabel()) + "()");
            } else if (item.getUmiType() != null) {
                item.setInsertText(HanaDialect.INSTANCE.fmtName(true, item.getInsertText()));
            }
        }
        return items;
    }
}
