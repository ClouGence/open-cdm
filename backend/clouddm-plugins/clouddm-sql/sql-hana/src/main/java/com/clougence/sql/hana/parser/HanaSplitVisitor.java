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

        List<String> words = statementWords(context);
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
            case "IMPORT" -> SplitQueryType.DATA_IMPORT;
            case "EXPORT" -> SplitQueryType.DATA_EXPORT;
            default -> SplitQueryType.UNKNOWN;
        };
    }

    SplitQueryType additionalType(HanaParser.SplitStatementContext context) {
        List<String> words = statementWords(context);
        // Service shutdown and removal of all licenses can make the database unavailable.
        if (startsWith(words, "ALTER", "SYSTEM", "STOP", "SERVICE") || startsWith(words, "UNSET", "SYSTEM", "LICENSE", "ALL")) {
            return SplitQueryType.UNSAFE;
        }
        return null;
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
            }
        }
        return words;
    }

    private SplitQueryType ddlType(List<String> words) {
        if (startsWith(words, "ALTER", "SYSTEM")) {
            return alterSystemType(words.subList(2, words.size()));
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

    private boolean startsWith(List<String> words, String... prefix) {
        return words.size() >= prefix.length && words.subList(0, prefix.length).equals(List.of(prefix));
    }
}
