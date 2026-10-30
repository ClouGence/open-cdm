/* Copyright 2026 杭州开云集致科技有限公司. Licensed under the Apache License, Version 2.0. */
package com.clougence.clouddm.ds.hologres.sql.parser;

import java.util.List;
import java.util.Locale;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.Trees;

import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.postgres.parser.antlr.PgSqlParser;

/** Permission procedures are installed in hologres, not in arbitrary user schemas. */
public final class HgPermissionCall {
    private HgPermissionCall(){
    }

    public static SplitQueryType type(PgSqlParser.CallstmtContext call) {
        var tokens = Trees.getDescendants(call.func_application().func_name())
            .stream()
            .filter(TerminalNode.class::isInstance)
            .map(TerminalNode.class::cast)
            .map(TerminalNode::getText)
            .toList();
        if (tokens.size() != 1 && tokens.size() != 3) {
            return null;
        }

        if (tokens.size() == 3 && (!".".equals(tokens.get(1)) || !"hologres".equals(identifier(tokens.get(0))))) {
            return null;
        }

        String normalized = identifier(tokens.get(tokens.size() - 1));
        return switch (normalized) {
            case "spm_grant", "slpm_grant" -> SplitQueryType.GRANT;
            case "spm_revoke", "slpm_revoke" -> SplitQueryType.REVOKE;
            case "spm_create_user", "slpm_create_user" -> SplitQueryType.CREATE_USER;
            case "spm_enable", "slpm_enable", "spm_disable", "slpm_disable", "spm_migrate", "slpm_migrate", "spm_cleanup", "slpm_cleanup" -> SplitQueryType.SYSTEM_SETTING_WRITE;
            case "apply_privileges" -> SplitQueryType.TRANSFER_PRIVILEGE;
            case "slpm_rename_schema" -> SplitQueryType.RENAME_SCHEMA;
            default -> null;
        };
    }

    private static String identifier(String text) {
        if (text.startsWith("\"")) {
            return text.substring(1, text.length() - 1).replace("\"\"", "\"");
        }
        return text.toLowerCase(Locale.ROOT);
    }

    public static List<PgSqlParser.Func_arg_exprContext> arguments(PgSqlParser.CallstmtContext call) {
        if (call.func_application().func_arg_list() == null) {
            return List.of();
        }
        return call.func_application().func_arg_list().func_arg_expr();
    }

    public static String literal(ParserRuleContext argument) {
        if (argument.getStart() != argument.getStop() || argument.getStart().getType() != PgSqlParser.StringConstant) {
            return null;
        }

        String text = argument.getText();
        return text.substring(1, text.length() - 1).replace("''", "'");
    }
}
