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
import com.clougence.sql.redis.parser.ast.commands.AbstractRedisCmd;
import com.clougence.sql.redis.parser.ast.token.ArgToken;
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
                object.setTargetType(TargetType.Schema);
                object.setResourcePath(resourcePath(levels, null));
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
                object.setTargetType(TargetType.Key);
                object.setResourcePath(resourcePath(levels, value));
                setRange(object, query, offset, value.length(), baseLine, baseColumn);

                addRelation(behavior, object, action(statementType));
            }
            result.add(behavior);
        }
        return result;
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
        List<String> nodes = new ArrayList<>();
        addPath(nodes, levels == null ? null : levels.get(UmiTypes.Instance));
        addPath(nodes, levels == null ? null : levels.get(UmiTypes.Schema));
        addPath(nodes, key);
        return "/" + String.join("/", nodes) + "/";
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

    private void addRelation(StatementBehavior behavior, BehaviorObject object, BehaviorAction action) {
        BehaviorRelation relation = new BehaviorRelation();
        relation.setSubject(object);
        relation.setAction(action);
        behavior.getRelations().add(relation);
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
