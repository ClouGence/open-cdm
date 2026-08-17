/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.hana.sql.analysis.behavior;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.iso.sql2003.analysis.behavior.Sql2003BehaviorAnalysisSpi;
import com.clougence.utils.StringUtils;

public class HanaBehaviorAnalysisSpi extends Sql2003BehaviorAnalysisSpi {

    private static final String  IDENTIFIER = "(?:\"(?:\"\"|[^\"])+\"|[A-Za-z_][A-Za-z0-9_$#]*)";
    private static final Pattern DML_TARGET = Pattern.compile("(?is)^\\s*(INSERT\\s+INTO|UPDATE|DELETE\\s+FROM)\\s+(" + IDENTIFIER + "(?:\\s*\\.\\s*" + IDENTIFIER + ")*)");
    private static final Pattern DML_SOURCE = Pattern.compile("(?is)\\b(?:FROM|JOIN|USING)\\s+(" + IDENTIFIER + "(?:\\s*\\.\\s*" + IDENTIFIER + ")*)");

    @Override
    public Stream<StatementBehavior> analysisBehaviorStream(Reader queryReader, Map<UmiTypes, Object> levels, int baseLine, int baseColumn) {
        String sql = read(queryReader);
        List<StatementBehavior> result = new ArrayList<>();
        for (String statement : statements(sql)) {
            Matcher matcher = DML_TARGET.matcher(statement);
            boolean dml = matcher.find();
            try (Stream<StatementBehavior> behaviors = super.analysisBehaviorStream(new StringReader(statement), levels, baseLine, baseColumn)) {
                try {
                    result.addAll(behaviors.toList());
                } catch (RuntimeException e) {
                    if (!dml) {
                        throw e;
                    }
                    result.add(dml(statement, matcher, levels));
                }
            }
        }
        return result.stream();
    }

    private StatementBehavior dml(String sql, Matcher matcher, Map<UmiTypes, Object> levels) {
        String operation = matcher.group(1).toUpperCase();
        SplitQueryType type;
        BehaviorAction action;
        if (operation.startsWith("INSERT")) {
            type = SplitQueryType.INSERT;
            action = BehaviorAction.INSERT;
        } else if (operation.startsWith("UPDATE")) {
            type = SplitQueryType.UPDATE;
            action = BehaviorAction.UPDATE;
        } else {
            type = SplitQueryType.DELETE;
            action = BehaviorAction.DELETE;
        }

        BehaviorRelation relation = new BehaviorRelation();
        relation.setAction(action);
        relation.setSubject(table(matcher.group(2), levels));
        Matcher sources = DML_SOURCE.matcher(sql);
        while (sources.find()) {
            BehaviorObject source = table(sources.group(1), levels);
            if (!source.getObjectPath().equals(relation.getSubject().getObjectPath())) {
                relation.getTarget().add(source);
            }
        }
        if (type == SplitQueryType.INSERT) {
            relation.setInsertRows(insertRows(sql));
        }

        StatementBehavior behavior = new StatementBehavior();
        behavior.setStatementType(type);
        behavior.setRelations(List.of(relation));
        return behavior;
    }

    private BehaviorObject table(String qualifiedName, Map<UmiTypes, Object> levels) {
        List<String> names = Stream.of(qualifiedName.split("\\s*\\.\\s*")).map(this::unquote).toList();
        String tableName = names.get(names.size() - 1);
        String schema = names.size() >= 2 ? names.get(names.size() - 2) : level(levels, UmiTypes.Schema);
        String catalog = names.size() >= 3 ? names.get(names.size() - 3) : level(levels, UmiTypes.Catalog);

        List<String> path = new ArrayList<>();
        addPath(path, level(levels, UmiTypes.Instance));
        addPath(path, catalog);
        addPath(path, schema);
        addPath(path, tableName);

        BehaviorObject object = new BehaviorObject();
        object.setObjectType(TargetType.Table);
        object.setObjectName(new ObjectName(catalog, schema, tableName));
        object.setObjectPath("/" + String.join("/", path) + "/");
        return object;
    }

    private Long insertRows(String sql) {
        int values = indexOfWord(sql, "VALUES");
        if (values < 0) {
            return null;
        }
        long rows = 0;
        int depth = 0;
        boolean quoted = false;
        for (int i = values + 6; i < sql.length(); i++) {
            char ch = sql.charAt(i);
            if (ch == '\'' && (i + 1 >= sql.length() || sql.charAt(i + 1) != '\'')) {
                quoted = !quoted;
            } else if (!quoted && ch == '(') {
                if (depth++ == 0) {
                    rows++;
                }
            } else if (!quoted && ch == ')' && depth > 0) {
                depth--;
            }
        }
        return rows == 0 ? null : rows;
    }

    private List<String> statements(String sql) {
        List<String> result = new ArrayList<>();
        int start = 0;
        char quote = 0;
        for (int i = 0; i < sql.length(); i++) {
            char ch = sql.charAt(i);
            if ((ch == '\'' || ch == '"') && (quote == 0 || quote == ch)) {
                if (quote == ch && i + 1 < sql.length() && sql.charAt(i + 1) == ch) {
                    i++;
                } else {
                    quote = quote == 0 ? ch : 0;
                }
            } else if (ch == ';' && quote == 0) {
                addStatement(result, sql.substring(start, i + 1));
                start = i + 1;
            }
        }
        addStatement(result, sql.substring(start));
        return result;
    }

    private void addStatement(List<String> result, String statement) {
        if (!statement.isBlank()) {
            result.add(statement);
        }
    }

    private int indexOfWord(String sql, String word) {
        Matcher matcher = Pattern.compile("(?i)\\b" + word + "\\b").matcher(sql);
        return matcher.find() ? matcher.start() : -1;
    }

    private void addPath(List<String> path, String value) {
        if (StringUtils.isNotBlank(value)) {
            for (String part : value.split("/")) {
                if (StringUtils.isNotBlank(part)) {
                    path.add(part);
                }
            }
        }
    }

    private String level(Map<UmiTypes, Object> levels, UmiTypes type) {
        return levels == null || levels.get(type) == null ? null : String.valueOf(levels.get(type));
    }

    private String unquote(String value) {
        String name = value.trim();
        return name.length() >= 2 && name.charAt(0) == '"' && name.charAt(name.length() - 1) == '"' ? name.substring(1, name.length() - 1).replace("\"\"", "\"") : name;
    }

    private String read(Reader reader) {
        StringBuilder value = new StringBuilder();
        char[] buffer = new char[4096];
        try {
            int length;
            while ((length = reader.read(buffer)) >= 0) {
                value.append(buffer, 0, length);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return value.toString();
    }
}
