/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.clouddm.ds.tidb.sql.analysis.behavior;

import java.util.*;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParser;
import com.clougence.clouddm.ds.tidb.sql.parser.antlr.TiDBParser.*;
import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.clouddm.ds.tidb.sql.analysis.reference.TiDBResourceRegistry;
import com.clougence.clouddm.ds.tidb.sql.parser.TiDBVersion;
import com.clougence.utils.StringUtils;

/**
 * Adds behavior-only object facts that must not alter legacy resource analysis.
 */
final class TiBehaviorObjectReferenceVisitor extends TiDBObjectReferenceVisitor {

    private final Parser                parser;
    private final TiDBVersion          version;
    private final int                   exactVersion;
    private final TiDBResourceRegistry resources;
    private final Set<String>           cteNames = new HashSet<>();

    TiBehaviorObjectReferenceVisitor(Parser parser, Map<UmiTypes, Object> levelsParam, int baseLine, int baseColumn, TiDBVersion version, int exactVersion,
                                     TiDBResourceRegistry resources){
        super(parser, levelsParam, baseLine, baseColumn, version, exactVersion, resources);
        this.parser = parser;
        this.version = version;
        this.exactVersion = exactVersion;
        this.resources = resources;
    }

    void prepareStatement(ParserRuleContext statement) {
        collectCteNames(statement);
    }

    @Override
    public Void visitCreateSequence(CreateSequenceContext ctx) {
        add(SplitQueryType.CREATE_SEQUENCE, TargetType.Sequence, false, ctx.tableName());
        return null;
    }

    @Override
    public Void visitAlterSequence(AlterSequenceContext ctx) {
        add(SplitQueryType.ALTER_SEQUENCE, TargetType.Sequence, true, ctx.tableName());
        return null;
    }

    @Override
    public Void visitDropSequence(DropSequenceContext ctx) {
        boolean require = ctx.ifExists() == null;
        ctx.tableName().forEach(sequence -> add(SplitQueryType.DROP_SEQUENCE, TargetType.Sequence, require, sequence));
        return null;
    }

    @Override
    public Void visitCreateStatisticsStatement(CreateStatisticsStatementContext ctx) {
        add(SplitQueryType.ADMIN_PERFORMANCE, TargetType.Table, true, ctx.tableName());
        return null;
    }

    @Override
    public Void visitSplitRegionStatement(SplitRegionStatementContext ctx) {
        add(SplitQueryType.ADMIN_TABLE, TargetType.Table, true, ctx.tableName());
        return null;
    }

    @Override
    public Void visitBrieBackup(BrieBackupContext ctx) {
        if (ctx.brieObjects().tables() != null) {
            ctx.brieObjects().tables().tableName().forEach(table -> add(SplitQueryType.DATA_EXPORT, TargetType.Table, true, table));
        }
        return null;
    }

    @Override
    public Void visitBrieRestore(BrieRestoreContext ctx) {
        if (ctx.brieObjects().tables() != null) {
            ctx.brieObjects().tables().tableName().forEach(table -> add(SplitQueryType.DATA_IMPORT, TargetType.Table, true, table));
        }
        return null;
    }

    void scanOptimizerHints(ParserRuleContext statement) {
        int start = statement.getStart().getTokenIndex();
        int stop = statement.getStop().getTokenIndex();
        for (int i = start; i <= stop; i++) {
            Token token = parser.getTokenStream().get(i);
            if (token.getType() != TiDBParser.COMMENT_INPUT || !token.getText().startsWith("/*+")) {
                continue;
            }
            scanSetVarHint(token);
        }
    }

    @Override
    public Void visitTidbAdminStatement(TidbAdminStatementContext ctx) {
        addLooseTableList(ctx, SplitQueryType.ADMIN_TABLE);
        return null;
    }

    @Override
    public Void visitTidbStatsStatement(TidbStatsStatementContext ctx) {
        addLooseTableList(ctx, SplitQueryType.ADMIN_PERFORMANCE, "STATS");
        return null;
    }

    @Override
    public Void visitQueryWatchStatement(QueryWatchStatementContext ctx) {
        // TiDB persists QUERY WATCH as a runaway-watch policy. The generated
        // watch ID is not present in ADD SQL, so the policy itself is unnamed.
        addUnnamedFallback(SplitQueryType.ADMIN_RESOURCE_GROUP, TargetType.Policy, ctx);

        List<Token> tokens = defaultChannelTokens(ctx);
        for (int index = 0; index < tokens.size(); index++) {
            Token token = tokens.get(index);
            if (token.getType() == TiDBParser.LOCAL_ID) {
                addSessionVariable(token);
            }
            if (matches(tokens, index, "RESOURCE", "GROUP")) {
                addQueryWatchResourceGroup(tokens, index + 2);
            } else if ("SWITCH_GROUP".equalsIgnoreCase(token.getText())) {
                addQueryWatchResourceGroup(tokens, index + 1);
            }
        }
        return null;
    }

    private List<Token> defaultChannelTokens(ParserRuleContext ctx) {
        List<Token> tokens = new ArrayList<>();
        for (int index = ctx.getStart().getTokenIndex(); index <= ctx.getStop().getTokenIndex(); index++) {
            Token token = parser.getTokenStream().get(index);
            if (token.getChannel() == Token.DEFAULT_CHANNEL && StringUtils.isNotBlank(token.getText())) {
                tokens.add(token);
            }
        }
        return tokens;
    }

    private boolean matches(List<Token> tokens, int index, String first, String second) {
        return index + 1 < tokens.size() && first.equalsIgnoreCase(tokens.get(index).getText()) && second.equalsIgnoreCase(tokens.get(index + 1).getText());
    }

    private void addQueryWatchResourceGroup(List<Token> tokens, int index) {
        while (index < tokens.size() && ("=".equals(tokens.get(index).getText()) || "(".equals(tokens.get(index).getText()))) {
            index++;
        }
        if (index >= tokens.size()) {
            return;
        }
        Token name = tokens.get(index);
        if (name.getType() == TiDBParser.LOCAL_ID) {
            // A variable is a runtime source for the group name, not a group
            // whose literal name happens to start with '@'.
            return;
        }
        String text = name.getText();
        if (")".equals(text) || ",".equals(text) || text.startsWith("'")) {
            return;
        }
        addInstanceResource(SplitQueryType.SELECT, TargetType.ResourceGroup, true, name, unquoteIdentifier(text));
    }

    @Override
    public Void visitRecoverTableStatement(RecoverTableStatementContext ctx) {
        addLooseTableList(ctx, SplitQueryType.ADMIN_TABLE);
        return null;
    }

    @Override
    public Void visitFlashbackStatement(FlashbackStatementContext ctx) {
        addLooseTableList(ctx, SplitQueryType.ADMIN_TABLE);
        return null;
    }

    @Override
    public Void visitTidbShowStatement(TidbShowStatementContext ctx) {
        addLooseTableList(ctx, SplitQueryType.METADATA);
        return null;
    }

    @Override
    public Void visitCompactTableStatement(CompactTableStatementContext ctx) {
        add(SplitQueryType.ADMIN_TABLE, TargetType.Table, true, ctx.tableName());
        return null;
    }

    @Override
    public Void visitImportIntoPath(ImportIntoPathContext ctx) {
        add(SplitQueryType.DATA_IMPORT, TargetType.Table, true, ctx.tableName());
        return super.visitImportIntoPath(ctx);
    }

    @Override
    public Void visitImportIntoSelect(ImportIntoSelectContext ctx) {
        add(SplitQueryType.DATA_IMPORT, TargetType.Table, true, ctx.tableName());
        return super.visitImportIntoSelect(ctx);
    }

    @Override
    public Void visitSequenceNextValueExpressionAtom(SequenceNextValueExpressionAtomContext ctx) {
        add(SplitQueryType.SELECT, TargetType.Sequence, true, ctx.tableName());
        return null;
    }

    @Override
    public Void visitAlterByRename(AlterByRenameContext ctx) {
        ParserRuleContext parent = ctx.getParent();
        while (parent != null && !(parent instanceof AlterTableContext)) {
            parent = parent.getParent();
        }
        if (parent instanceof AlterTableContext alterTable) {
            references().removeIf(reference -> reference.sqlType() == SplitQueryType.ALTER_TABLE && reference.targetType() == TargetType.Table);
            add(SplitQueryType.RENAME_TABLE, TargetType.Table, true, alterTable.tableName());
            add(SplitQueryType.RENAME_TABLE, TargetType.Table, false, ctx.tableName());
        }
        return null;
    }

    private void addLooseTableList(ParserRuleContext ctx, SplitQueryType type) {
        addLooseTableList(ctx, type, "TABLE");
    }

    private void addLooseTableList(ParserRuleContext ctx, SplitQueryType type, String anchor) {
        int start = ctx.getStart().getTokenIndex();
        int stop = ctx.getStop().getTokenIndex();
        boolean afterTable = false;
        boolean expectName = false;
        boolean afterDot = false;
        Token nameStart = null;
        Token nameStop = null;
        List<String> parts = new java.util.ArrayList<>();
        for (int index = start; index <= stop; index++) {
            Token token = parser.getTokenStream().get(index);
            String text = token.getText();
            if (token.getChannel() != Token.DEFAULT_CHANNEL || StringUtils.isBlank(text)) {
                continue;
            }
            if (!afterTable) {
                if (anchor.equalsIgnoreCase(text)) {
                    afterTable = true;
                    expectName = true;
                }
                continue;
            }
            if (expectName && "LOCK".equalsIgnoreCase(text)) {
                continue;
            }
            if (expectName && "BY".equalsIgnoreCase(text)) {
                return;
            }
            if (expectName) {
                if (text.startsWith("'") || text.startsWith("\"")) {
                    return;
                }
                nameStart = token;
                nameStop = token;
                parts.add(unquoteIdentifier(text));
                expectName = false;
                afterDot = false;
                continue;
            }
            if (".".equals(text)) {
                afterDot = true;
                continue;
            }
            if (afterDot) {
                nameStop = token;
                parts.add(unquoteIdentifier(text));
                afterDot = false;
                continue;
            }
            if (",".equals(text)) {
                addQualifiedResource(type, TargetType.Table, true, nameStart, nameStop, parts);
                parts = new java.util.ArrayList<>();
                nameStart = null;
                nameStop = null;
                expectName = true;
                continue;
            }
            addQualifiedResource(type, TargetType.Table, true, nameStart, nameStop, parts);
            return;
        }
        addQualifiedResource(type, TargetType.Table, true, nameStart, nameStop, parts);
    }

    private void scanSetVarHint(Token token) {
        String text = token.getText();
        int searchFrom = 0;
        while (true) {
            int setVar = TiBehaviorText.findWord(text, searchFrom, "SET_VAR");
            if (setVar < 0) {
                return;
            }
            int open = TiBehaviorText.skipWhitespace(text, setVar + "SET_VAR".length());
            if (open >= text.length() || text.charAt(open) != '(') {
                searchFrom = setVar + "SET_VAR".length();
                continue;
            }
            int variableStart = TiBehaviorText.skipWhitespace(text, open + 1);
            int scopeEnd = scopeEnd(text, variableStart);
            if (scopeEnd >= 0) {
                int afterScope = TiBehaviorText.skipWhitespace(text, scopeEnd);
                if (afterScope > scopeEnd) {
                    variableStart = afterScope;
                }
            }
            int variableEnd = variableEnd(text, variableStart);
            int equals = TiBehaviorText.skipWhitespace(text, variableEnd);
            if (variableEnd > variableStart && equals < text.length() && text.charAt(equals) == '=') {
                String variable = text.substring(variableStart, variableEnd);
                addConfigKey(SplitQueryType.SYSTEM_SETTING_WRITE, subToken(token, variableStart, variable), variable);
                searchFrom = variableEnd;
            } else {
                searchFrom = setVar + "SET_VAR".length();
            }
        }
    }

    private static int variableEnd(String text, int start) {
        int index = start;
        if (index + 1 < text.length() && text.charAt(index) == '@' && text.charAt(index + 1) == '@') {
            index += 2;
            int scopeEnd = scopeEnd(text, index);
            if (scopeEnd >= 0 && scopeEnd < text.length() && text.charAt(scopeEnd) == '.') {
                index = scopeEnd + 1;
            }
        }
        if (index < text.length() && text.charAt(index) == '`') {
            int closing = text.indexOf('`', index + 1);
            return closing < 0 ? start : closing + 1;
        }
        if (index >= text.length() || !TiBehaviorText.isIdentifierStart(text.charAt(index))) {
            return start;
        }
        index++;
        while (index < text.length() && TiBehaviorText.isIdentifierPart(text.charAt(index))) {
            index++;
        }
        return index;
    }

    private static int scopeEnd(String text, int start) {
        String[] scopes = { "GLOBAL", "SESSION", "LOCAL" };
        for (String scope : scopes) {
            if (TiBehaviorText.startsWithWord(text, start, scope)) {
                return start + scope.length();
            }
        }
        return -1;
    }

    @Override
    public Void visitGenericFunctionCall(GenericFunctionCallContext ctx) {
        if (ctx.genericFunction().name instanceof CustomGenericFunctionNameContext custom) {
            FullIdContext fullId = custom.function.fullId();
            if (fullId.DOT() == null) {
                String functionName = parser.getTokenStream().getText(fullId.getStart(), fullId.getStop());
                if (resources.isUserDefinedFunction(functionName, false, version)) {
                    add(SplitQueryType.CALL_PROG_OBJ, TargetType.Function, true, fullId);
                } else {
                    addFunction(fullId.getStart());
                }
            } else {
                add(SplitQueryType.CALL_PROG_OBJ, TargetType.Function, true, fullId);
            }
        } else {
            Token token = ctx.genericFunction().name.getStart();
            addFunction(token);
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitAggregateFunctionCall(AggregateFunctionCallContext ctx) {
        addFunction(ctx.aggregateFunction().getStart());
        return visitChildren(ctx);
    }

    @Override
    public Void visitSpatialAggregateFunctionCall(SpatialAggregateFunctionCallContext ctx) {
        addFunction(ctx.customFunctionName().getStart());
        return visitChildren(ctx);
    }

    @Override
    public Void visitNonKeywordFunctionCall(NonKeywordFunctionCallContext ctx) {
        addFunction(ctx.getStart());
        return visitChildren(ctx);
    }

    @Override
    public Void visitSpecificFunctionCall(SpecificFunctionCallContext ctx) {
        SpecificFunctionContext function = ctx.specificFunction();
        if (!(function instanceof CaseFunctionCallContext) &&
                !(function instanceof ValuesFunctionCallContext)) {
            addFunction(ctx.getStart());
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitKeywordFunctionCall(KeywordFunctionCallContext ctx) {
        addFunction(ctx.getStart());
        return visitChildren(ctx);
    }

    @Override
    public Void visitPasswordFunctionCall(PasswordFunctionCallContext ctx) {
        addFunction(ctx.getStart());
        return visitChildren(ctx);
    }

    @Override
    public Void visitNonAggregateFunctionCall(NonAggregateFunctionCallContext ctx) {
        addFunction(ctx.getStart());
        return visitChildren(ctx);
    }

    @Override
    public Void visitCurrentTimestamp(CurrentTimestampContext ctx) {
        addFunction(ctx.getStart());
        return visitChildren(ctx);
    }

    @Override
    public Void visitDefaultValue(DefaultValueContext ctx) {
        if (ctx.NEXT() != null && ctx.tableName() != null) {
            add(SplitQueryType.SELECT, TargetType.Sequence, true, ctx.tableName());
        }
        return visitChildren(ctx);
    }

    @Override
    public Void visitJsonDualityObjectFunctionCall(JsonDualityObjectFunctionCallContext ctx) {
        addFunction(ctx.getStart());
        return visitChildren(ctx);
    }

    private void addFunction(Token token) {
        if (token == null) {
            return;
        }
        BehaviorAction behavior = resources.functionBehavior(token.getText(), exactVersion);
        SplitQueryType type = switch (behavior) {
            case CALL -> SplitQueryType.CALL_PROG_OBJ;
            case READ -> SplitQueryType.SELECT;
            case LOCK -> SplitQueryType.QUERY_LOCK;
            case CONFIGURE -> SplitQueryType.SYSTEM_SETTING_WRITE;
            case ALTER, RESET, SWITCH -> SplitQueryType.ALTER_REPLICATION;
            default -> throw new IllegalStateException("unsupported functional function action " + behavior);
        };
        boolean quotedIdentifier = token.getType() == TiDBParser.REVERSE_QUOTE_ID || token.getType() == TiDBParser.DOUBLE_QUOTE_ID;
        if (quotedIdentifier || resources.isUserDefinedFunction(token.getText(), false, version)) {
            add(type, TargetType.Function, true, token);
        } else {
            addInstanceBehaviorResource(type, TargetType.Function, true, token, token.getText(), behavior);
        }
    }

    @Override
    public Void visitLockTableElement(LockTableElementContext ctx) {
        add(SplitQueryType.SESSION_LOCK, TargetType.Table, ctx.tableName());
        return null;
    }

    @Override
    public Void visitUseStatement(UseStatementContext ctx) {
        add(SplitQueryType.SWITCH_SCHEMA, TargetType.Schema, true, ctx.uid());
        return null;
    }

    @Override
    public Void visitAlterUpgradeName(AlterUpgradeNameContext ctx) {
        add(SplitQueryType.ALTER_SCHEMA, TargetType.Schema, true, ctx.uid());
        return null;
    }

    @Override
    public Void visitTableStatement(TableStatementContext ctx) {
        if (isCte(ctx.tableName())) {
            return null;
        }
        addReadTable(ctx.tableName());
        return null;
    }

    @Override
    public Void visitAtomTableItem(AtomTableItemContext ctx) {
        if (isCte(ctx.tableName())) {
            return null;
        }
        if (isUnnamedTable(ctx.tableName())) {
            addUnnamedAtCurrentSchema(SplitQueryType.SELECT, TargetType.Table, true, ctx.tableName());
            return null;
        }
        if (isDual(ctx.tableName())) {
            addReadTable(ctx.tableName());
            return null;
        }
        return super.visitAtomTableItem(ctx);
    }

    private void addReadTable(TableNameContext tableName) {
        add(SplitQueryType.SELECT, TargetType.Table, tableName);
    }

    private static boolean isDual(TableNameContext tableName) {
        return tableName != null && tableName.fullId() != null && tableName.fullId().DOT() == null && StringUtils.equalsIgnoreCase("DUAL", tableName.fullId().getText());
    }

    @Override
    public Void visitInsertStatement(InsertStatementContext ctx) {
        if (!isUnnamedTable(ctx.tableName())) {
            return super.visitInsertStatement(ctx);
        }
        SplitQueryType type = ctx.duplicatedFirst == null ? SplitQueryType.INSERT : SplitQueryType.MERGE;
        addUnnamedAtCurrentSchema(type, TargetType.Table, true, ctx.tableName());
        return null;
    }

    @Override
    public Void visitMultipleUpdateStatement(MultipleUpdateStatementContext ctx) {
        if (ctx.withClause() != null) {
            visit(ctx.withClause());
        }
        if (ctx.tableSources() == null) {
            return null;
        }
        visit(ctx.tableSources());

        List<AtomTableItemContext> tables = descendants(ctx.tableSources(), AtomTableItemContext.class).stream()
            .filter(table -> topLevelUpdateTable(table, ctx.tableSources()))
            .toList();
        Set<AtomTableItemContext> targets = new LinkedHashSet<>();
        boolean hasUnqualifiedAssignment = false;
        for (UpdatedElementContext element : ctx.updatedElement()) {
            List<String> parts = identifierParts(parser.getTokenStream().getText(element.fullColumnName().getStart(), element.fullColumnName().getStop()));
            if (parts.size() < 2) {
                hasUnqualifiedAssignment = true;
                continue;
            }
            String qualifier = parts.get(parts.size() - 2);
            tables.stream().filter(table -> matchesUpdateQualifier(table, qualifier)).forEach(targets::add);
        }

        BehaviorAction action = BehaviorAction.UPDATE;
        if (targets.isEmpty() && hasUnqualifiedAssignment) {
            if (tables.size() == 1) {
                targets.add(tables.get(0));
            } else {
                targets.addAll(tables);
                action = BehaviorAction.UNKNOWN;
            }
        }
        for (AtomTableItemContext target : targets) {
            addBehaviorResource(SplitQueryType.UPDATE, TargetType.Table, true, target.tableName(), action);
        }
        if (ctx.whereClause() != null) {
            visit(ctx.whereClause());
        }
        return null;
    }

    private static boolean topLevelUpdateTable(AtomTableItemContext table, TableSourcesContext tableSources) {
        ParseTree parent = table.getParent();
        while (parent != null && parent != tableSources) {
            if (parent instanceof SubqueryStatementContext) {
                return false;
            }
            parent = parent.getParent();
        }
        return parent == tableSources;
    }

    private static boolean matchesUpdateQualifier(AtomTableItemContext table, String qualifier) {
        if (table.aliasName() != null) {
            return StringUtils.equalsIgnoreCase(unquoteIdentifier(table.aliasName().getText()), qualifier);
        }
        List<String> tableParts = identifierParts(table.tableName().getText());
        return !tableParts.isEmpty() && StringUtils.equalsIgnoreCase(tableParts.get(tableParts.size() - 1), qualifier);
    }

    private static List<String> identifierParts(String value) {
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        char quote = 0;
        for (int index = 0; index < value.length(); index++) {
            char character = value.charAt(index);
            if (quote != 0) {
                current.append(character);
                if (character == quote) {
                    if (index + 1 < value.length() && value.charAt(index + 1) == quote) {
                        current.append(value.charAt(++index));
                    } else {
                        quote = 0;
                    }
                }
            } else if (character == '`' || character == '"') {
                quote = character;
                current.append(character);
            } else if (character == '.') {
                parts.add(unquoteIdentifier(current.toString().strip()));
                current.setLength(0);
            } else {
                current.append(character);
            }
        }
        parts.add(unquoteIdentifier(current.toString().strip()));
        return parts;
    }

    private static <T extends ParseTree> List<T> descendants(ParseTree root, Class<T> type) {
        List<T> result = new ArrayList<>();
        collectDescendants(root, type, result);
        return result;
    }

    private static <T extends ParseTree> void collectDescendants(ParseTree node, Class<T> type, List<T> result) {
        if (type.isInstance(node)) {
            result.add(type.cast(node));
        }
        for (int index = 0; index < node.getChildCount(); index++) {
            collectDescendants(node.getChild(index), type, result);
        }
    }

    @Override
    public Void visitReplaceStatement(ReplaceStatementContext ctx) {
        if (!isUnnamedTable(ctx.tableName())) {
            return super.visitReplaceStatement(ctx);
        }
        addUnnamedAtCurrentSchema(SplitQueryType.MERGE, TargetType.Table, true, ctx.tableName());
        return null;
    }

    @Override
    public Void visitReferenceDefinition(ReferenceDefinitionContext ctx) {
        add(SplitQueryType.SELECT, TargetType.Table, ctx.tableName());
        return null;
    }

    @Override
    public Void visitPrimaryKeyTableConstraint(PrimaryKeyTableConstraintContext ctx) {
        if (ctx.name == null) {
            addNamedAtCurrentSchema(SplitQueryType.ADD_CONSTRAINT, TargetType.Constraint, false, ctx, "PRIMARY");
        } else {
            add(SplitQueryType.ADD_CONSTRAINT, TargetType.Constraint, false, ctx.name);
        }
        if (ctx.index != null) {
            add(SplitQueryType.ADD_INDEX, TargetType.Index, false, ctx.index);
        }
        return null;
    }

    @Override
    public Void visitColumnDeclaration(ColumnDeclarationContext ctx) {
        UidContext column = ctx.columnDefinition().uid();
        TableNameContext table = enclosingCreateTable(ctx);
        if (column != null && table != null) {
            addTableChild(SplitQueryType.ADD_COLUMN, TargetType.Column, false, table, column, normalizeIdentifier(column.getText()));
        }
        return null;
    }

    private static TableNameContext enclosingCreateTable(ParseTree node) {
        for (ParseTree current = node.getParent(); current != null; current = current.getParent()) {
            if (current instanceof ColumnCreateTableContext createTable) {
                return createTable.tableName();
            }
            if (current instanceof QueryCreateTableContext createTable) {
                return createTable.tableName();
            }
        }
        return null;
    }

    private static TableNameContext enclosingAlterTable(ParseTree node) {
        for (ParseTree current = node.getParent(); current != null; current = current.getParent()) {
            if (current instanceof AlterTableContext alterTable) {
                return alterTable.tableName();
            }
        }
        return null;
    }

    @Override
    public Void visitUniqueKeyTableConstraint(UniqueKeyTableConstraintContext ctx) {
        addNamedOrUnnamed(SplitQueryType.ADD_CONSTRAINT, TargetType.Constraint, false, ctx.name, ctx);
        if (ctx.index != null) {
            add(SplitQueryType.ADD_INDEX, TargetType.Index, false, ctx.index);
        }
        return null;
    }

    @Override
    public Void visitForeignKeyTableConstraint(ForeignKeyTableConstraintContext ctx) {
        addNamedOrUnnamed(SplitQueryType.ADD_CONSTRAINT, TargetType.Constraint, false, ctx.name, ctx);
        if (ctx.index != null) {
            add(SplitQueryType.ADD_INDEX, TargetType.Index, false, ctx.index);
        }
        return null;
    }

    @Override
    public Void visitCheckTableConstraint(CheckTableConstraintContext ctx) {
        addNamedOrUnnamed(SplitQueryType.ADD_CONSTRAINT, TargetType.Constraint, false, ctx.name, ctx);
        return null;
    }

    @Override
    public Void visitReferenceColumnConstraint(ReferenceColumnConstraintContext ctx) {
        addUnnamedAtCurrentSchema(SplitQueryType.ADD_CONSTRAINT, TargetType.Constraint, false, ctx);
        return null;
    }

    @Override
    public Void visitPrimaryKeyColumnConstraint(PrimaryKeyColumnConstraintContext ctx) {
        addUnnamedAtCurrentSchema(SplitQueryType.ADD_CONSTRAINT, TargetType.Constraint, false, ctx);
        return null;
    }

    @Override
    public Void visitUniqueKeyColumnConstraint(UniqueKeyColumnConstraintContext ctx) {
        addUnnamedAtCurrentSchema(SplitQueryType.ADD_CONSTRAINT, TargetType.Constraint, false, ctx);
        return null;
    }

    @Override
    public Void visitCheckColumnConstraint(CheckColumnConstraintContext ctx) {
        addNamedOrUnnamed(SplitQueryType.ADD_CONSTRAINT, TargetType.Constraint, false, ctx.name, ctx);
        return null;
    }

    @Override
    public Void visitSimpleIndexDeclaration(SimpleIndexDeclarationContext ctx) {
        addNamedOrUnnamed(SplitQueryType.ADD_INDEX, TargetType.Index, false, ctx.uid(), ctx);
        return null;
    }

    @Override
    public Void visitSpecialIndexDeclaration(SpecialIndexDeclarationContext ctx) {
        addNamedOrUnnamed(SplitQueryType.ADD_INDEX, TargetType.Index, false, ctx.uid(), ctx);
        return null;
    }

    @Override
    public Void visitAlterByAddColumn(AlterByAddColumnContext ctx) {
        UidContext column = ctx.columnDefinition().uid();
        TableNameContext table = enclosingAlterTable(ctx);
        if (column != null && table != null) {
            addTableChild(SplitQueryType.ADD_COLUMN, TargetType.Column, false, table, column, normalizeIdentifier(column.getText()));
        }
        return null;
    }

    @Override
    public Void visitAlterByAddIndex(AlterByAddIndexContext ctx) {
        addNamedOrUnnamed(SplitQueryType.ADD_INDEX, TargetType.Index, false, ctx.indexName(), ctx);
        return null;
    }

    @Override
    public Void visitAlterByAddPrimaryKey(AlterByAddPrimaryKeyContext ctx) {
        addNamedOrUnnamed(SplitQueryType.ADD_CONSTRAINT, TargetType.Constraint, false, ctx.name, ctx);
        if (ctx.index != null) {
            add(SplitQueryType.ADD_INDEX, TargetType.Index, false, ctx.index);
        }
        return null;
    }

    @Override
    public Void visitAlterByAddUniqueKey(AlterByAddUniqueKeyContext ctx) {
        if (ctx.CONSTRAINT() != null) {
            addNamedOrUnnamed(SplitQueryType.ADD_CONSTRAINT, TargetType.Constraint, false, ctx.name, ctx);
        }
        addNamedOrUnnamed(SplitQueryType.ADD_INDEX, TargetType.Index, false, ctx.indexName(), ctx);
        return null;
    }

    @Override
    public Void visitAlterByAddSpecialIndex(AlterByAddSpecialIndexContext ctx) {
        addNamedOrUnnamed(SplitQueryType.ADD_INDEX, TargetType.Index, false, ctx.indexName(), ctx);
        return null;
    }

    @Override
    public Void visitAlterByAddForeignKey(AlterByAddForeignKeyContext ctx) {
        addNamedOrUnnamed(SplitQueryType.ADD_CONSTRAINT, TargetType.Constraint, false, ctx.name, ctx);
        if (ctx.indexName() != null) {
            add(SplitQueryType.ADD_INDEX, TargetType.Index, false, ctx.indexName());
        }
        return null;
    }

    @Override
    public Void visitAlterByAddCheckTableConstraint(AlterByAddCheckTableConstraintContext ctx) {
        addNamedOrUnnamed(SplitQueryType.ADD_CONSTRAINT, TargetType.Constraint, false, ctx.name, ctx);
        return null;
    }

    @Override
    public Void visitAlterByAlterConstraintEnforcement(AlterByAlterConstraintEnforcementContext ctx) {
        add(SplitQueryType.ALTER_CONSTRAINT, TargetType.Constraint, true, ctx.uid());
        return null;
    }

    @Override
    public Void visitAlterByDropConstraintCheck(AlterByDropConstraintCheckContext ctx) {
        add(SplitQueryType.DROP_CONSTRAINT, TargetType.Constraint, true, ctx.uid());
        return null;
    }

    @Override
    public Void visitAlterByDropPrimaryKey(AlterByDropPrimaryKeyContext ctx) {
        addUnnamedAtCurrentSchema(SplitQueryType.DROP_CONSTRAINT, TargetType.Constraint, true, ctx);
        return null;
    }

    @Override
    public Void visitAlterByDropForeignKey(AlterByDropForeignKeyContext ctx) {
        add(SplitQueryType.DROP_CONSTRAINT, TargetType.Constraint, true, ctx.uid());
        return null;
    }

    @Override
    public Void visitAlterByDropIndex(AlterByDropIndexContext ctx) {
        add(SplitQueryType.DROP_INDEX, TargetType.Index, true, ctx.indexName());
        return null;
    }

    @Override
    public Void visitAlterByRenameIndex(AlterByRenameIndexContext ctx) {
        add(SplitQueryType.RENAME_INDEX, TargetType.Index, true, ctx.uid(0));
        add(SplitQueryType.RENAME_INDEX, TargetType.Index, false, ctx.uid(1));
        return null;
    }

    @Override
    public Void visitAlterByAlterIndexVisibility(AlterByAlterIndexVisibilityContext ctx) {
        add(SplitQueryType.ALTER_INDEX, TargetType.Index, true, ctx.uid());
        return null;
    }

    @Override
    public Void visitHandlerOpenStatement(HandlerOpenStatementContext ctx) {
        add(SplitQueryType.SELECT, TargetType.Table, ctx.tableName());
        return super.visitHandlerOpenStatement(ctx);
    }

    @Override
    public Void visitRenameUser(RenameUserContext ctx) {
        ctx.renameUserClause()
            .stream()
            .map(clause -> clause.fromFirst)
            .filter(source -> source.CURRENT_USER() != null)
            .forEach(source -> addUnnamedFallback(SplitQueryType.RENAME_USER, TargetType.User, source));
        return super.visitRenameUser(ctx);
    }

    @Override
    public Void visitSetDefaultRole(SetDefaultRoleContext ctx) {
        ctx.userName().forEach(user -> addAccount(SplitQueryType.ALTER_USER, TargetType.User, true, user));
        addDescendantAccounts(SplitQueryType.ALTER_USER, TargetType.Role, true, ctx.roleOption());
        return null;
    }

    @Override
    public Void visitSetRole(SetRoleContext ctx) {
        addDescendantAccounts(SplitQueryType.SWITCH_ROLE, TargetType.Role, true, ctx.roleOption());
        return null;
    }

    @Override
    public Void visitAlterUserDefaultRole(AlterUserDefaultRoleContext ctx) {
        addAccount(SplitQueryType.ALTER_USER, TargetType.User, true, ctx.userName());
        ctx.alterUserDefaultRoleClause().roleName().forEach(role -> addAccount(SplitQueryType.ALTER_USER, TargetType.Role, true, role));
        return null;
    }

    @Override
    public Void visitGrantStatement(GrantStatementContext ctx) {
        if (!ctx.privelegeClause().isEmpty()) {
            addPrivilegeTarget(SplitQueryType.GRANT, ctx.privilegeObject, ctx.privilegeLevel());
            ctx.grantUser().forEach(user -> {
                if (user.accountTarget() != null && user.accountTarget().CURRENT_USER() != null) {
                    addUnnamed(SplitQueryType.GRANT, TargetType.UserOrRole, true, user.accountTarget().CURRENT_USER().getSymbol());
                } else if (user.currentUserGrantAuthOption() != null) {
                    addUnnamed(SplitQueryType.GRANT, TargetType.UserOrRole, true, user.currentUserGrantAuthOption().CURRENT_USER().getSymbol());
                } else {
                    addDescendantAccounts(SplitQueryType.GRANT, TargetType.UserOrRole, true, user);
                }
            });
        } else {
            ctx.roleName().forEach(role -> addAccount(SplitQueryType.GRANT, TargetType.Role, true, role));
            ctx.accountTarget().forEach(target -> addAccountTarget(SplitQueryType.GRANT, target));
            ctx.uid().forEach(target -> addAccount(SplitQueryType.GRANT, TargetType.UserOrRole, true, target));
        }
        return null;
    }

    @Override
    public Void visitRevokeStatement(RevokeStatementContext ctx) {
        if (!ctx.privelegeClause().isEmpty()) {
            addPrivilegeTarget(SplitQueryType.REVOKE, ctx.privilegeObject, ctx.privilegeLevel());
            ctx.accountTarget().forEach(target -> addAccountTarget(SplitQueryType.REVOKE, target));
        } else if (!ctx.roleName().isEmpty()) {
            ctx.roleName().forEach(role -> addAccount(SplitQueryType.REVOKE, TargetType.Role, true, role));
            ctx.accountTarget().forEach(target -> addAccountTarget(SplitQueryType.REVOKE, target));
            ctx.uid().forEach(target -> addAccount(SplitQueryType.REVOKE, TargetType.UserOrRole, true, target));
        } else {
            ctx.accountTarget().forEach(target -> addAccountTarget(SplitQueryType.REVOKE, target));
        }
        return null;
    }

    private void addAccountTarget(SplitQueryType type, AccountTargetContext target) {
        if (target.CURRENT_USER() != null) {
            addUnnamed(type, TargetType.UserOrRole, true, target.CURRENT_USER().getSymbol());
        } else {
            addDescendantAccounts(type, TargetType.UserOrRole, true, target);
        }
    }

    @Override
    public Void visitSignalAllowedExpression(SignalAllowedExpressionContext ctx) {
        if (ctx.mysqlVariable() != null) {
            addConfigKey(ctx.mysqlVariable());
        }
        return null;
    }

    @Override
    public Void visitMysqlVariable(MysqlVariableContext ctx) {
        addConfigKey(ctx);
        return null;
    }

    @Override
    public Void visitPrepareStatement(PrepareStatementContext ctx) {
        if (ctx.variable != null) {
            addSessionVariable(ctx.variable);
        }
        return super.visitPrepareStatement(ctx);
    }

    @Override
    public Void visitUserVariables(UserVariablesContext ctx) {
        ctx.LOCAL_ID().forEach(variable -> addSessionVariable(variable.getSymbol()));
        return null;
    }

    @Override
    public Void visitStableInteger(StableIntegerContext ctx) {
        if (ctx.LOCAL_ID() != null) {
            addSessionVariable(ctx.LOCAL_ID().getSymbol());
        }
        return null;
    }

    @Override
    public Void visitSelectExpressionElement(SelectExpressionElementContext ctx) {
        if (ctx.LOCAL_ID() != null) {
            addSessionVariable(ctx.LOCAL_ID().getSymbol());
        }
        return super.visitSelectExpressionElement(ctx);
    }

    @Override
    public Void visitVariableAssignmentExpression(VariableAssignmentExpressionContext ctx) {
        addSessionVariable(ctx.LOCAL_ID().getSymbol());
        return null;
    }

    @Override
    public Void visitNestedVariableAssignmentExpression(NestedVariableAssignmentExpressionContext ctx) {
        addSessionVariable(ctx.LOCAL_ID().getSymbol());
        return null;
    }

    @Override
    public Void visitFullDescribeStatement(FullDescribeStatementContext ctx) {
        if (ctx.LOCAL_ID() != null) {
            addSessionVariable(ctx.LOCAL_ID().getSymbol());
        }
        return null;
    }

    private void addNamedOrUnnamed(SplitQueryType type, TargetType targetType, boolean require, ParserRuleContext name, ParserRuleContext owner) {
        if (name == null) {
            addUnnamedAtCurrentSchema(type, targetType, require, owner);
        } else {
            add(type, targetType, require, name);
        }
    }

    private static Token subToken(Token source, int offset, String text) {
        String prefix = source.getText().substring(0, offset);
        int lineBreak = prefix.lastIndexOf('\n');
        int line = source.getLine();
        int column = source.getCharPositionInLine() + offset;
        for (int i = 0; i < prefix.length(); i++) {
            if (prefix.charAt(i) == '\n') {
                line++;
            }
        }
        if (lineBreak >= 0) {
            column = prefix.length() - lineBreak - 1;
        }
        CommonToken token = new CommonToken(0, text);
        token.setLine(line);
        token.setCharPositionInLine(column);
        return token;
    }

    private void collectCteNames(ParseTree tree) {
        if (tree instanceof WithSelectExprContext cte) {
            cteNames.add(normalizeIdentifier(cte.uid().getText()));
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectCteNames(tree.getChild(i));
        }
    }

    private boolean isCte(TableNameContext table) {
        if (table == null || table.delphiName != null || table.fullId() == null || table.fullId().DOT() != null || table.fullId().uid().size() != 1) {
            return false;
        }
        return cteNames.contains(normalizeIdentifier(table.getText()));
    }

    private static boolean isUnnamedTable(TableNameContext table) {
        return table != null && table.getText().replace("`", "").isBlank();
    }

    private static String normalizeIdentifier(String identifier) {
        String value = identifier;
        if (value.length() >= 2 && value.startsWith("`") && value.endsWith("`")) {
            value = value.substring(1, value.length() - 1).replace("``", "`");
        }
        return value.toLowerCase(Locale.ROOT);
    }
}
