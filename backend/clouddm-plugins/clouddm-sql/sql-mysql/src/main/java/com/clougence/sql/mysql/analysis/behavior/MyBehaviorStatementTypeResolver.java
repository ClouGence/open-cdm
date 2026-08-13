/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.mysql.analysis.behavior;

import java.util.List;
import java.util.Locale;

import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.mysql.analysis.reference.MySqlObjectReference;
import com.clougence.utils.StringUtils;

final class MyBehaviorStatementTypeResolver {

    private MyBehaviorStatementTypeResolver(){
    }

    static SplitQueryType resolve(String sql, List<MySqlObjectReference> references) {
        String normalized = sql.stripLeading().toUpperCase(Locale.ROOT);
        if (normalized.startsWith("EXPLAIN")) {
            int statementStart = MyBehaviorText.findWord(sql, 0, "SELECT", "WITH", "UPDATE", "DELETE", "INSERT", "REPLACE");
            int analyzeStart = MyBehaviorText.findWord(sql, 0, "ANALYZE");
            if (analyzeStart >= 0 && (statementStart < 0 || analyzeStart < statementStart)) {
                return SplitQueryType.UNSAFE;
            }
            return SplitQueryType.SELECT;
        }
        if (normalized.startsWith("DESC ") || normalized.startsWith("DESCRIBE ")) {
            return SplitQueryType.METADATA;
        }
        if (normalized.startsWith("DO ") || normalized.startsWith("DO(")) {
            return SplitQueryType.BLOCK;
        }
        if (normalized.startsWith("WITH ")) {
            int writeStart = findWithWrite(normalized);
            if (writeStart >= 0) {
                int writeEnd = MyBehaviorText.wordEnd(normalized, writeStart);
                return switch (normalized.substring(writeStart, writeEnd)) {
                    case "UPDATE" -> SplitQueryType.UPDATE;
                    case "DELETE" -> SplitQueryType.DELETE;
                    case "REPLACE" -> SplitQueryType.MERGE;
                    default -> contains(references, SplitQueryType.MERGE) ? SplitQueryType.MERGE : SplitQueryType.INSERT;
                };
            }
            if (containsNode(references, "information_schema")) {
                return SplitQueryType.METADATA;
            }
            return SplitQueryType.SELECT;
        }
        if (isSelectExpression(normalized)) {
            if (contains(references, SplitQueryType.LOG_READ)) {
                return SplitQueryType.LOG_READ;
            }
            if (contains(references, SplitQueryType.PERFORMANCE)) {
                return SplitQueryType.PERFORMANCE;
            }
            if (contains(references, SplitQueryType.DATA_EXPORT)) {
                return SplitQueryType.DATA_EXPORT;
            }
            List<MySqlObjectReference> dataObjects = references.stream()
                .filter(reference -> reference.targetType() == TargetType.Table || reference.targetType() == TargetType.View || reference.targetType() == TargetType.Materialized)
                .toList();
            if (dataObjects.isEmpty() && (normalized.contains("AUDIT_LOG_READ(") || normalized.contains("AUDIT_LOG_READ_BOOKMARK("))) {
                return SplitQueryType.LOG_READ;
            }
            if (!dataObjects.isEmpty() && dataObjects.stream().allMatch(reference -> containsNode(reference, "information_schema"))) {
                return SplitQueryType.METADATA;
            }
            if (!dataObjects.isEmpty() && dataObjects.stream().allMatch(reference -> containsNode(reference, "performance_schema"))) {
                return SplitQueryType.PERFORMANCE;
            }
            return SplitQueryType.SELECT;
        }
        if (normalized.startsWith("INSERT")) {
            return contains(references, SplitQueryType.MERGE) ? SplitQueryType.MERGE : SplitQueryType.INSERT;
        }
        if (normalized.startsWith("REPLACE")) {
            return SplitQueryType.MERGE;
        }
        if (normalized.startsWith("UPDATE")) {
            return SplitQueryType.UPDATE;
        }
        if (normalized.startsWith("DELETE")) {
            return SplitQueryType.DELETE;
        }
        if (normalized.startsWith("CALL")) {
            return SplitQueryType.CALL_PROG_OBJ;
        }
        if (normalized.startsWith("HANDLER ")) {
            return SplitQueryType.SELECT;
        }
        if (normalized.startsWith("SHOW ")) {
            return resolveShow(normalized);
        }
        if (normalized.startsWith("GET DIAGNOSTICS") || normalized.startsWith("GET CURRENT DIAGNOSTICS") || normalized.startsWith("GET STACKED DIAGNOSTICS")) {
            return SplitQueryType.PERFORMANCE;
        }
        if (normalized.startsWith("EXECUTE") || normalized.startsWith("PREPARE") || normalized.startsWith("DEALLOCATE PREPARE")) {
            return SplitQueryType.UNSAFE;
        }
        if (normalized.startsWith("CLONE LOCAL DATA DIRECTORY")) {
            return SplitQueryType.DATA_EXPORT;
        }
        if (normalized.startsWith("LOCK INSTANCE") || normalized.startsWith("UNLOCK INSTANCE") || normalized.startsWith("LOCK TABLE") || normalized.startsWith("UNLOCK TABLE")) {
            return SplitQueryType.SESSION_LOCK;
        }
        if (normalized.startsWith("START TRANSACTION") || normalized.startsWith("COMMIT") || normalized.startsWith("ROLLBACK") || normalized.startsWith("SAVEPOINT")
            || normalized.startsWith("RELEASE SAVEPOINT") || normalized.startsWith("XA ") || normalized.startsWith("BEGIN") || normalized.startsWith("SET TRANSACTION")
            || normalized.startsWith("SET SESSION TRANSACTION") || normalized.startsWith("SET GLOBAL TRANSACTION")) {
            return SplitQueryType.TRANSACTION;
        }
        if (normalized.startsWith("START REPLICA") || normalized.startsWith("STOP REPLICA") || normalized.startsWith("START SLAVE") || normalized.startsWith("STOP SLAVE")
            || normalized.startsWith("START GROUP_REPLICATION") || normalized.startsWith("STOP GROUP_REPLICATION") || normalized.startsWith("RESET REPLICA")
            || normalized.startsWith("RESET SLAVE") || normalized.startsWith("CHANGE REPLICATION") || normalized.startsWith("CHANGE MASTER")) {
            return SplitQueryType.ALTER_REPLICATION;
        }
        if (normalized.startsWith("BINLOG ")) {
            return SplitQueryType.ADMIN_REPLICATION;
        }
        if (normalized.startsWith("ALTER INSTANCE") && normalized.contains("LOG")) {
            return SplitQueryType.ADMIN_LOG;
        }
        if (normalized.startsWith("FLUSH")) {
            if (normalized.contains(" FOR EXPORT")) {
                return SplitQueryType.DATA_EXPORT;
            }
            if (normalized.contains(" LOG")) {
                return SplitQueryType.MAINTAIN_LOG;
            }
            if (normalized.contains("DES_KEY_FILE")) {
                return SplitQueryType.SYSTEM_SETTING_WRITE;
            }
            if (normalized.contains("STATUS") || normalized.contains("USER_RESOURCES") || normalized.contains("OPTIMIZER_COSTS") || normalized.contains("HOSTS")
                || normalized.contains("QUERY CACHE")) {
                return SplitQueryType.ADMIN_PERFORMANCE;
            }
            if (normalized.contains("TABLE")) {
                return SplitQueryType.ADMIN_TABLE;
            }
            if (normalized.contains("PRIVILEGES")) {
                return SplitQueryType.SYSTEM_SETTING_WRITE;
            }
            return SplitQueryType.ADMIN;
        }
        if (normalized.startsWith("KILL ")) {
            return SplitQueryType.ADMIN;
        }
        if (normalized.startsWith("PURGE BINARY LOGS") || normalized.startsWith("PURGE MASTER LOGS") || normalized.startsWith("RESET BINARY LOGS")
            || normalized.startsWith("RESET MASTER")) {
            return SplitQueryType.MAINTAIN_LOG;
        }
        if (normalized.startsWith("RESET QUERY CACHE")) {
            return SplitQueryType.ADMIN_PERFORMANCE;
        }
        if (normalized.startsWith("CACHE INDEX") || normalized.startsWith("LOAD INDEX INTO CACHE")) {
            return SplitQueryType.ADMIN_PERFORMANCE;
        }
        if (normalized.startsWith("CHECK TABLE")) {
            return SplitQueryType.ADMIN_TABLE;
        }
        if (normalized.startsWith("HELP ")) {
            return SplitQueryType.METADATA;
        }
        if (normalized.startsWith("CLONE INSTANCE") || normalized.startsWith("IMPORT TABLE")) {
            return SplitQueryType.DATA_IMPORT;
        }
        if (normalized.startsWith("SET PASSWORD")) {
            return SplitQueryType.ALTER_USER;
        }
        if (normalized.startsWith("SET DEFAULT ROLE")) {
            return SplitQueryType.ALTER_USER;
        }
        if (normalized.startsWith("SET ROLE")) {
            return SplitQueryType.SWITCH_ROLE;
        }
        if (normalized.startsWith("USE ")) {
            return SplitQueryType.SWITCH_SCHEMA;
        }
        if (isScopedSetAssignment(normalized)) {
            return SplitQueryType.SESSION_SETTING_WRITE;
        }
        if (normalized.startsWith("SET @@PERSIST") || normalized.startsWith("SET PERSIST")) {
            return SplitQueryType.SYSTEM_SETTING_WRITE;
        }
        if (normalized.startsWith("INSTALL PLUGIN") || normalized.startsWith("INSTALL COMPONENT")) {
            return SplitQueryType.CREATE_LIBRARY;
        }
        if (normalized.startsWith("UNINSTALL PLUGIN") || normalized.startsWith("UNINSTALL COMPONENT")) {
            return SplitQueryType.DROP_LIBRARY;
        }
        if (normalized.startsWith("SIGNAL ") || normalized.startsWith("RESIGNAL")) {
            return SplitQueryType.PROGRAM_CONTROL;
        }
        if (normalized.startsWith("RESTART") || normalized.startsWith("SHUTDOWN")) {
            return SplitQueryType.UNSAFE;
        }
        if (normalized.startsWith("REVOKE ")) {
            return SplitQueryType.REVOKE;
        }
        if (normalized.startsWith("GRANT ")) {
            return SplitQueryType.GRANT;
        }
        if (normalized.startsWith("DROP USER")) {
            return SplitQueryType.DROP_USER;
        }
        int createOrReplaceEnd = MyBehaviorText.afterStartingWords(normalized, "CREATE", "OR", "REPLACE");
        if (createOrReplaceEnd >= 0 && MyBehaviorText.findWord(normalized, createOrReplaceEnd, "VIEW") >= 0) {
            return SplitQueryType.ALTER_VIEW;
        }
        if (normalized.startsWith("ALTER DATABASE") || normalized.startsWith("ALTER SCHEMA")) {
            return SplitQueryType.ALTER_SCHEMA;
        }

        for (MySqlObjectReference reference : references) {
            if (reference.sqlType() != null && reference.sqlType() != SplitQueryType.UNKNOWN) {
                return reference.sqlType();
            }
        }
        return SplitQueryType.UNKNOWN;
    }

    private static boolean contains(List<MySqlObjectReference> references, SplitQueryType type) {
        return references.stream().anyMatch(reference -> reference.sqlType() == type);
    }

    private static SplitQueryType first(List<MySqlObjectReference> references, SplitQueryType... candidates) {
        for (SplitQueryType candidate : candidates) {
            if (contains(references, candidate)) {
                return candidate;
            }
        }
        return null;
    }

    private static boolean containsNode(MySqlObjectReference reference, String name) {
        return reference.nodes().stream().anyMatch(node -> StringUtils.equalsIgnoreCase(node, name));
    }

    private static boolean containsNode(List<MySqlObjectReference> references, String name) {
        return references.stream().anyMatch(reference -> containsNode(reference, name));
    }

    private static boolean isSelectExpression(String normalized) {
        if (normalized.startsWith("SELECT") || normalized.startsWith("TABLE ") || normalized.startsWith("VALUES ")) {
            return true;
        }
        int index = 0;
        while (index < normalized.length() && normalized.charAt(index) == '(') {
            index = MyBehaviorText.skipWhitespace(normalized, index + 1);
        }
        return index > 0 && (MyBehaviorText.startsWithWord(normalized, index, "SELECT") || MyBehaviorText.startsWithWord(normalized, index, "TABLE")
                             || MyBehaviorText.startsWithWord(normalized, index, "VALUES"));
    }

    private static SplitQueryType resolveShow(String normalized) {
        if (normalized.startsWith("SHOW MASTER STATUS") || normalized.startsWith("SHOW BINARY LOG") || normalized.startsWith("SHOW MASTER LOG")
            || normalized.startsWith("SHOW BINLOG") || normalized.startsWith("SHOW RELAYLOG") || isShowEngineCommand(normalized, "LOGS")) {
            return SplitQueryType.LOG_READ;
        }
        if (normalized.startsWith("SHOW STATUS") || normalized.startsWith("SHOW GLOBAL STATUS") || normalized.startsWith("SHOW SESSION STATUS")
            || normalized.startsWith("SHOW LOCAL STATUS") || normalized.startsWith("SHOW WARNINGS") || normalized.startsWith("SHOW ERRORS") || normalized.startsWith("SHOW COUNT(")
            || normalized.startsWith("SHOW PROFILE") || normalized.startsWith("SHOW PROCESSLIST") || normalized.startsWith("SHOW FULL PROCESSLIST")
            || normalized.startsWith("SHOW OPEN TABLES") || normalized.startsWith("SHOW PARSE_TREE") || isShowEngineCommand(normalized, "STATUS", "MUTEX")) {
            return SplitQueryType.PERFORMANCE;
        }
        return SplitQueryType.METADATA;
    }

    private static int findWithWrite(String sql) {
        for (int i = 0; i < sql.length(); i++) {
            if (sql.charAt(i) != ')') {
                continue;
            }
            int start = MyBehaviorText.skipWhitespace(sql, i + 1);
            if (MyBehaviorText.startsWithWord(sql, start, "UPDATE") || MyBehaviorText.startsWithWord(sql, start, "DELETE") || MyBehaviorText.startsWithWord(sql, start, "INSERT")
                || MyBehaviorText.startsWithWord(sql, start, "REPLACE")) {
                return start;
            }
        }
        return -1;
    }

    private static boolean isScopedSetAssignment(String sql) {
        int scopeStart = MyBehaviorText.afterStartingWords(sql, "SET");
        if (scopeStart < 0) {
            return false;
        }
        int scope = MyBehaviorText.skipWhitespace(sql, scopeStart);
        if (scope == scopeStart) {
            return false;
        }
        String[] names = { "GLOBAL", "LOCAL", "SESSION", "PERSIST_ONLY" };
        for (String name : names) {
            if (MyBehaviorText.startsWithWord(sql, scope, name)) {
                int equals = MyBehaviorText.skipWhitespace(sql, scope + name.length());
                return equals < sql.length() && sql.charAt(equals) == '=';
            }
        }
        return false;
    }

    private static boolean isShowEngineCommand(String sql, String... commands) {
        int engineEnd = MyBehaviorText.afterStartingWords(sql, "SHOW", "ENGINE");
        if (engineEnd < 0) {
            return false;
        }
        int engineName = MyBehaviorText.skipWhitespace(sql, engineEnd);
        if (engineName == engineEnd || engineName >= sql.length()) {
            return false;
        }
        int engineNameEnd = engineName;
        while (engineNameEnd < sql.length() && !Character.isWhitespace(sql.charAt(engineNameEnd))) {
            engineNameEnd++;
        }
        int commandStart = MyBehaviorText.skipWhitespace(sql, engineNameEnd);
        if (commandStart == engineNameEnd) {
            return false;
        }
        for (String command : commands) {
            if (MyBehaviorText.startsWithWord(sql, commandStart, command)) {
                return true;
            }
        }
        return false;
    }
}
