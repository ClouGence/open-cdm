/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.hana.sql.analysis.behavior;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.antlr.v4.runtime.Token;

import com.clougence.clouddm.ds.hana.sql.parser.HanaParserConfig;
import com.clougence.clouddm.sdk.sql.analysis.behavior.StatementType;

/** Preserved HANA lexical statement classification used by behavior analysis. */
final class HanaStatementTypeResolver {

    private HanaStatementTypeResolver(){
    }

    public static Set<StatementType> classify(List<Token> tokens) {
        List<String> words = HanaTokenStream.words(tokens, tokens.size());
        if (words.isEmpty()) {
            return Set.of(StatementType.PROGRAM_CONTROL);
        }
        String first = words.get(0);
        return switch (first) {
            case "SELECT", "WITH" -> hasIdentifier(tokens, "M_PERFTRACE") ? ordered(StatementType.SELECT, StatementType.PERFORMANCE) : Set.of(StatementType.SELECT);
            case "EXPLAIN" -> Set.of(StatementType.PERFORMANCE);
            case "INSERT" -> Set.of(StatementType.INSERT);
            case "UPSERT", "REPLACE" -> words.contains("SELECT") ? ordered(StatementType.MERGE, StatementType.SELECT) : Set.of(StatementType.MERGE);
            case "UPDATE" -> Set.of(StatementType.UPDATE);
            case "DELETE" -> Set.of(StatementType.DELETE);
            case "MERGE" -> wordAt(words, 1, "DELTA") ? Set.of(StatementType.ADMIN_TABLE) : Set.of(StatementType.MERGE);
            case "CREATE", "ALTER", "DROP", "RENAME" -> ddlTypes(tokens, words, first);
            case "TRUNCATE" -> Set.of(StatementType.TRUNCATE_TABLE);
            case "COMMENT", "ANNOTATE" -> Set.of(commentType(words));
            case "CALL" -> Set.of(StatementType.CALL_PROG_OBJ);
            case "DO", "BEGIN" -> Set.of(StatementType.BLOCK);
            case "DECLARE", "DEFAULT", "END", "FOR", "RETURN" -> Set.of(StatementType.PROGRAM_CONTROL);
            case "SET" -> setTypes(words);
            case "GRANT" -> Set.of(StatementType.GRANT);
            case "REVOKE" -> Set.of(StatementType.REVOKE);
            case "COMMIT", "ROLLBACK", "SAVEPOINT", "RELEASE" -> Set.of(StatementType.TRANSACTION);
            case "LOCK" -> wordAt(words, 1, "TABLE") ? Set.of(StatementType.SESSION_LOCK) : Set.of(StatementType.QUERY_LOCK);
            case "LOAD", "IMPORT" -> Set.of(StatementType.DATA_IMPORT);
            case "UNLOAD", "EXPORT" -> Set.of(StatementType.DATA_EXPORT);
            case "REFRESH" -> wordAt(words, 1, "STATISTICS") ? Set.of(StatementType.ADMIN_PERFORMANCE) : Set.of(StatementType.METADATA);
            default -> Set.of(StatementType.PROGRAM_CONTROL);
        };
    }

    public static Set<StatementType> classifyStatement(String script, HanaParserConfig config) {
        HanaTokenStream.LexedSql lexed = HanaTokenStream.lex(script);
        HanaVersionSyntaxValidator.validate(script, config, lexed.visible());
        return classify(lexed.visible());
    }

    private static Set<StatementType> setTypes(List<String> words) {
        if (wordAt(words, 1, "SCHEMA")) {
            return Set.of(StatementType.SWITCH_SCHEMA);
        }
        if (wordAt(words, 1, "TRANSACTION") && wordAt(words, 2, "ISOLATION")) {
            return ordered(StatementType.TRANSACTION, StatementType.SESSION_SETTING_WRITE);
        }
        return Set.of(StatementType.SESSION_SETTING_WRITE);
    }

    private static Set<StatementType> ddlTypes(List<Token> tokens, List<String> words, String action) {
        if (action.equals("CREATE") && isColumnTable(words)) {
            return createTableTypes(tokens, words);
        }
        if (sequence(words, action, "SCHEMA", "SYNONYM")) {
            return Set.of(action.equals("CREATE") ? StatementType.CREATE_SYNONYM : StatementType.DROP_SYNONYM);
        }
        if (sequence(words, action, "WORKLOAD", "CLASS")) {
            return Set.of(switch (action) {
                case "CREATE" -> StatementType.CREATE_RESOURCE_GROUP;
                case "ALTER" -> StatementType.ALTER_RESOURCE_GROUP;
                default -> StatementType.DROP_RESOURCE_GROUP;
            });
        }
        if ((action.equals("CREATE") || action.equals("ALTER")) && wordAt(words, 1, "STATISTICS")) {
            return Set.of(StatementType.ADMIN_PERFORMANCE);
        }
        if (action.equals("ALTER") && wordAt(words, 1, "SYSTEM") && isPerformanceAdministration(tokens, words)) {
            return Set.of(StatementType.ADMIN_PERFORMANCE);
        }
        if (action.equals("ALTER") && sequence(words, "ALTER", "SYSTEM", "ALTER", "CONFIGURATION")) {
            return Set.of(StatementType.SYSTEM_SETTING_WRITE);
        }
        return Set.of(ddlType(words, action));
    }

    private static Set<StatementType> createTableTypes(List<Token> tokens, List<String> words) {
        LinkedHashSet<StatementType> result = new LinkedHashSet<>();
        result.add(StatementType.CREATE_TABLE);
        if (!words.contains("LIKE")) {
            result.add(StatementType.ADD_COLUMN);
            if (sequence(words, "PRIMARY", "KEY") || words.contains("UNIQUE") || sequence(words, "FOREIGN", "KEY") || words.contains("CHECK")) {
                result.add(StatementType.ADD_CONSTRAINT);
            }
        }
        if (sequence(words, "FUZZY", "SEARCH", "INDEX") || sequence(words, "WITH", "INDEX")) {
            result.add(StatementType.ADD_INDEX);
        }
        if (words.contains("COMMENT")) {
            result.add(StatementType.COMMENT_COLUMN);
            result.add(StatementType.COMMENT_TABLE);
        }
        return Collections.unmodifiableSet(result);
    }

    private static boolean isColumnTable(List<String> words) {
        return wordAt(words, 1, "COLUMN") && wordAt(words, 2, "TABLE")
               || wordAt(words, 1, "LOCAL") && wordAt(words, 2, "TEMPORARY") && wordAt(words, 3, "COLUMN") && wordAt(words, 4, "TABLE");
    }

    private static boolean isPerformanceAdministration(List<Token> tokens, List<String> words) {
        return words.contains("PERFTRACE") || sequence(words, "PLAN", "CACHE") || sequence(words, "COLUMN", "JOIN", "DATA", "STATISTICS")
               || tokens.stream()
                   .map(Token::getText)
                   .map(value -> value.toUpperCase(java.util.Locale.ROOT))
                   .anyMatch(value -> value.contains("TRACEPROFILE_") || value.contains("SQLOPTSTEP") || value.contains("SQLOPTTIME"));
    }

    private static boolean hasIdentifier(List<Token> tokens, String identifier) {
        for (Token token : tokens) {
            String text = token.getText();
            if (text.equalsIgnoreCase(identifier) || text.equalsIgnoreCase('"' + identifier + '"')) {
                return true;
            }
        }
        return false;
    }

    private static boolean wordAt(List<String> words, int index, String value) {
        return index < words.size() && words.get(index).equals(value);
    }

    private static boolean sequence(List<String> words, String... values) {
        if (values.length > words.size()) {
            return false;
        }
        for (int start = 0; start <= words.size() - values.length; start++) {
            boolean matched = true;
            for (int offset = 0; offset < values.length; offset++) {
                if (!words.get(start + offset).equals(values[offset])) {
                    matched = false;
                    break;
                }
            }
            if (matched) {
                return true;
            }
        }
        return false;
    }

    private static Set<StatementType> ordered(StatementType... values) {
        return Collections.unmodifiableSet(new LinkedHashSet<>(List.of(values)));
    }

    private static StatementType ddlType(List<String> words, String action) {
        String object = firstObject(words);
        String key = action + '_' + object;
        try {
            return StatementType.valueOf(key);
        } catch (IllegalArgumentException ignored) {
            if (object.equals("INDEX")) {
                return switch (action) {
                    case "CREATE" -> StatementType.ADD_INDEX;
                    case "ALTER" -> StatementType.ALTER_INDEX;
                    case "DROP" -> StatementType.DROP_INDEX;
                    default -> StatementType.RENAME_INDEX;
                };
            }
            if (object.equals("PROCEDURE") || object.equals("FUNCTION") || object.equals("AGGREGATE") || object.equals("PACKAGE")) {
                return switch (action) {
                    case "CREATE" -> StatementType.CREATE_PROG_OBJ;
                    case "ALTER" -> StatementType.ALTER_PROG_OBJ;
                    case "DROP" -> StatementType.DROP_PROG_OBJ;
                    default -> StatementType.RENAME_PROG_OBJ;
                };
            }
            return StatementType.ADMIN;
        }
    }

    private static StatementType commentType(List<String> words) {
        String object = firstObject(words);
        try {
            return StatementType.valueOf("COMMENT_" + object);
        } catch (IllegalArgumentException ignored) {
            return StatementType.METADATA;
        }
    }

    private static String firstObject(List<String> words) {
        Set<String> objects = new LinkedHashSet<>(List
            .of("CATALOG", "SCHEMA", "TABLESPACE", "TABLE", "COLUMN", "CONSTRAINT", "INDEX", "PARTITION", "VIEW", "SEQUENCE", "TYPE", "SYNONYM", "PROCEDURE", "FUNCTION", "AGGREGATE", "PACKAGE", "TRIGGER", "EVENT", "JOB", "RESOURCE_GROUP", "USER", "ROLE", "LIBRARY", "LANGUAGE", "TRANSFORM", "REPLICATION", "POLICY"));
        for (int index = 1; index < words.size(); index++) {
            String word = words.get(index);
            if (objects.contains(word)) {
                return word;
            }
            if (word.equals("MATERIALIZED") && index + 1 < words.size() && words.get(index + 1).equals("VIEW")) {
                return "MATERIALIZED_VIEW";
            }
        }
        return "ADMIN";
    }

}
