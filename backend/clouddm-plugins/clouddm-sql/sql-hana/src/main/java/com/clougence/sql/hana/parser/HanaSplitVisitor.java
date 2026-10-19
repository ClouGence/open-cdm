/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.hana.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.hana.parser.antlr.HanaParser;
import com.clougence.sql.hana.parser.antlr.HanaParserBaseVisitor;

final class HanaSplitVisitor extends HanaParserBaseVisitor<SplitQueryType> {

    static final HanaSplitVisitor    INSTANCE          = new HanaSplitVisitor();
    private static final Set<String> CREATE_MODIFIERS  = Set
        .of("OR", "REPLACE", "LOCAL", "GLOBAL", "TEMPORARY", "ROW", "COLUMN", "VIRTUAL", "UNIQUE", "FULLTEXT", "BTREE", "CPBTREE", "INVERTED", "HASH", "VALUE", "INDIVIDUAL");
    private static final Set<String> DML               = Set.of("SELECT", "INSERT", "UPDATE", "DELETE", "MERGE");

    private static final Set<String> DATETIME_SETTINGS = Set.of("'DATE_FORMAT'", "'TIME_FORMAT'", "'SECONDDATE_FORMAT'", "'TIMESTAMP_FORMAT'");

    private HanaSplitVisitor(){
    }

    @Override
    public SplitQueryType visitSplitStatement(HanaParser.SplitStatementContext context) {
        if (context.splitItem(0).splitBlock() != null) {
            return SplitQueryType.BLOCK;
        }
        List<String> words = new ArrayList<>();
        // CTE subqueries and SQLScript bodies belong to a different scope.
        for (HanaParser.SplitItemContext item : context.splitItem()) {
            if (item.splitAtom() != null) {
                // Session variable names are case-sensitive string values.
                if (item.getStart().getType() == HanaParser.STRING) {
                    words.add(item.getText());
                } else {
                    words.add(item.getText().toUpperCase(Locale.ROOT));
                }
            }
        }
        if (words.isEmpty()) {
            return SplitQueryType.UNKNOWN;
        }
        String first = words.get(0);
        if ("WITH".equals(first)) {
            for (String word : words) {
                if (DML.contains(word)) {
                    return SplitQueryType.valueOf(word);
                }
            }
            return SplitQueryType.UNKNOWN;
        }
        return switch (first) {
            case "SELECT", "INSERT", "UPDATE", "DELETE", "MERGE", "GRANT", "REVOKE" -> SplitQueryType.valueOf(first);
            case "CREATE", "ALTER", "DROP", "RENAME", "COMMENT" -> ddlType(words);
            case "TRUNCATE" -> SplitQueryType.TRUNCATE_TABLE;
            case "CALL" -> SplitQueryType.CALL_PROG_OBJ;
            case "CONNECT" -> SplitQueryType.SWITCH_USER;
            case "DO" -> SplitQueryType.BLOCK;
            case "COMMIT", "ROLLBACK", "SAVEPOINT" -> SplitQueryType.TRANSACTION;
            case "SET", "UNSET" -> settingType(words);
            case "EXPLAIN" -> SplitQueryType.PERFORMANCE;
            case "IMPORT" -> SplitQueryType.DATA_IMPORT;
            case "EXPORT" -> SplitQueryType.DATA_EXPORT;
            default -> SplitQueryType.UNKNOWN;
        };
    }

    private SplitQueryType ddlType(List<String> words) {
        // Explicit session targets are runtime administration, not system configuration.
        if (words.size() > 3 && "ALTER".equals(words.get(0)) && "SYSTEM".equals(words.get(1))) {
            String action = words.get(2);
            if (("ALTER".equals(action) || "DISCONNECT".equals(action)) && "SESSION".equals(words.get(3))) {
                return SplitQueryType.ADMIN;
            }
            if ("CANCEL".equals(action)
                && ("SESSION".equals(words.get(3)) || words.size() > 5 && "WORK".equals(words.get(3)) && "IN".equals(words.get(4)) && "SESSION".equals(words.get(5)))) {
                return SplitQueryType.ADMIN;
            }
        }
        int index = 1;
        if ("CREATE".equals(words.get(0))) {
            while (index < words.size() && CREATE_MODIFIERS.contains(words.get(index))) {
                index++;
            }
        } else if ("COMMENT".equals(words.get(0)) && words.size() > 1 && "ON".equals(words.get(1))) {
            index++;
        }
        if (index == words.size()) {
            return SplitQueryType.UNKNOWN;
        }
        String object = words.get(index);
        if ("PROCEDURE".equals(object) || "FUNCTION".equals(object)) {
            object = "PROG_OBJ";
        }
        if ("INDEX".equals(object) && "CREATE".equals(words.get(0))) {
            return SplitQueryType.ADD_INDEX;
        }
        try {
            return SplitQueryType.valueOf(words.get(0) + "_" + object);
        } catch (IllegalArgumentException e) {
            return SplitQueryType.UNKNOWN;
        }
    }

    private SplitQueryType settingType(List<String> words) {
        if (words.size() > 1) {
            if ("SCHEMA".equals(words.get(1))) {
                return SplitQueryType.SWITCH_SCHEMA;
            }
            if ("TRANSACTION".equals(words.get(1))) {
                return SplitQueryType.TRANSACTION;
            }
        }
        int keyIndex = 1;
        if (words.size() > 1 && "SESSION".equals(words.get(1))) {
            keyIndex++;
        }
        if (keyIndex < words.size()) {
            String key = words.get(keyIndex);
            if (key.startsWith("N'") || key.startsWith("n'")) {
                key = key.substring(1);
            }
            if (key.startsWith("'") && !DATETIME_SETTINGS.contains(key)) {
                return SplitQueryType.SESSION_VARIABLE_RW;
            }
        }
        return SplitQueryType.SESSION_SETTING_WRITE;
    }

}
