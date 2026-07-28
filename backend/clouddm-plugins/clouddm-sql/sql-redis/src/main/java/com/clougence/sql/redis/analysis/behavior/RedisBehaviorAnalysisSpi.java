/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.redis.analysis.behavior;

import java.lang.reflect.Field;
import java.util.*;

import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.ast.Statement;
import com.clougence.dslpaser.ast.StatementSet;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.redis.analysis.security.RedisAnalysisHelper;
import com.clougence.sql.redis.parser.RedisDslProvider;
import com.clougence.sql.redis.parser.ast.RedisCmdType;
import com.clougence.sql.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.sql.redis.parser.ast.commands.control.SwapDbRedisCmd;
import com.clougence.sql.redis.parser.ast.token.ArgToken;
import com.clougence.sql.redis.parser.ast.token.IntToken;
import com.clougence.sql.redis.parser.ast.token.StrToken;
import com.clougence.utils.StringUtils;

public class RedisBehaviorAnalysisSpi implements BehaviorAnalysisSpi {

    @Override
    public List<StatementBehavior> analysisBehavior(String query, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        if (StringUtils.isBlank(query)) {
            return Collections.emptyList();
        }

        StatementSet statementSet = DslHelper.parserDsl(RedisDslProvider.INSTANCE, query);
        List<StatementBehavior> result = new ArrayList<>();
        int searchOffset = 0;
        for (Statement statement : statementSet.getStatements()) {
            if (!(statement instanceof AbstractRedisCmd command)) {
                continue;
            }
            SplitQueryType statementType = RedisAnalysisHelper.cmdTypeToSecQueryType(command.getCmdType());
            StatementBehavior behavior = new StatementBehavior();
            behavior.setStatementType(statementType);
            List<StrToken> keys = keyTokens(command);
            if (command instanceof SwapDbRedisCmd swapDb) {
                addSwapDbRelation(behavior, levels, swapDb, baseLine, baseColumn);
                searchOffset = nextLineOffset(query, searchOffset);
                result.add(behavior);
                continue;
            }
            if (keys.isEmpty()) {
                int offset = searchOffset;
                while (offset < query.length() && Character.isWhitespace(query.charAt(offset))) {
                    offset++;
                }
                int end = offset;
                while (end < query.length() && !Character.isWhitespace(query.charAt(end))) {
                    end++;
                }

                BehaviorObject object = new BehaviorObject();
                boolean registeredCommand = command.getCmdType() == RedisCmdType.TIME;
                if (registeredCommand) {
                    String commandName = query.substring(offset, end);
                    object.setObjectType(TargetType.Function);
                    object.setObjectPath(resourcePath(levels, commandName));
                    object.setObjectName(new ObjectName(null, null, commandName));
                } else {
                    String schema = level(levels, UmiTypes.Schema);
                    object.setObjectType(TargetType.Schema);
                    object.setObjectPath(resourcePath(levels, null));
                    object.setObjectName(new ObjectName(null, schema, null));
                }
                setRange(object, query, offset, end - offset, baseLine, baseColumn);
                addRelation(behavior, object, action(statementType));
                searchOffset = end;
            }
            for (StrToken key : keys) {
                String value = key.isArg() ? "?" : key.getValue();
                int offset = query.indexOf(value, searchOffset);
                if (offset < 0) {
                    offset = searchOffset;
                }
                searchOffset = offset + value.length();

                BehaviorObject object = new BehaviorObject();
                object.setObjectType(TargetType.Key);
                object.setObjectPath(resourcePath(levels, value));
                object.setObjectName(new ObjectName(null, level(levels, UmiTypes.Schema), value));
                setRange(object, query, offset, value.length(), baseLine, baseColumn);

                addRelation(behavior, object, action(statementType));
            }
            result.add(behavior);
        }
        return result;
    }

    private void addSwapDbRelation(StatementBehavior behavior, Map<UmiTypes, Object> levels, SwapDbRedisCmd command, int baseLine, int baseColumn) {
        BehaviorObject source = schemaObject(levels, command.getIndex1(), baseLine, baseColumn);
        BehaviorObject target = schemaObject(levels, command.getIndex2(), baseLine, baseColumn);
        addRelation(behavior, source, BehaviorAction.TRANSFER).getTarget().add(target);
    }

    private BehaviorObject schemaObject(Map<UmiTypes, Object> levels, IntToken index, int baseLine, int baseColumn) {
        String schema = index.isArg() ? "?" : index.getBigInteger().toString();
        BehaviorObject object = new BehaviorObject();
        object.setObjectType(TargetType.Schema);
        object.setObjectPath(resourcePath(levels, schema, null));
        object.setObjectName(new ObjectName(null, schema, null));
        setRange(object, index.getStartPosition().getLineNumber(), index.getStartPosition().getColumnNumber(),//
                index.getEndPosition().getLineNumber(), index.getEndPosition().getColumnNumber(), baseLine, baseColumn);
        return object;
    }

    private int nextLineOffset(String query, int offset) {
        int lineEnd = query.indexOf('\n', offset);
        return lineEnd < 0 ? query.length() : lineEnd + 1;
    }

    private List<StrToken> keyTokens(AbstractRedisCmd command) {
        List<StrToken> keys = new ArrayList<>();
        collectKeys(command, keys, false);
        return keys;
    }

    private void collectKeys(Object value, List<StrToken> keys, boolean keyScope) {
        if (value == null) {
            return;
        }
        if (value instanceof StrToken token) {
            if (keyScope) {
                keys.add(token);
            }
            return;
        }
        if (value instanceof ArgToken) {
            return;
        }
        if (value instanceof Collection<?> collection) {
            for (Object item : collection) {
                collectKeys(item, keys, keyScope);
            }
            return;
        }
        if (!value.getClass().getName().startsWith("com.clougence.sql.redis.parser.ast.")) {
            return;
        }

        Class<?> type = value.getClass();
        while (type != null && type != Object.class) {
            for (Field field : type.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    boolean childKeyScope = keyScope || field.getName().toLowerCase(Locale.ROOT).contains("key");
                    collectKeys(field.get(value), keys, childKeyScope);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException("Cannot read Redis AST field: " + field.getName(), e);
                }
            }
            type = type.getSuperclass();
        }
    }

    private String resourcePath(Map<UmiTypes, Object> levels, String key) {
        return resourcePath(levels, level(levels, UmiTypes.Schema), key);
    }

    private String resourcePath(Map<UmiTypes, Object> levels, String schema, String key) {
        List<String> nodes = new ArrayList<>();
        addPath(nodes, levels == null ? null : levels.get(UmiTypes.Instance));
        addPath(nodes, schema);
        addPath(nodes, key);
        return "/" + String.join("/", nodes) + "/";
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

    private void setRange(BehaviorObject object, int startLine, int startColumn, int endLine, int endColumn, int baseLine, int baseColumn) {
        int lineOffset = Math.max(1, baseLine) - 1;
        object.setStartLine(lineOffset + startLine);
        object.setStartColumn(startLine == 1 ? Math.max(0, baseColumn) + startColumn : startColumn);
        object.setEndLine(lineOffset + endLine);
        object.setEndColumn(endLine == 1 ? Math.max(0, baseColumn) + endColumn : endColumn);
    }

    private BehaviorRelation addRelation(StatementBehavior behavior, BehaviorObject object, BehaviorAction action) {
        BehaviorRelation relation = new BehaviorRelation();
        relation.setSubject(object);
        relation.setAction(action);
        behavior.getRelations().add(relation);
        return relation;
    }

    private BehaviorAction action(SplitQueryType type) {
        return switch (type) {
            case SELECT, METADATA, PERFORMANCE, LOG_READ -> BehaviorAction.READ;
            case INSERT -> BehaviorAction.INSERT;
            case UPDATE -> BehaviorAction.UPDATE;
            case DELETE -> BehaviorAction.DELETE;
            case MERGE -> BehaviorAction.MERGE;
            default -> BehaviorAction.OTHER;
        };
    }
}
