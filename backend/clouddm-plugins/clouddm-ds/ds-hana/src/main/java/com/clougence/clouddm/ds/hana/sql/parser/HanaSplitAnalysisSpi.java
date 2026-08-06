/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.hana.sql.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.antlr.v4.runtime.Token;

import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.sql.parser.SplitAnalysisSpi;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.clouddm.sdk.sql.parser.SplitScript;
import com.clougence.clouddm.ds.hana.sql.parser.antlr.HanaLexer;

/**
 * Version-aware HANA statement splitter.
 *
 * <p>HANA is not an ISO SQL:2003 dialect. Using the ISO grammar here rejected
 * quoted identifiers and most HANA extensions, and parser recovery could throw
 * a {@link NullPointerException}. Splitting is intentionally lexical: the HANA
 * server is the syntax authority, while this class preserves statement text,
 * recognizes quoted/commented semicolons, keeps SQLScript blocks intact, and
 * assigns a non-UNKNOWN top-level operation type.</p>
 */
public class HanaSplitAnalysisSpi implements SplitAnalysisSpi {

    private final HanaParserConfig config;

    public HanaSplitAnalysisSpi(){
        this(HanaParserConfig.of(null, null));
    }

    public HanaSplitAnalysisSpi(HanaParserConfig config){
        this.config = config;
    }

    public HanaParserConfig config() {
        return config;
    }

    @Override
    public List<SplitScript> splitScript(String script, List<QueryArg> args, int baseLine, int baseColumn) {
        if (script == null || script.isBlank()) {
            return Collections.emptyList();
        }

        HanaTokenStream.LexedSql lexed = HanaTokenStream.lex(script);
        HanaVersionSyntaxValidator.validate(script, config, lexed.visible());

        List<Segment> segments = isSqlScriptBlock(lexed.visible())
                ? List.of(trimmed(script, 0, script.length()))
                : scan(script, lexed.all());
        List<SplitScript> result = new ArrayList<>(segments.size());
        for (Segment segment : segments) {
            if (segment == null) {
                continue;
            }
            List<Token> visible = HanaTokenStream.lex(segment.text()).visible();
            if (isCommentOnly(visible)) continue;
            SplitScript split = new SplitScript();
            split.setScript(segment.text());
            split.setType(classify(visible));
            split.setChildren(Collections.emptyList());
            Location start = location(script, segment.start(), baseLine, baseColumn);
            Location end = location(script, segment.end(), baseLine, baseColumn);
            split.setBodyStartCodeLine(start.line());
            split.setBodyStartCodeColumn(start.column());
            split.setBodyEndCodeLine(end.line());
            split.setBodyEndCodeColumn(end.column());
            result.add(split);
        }
        return result;
    }

    private static List<Segment> scan(String script, List<Token> tokens) {
        List<Segment> result = new ArrayList<>();
        int start = 0;
        for (Token token : tokens) {
            if (!HanaTokenStream.isType(token, HanaLexer.SEMICOLON)) continue;
            int end = token.getStopIndex() + 1;
            Segment value = trimmed(script, start, end);
            if (value != null) result.add(value);
            start = end;
        }
        Segment tail = trimmed(script, start, script.length());
        if (tail != null) {
            result.add(tail);
        }
        return result;
    }

    private static boolean isSqlScriptBlock(List<Token> tokens) {
        List<String> words = HanaTokenStream.words(tokens, 12);
        if (words.isEmpty()) {
            return false;
        }
        String first = words.get(0);
        if (first.equals("DO") || first.equals("BEGIN")) {
            return true;
        }
        if (!first.equals("CREATE") && !first.equals("ALTER")) {
            return false;
        }
        int index = 1;
        if (index + 1 < words.size() && words.get(index).equals("OR") && words.get(index + 1).equals("REPLACE")) {
            index += 2;
        }
        if (index >= words.size()) {
            return false;
        }
        String object = words.get(index);
        return object.equals("PROCEDURE") || object.equals("FUNCTION") || object.equals("TRIGGER") ||
                object.equals("ANONYMOUS");
    }

    private static Set<SplitQueryType> classify(List<Token> tokens) {
        List<String> words = HanaTokenStream.words(tokens, tokens.size());
        if (words.isEmpty()) {
            return Set.of(SplitQueryType.PROGRAM_CONTROL);
        }
        String first = words.get(0);
        return switch (first) {
            case "SELECT", "WITH" -> hasIdentifier(tokens, "M_PERFTRACE")
                    ? ordered(SplitQueryType.SELECT, SplitQueryType.PERFORMANCE)
                    : Set.of(SplitQueryType.SELECT);
            case "EXPLAIN" -> Set.of(SplitQueryType.PERFORMANCE);
            case "INSERT" -> Set.of(SplitQueryType.INSERT);
            case "UPSERT", "REPLACE" -> words.contains("SELECT")
                    ? ordered(SplitQueryType.MERGE, SplitQueryType.SELECT)
                    : Set.of(SplitQueryType.MERGE);
            case "UPDATE" -> Set.of(SplitQueryType.UPDATE);
            case "DELETE" -> Set.of(SplitQueryType.DELETE);
            case "MERGE" -> wordAt(words, 1, "DELTA")
                    ? Set.of(SplitQueryType.ADMIN_TABLE)
                    : Set.of(SplitQueryType.MERGE);
            case "CREATE", "ALTER", "DROP", "RENAME" -> ddlTypes(tokens, words, first);
            case "TRUNCATE" -> Set.of(SplitQueryType.TRUNCATE_TABLE);
            case "COMMENT", "ANNOTATE" -> Set.of(commentType(words));
            case "CALL" -> Set.of(SplitQueryType.CALL_PROG_OBJ);
            case "DO", "BEGIN" -> Set.of(SplitQueryType.BLOCK);
            case "DECLARE", "DEFAULT", "END", "FOR", "RETURN" -> Set.of(SplitQueryType.PROGRAM_CONTROL);
            case "SET" -> setTypes(words);
            case "GRANT" -> Set.of(SplitQueryType.GRANT);
            case "REVOKE" -> Set.of(SplitQueryType.REVOKE);
            case "COMMIT", "ROLLBACK", "SAVEPOINT", "RELEASE" -> Set.of(SplitQueryType.TRANSACTION);
            case "LOCK" -> wordAt(words, 1, "TABLE")
                    ? Set.of(SplitQueryType.SESSION_LOCK)
                    : Set.of(SplitQueryType.QUERY_LOCK);
            case "LOAD", "IMPORT" -> Set.of(SplitQueryType.DATA_IMPORT);
            case "UNLOAD", "EXPORT" -> Set.of(SplitQueryType.DATA_EXPORT);
            case "REFRESH" -> wordAt(words, 1, "STATISTICS")
                    ? Set.of(SplitQueryType.ADMIN_PERFORMANCE)
                    : Set.of(SplitQueryType.METADATA);
            default -> Set.of(SplitQueryType.PROGRAM_CONTROL);
        };
    }

    private static Set<SplitQueryType> setTypes(List<String> words) {
        if (wordAt(words, 1, "SCHEMA")) {
            return Set.of(SplitQueryType.SWITCH_SCHEMA);
        }
        if (wordAt(words, 1, "TRANSACTION") && wordAt(words, 2, "ISOLATION")) {
            return ordered(SplitQueryType.TRANSACTION, SplitQueryType.SESSION_SETTING_WRITE);
        }
        return Set.of(SplitQueryType.SESSION_SETTING_WRITE);
    }

    private static Set<SplitQueryType> ddlTypes(List<Token> tokens, List<String> words, String action) {
        if (action.equals("CREATE") && isColumnTable(words)) {
            return createTableTypes(tokens, words);
        }
        if (sequence(words, action, "SCHEMA", "SYNONYM")) {
            return Set.of(action.equals("CREATE") ? SplitQueryType.CREATE_SYNONYM : SplitQueryType.DROP_SYNONYM);
        }
        if (sequence(words, action, "WORKLOAD", "CLASS")) {
            return Set.of(switch (action) {
                case "CREATE" -> SplitQueryType.CREATE_RESOURCE_GROUP;
                case "ALTER" -> SplitQueryType.ALTER_RESOURCE_GROUP;
                default -> SplitQueryType.DROP_RESOURCE_GROUP;
            });
        }
        if ((action.equals("CREATE") || action.equals("ALTER")) && wordAt(words, 1, "STATISTICS")) {
            return Set.of(SplitQueryType.ADMIN_PERFORMANCE);
        }
        if (action.equals("ALTER") && wordAt(words, 1, "SYSTEM") && isPerformanceAdministration(tokens, words)) {
            return Set.of(SplitQueryType.ADMIN_PERFORMANCE);
        }
        if (action.equals("ALTER") && sequence(words, "ALTER", "SYSTEM", "ALTER", "CONFIGURATION")) {
            return Set.of(SplitQueryType.SYSTEM_SETTING_WRITE);
        }
        return Set.of(ddlType(words, action));
    }

    private static Set<SplitQueryType> createTableTypes(List<Token> tokens, List<String> words) {
        LinkedHashSet<SplitQueryType> result = new LinkedHashSet<>();
        result.add(SplitQueryType.CREATE_TABLE);
        if (!words.contains("LIKE")) {
            result.add(SplitQueryType.ADD_COLUMN);
            if (sequence(words, "PRIMARY", "KEY") || words.contains("UNIQUE") ||
                    sequence(words, "FOREIGN", "KEY") || words.contains("CHECK")) {
                result.add(SplitQueryType.ADD_CONSTRAINT);
            }
        }
        if (sequence(words, "FUZZY", "SEARCH", "INDEX") || sequence(words, "WITH", "INDEX")) {
            result.add(SplitQueryType.ADD_INDEX);
        }
        if (words.contains("COMMENT")) {
            result.add(SplitQueryType.COMMENT_COLUMN);
            result.add(SplitQueryType.COMMENT_TABLE);
        }
        return Collections.unmodifiableSet(result);
    }

    private static boolean isColumnTable(List<String> words) {
        return wordAt(words, 1, "COLUMN") && wordAt(words, 2, "TABLE") ||
                wordAt(words, 1, "LOCAL") && wordAt(words, 2, "TEMPORARY") &&
                        wordAt(words, 3, "COLUMN") && wordAt(words, 4, "TABLE");
    }

    private static boolean isPerformanceAdministration(List<Token> tokens, List<String> words) {
        return words.contains("PERFTRACE") || sequence(words, "PLAN", "CACHE") ||
                sequence(words, "COLUMN", "JOIN", "DATA", "STATISTICS") ||
                tokens.stream().map(Token::getText).map(value -> value.toUpperCase(java.util.Locale.ROOT))
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

    private static Set<SplitQueryType> ordered(SplitQueryType... values) {
        return Collections.unmodifiableSet(new LinkedHashSet<>(List.of(values)));
    }

    private static SplitQueryType ddlType(List<String> words, String action) {
        String object = firstObject(words);
        String key = action + '_' + object;
        try {
            return SplitQueryType.valueOf(key);
        } catch (IllegalArgumentException ignored) {
            if (object.equals("INDEX")) {
                return switch (action) {
                    case "CREATE" -> SplitQueryType.ADD_INDEX;
                    case "ALTER" -> SplitQueryType.ALTER_INDEX;
                    case "DROP" -> SplitQueryType.DROP_INDEX;
                    default -> SplitQueryType.RENAME_INDEX;
                };
            }
            if (object.equals("PROCEDURE") || object.equals("FUNCTION") || object.equals("AGGREGATE") ||
                    object.equals("PACKAGE")) {
                return switch (action) {
                    case "CREATE" -> SplitQueryType.CREATE_PROG_OBJ;
                    case "ALTER" -> SplitQueryType.ALTER_PROG_OBJ;
                    case "DROP" -> SplitQueryType.DROP_PROG_OBJ;
                    default -> SplitQueryType.RENAME_PROG_OBJ;
                };
            }
            return SplitQueryType.ADMIN;
        }
    }

    private static SplitQueryType commentType(List<String> words) {
        String object = firstObject(words);
        try {
            return SplitQueryType.valueOf("COMMENT_" + object);
        } catch (IllegalArgumentException ignored) {
            return SplitQueryType.METADATA;
        }
    }

    private static String firstObject(List<String> words) {
        Set<String> objects = new LinkedHashSet<>(List.of("CATALOG", "SCHEMA", "TABLESPACE", "TABLE", "COLUMN",
                "CONSTRAINT", "INDEX", "PARTITION", "VIEW", "SEQUENCE", "TYPE", "SYNONYM", "PROCEDURE",
                "FUNCTION", "AGGREGATE", "PACKAGE", "TRIGGER", "EVENT", "JOB", "RESOURCE_GROUP", "USER",
                "ROLE", "LIBRARY", "LANGUAGE", "TRANSFORM", "REPLICATION", "POLICY"));
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

    private static boolean isCommentOnly(List<Token> tokens) {
        return tokens.stream().allMatch(token -> HanaTokenStream.isType(token, HanaLexer.SEMICOLON));
    }

    private static Segment trimmed(String script, int start, int end) {
        while (start < end && Character.isWhitespace(script.charAt(start))) start++;
        while (end > start && Character.isWhitespace(script.charAt(end - 1))) end--;
        return start == end ? null : new Segment(script.substring(start, end), start, end);
    }

    private static Location location(String script, int offset, int baseLine, int baseColumn) {
        int line = baseLine;
        int column = baseColumn;
        for (int index = 0; index < offset; index++) {
            char value = script.charAt(index);
            if (value == '\n') {
                line++;
                column = 0;
            } else if (value != '\r') {
                column++;
            }
        }
        return new Location(line, column);
    }

    private record Segment(String text, int start, int end) { }

    private record Location(int line, int column) { }
}
