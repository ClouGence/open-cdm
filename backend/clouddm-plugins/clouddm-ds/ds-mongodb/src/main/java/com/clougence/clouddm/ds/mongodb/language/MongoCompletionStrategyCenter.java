/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.clouddm.ds.mongodb.language;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.clougence.clouddm.dsfamily.language.completion.CompletionContext;
import com.clougence.clouddm.dsfamily.language.completion.CompletionDialect;
import com.clougence.clouddm.dsfamily.language.completion.CompletionStrategy;
import com.clougence.clouddm.dsfamily.language.completion.CompletionStrategyCenter;
import com.clougence.clouddm.sdk.language.completion.CompletionItem;
import com.clougence.clouddm.sdk.language.completion.CompletionItemKind;
import com.clougence.clouddm.sdk.language.completion.CompletionRequest;
import com.clougence.clouddm.sdk.service.execute.MetaCol;
import com.clougence.clouddm.sdk.service.execute.MetaObj;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.mongodb.parser.ast.commands.db.MongoAdminReadCommands;
import com.clougence.utils.StringUtils;

public class MongoCompletionStrategyCenter extends CompletionStrategyCenter {

    @Override
    protected CompletionDialect dialect(CompletionRequest request) {
        return MongoCompletionDialect.INSTANCE;
    }

    @Override
    protected void register(List<CompletionStrategy> strategies) {
        // Higher than field strategy so rs./sh. are not treated as collection qualifiers.
        strategies.add(new MongoShellHelperCompletionStrategy());
        strategies.add(new MongoCollectionCompletionStrategy());
        strategies.add(new MongoFieldCompletionStrategy());
    }

    private static class MongoShellHelperCompletionStrategy implements CompletionStrategy {
        @Override
        public int weight() {
            return 300;
        }

        @Override
        public boolean match(CompletionContext context) {
            if (context.getPreviousSignificantChar() != '.') {
                return false;
            }
            String qualifier = context.getQualifier();
            return "rs".equalsIgnoreCase(qualifier) || "sh".equalsIgnoreCase(qualifier);
        }

        @Override
        public List<CompletionItem> complete(CompletionContext context, MetaService metaService) {
            List<String> methods = "rs".equalsIgnoreCase(context.getQualifier())
                ? MongoAdminReadCommands.listRsMethods()
                : MongoAdminReadCommands.listShMethods();
            List<CompletionItem> items = new ArrayList<>();
            for (String method : methods) {
                if (!context.matchPrefix(method)) {
                    continue;
                }
                CompletionItem item = new CompletionItem();
                item.setLabel(method);
                item.setKind(CompletionItemKind.FUNCTION);
                item.setUmiType(UmiTypes.Function);
                item.setIcon("FUNCTION");
                item.setInsertText(method + "()");
                item.setWeight(850);
                items.add(item);
            }
            return items;
        }
    }

    private static class MongoCollectionCompletionStrategy implements CompletionStrategy {
        @Override
        public int weight() {
            return 100;
        }

        @Override
        public boolean match(CompletionContext context) {
            return context.getPreviousSignificantChar() == '.' && "db".equalsIgnoreCase(context.previousToken());
        }

        @Override
        public List<CompletionItem> complete(CompletionContext context, MetaService metaService) {
            List<CompletionItem> items = new ArrayList<>();
            List<MetaObj> metaObjs = metaService.cachedObjectNames(context.getRequest().getPrimaryUserId(), context.getRequest().getCurrentUserId(), context.getRequest()
                .getDataSourceId(), context.getRequest().getLevels(), context.getRequest().getLevelsParam());
            for (MetaObj metaObj : metaObjs) {
                if (!isCollection(metaObj) || !context.matchPrefix(metaObj.getName())) {
                    continue;
                }
                CompletionItem item = new CompletionItem();
                item.setLabel(metaObj.getName());
                item.setKind(CompletionItemKind.TABLE);
                item.setUmiType(UmiTypes.Table);
                item.setIcon("TABLE");
                item.setInsertText(metaObj.getName());
                item.setWeight(800);
                items.add(item);
            }
            return items;
        }

        private boolean isCollection(MetaObj metaObj) {
            return metaObj != null && StringUtils.isNotBlank(metaObj.getName()) && (metaObj.getType() == UmiTypes.Table || metaObj.getType() == UmiTypes.View);
        }
    }

    private static class MongoFieldCompletionStrategy implements CompletionStrategy {
        @Override
        public int weight() {
            return 200;
        }

        @Override
        public boolean match(CompletionContext context) {
            return context.hasQualifier() && !"db".equalsIgnoreCase(context.getQualifier());
        }

        @Override
        public List<CompletionItem> complete(CompletionContext context, MetaService metaService) {
            List<CompletionItem> items = new ArrayList<>();
            for (MetaCol column : metaService.fetchTableColumns(context.getRequest().getCurrentUserId(), context.getRequest().getDataSourceId(), context.getRequest()
                .getLevelsParam(), context.getQualifier())) {
                if (column == null || StringUtils.isBlank(column.getColumn()) || !context.matchPrefix(column.getColumn())) {
                    continue;
                }
                CompletionItem item = new CompletionItem();
                item.setLabel(column.getColumn());
                item.setKind(CompletionItemKind.COLUMN);
                item.setUmiType(UmiTypes.Column);
                item.setIcon(StringUtils.defaultIfBlank(column.getIcon(), "COLUMN-DEFAULT"));
                item.setInsertText(column.getColumn());
                item.setWeight("COLUMN-PK".equalsIgnoreCase(StringUtils.toString(column.getIcon())) ? 950 : 900);
                items.add(item);
            }
            return items;
        }
    }

    private enum MongoCompletionDialect implements CompletionDialect {
        INSTANCE;

        @Override
        public boolean isIdentifierChar(char c) {
            return Character.isLetterOrDigit(c) || c == '_' || c == '$';
        }

        @Override
        public String unquoteIdentifier(String value) {
            return StringUtils.toString(value).trim().toLowerCase(Locale.ROOT);
        }
    }
}
