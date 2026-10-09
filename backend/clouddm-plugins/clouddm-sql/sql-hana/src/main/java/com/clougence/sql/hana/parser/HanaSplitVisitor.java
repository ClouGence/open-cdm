/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.hana.parser;

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.hana.parser.antlr.HanaParser;
import com.clougence.sql.hana.parser.antlr.HanaParserBaseVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

final class HanaSplitVisitor extends HanaParserBaseVisitor<SplitQueryType> {

    static final HanaSplitVisitor    INSTANCE         = new HanaSplitVisitor();
    private static final Set<String> CREATE_MODIFIERS = Set
        .of("OR", "REPLACE", "LOCAL", "GLOBAL", "TEMPORARY", "ROW", "COLUMN", "VIRTUAL", "UNIQUE", "FULLTEXT", "BTREE", "CPBTREE", "INVERTED", "HASH", "VALUE", "INDIVIDUAL");
    private static final Set<String> DML              = Set.of("SELECT", "INSERT", "UPDATE", "DELETE", "MERGE");

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
                words.add(item.getText().toUpperCase(Locale.ROOT));
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
        return SplitQueryType.SESSION_SETTING_WRITE;
    }

}
