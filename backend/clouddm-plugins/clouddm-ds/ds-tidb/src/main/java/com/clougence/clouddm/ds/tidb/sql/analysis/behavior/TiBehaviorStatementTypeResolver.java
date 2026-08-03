/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.tidb.sql.analysis.behavior;

import java.util.List;
import java.util.Locale;

import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.clouddm.ds.tidb.sql.analysis.reference.TiDBObjectReference;
import com.clougence.utils.StringUtils;

final class TiBehaviorStatementTypeResolver {

    private TiBehaviorStatementTypeResolver(){
    }

    static SplitQueryType resolve(String sql, List<TiDBObjectReference> references) {
        String normalized = sql.stripLeading().toUpperCase(Locale.ROOT);
        if (normalized.startsWith("EXPLAIN ANALYZE ")) {
            int optionsStart = indexAfterPrefix(sql, "EXPLAIN ANALYZE");
            int statementStart = TiBehaviorText.findWord(sql, optionsStart, "SELECT", "WITH", "UPDATE", "DELETE", "INSERT", "REPLACE");
            return statementStart >= 0 ? resolve(sql.substring(statementStart), references) : SplitQueryType.PERFORMANCE;
        }
        if (normalized.startsWith("EXPLAIN")) {
            return SplitQueryType.PERFORMANCE;
        }
        if (normalized.startsWith("DESC ") || normalized.startsWith("DESCRIBE ")) {
            return SplitQueryType.METADATA;
        }
        if (normalized.startsWith("DO ") || normalized.startsWith("DO(")) {
            return SplitQueryType.BLOCK;
        }
        if (normalized.startsWith("TRACE ")) {
            return SplitQueryType.PERFORMANCE;
        }
        if (normalized.startsWith("WITH ")) {
            int writeStart = findWithWrite(normalized);
            if (writeStart >= 0) {
                int writeEnd = TiBehaviorText.wordEnd(normalized, writeStart);
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
        if (normalized.startsWith("(") && normalized.contains("SELECT")) {
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
            List<TiDBObjectReference> dataObjects = references.stream()
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
            || normalized.startsWith("RELEASE SAVEPOINT") || normalized.startsWith("XA ") || normalized.startsWith("BEGIN")
            || TiBehaviorText.afterStartingWords(normalized, "SET", "TRANSACTION") >= 0 || TiBehaviorText.afterStartingWords(normalized, "SET", "SESSION", "TRANSACTION") >= 0
            || TiBehaviorText.afterStartingWords(normalized, "SET", "GLOBAL", "TRANSACTION") >= 0) {
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
        if (normalized.startsWith("DISTRIBUTE TABLE")) {
            return SplitQueryType.ADMIN_TABLE;
        }
        if (normalized.startsWith("CANCEL DISTRIBUTION JOB") || normalized.startsWith("ADMIN CANCEL DDL JOB")
            || normalized.matches("(?s)(DROP|PAUSE|RESUME)\\s+LOAD\\s+DATA\\s+JOB\\b.*")) {
            return SplitQueryType.ADMIN_JOB;
        }
        if (normalized.startsWith("ADMIN CREATE WORKLOAD SNAPSHOT")) {
            return SplitQueryType.ADMIN_PERFORMANCE;
        }
        if (normalized.startsWith("DROP STATS") || normalized.startsWith("DROP STATISTICS") || normalized.startsWith("LOCK STATS") || normalized.startsWith("UNLOCK STATS")
            || normalized.startsWith("ADMIN RELOAD STATISTICS") || normalized.startsWith("ADMIN RELOAD STATS_EXTENDED")) {
            return SplitQueryType.ADMIN_PERFORMANCE;
        }
        if (normalized.startsWith("LOAD STATS")) {
            return SplitQueryType.DATA_IMPORT;
        }
        if (normalized.startsWith("ADMIN CAPTURE BINDINGS") || normalized.startsWith("ADMIN EVOLVE BINDINGS") || normalized.startsWith("ADMIN RELOAD BINDINGS")
            || normalized.startsWith("ADMIN FLUSH BINDINGS") || normalized.startsWith("ADMIN FLUSH GLOBAL PLAN_CACHE") || normalized.startsWith("ADMIN FLUSH SESSION PLAN_CACHE")
            || normalized.startsWith("ADMIN FLUSH INSTANCE PLAN_CACHE")) {
            return SplitQueryType.ADMIN_PERFORMANCE;
        }
        if (normalized.startsWith("ADMIN PLUGINS ") || normalized.startsWith("ADMIN RELOAD EXPR_PUSHDOWN_BLACKLIST") || normalized.startsWith("ADMIN RELOAD OPT_RULE_BLACKLIST")
            || normalized.startsWith("ADMIN RESET TELEMETRY_ID")) {
            return SplitQueryType.ADMIN;
        }
        if (normalized.startsWith("HELP ")) {
            return SplitQueryType.METADATA;
        }
        if (normalized.startsWith("CREATE PLACEMENT POLICY") || normalized.startsWith("CREATE OR REPLACE PLACEMENT POLICY")) {
            return SplitQueryType.CREATE_POLICY;
        }
        if (normalized.startsWith("ALTER PLACEMENT POLICY") || normalized.startsWith("ALTER RANGE ")) {
            return SplitQueryType.ALTER_POLICY;
        }
        if (normalized.startsWith("DROP PLACEMENT POLICY")) {
            return SplitQueryType.DROP_POLICY;
        }
        if (normalized.startsWith("BACKUP ") || normalized.startsWith("TRAFFIC CAPTURE ")) {
            return SplitQueryType.DATA_EXPORT;
        }
        if (normalized.startsWith("RESTORE ") || normalized.startsWith("TRAFFIC REPLAY ")) {
            return SplitQueryType.DATA_IMPORT;
        }
        if (normalized.startsWith("FLASHBACK ")) {
            return normalized.startsWith("FLASHBACK TABLE") ? SplitQueryType.ADMIN_TABLE : SplitQueryType.ADMIN;
        }
        if (normalized.startsWith("INDEX ADVISE ") || normalized.startsWith("RECOMMEND INDEX ") || normalized.startsWith("CALIBRATE RESOURCE")) {
            return SplitQueryType.ADMIN_PERFORMANCE;
        }
        if (normalized.startsWith("ADMIN SHOW SLOW")) {
            return SplitQueryType.ADMIN_PERFORMANCE;
        }
        if (normalized.startsWith("ADMIN SHOW DDL") || normalized.startsWith("RECOVER TABLE BY JOB")) {
            return SplitQueryType.ADMIN_JOB;
        }
        if (normalized.startsWith("ADMIN SHOW ") && normalized.contains("NEXT_ROW_ID")) {
            return SplitQueryType.ADMIN_TABLE;
        }
        if (normalized.startsWith("ADMIN SHOW TELEMETRY")) {
            return SplitQueryType.ADMIN_PERFORMANCE;
        }
        if (normalized.startsWith("ADMIN CHECK INDEX") || normalized.startsWith("ADMIN CLEANUP INDEX") || normalized.startsWith("ADMIN RECOVER INDEX")) {
            return SplitQueryType.ADMIN_TABLE;
        }
        if (normalized.startsWith("CREATE IMPORT ")) {
            return SplitQueryType.CREATE_JOB;
        }
        if (normalized.startsWith("ALTER IMPORT ")) {
            return SplitQueryType.ALTER_JOB;
        }
        if (normalized.startsWith("DROP IMPORT ")) {
            return SplitQueryType.DROP_JOB;
        }
        if (normalized.startsWith("RESUME IMPORT ") || normalized.startsWith("STOP IMPORT ") || normalized.startsWith("PURGE IMPORT ") || normalized.startsWith("CANCEL IMPORT JOB")
            || normalized.startsWith("SHOW IMPORT") || normalized.startsWith("SHOW CREATE IMPORT") || normalized.startsWith("ADMIN ALTER DDL JOBS")
            || normalized.startsWith("ADMIN PAUSE DDL JOBS") || normalized.startsWith("ADMIN RESUME DDL JOBS") || normalized.startsWith("SHOW TRAFFIC JOBS")
            || normalized.startsWith("CANCEL TRAFFIC JOBS") || normalized.startsWith("CANCEL BR JOB")) {
            return SplitQueryType.ADMIN_JOB;
        }
        if (normalized.startsWith("CHANGE PUMP ") || normalized.startsWith("CHANGE DRAINER ")) {
            return SplitQueryType.ALTER_REPLICATION;
        }
        if (normalized.startsWith("ADMIN SET BDR ROLE") || normalized.startsWith("ADMIN UNSET BDR ROLE") || normalized.startsWith("ADMIN SHOW BDR ROLE")) {
            return SplitQueryType.ADMIN_REPLICATION;
        }
        if (normalized.startsWith("SET CONFIG ")) {
            return SplitQueryType.SYSTEM_SETTING_WRITE;
        }
        if (normalized.startsWith("STOP BACKUP LOGS") || normalized.startsWith("PAUSE BACKUP LOGS") || normalized.startsWith("RESUME BACKUP LOGS")
            || normalized.startsWith("PURGE BACKUP LOGS")) {
            return SplitQueryType.MAINTAIN_LOG;
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
        if (normalized.startsWith("SET BINDING") || normalized.startsWith("CREATE GLOBAL BINDING") || normalized.startsWith("CREATE SESSION BINDING")
            || normalized.startsWith("CREATE BINDING") || normalized.startsWith("DROP GLOBAL BINDING") || normalized.startsWith("DROP SESSION BINDING")
            || normalized.startsWith("DROP BINDING") || normalized.startsWith("PLAN REPLAYER")) {
            return SplitQueryType.ADMIN_PERFORMANCE;
        }
        if (normalized.startsWith("SET ROLE")) {
            return SplitQueryType.SWITCH_ROLE;
        }
        if (normalized.startsWith("SET RESOURCE GROUP")) {
            return SplitQueryType.ADMIN_RESOURCE_GROUP;
        }
        if (normalized.startsWith("SET SESSION_STATES")) {
            return SplitQueryType.SESSION_SETTING_WRITE;
        }
        if (normalized.startsWith("USE ")) {
            return SplitQueryType.SWITCH_SCHEMA;
        }
        if (normalized.startsWith("SET ") && contains(references, SplitQueryType.ALTER_REPLICATION)) {
            return SplitQueryType.ALTER_REPLICATION;
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
        if (normalized.startsWith("CREATE TABLE")) {
            return SplitQueryType.CREATE_TABLE;
        }
        int createOrReplaceEnd = TiBehaviorText.afterStartingWords(normalized, "CREATE", "OR", "REPLACE");
        if (createOrReplaceEnd >= 0 && TiBehaviorText.findWord(normalized, createOrReplaceEnd, "VIEW") >= 0) {
            return SplitQueryType.ALTER_VIEW;
        }
        if (normalized.startsWith("ALTER DATABASE") || normalized.startsWith("ALTER SCHEMA")) {
            return SplitQueryType.ALTER_SCHEMA;
        }

        for (TiDBObjectReference reference : references) {
            if (reference.sqlType() != null && reference.sqlType() != SplitQueryType.UNKNOWN) {
                return reference.sqlType();
            }
        }
        return SplitQueryType.UNKNOWN;
    }

    private static boolean contains(List<TiDBObjectReference> references, SplitQueryType type) {
        return references.stream().anyMatch(reference -> reference.sqlType() == type);
    }

    private static SplitQueryType first(List<TiDBObjectReference> references, SplitQueryType... candidates) {
        for (SplitQueryType candidate : candidates) {
            if (contains(references, candidate)) {
                return candidate;
            }
        }
        return null;
    }

    private static boolean containsNode(TiDBObjectReference reference, String name) {
        return reference.nodes().stream().anyMatch(node -> StringUtils.equalsIgnoreCase(node, name));
    }

    private static boolean containsNode(List<TiDBObjectReference> references, String name) {
        return references.stream().anyMatch(reference -> containsNode(reference, name));
    }

    private static boolean isSelectExpression(String normalized) {
        if (normalized.startsWith("SELECT") || normalized.startsWith("TABLE ") || normalized.startsWith("VALUES ")) {
            return true;
        }
        int index = 0;
        while (index < normalized.length() && normalized.charAt(index) == '(') {
            index = TiBehaviorText.skipWhitespace(normalized, index + 1);
        }
        return index > 0 && (TiBehaviorText.startsWithWord(normalized, index, "SELECT") || TiBehaviorText.startsWithWord(normalized, index, "TABLE")
                             || TiBehaviorText.startsWithWord(normalized, index, "VALUES"));
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
            int start = TiBehaviorText.skipWhitespace(sql, i + 1);
            if (TiBehaviorText.startsWithWord(sql, start, "UPDATE") || TiBehaviorText.startsWithWord(sql, start, "DELETE") || TiBehaviorText.startsWithWord(sql, start, "INSERT")
                || TiBehaviorText.startsWithWord(sql, start, "REPLACE")) {
                return start;
            }
        }
        return -1;
    }

    private static boolean isScopedSetAssignment(String sql) {
        int scopeStart = TiBehaviorText.afterStartingWords(sql, "SET");
        if (scopeStart < 0) {
            return false;
        }
        int scope = TiBehaviorText.skipWhitespace(sql, scopeStart);
        if (scope == scopeStart) {
            return false;
        }
        String[] names = { "GLOBAL", "LOCAL", "SESSION", "PERSIST_ONLY" };
        for (String name : names) {
            if (TiBehaviorText.startsWithWord(sql, scope, name)) {
                int equals = TiBehaviorText.skipWhitespace(sql, scope + name.length());
                return equals < sql.length() && sql.charAt(equals) == '=';
            }
        }
        return false;
    }

    private static boolean isShowEngineCommand(String sql, String... commands) {
        int engineEnd = TiBehaviorText.afterStartingWords(sql, "SHOW", "ENGINE");
        if (engineEnd < 0) {
            return false;
        }
        int engineName = TiBehaviorText.skipWhitespace(sql, engineEnd);
        if (engineName == engineEnd || engineName >= sql.length()) {
            return false;
        }
        int engineNameEnd = engineName;
        while (engineNameEnd < sql.length() && !Character.isWhitespace(sql.charAt(engineNameEnd))) {
            engineNameEnd++;
        }
        int commandStart = TiBehaviorText.skipWhitespace(sql, engineNameEnd);
        if (commandStart == engineNameEnd) {
            return false;
        }
        for (String command : commands) {
            if (TiBehaviorText.startsWithWord(sql, commandStart, command)) {
                return true;
            }
        }
        return false;
    }
}
