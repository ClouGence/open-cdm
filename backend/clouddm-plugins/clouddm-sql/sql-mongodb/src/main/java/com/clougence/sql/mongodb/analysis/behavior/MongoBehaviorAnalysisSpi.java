/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.mongodb.analysis.behavior;

import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.ast.Statement;
import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.mongodb.parser.MongoDslProvider;
import com.clougence.sql.mongodb.parser.ast.MongoFuncType;
import com.clougence.sql.mongodb.parser.ast.commands.AbstractMongoFunc;
import com.clougence.sql.mongodb.parser.ast.commands.collection.CollectionFunc;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.IOUtils;

public class MongoBehaviorAnalysisSpi implements BehaviorAnalysisSpi {

    @Override
    public Stream<StatementBehavior> analysisBehaviorStream(Reader queryReader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        return analysisBehaviorMaterialized(queryReader, levels, baseLine, baseColumn).stream();
    }

    private List<StatementBehavior> analysisBehaviorMaterialized(Reader queryReader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        String query;
        try {
            query = IOUtils.readToString(queryReader);
        } catch (java.io.IOException e) {
            throw new UncheckedIOException(e);
        }

        StatementSet statementSet;
        try (StringReader reader = new StringReader(query)) {
            statementSet = DslHelper.parserDsl(MongoDslProvider.INSTANCE, reader);
        }
        List<StatementBehavior> result = new ArrayList<>();
        int searchOffset = 0;
        for (Statement statement : statementSet.getStatements()) {
            if (!(statement instanceof AbstractMongoFunc mongoFunc)) {
                continue;
            }
            MongoFuncType funcType = mongoFunc.getFuncType();
            SplitQueryType statementType = statementType(funcType);
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
            object.setObjectType(StringUtils.isNotBlank(collection) ? TargetType.Table : TargetType.Schema);
            object.setObjectPath(resourcePath(levels, collection));
            object.setObjectName(new ObjectName(null, level(levels, UmiTypes.Schema), collection));
            setRange(object, query, offset, marker.length(), baseLine, baseColumn);

            BehaviorRelation relation = new BehaviorRelation();
            relation.setSubject(object);
            relation.setAction(action(funcType, statementType));
            behavior.getRelations().add(relation);
            result.add(behavior);
        }
        return result;
    }

    @Deprecated
    private SplitQueryType statementType(MongoFuncType type) {
        return switch (type) {
            case FIND, AGGREGATE, FIND_ONE, COUNT, DISTINCT, COUNT_DOCUMENTS -> SplitQueryType.SELECT;
            case DATA_SIZE, HELLO, EXPLAIN -> SplitQueryType.PERFORMANCE;
            case LIST_COLLECTIONS, LIST_INDEXES, SHOW_DATABASES, SHOW_COLLECTIONS -> SplitQueryType.METADATA;
            case VALIDATE -> SplitQueryType.ADMIN_TABLE;
            case CREATE_INDEX, CREATE_INDEXES -> SplitQueryType.ADD_INDEX;
            case CREATE_VIEW -> SplitQueryType.CREATE_VIEW;
            case CREATE_COLLECTION -> SplitQueryType.CREATE_TABLE;
            case INSERT, INSERT_ONE, INSERT_MANY -> SplitQueryType.INSERT;
            case UPDATE, UPDATE_MANY, UPDATE_ONE, REPLACE_ONE, FIND_ONE_AND_REPLACE, FIND_ONE_AND_UPDATE -> SplitQueryType.UPDATE;
            case FIND_ONE_AND_DELETE, DELETE_ONE, DELETE_MANY -> SplitQueryType.DELETE;
            case DROP -> SplitQueryType.DROP_TABLE;
            case DROP_DATABASE -> SplitQueryType.DROP_SCHEMA;
            case RENAME_COLLECTION -> SplitQueryType.RENAME_TABLE;
            case ALTER_INDEX -> SplitQueryType.ALTER_INDEX;
            case DROP_INDEXES, DROP_INDEX -> SplitQueryType.DROP_INDEX;
            case USE -> SplitQueryType.SWITCH_SCHEMA;
            case HOST_INFO, FSYNC_LOCK, CURRENT_OP, KILL_OP, SERVER_STATUS, BUILD_INFO, GET_LOG_COMPONENTS, PROFILE, FSYNC_UNLOCK, DB_STATS, LATENCY_STATS -> SplitQueryType.ADMIN;
            default -> SplitQueryType.UNKNOWN;
        };
    }

    private BehaviorAction action(MongoFuncType funcType, SplitQueryType type) {
        BehaviorAction commandAction = switch (funcType) {
            case VALIDATE -> BehaviorAction.VALIDATE;
            case FSYNC_LOCK -> BehaviorAction.LOCK;
            case FSYNC_UNLOCK -> BehaviorAction.UNLOCK;
            case PROFILE -> BehaviorAction.CONFIGURE;
            case KILL_OP -> BehaviorAction.TERMINATE;
            case HOST_INFO, CURRENT_OP, SERVER_STATUS, BUILD_INFO, GET_LOG_COMPONENTS, DB_STATS, LATENCY_STATS -> BehaviorAction.READ;
            default -> null;
        };
        if (commandAction != null) {
            return commandAction;
        }
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
            case ADMIN, ADMIN_TABLE -> BehaviorAction.UNKNOWN;
            default -> BehaviorAction.UNKNOWN;
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
        String path = StringUtils.toString(value);
        int start = 0;
        for (int i = 0; i <= path.length(); i++) {
            if (i == path.length() || path.charAt(i) == '/') {
                String node = path.substring(start, i);
                if (StringUtils.isNotBlank(node)) {
                    nodes.add(node);
                }
                start = i + 1;
            }
        }
    }

    private String level(Map<UmiTypes, Object> levels, UmiTypes type) {
        return levels == null || levels.get(type) == null ? null : StringUtils.toString(levels.get(type));
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
