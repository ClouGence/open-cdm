/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.mongodb.analysis.behavior;

import java.util.*;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.ast.Statement;
import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.mongodb.analysis.security.MongoAnalysisHelper;
import com.clougence.sql.mongodb.parser.MongoDslProvider;
import com.clougence.sql.mongodb.parser.ast.MongoFuncType;
import com.clougence.sql.mongodb.parser.ast.commands.AbstractMongoFunc;
import com.clougence.sql.mongodb.parser.ast.commands.collection.CollectionFunc;
import com.clougence.utils.StringUtils;

public class MongoBehaviorAnalysisSpi implements BehaviorAnalysisSpi {

    @Override
    public List<StatementBehavior> analysisBehavior(String query, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        if (StringUtils.isBlank(query)) {
            return Collections.emptyList();
        }

        StatementSet statementSet = DslHelper.parserDsl(MongoDslProvider.INSTANCE, query);
        List<StatementBehavior> result = new ArrayList<>();
        int searchOffset = 0;
        for (Statement statement : statementSet.getStatements()) {
            if (!(statement instanceof AbstractMongoFunc mongoFunc)) {
                continue;
            }
            MongoFuncType funcType = mongoFunc.getFuncType();
            SecQueryType statementType = MongoAnalysisHelper.convert(funcType);
            StatementBehavior behavior = new StatementBehavior();
            behavior.setStatementType(statementType);

            String collection = mongoFunc instanceof CollectionFunc collectionFunc && funcType != MongoFuncType.AGGREGATE ? collectionFunc.getCollectionName() : null;
            String marker = StringUtils.isNotBlank(collection) ? collection : funcType.getFuncStr();
            int offset = indexOfIgnoreCase(query, marker, searchOffset);
            if (offset < 0) {
                offset = searchOffset;
            }
            searchOffset = Math.min(query.length(), offset + marker.length());

            BehaviorObject object = new BehaviorObject();
            object.setTargetType(StringUtils.isNotBlank(collection) ? TargetType.Table : TargetType.Schema);
            object.setResourcePath(resourcePath(levels, collection));
            setRange(object, query, offset, marker.length(), baseLine, baseColumn);

            BehaviorRelation relation = new BehaviorRelation();
            relation.setSubject(object);
            relation.setAction(action(statementType));
            behavior.getRelations().add(relation);
            result.add(behavior);
        }
        return result;
    }

    private BehaviorAction action(SecQueryType type) {
        return switch (type) {
            case SELECT, METADATA, PERFORMANCE, LOG_READ -> BehaviorAction.READ;
            case CREATE_TABLE, CREATE_VIEW, ADD_INDEX -> BehaviorAction.CREATE;
            case ALTER_TABLE, ALTER_VIEW, ALTER_INDEX -> BehaviorAction.ALTER;
            case DROP_TABLE, DROP_VIEW, DROP_SCHEMA, DROP_INDEX -> BehaviorAction.DROP;
            case RENAME_TABLE, RENAME_VIEW -> BehaviorAction.RENAME;
            case INSERT -> BehaviorAction.INSERT;
            case UPDATE -> BehaviorAction.UPDATE;
            case DELETE -> BehaviorAction.DELETE;
            case MERGE -> BehaviorAction.MERGE;
            case SWITCH_SCHEMA -> BehaviorAction.SWITCH;
            case ADMIN, ADMIN_TABLE -> BehaviorAction.ADMIN;
            default -> BehaviorAction.OTHER;
        };
    }

    private String resourcePath(Map<UmiTypes, Object> levels, String collection) {
        List<String> nodes = new ArrayList<>();
        addPath(nodes, levels == null ? null : levels.get(UmiTypes.Instance));
        addPath(nodes, levels == null ? null : levels.get(UmiTypes.Schema));
        addPath(nodes, collection);
        return nodes.isEmpty() ? "/" : "/" + String.join("/", nodes) + "/";
    }

    private void addPath(List<String> nodes, Object value) {
        if (value == null) {
            return;
        }
        for (String node : StringUtils.toString(value).split("/")) {
            if (StringUtils.isNotBlank(node)) {
                nodes.add(node);
            }
        }
    }

    private int indexOfIgnoreCase(String text, String marker, int fromIndex) {
        if (StringUtils.isBlank(marker)) {
            return -1;
        }
        return text.toLowerCase(Locale.ROOT).indexOf(marker.toLowerCase(Locale.ROOT), Math.max(0, fromIndex));
    }

    private void setRange(BehaviorObject object, String query, int offset, int length, int baseLine, int baseColumn) {
        int line = Math.max(1, baseLine);
        int column = Math.max(0, baseColumn);
        for (int i = 0; i < offset; i++) {
            if (query.charAt(i) == '\n') {
                line++;
                column = 0;
            } else {
                column++;
            }
        }
        object.setStartLine(line);
        object.setStartColumn(column);
        object.setEndLine(line);
        object.setEndColumn(column + length);
    }
}
