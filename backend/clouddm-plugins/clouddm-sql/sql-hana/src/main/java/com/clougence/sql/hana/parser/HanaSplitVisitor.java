/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.sql.hana.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
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
        return collectTypes(context).iterator().next();
    }

    Set<SplitQueryType> collectTypes(HanaParser.SplitStatementContext context) {
        Set<SplitQueryType> types = new LinkedHashSet<>();
        if (context.splitItem(0).splitBlock() != null) {
            types.add(SplitQueryType.BLOCK);
            return types;
        }

        List<String> words = statementWords(context);
        SplitQueryType primary = statementType(words);
        types.add(primary);
        collectDatabaseTypes(words, types);
        if (startsWith(words, "RECOVER")) {
            collectRecoveryTypes(words, types);
        }

        if (startsWith(words, "BACKUP", "ENCRYPTION", "ROOT", "KEYS")) {
            types.add(SplitQueryType.ADMIN);
        }

        if (startsWith(words, "BACKUP", "CATALOG", "DELETE")) {
            types.add(SplitQueryType.UNSAFE);
        }

        if (primary == SplitQueryType.ADMIN_TABLE) {
            int nameIndex = 1;
            if (startsWith(words, "MERGE", "DELTA", "OF")) {
                nameIndex = 3;
            } else if (startsWith(words, "MERGE", "HISTORY", "DELTA", "OF")) {
                nameIndex = 4;
            }
            List<String> options = tableOptions(words, nameIndex);
            if (startsWith(options, "PART") || startsWith(options, "PARTITION")) {
                types.add(SplitQueryType.ADMIN_PARTITION);
            }
        }

        if (startsWith(words, "TRUNCATE", "TABLE") && startsWith(tableOptions(words, 2), "PARTITION")) {
            types.add(SplitQueryType.TRUNCATE_PARTITION);
        }

        if (startsWith(words, "ALTER", "SYSTEM", "LOAD", "PERFTRACE")) {
            types.add(SplitQueryType.CREATE_TABLE);
            int tablesIndex = Collections.indexOfSubList(words, List.of("INTO", "TABLES"));
            // Only the clause after the qualified output prefix replaces tables.
            if (tablesIndex >= 0 && startsWith(tableOptions(words, tablesIndex + 2), "WITH", "REPLACE")) {
                types.add(SplitQueryType.DROP_TABLE);
            }
        }

        return types;
    }

    private SplitQueryType statementType(List<String> words) {
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
            case "SELECT", "INSERT", "UPDATE", "DELETE", "GRANT", "REVOKE" -> SplitQueryType.valueOf(first);
            case "CREATE", "ALTER", "DROP", "RENAME", "COMMENT" -> ddlType(words);
            case "MERGE" -> {
                if (startsWith(words, "MERGE", "DELTA", "OF") || startsWith(words, "MERGE", "HISTORY", "DELTA", "OF")) {
                    yield SplitQueryType.ADMIN_TABLE;
                }
                yield SplitQueryType.MERGE;
            }
            case "LOAD", "UNLOAD" -> SplitQueryType.ADMIN_TABLE;
            case "BACKUP" -> backupType(words);
            case "RECOVER" -> {
                if (startsWith(words, "RECOVER", "DATA") || startsWith(words, "RECOVER", "DATABASE") || startsWith(words, "RECOVER", "ENCRYPTION", "ROOT", "KEYS")) {
                    yield SplitQueryType.DATA_IMPORT;
                }
                yield SplitQueryType.UNKNOWN;
            }
            case "TRUNCATE" -> SplitQueryType.TRUNCATE_TABLE;
            case "CALL" -> SplitQueryType.CALL_PROG_OBJ;
            case "CONNECT" -> SplitQueryType.SWITCH_USER;
            case "VALIDATE" -> {
                if (startsWith(words, "VALIDATE", "USER") || startsWith(words, "VALIDATE", "LDAP", "PROVIDER")) {
                    yield SplitQueryType.ADMIN;
                }
                yield SplitQueryType.UNKNOWN;
            }
            case "DO" -> SplitQueryType.BLOCK;
            case "COMMIT", "ROLLBACK", "SAVEPOINT" -> SplitQueryType.TRANSACTION;
            case "SET", "UNSET" -> settingType(words);
            case "EXPLAIN" -> SplitQueryType.PERFORMANCE;
            case "REFRESH" -> {
                if (startsWith(words, "REFRESH", "STATISTICS")) {
                    yield SplitQueryType.ADMIN_PERFORMANCE;
                }
                yield SplitQueryType.UNKNOWN;
            }
            case "IMPORT" -> {
                if (startsWith(words, "IMPORT", "SCAN")) {
                    yield SplitQueryType.METADATA;
                }
                yield SplitQueryType.DATA_IMPORT;
            }
            case "EXPORT" -> SplitQueryType.DATA_EXPORT;
            default -> SplitQueryType.UNKNOWN;
        };
    }

    private void collectDatabaseTypes(List<String> words, Set<SplitQueryType> types) {
        if (startsWith(words, "CREATE", "DATABASE") && words.size() > 3 && Collections.indexOfSubList(words.subList(3, words.size()), List.of("AS", "REPLICA", "OF")) >= 0) {
            types.add(SplitQueryType.CREATE_REPLICATION);
        }

        if (startsWith(words, "DROP", "DATABASE") && words.size() > 3 && startsWith(words.subList(3, words.size()), "DROP", "BACKUPS")) {
            types.add(SplitQueryType.ADMIN);
            types.add(SplitQueryType.UNSAFE);
        }

        if (startsWith(words, "ALTER", "DATABASE") && words.size() > 3) {
            List<String> command = words.subList(3, words.size());
            if (startsWith(command, "FINALIZE", "REPLICA")) {
                if (startsWith(command, "FINALIZE", "REPLICA", "DROP", "SOURCE", "DATABASE")) {
                    types.add(SplitQueryType.DROP_CATALOG);
                }
                types.add(SplitQueryType.UNSAFE);
            }
        }

        // Removing live replication state, stopping a database, or rewinding it affects availability/recovery.
        if (startsWith(words, "ALTER", "SYSTEM", "STOP", "SERVICE") || startsWith(words, "ALTER", "SYSTEM", "STOP", "DATABASE")
            || startsWith(words, "UNSET", "SYSTEM", "LICENSE", "ALL") || startsWith(words, "ALTER", "SYSTEM", "LOGGING", "OFF")
            || startsWith(words, "ALTER", "SYSTEM", "DISABLE", "SYSTEM", "REPLICATION") || startsWith(words, "ALTER", "SYSTEM", "UNREGISTER", "SYSTEM", "REPLICATION", "SITE")
            || (startsWith(words, "ALTER", "SYSTEM", "START", "DATABASE") && words.size() > 5 && startsWith(words.subList(5, words.size()), "FROM", "FALLBACK", "SNAPSHOT"))) {
            types.add(SplitQueryType.UNSAFE);
        }
    }

    private void collectRecoveryTypes(List<String> words, Set<SplitQueryType> types) {
        if (startsWith(words, "RECOVER", "ENCRYPTION", "ROOT", "KEYS")) {
            types.add(SplitQueryType.SYSTEM_SETTING_WRITE);
            // Without AND SETTINGS, recovered root keys are added rather than replacing the secure store.
            if (startsWith(words, "RECOVER", "ENCRYPTION", "ROOT", "KEYS", "AND", "SETTINGS")) {
                types.add(SplitQueryType.UNSAFE);
            }
        } else if (startsWith(words, "RECOVER", "DATA") || startsWith(words, "RECOVER", "DATABASE")) {
            // Only outer clauses count; backup paths, password lists and comments are not executable SQL.
            for (int index = 2; index < words.size(); index++) {
                List<String> clause = words.subList(index, words.size());
                if (startsWith(clause, "CLEAR", "LOG")) {
                    types.add(SplitQueryType.MAINTAIN_LOG);
                }
                if (startsWith(clause, "SET", "LICENSE") || startsWith(clause, "INCLUDE", "CONFIGURATION")) {
                    types.add(SplitQueryType.SYSTEM_SETTING_WRITE);
                }
            }
            types.add(SplitQueryType.UNSAFE);
        }
    }

    private List<String> tableOptions(List<String> words, int nameIndex) {
        int index = Math.min(nameIndex + 1, words.size());
        // A keyword-shaped schema/table name is not a PART/PARTITION clause.
        while (index + 1 < words.size() && ".".equals(words.get(index))) {
            index += 2;
        }
        return words.subList(index, words.size());
    }

    private SplitQueryType backupType(List<String> words) {
        if (startsWith(words, "BACKUP", "LIST", "DATA") || startsWith(words, "BACKUP", "COMPLETE", "LIST", "DATA")) {
            return SplitQueryType.METADATA;
        }
        if (startsWith(words, "BACKUP", "DATA", "FOR", "FULL", "SYSTEM") || startsWith(words, "BACKUP", "CATALOG") || startsWith(words, "BACKUP", "CANCEL")
            || startsWith(words, "BACKUP", "CHECK")) {
            return SplitQueryType.ADMIN;
        }
        if (startsWith(words, "BACKUP", "DATA") || startsWith(words, "BACKUP", "ENCRYPTION", "ROOT", "KEYS")) {
            return SplitQueryType.DATA_EXPORT;
        }
        return SplitQueryType.UNKNOWN;
    }

    private List<String> statementWords(HanaParser.SplitStatementContext context) {
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
            } else {
                words.add("");
            }
        }
        return words;
    }

    private SplitQueryType ddlType(List<String> words) {
        if (startsWith(words, "ALTER", "SYSTEM")) {
            return alterSystemType(words.subList(2, words.size()));
        }
        if (startsWith(words, "CREATE", "STATISTICS") || startsWith(words, "ALTER", "STATISTICS") || startsWith(words, "DROP", "STATISTICS")) {
            return SplitQueryType.ADMIN_PERFORMANCE;
        }

        if (startsWith(words, "ALTER", "DATABASE") && words.size() > 3) {
            List<String> command = words.subList(3, words.size());
            if (startsWith(command, "FINALIZE", "REPLICA")) {
                return SplitQueryType.ALTER_REPLICATION;
            }
            if (startsWith(command, "CANCEL", "REPLICA")) {
                return SplitQueryType.ADMIN_REPLICATION;
            }
            if (startsWith(command, "CREATE", "FALLBACK", "SNAPSHOT") || startsWith(command, "DROP", "FALLBACK", "SNAPSHOT")) {
                return SplitQueryType.ADMIN;
            }
        }

        if (startsWith(words, "CREATE", "RESTRICTED", "USER")) {
            return SplitQueryType.CREATE_USER;
        }
        if (startsWith(words, "ALTER", "USER") && words.size() > 3) {
            // These clauses change privileges directly, rather than user attributes.
            String action = words.get(3);
            List<String> privilege = words.subList(4, words.size());
            if (("GRANT".equals(action) || "REVOKE".equals(action)) && (startsWith(privilege, "CREATE", "ANY", "ON", "OWN", "SCHEMA") || startsWith(privilege, "ROLE", "PUBLIC"))) {
                return SplitQueryType.valueOf(action);
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
        String action = words.get(0);
        if ("CREATE".equals(action) || "ALTER".equals(action) || "DROP".equals(action)) {
            List<String> target = words.subList(index, words.size());
            if (startsWith(target, "AUDIT", "POLICY") || startsWith(target, "STRUCTURED", "PRIVILEGE") || startsWith(target, "WORKLOAD", "MAPPING")) {
                return SplitQueryType.valueOf(action + "_POLICY");
            }
            if (startsWith(target, "WORKLOAD", "CLASS")) {
                return SplitQueryType.valueOf(action + "_RESOURCE_GROUP");
            }
            if (Set.of("USERGROUP", "CREDENTIAL", "CERTIFICATE", "PSE").contains(object)
                || (Set.of("JWT", "SAML", "LDAP", "X509").contains(object) && startsWith(target.subList(1, target.size()), "PROVIDER")) || startsWith(target, "PUBLIC", "KEY")
                || startsWith(target, "CLIENTSIDE", "ENCRYPTION", "KEYPAIR") || startsWith(target, "CLIENTSIDE", "ENCRYPTION", "COLUMN", "KEY")) {
                return SplitQueryType.SYSTEM_SETTING_WRITE;
            }
        }

        if ("DATABASE".equals(object)) {
            object = "CATALOG";
        }
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
        if (startsWith(words, "SET", "SYSTEM", "LICENSE") || startsWith(words, "UNSET", "SYSTEM", "LICENSE", "ALL")) {
            return SplitQueryType.SYSTEM_SETTING_WRITE;
        }
        if (words.size() > 1) {
            if ("PSE".equals(words.get(1))) {
                return SplitQueryType.SYSTEM_SETTING_WRITE;
            }
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

    private SplitQueryType alterSystemType(List<String> command) {
        SplitQueryType diagnostic = diagnosticSystemType(command);
        if (diagnostic != SplitQueryType.UNKNOWN) {
            return diagnostic;
        }
        if (startsWith(command, "LOGGING")) {
            return SplitQueryType.ADMIN_LOG;
        }
        if (startsWith(command, "RECLAIM", "LOG")) {
            return SplitQueryType.MAINTAIN_LOG;
        }
        if (startsWith(command, "SAVEPOINT") || startsWith(command, "START", "DATABASE") || startsWith(command, "STOP", "DATABASE")
            || startsWith(command, "RECLAIM", "DATA", "SPACE") || startsWith(command, "RECLAIM", "ROW", "DATA", "SPACE") || startsWith(command, "RECLAIM", "DATAVOLUME")
            || startsWith(command, "RECLAIM", "FLEXIBLE", "TABLES") || startsWith(command, "RECLAIM", "LOB", "SPACE") || startsWith(command, "RECLAIM", "ROW", "LOB", "SPACE")
            || startsWith(command, "RECLAIM", "COLUMN", "LOB", "SPACE") || startsWith(command, "RECLAIM", "VERSION", "SPACE")) {
            return SplitQueryType.ADMIN;
        }
        if (startsWith(command, "ALTER", "DATAVOLUME", "ADD", "PARTITION") || startsWith(command, "ALTER", "DATAVOLUME", "DROP", "PARTITION")) {
            return SplitQueryType.SYSTEM_SETTING_WRITE;
        }
        if (startsWith(command, "REGISTER", "SYSTEM", "REPLICATION", "SITE")) {
            return SplitQueryType.CREATE_REPLICATION;
        }
        if (startsWith(command, "UNREGISTER", "SYSTEM", "REPLICATION", "SITE")) {
            return SplitQueryType.DROP_REPLICATION;
        }
        if (startsWith(command, "ENABLE") || startsWith(command, "DISABLE")) {
            List<String> target = command.subList(1, command.size());
            if (startsWith(target, "SYSTEM", "REPLICATION") || startsWith(target, "ALL", "TABLE", "REPLICAS") || startsWith(target, "ALL", "ASYNCHRONOUS", "TABLE", "REPLICAS")
                || startsWith(target, "ALL", "SYNCHRONOUS", "TABLE", "REPLICAS")) {
                return SplitQueryType.ALTER_REPLICATION;
            }
        }
        // Explicit session targets are runtime administration, not system configuration.
        if (startsWith(command, "ALTER", "SESSION") || startsWith(command, "DISCONNECT", "SESSION") || startsWith(command, "CANCEL", "SESSION")
            || startsWith(command, "CANCEL", "WORK", "IN", "SESSION")) {
            return SplitQueryType.ADMIN;
        }
        if (startsWith(command, "CLEAR", "INIFILE", "CONTENT", "HISTORY") || startsWith(command, "STOP", "SERVICE")) {
            return SplitQueryType.ADMIN;
        }
        if (startsWith(command, "ALTER", "CONFIGURATION") || startsWith(command, "RECONFIGURE", "SERVICE") || startsWith(command, "ALTER", "TABLE", "PLACEMENT")) {
            return SplitQueryType.SYSTEM_SETTING_WRITE;
        }
        if (startsWith(command, "ALTER", "AUDIT", "LOG")) {
            return SplitQueryType.ALTER_LOG;
        }
        if (startsWith(command, "CLEAR", "AUDIT", "LOG")) {
            return SplitQueryType.MAINTAIN_LOG;
        }
        if (startsWith(command, "VALIDATE", "ENCRYPTION", "ROOT", "KEYS", "BACKUP", "PASSWORD") || startsWith(command, "VALIDATE", "LSS", "AUTO", "BACKUP", "PASSWORD")) {
            return SplitQueryType.ADMIN;
        }
        if (startsWith(command, "APPLICATION", "ENCRYPTION") || startsWith(command, "BACKUP", "ENCRYPTION") || startsWith(command, "LOG", "ENCRYPTION")
            || startsWith(command, "PERSISTENCE", "ENCRYPTION") || startsWith(command, "ENCRYPTION", "CONFIGURATION") || startsWith(command, "CLIENTPKI")
            || startsWith(command, "SET", "ENCRYPTION", "ROOT", "KEYS", "BACKUP", "PASSWORD") || startsWith(command, "SET", "LSS", "AUTO", "BACKUP", "PASSWORD")
            || startsWith(command, "ADD", "KEY", "MANAGEMENT", "CONFIGURATION") || startsWith(command, "ACTIVATE", "KEY", "MANAGEMENT", "CONFIGURATION")
            || startsWith(command, "ALTER", "KEY", "MANAGEMENT", "CONFIGURATION") || startsWith(command, "DROP", "KEY", "MANAGEMENT", "CONFIGURATION")) {
            return SplitQueryType.SYSTEM_SETTING_WRITE;
        }
        return SplitQueryType.UNKNOWN;
    }

    private SplitQueryType diagnosticSystemType(List<String> command) {
        if (command.isEmpty()) {
            return SplitQueryType.UNKNOWN;
        }
        String action = command.get(0);
        List<String> target = command.subList(1, command.size());
        // Match the administrative command, never keywords inside its target SQL or options.
        if (Set.of("ADD", "ALTER", "REMOVE", "ENABLE", "DISABLE").contains(action) && startsWith(target, "STATEMENT", "HINT")) {
            return SplitQueryType.ADMIN_PERFORMANCE;
        }
        if ((Set.of("ADD", "REMOVE").contains(action) && startsWith(target, "ABSTRACT", "SQL", "PLAN", "FILTER"))
            || (Set.of("ENABLE", "DISABLE", "REMOVE").contains(action) && startsWith(target, "ABSTRACT", "SQL", "PLAN", "ENTRY"))
            || (Set.of("MIGRATE", "UPDATE").contains(action) && startsWith(target, "ABSTRACT", "SQL", "PLAN"))) {
            return SplitQueryType.ADMIN_PERFORMANCE;
        }
        if (Set.of("START", "STOP").contains(action)) {
            if (startsWith(target, "APPLY", "ABSTRACT", "SQL", "PLAN") || startsWith(target, "CAPTURE", "ABSTRACT", "SQL", "PLAN")
                || startsWith(target, "SQLSCRIPT", "CODE", "COVERAGE")) {
                return SplitQueryType.ADMIN_PERFORMANCE;
            }
            if (startsWith(target, "PERFTRACE")) {
                return SplitQueryType.ADMIN_LOG;
            }
        }
        if ((Set.of("START", "STOP", "SAVE", "CLEAR").contains(action) && startsWith(target, "KERNEL", "PROFILER"))
            || (Set.of("START", "STOP", "CLEAR").contains(action) && startsWith(target, "SQLSCRIPT", "PLAN", "PROFILER"))) {
            return SplitQueryType.ADMIN_PERFORMANCE;
        }
        if ("CLEAR".equals(action) && (startsWith(target, "CACHE") || startsWith(target, "COLUMN", "JOIN", "DATA", "STATISTICS") || startsWith(target, "TIMEZONE", "CACHE")
                                       || startsWith(target, "SQL", "PLAN", "CACHE"))) {
            return SplitQueryType.ADMIN_PERFORMANCE;
        }
        if (Set.of("CLEAR", "REFRESH", "REMOVE").contains(action)
            && (startsWith(target, "RESULT", "CACHE") || startsWith(target, "STATIC", "RESULT", "CACHE") || startsWith(target, "DYNAMIC", "RESULT", "CACHE"))) {
            return SplitQueryType.ADMIN_PERFORMANCE;
        }
        if ((Set.of("PIN", "UNPIN", "RECOMPILE").contains(action) && startsWith(target, "SQL", "PLAN", "CACHE", "ENTRY")) || startsWith(command, "REMOVE", "SQL", "PLAN", "CACHE")
            || startsWith(command, "RESET", "MONITORING", "VIEW")) {
            return SplitQueryType.ADMIN_PERFORMANCE;
        }
        if (startsWith(command, "CREATE", "RUNTIMEDUMP") || startsWith(command, "CREATE", "WAITGRAPH")) {
            return SplitQueryType.PERFORMANCE;
        }
        if ((Set.of("CLEAR", "REMOVE").contains(action) && startsWith(target, "TRACES")) || startsWith(command, "SAVE", "PERFTRACE")) {
            return SplitQueryType.MAINTAIN_LOG;
        }
        if (startsWith(command, "LOAD", "PERFTRACE")) {
            return SplitQueryType.DATA_IMPORT;
        }
        return SplitQueryType.UNKNOWN;
    }

    private boolean startsWith(List<String> words, String... prefix) {
        return words.size() >= prefix.length && words.subList(0, prefix.length).equals(List.of(prefix));
    }
}
