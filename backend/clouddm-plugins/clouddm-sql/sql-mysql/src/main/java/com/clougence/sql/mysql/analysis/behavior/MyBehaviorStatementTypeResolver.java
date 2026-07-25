/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.mysql.analysis.behavior;

import java.util.List;
import java.util.Locale;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.sql.mysql.analysis.reference.MySqlObjectReference;

final class MyBehaviorStatementTypeResolver {

    private MyBehaviorStatementTypeResolver(){
    }

    static SecQueryType resolve(String sql, List<MySqlObjectReference> references) {
        String normalized = sql.stripLeading().toUpperCase(Locale.ROOT);
        if (normalized.startsWith("EXPLAIN ANALYZE ")) {
            int optionsStart = indexAfterPrefix(sql, "EXPLAIN ANALYZE");
            int statementStart = MyBehaviorText.findWord(sql, optionsStart, "SELECT", "WITH", "UPDATE", "DELETE", "INSERT", "REPLACE");
            return statementStart >= 0 ? resolve(sql.substring(statementStart), references) : SecQueryType.PERFORMANCE;
        }
        if (normalized.startsWith("EXPLAIN")) {
            return SecQueryType.PERFORMANCE;
        }
        if (normalized.startsWith("DESC ") || normalized.startsWith("DESCRIBE ")) {
            return SecQueryType.METADATA;
        }
        if (normalized.startsWith("DO ") || normalized.startsWith("DO(")) {
            return SecQueryType.BLOCK;
        }
        if (normalized.startsWith("WITH ")) {
            int writeStart = findWithWrite(normalized);
            if (writeStart >= 0) {
                int writeEnd = MyBehaviorText.wordEnd(normalized, writeStart);
                return switch (normalized.substring(writeStart, writeEnd)) {
                    case "UPDATE" -> SecQueryType.UPDATE;
                    case "DELETE" -> SecQueryType.DELETE;
                    case "REPLACE" -> SecQueryType.MERGE;
                    default -> contains(references, SecQueryType.MERGE) ? SecQueryType.MERGE : SecQueryType.INSERT;
                };
            }
            if (containsNode(references, "information_schema")) {
                return SecQueryType.METADATA;
            }
            return SecQueryType.SELECT;
        }
        if (isSelectExpression(normalized)) {
            if (contains(references, SecQueryType.LOG_READ)) {
                return SecQueryType.LOG_READ;
            }
            if (contains(references, SecQueryType.PERFORMANCE)) {
                return SecQueryType.PERFORMANCE;
            }
            if (contains(references, SecQueryType.DATA_EXPORT)) {
                return SecQueryType.DATA_EXPORT;
            }
            List<MySqlObjectReference> dataObjects = references.stream()
                .filter(reference -> reference.targetType() == TargetType.Table || reference.targetType() == TargetType.View || reference.targetType() == TargetType.Materialized)
                .toList();
            if (dataObjects.isEmpty() && (normalized.contains("AUDIT_LOG_READ(") || normalized.contains("AUDIT_LOG_READ_BOOKMARK("))) {
                return SecQueryType.LOG_READ;
            }
            if (!dataObjects.isEmpty() && dataObjects.stream().allMatch(reference -> containsNode(reference, "information_schema"))) {
                return SecQueryType.METADATA;
            }
            if (!dataObjects.isEmpty() && dataObjects.stream().allMatch(reference -> containsNode(reference, "performance_schema"))) {
                return SecQueryType.PERFORMANCE;
            }
            return SecQueryType.SELECT;
        }
        if (normalized.startsWith("INSERT")) {
            return contains(references, SecQueryType.MERGE) ? SecQueryType.MERGE : SecQueryType.INSERT;
        }
        if (normalized.startsWith("REPLACE")) {
            return SecQueryType.MERGE;
        }
        if (normalized.startsWith("UPDATE")) {
            return SecQueryType.UPDATE;
        }
        if (normalized.startsWith("DELETE")) {
            return SecQueryType.DELETE;
        }
        if (normalized.startsWith("CALL")) {
            return SecQueryType.CALL_PROG_OBJ;
        }
        if (normalized.startsWith("HANDLER ")) {
            return SecQueryType.SELECT;
        }
        if (normalized.startsWith("SHOW ")) {
            return resolveShow(normalized);
        }
        if (normalized.startsWith("GET DIAGNOSTICS") || normalized.startsWith("GET CURRENT DIAGNOSTICS") || normalized.startsWith("GET STACKED DIAGNOSTICS")) {
            return SecQueryType.PERFORMANCE;
        }
        if (normalized.startsWith("EXECUTE") || normalized.startsWith("PREPARE") || normalized.startsWith("DEALLOCATE PREPARE")) {
            return SecQueryType.UNSAFE;
        }
        if (normalized.startsWith("CLONE LOCAL DATA DIRECTORY")) {
            return SecQueryType.DATA_EXPORT;
        }
        if (normalized.startsWith("LOCK INSTANCE") || normalized.startsWith("UNLOCK INSTANCE") || normalized.startsWith("LOCK TABLE") || normalized.startsWith("UNLOCK TABLE")) {
            return SecQueryType.SESSION_LOCK;
        }
        if (normalized.startsWith("START TRANSACTION") || normalized.startsWith("COMMIT") || normalized.startsWith("ROLLBACK") || normalized.startsWith("SAVEPOINT")
            || normalized.startsWith("RELEASE SAVEPOINT") || normalized.startsWith("XA ") || normalized.startsWith("BEGIN") || normalized.startsWith("SET TRANSACTION")
            || normalized.startsWith("SET SESSION TRANSACTION") || normalized.startsWith("SET GLOBAL TRANSACTION")) {
            return SecQueryType.TRANSACTION;
        }
        if (normalized.startsWith("START REPLICA") || normalized.startsWith("STOP REPLICA") || normalized.startsWith("START SLAVE") || normalized.startsWith("STOP SLAVE")
            || normalized.startsWith("START GROUP_REPLICATION") || normalized.startsWith("STOP GROUP_REPLICATION") || normalized.startsWith("RESET REPLICA")
            || normalized.startsWith("RESET SLAVE") || normalized.startsWith("CHANGE REPLICATION") || normalized.startsWith("CHANGE MASTER")) {
            return SecQueryType.ALTER_REPLICATION;
        }
        if (normalized.startsWith("BINLOG ")) {
            return SecQueryType.ADMIN_REPLICATION;
        }
        if (normalized.startsWith("ALTER INSTANCE") && normalized.contains("LOG")) {
            return SecQueryType.ADMIN_LOG;
        }
        if (normalized.startsWith("FLUSH")) {
            if (normalized.contains(" FOR EXPORT")) {
                return SecQueryType.DATA_EXPORT;
            }
            if (normalized.contains(" LOG")) {
                return SecQueryType.MAINTAIN_LOG;
            }
            if (normalized.contains("DES_KEY_FILE")) {
                return SecQueryType.SYSTEM_SETTING_WRITE;
            }
            if (normalized.contains("STATUS") || normalized.contains("USER_RESOURCES") || normalized.contains("OPTIMIZER_COSTS") || normalized.contains("HOSTS")
                || normalized.contains("QUERY CACHE")) {
                return SecQueryType.ADMIN_PERFORMANCE;
            }
            if (normalized.contains("TABLE")) {
                return SecQueryType.ADMIN_TABLE;
            }
            if (normalized.contains("PRIVILEGES")) {
                return SecQueryType.SYSTEM_SETTING_WRITE;
            }
            return SecQueryType.ADMIN;
        }
        if (normalized.startsWith("KILL ")) {
            return SecQueryType.ADMIN;
        }
        if (normalized.startsWith("PURGE BINARY LOGS") || normalized.startsWith("PURGE MASTER LOGS") || normalized.startsWith("RESET BINARY LOGS")
            || normalized.startsWith("RESET MASTER")) {
            return SecQueryType.MAINTAIN_LOG;
        }
        if (normalized.startsWith("RESET QUERY CACHE")) {
            return SecQueryType.ADMIN_PERFORMANCE;
        }
        if (normalized.startsWith("CACHE INDEX") || normalized.startsWith("LOAD INDEX INTO CACHE")) {
            return SecQueryType.ADMIN_PERFORMANCE;
        }
        if (normalized.startsWith("CHECK TABLE")) {
            return SecQueryType.ADMIN_TABLE;
        }
        if (normalized.startsWith("HELP ")) {
            return SecQueryType.METADATA;
        }
        if (normalized.startsWith("CLONE INSTANCE") || normalized.startsWith("IMPORT TABLE")) {
            return SecQueryType.DATA_IMPORT;
        }
        if (normalized.startsWith("SET PASSWORD")) {
            return SecQueryType.ALTER_USER;
        }
        if (normalized.startsWith("SET DEFAULT ROLE")) {
            return SecQueryType.ALTER_USER;
        }
        if (normalized.startsWith("SET ROLE")) {
            return SecQueryType.SWITCH_ROLE;
        }
        if (normalized.startsWith("USE ")) {
            return SecQueryType.SWITCH_SCHEMA;
        }
        if (isScopedSetAssignment(normalized)) {
            return SecQueryType.SESSION_SETTING_WRITE;
        }
        if (normalized.startsWith("SET @@PERSIST") || normalized.startsWith("SET PERSIST")) {
            return SecQueryType.SYSTEM_SETTING_WRITE;
        }
        if (normalized.startsWith("INSTALL PLUGIN")) {
            return SecQueryType.CREATE_LIBRARY;
        }
        if (normalized.startsWith("UNINSTALL PLUGIN")) {
            return SecQueryType.DROP_LIBRARY;
        }
        if (normalized.startsWith("SIGNAL ") || normalized.startsWith("RESIGNAL")) {
            return SecQueryType.PROGRAM_CONTROL;
        }
        if (normalized.startsWith("RESTART") || normalized.startsWith("SHUTDOWN")) {
            return SecQueryType.UNSAFE;
        }
        if (normalized.startsWith("REVOKE ")) {
            return SecQueryType.REVOKE;
        }
        if (normalized.startsWith("GRANT ")) {
            return SecQueryType.GRANT;
        }
        if (normalized.startsWith("DROP USER")) {
            return SecQueryType.DROP_USER;
        }
        int createOrReplaceEnd = MyBehaviorText.afterStartingWords(normalized, "CREATE", "OR", "REPLACE");
        if (createOrReplaceEnd >= 0 && MyBehaviorText.findWord(normalized, createOrReplaceEnd, "VIEW") >= 0) {
            return SecQueryType.ALTER_VIEW;
        }
        if (normalized.startsWith("ALTER DATABASE") || normalized.startsWith("ALTER SCHEMA")) {
            return SecQueryType.ALTER_SCHEMA;
        }

        for (MySqlObjectReference reference : references) {
            if (reference.sqlType() != null && reference.sqlType() != SecQueryType.UNKNOWN) {
                return reference.sqlType();
            }
        }
        return SecQueryType.UNKNOWN;
    }

    private static boolean contains(List<MySqlObjectReference> references, SecQueryType type) {
        return references.stream().anyMatch(reference -> reference.sqlType() == type);
    }

    private static SecQueryType first(List<MySqlObjectReference> references, SecQueryType... candidates) {
        for (SecQueryType candidate : candidates) {
            if (contains(references, candidate)) {
                return candidate;
            }
        }
        return null;
    }

    private static boolean containsNode(MySqlObjectReference reference, String name) {
        return reference.nodes().stream().anyMatch(node -> node.equalsIgnoreCase(name));
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

    private static SecQueryType resolveShow(String normalized) {
        if (normalized.startsWith("SHOW MASTER STATUS") || normalized.startsWith("SHOW BINARY LOG") || normalized.startsWith("SHOW MASTER LOG")
            || normalized.startsWith("SHOW BINLOG") || normalized.startsWith("SHOW RELAYLOG") || isShowEngineCommand(normalized, "LOGS")) {
            return SecQueryType.LOG_READ;
        }
        if (normalized.startsWith("SHOW STATUS") || normalized.startsWith("SHOW GLOBAL STATUS") || normalized.startsWith("SHOW SESSION STATUS")
            || normalized.startsWith("SHOW LOCAL STATUS") || normalized.startsWith("SHOW WARNINGS") || normalized.startsWith("SHOW ERRORS") || normalized.startsWith("SHOW COUNT(")
            || normalized.startsWith("SHOW PROFILE") || normalized.startsWith("SHOW PROCESSLIST") || normalized.startsWith("SHOW FULL PROCESSLIST")
            || normalized.startsWith("SHOW OPEN TABLES") || normalized.startsWith("SHOW PARSE_TREE")
            || isShowEngineCommand(normalized, "STATUS", "MUTEX")) {
            return SecQueryType.PERFORMANCE;
        }
        return SecQueryType.METADATA;
    }

    private static int indexAfterPrefix(String sql, String prefix) {
        String upper = sql.toUpperCase(Locale.ROOT);
        int start = upper.indexOf(prefix);
        return start < 0 ? 0 : start + prefix.length();
    }

    private static int findWithWrite(String sql) {
        for (int i = 0; i < sql.length(); i++) {
            if (sql.charAt(i) != ')') {
                continue;
            }
            int start = MyBehaviorText.skipWhitespace(sql, i + 1);
            if (MyBehaviorText.startsWithWord(sql, start, "UPDATE") || MyBehaviorText.startsWithWord(sql, start, "DELETE")
                || MyBehaviorText.startsWithWord(sql, start, "INSERT") || MyBehaviorText.startsWithWord(sql, start, "REPLACE")) {
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
