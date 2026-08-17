/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 */
package com.clougence.sql.mysql.analysis.behavior;

import java.util.*;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.sdk.sql.analysis.behavior.BehaviorAction;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.mysql.analysis.reference.MySqlObjectReferenceVisitor;
import com.clougence.sql.mysql.analysis.reference.MySqlResourceRegistry;
import com.clougence.sql.mysql.parser.MySqlVersion;
import com.clougence.sql.mysql.parser.antlr.MySqlParser;
import com.clougence.sql.mysql.parser.antlr.MySqlParser.*;
import com.clougence.utils.StringUtils;

/**
 * Adds behavior-only object facts that must not alter legacy resource analysis.
 */
final class MyBehaviorObjectReferenceVisitor extends MySqlObjectReferenceVisitor {

    private final Parser                parser;
    private final MySqlVersion          version;
    private final int                   exactVersion;
    private final MySqlResourceRegistry resources;
    private final Set<String>           cteNames = new HashSet<>();

    MyBehaviorObjectReferenceVisitor(Parser parser, Map<UmiTypes, Object> levelsParam, int baseLine, int baseColumn, MySqlVersion version, int exactVersion,
                                     MySqlResourceRegistry resources){
        super(parser, levelsParam, baseLine, baseColumn, version, exactVersion, resources);
        this.parser = parser;
        this.version = version;
        this.exactVersion = exactVersion;
        this.resources = resources;
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
        if (index >= text.length() || !MyBehaviorText.isIdentifierStart(text.charAt(index))) {
            return start;
        }
        index++;
        while (index < text.length() && MyBehaviorText.isIdentifierPart(text.charAt(index))) {
            index++;
        }
        return index;
    }

    private static int scopeEnd(String text, int start) {
        String[] scopes = { "GLOBAL", "SESSION", "LOCAL" };
        for (String scope : scopes) {
            if (MyBehaviorText.startsWithWord(text, start, scope)) {
                return start + scope.length();
            }
        }
        return -1;
    }

    private static boolean isDual(TableNameContext tableName) {
        return tableName != null && tableName.fullId() != null && tableName.fullId().DOT() == null && StringUtils.equalsIgnoreCase("DUAL", tableName.fullId().uid(0).getText());
    }

    private static <T extends ParseTree> List<T> descendants(ParseTree tree, Class<T> type) {
        List<T> result = new java.util.ArrayList<>();
        collect(tree, type, result);
        return result;
    }

    private static <T extends ParseTree> void collect(ParseTree tree, Class<T> type, List<T> result) {
        if (tree == null) {
            return;
        }
        if (type.isInstance(tree)) {
            result.add(type.cast(tree));
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collect(tree.getChild(i), type, result);
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

    void prepareStatement(ParserRuleContext statement) {
        collectCteNames(statement);
    }

    void scanOptimizerHints(ParserRuleContext statement) {
        int start = statement.getStart().getTokenIndex();
        int stop = statement.getStop().getTokenIndex();
        for (int i = start; i <= stop; i++) {
            Token token = parser.getTokenStream().get(i);
            if (token.getType() != MySqlParser.COMMENT_INPUT || !token.getText().startsWith("/*+")) {
                continue;
            }
            scanSetVarHint(token);
        }
    }

    private void scanSetVarHint(Token token) {
        String text = token.getText();
        int searchFrom = 0;
        while (true) {
            int setVar = MyBehaviorText.findWord(text, searchFrom, "SET_VAR");
            if (setVar < 0) {
                return;
            }
            int open = MyBehaviorText.skipWhitespace(text, setVar + "SET_VAR".length());
            if (open >= text.length() || text.charAt(open) != '(') {
                searchFrom = setVar + "SET_VAR".length();
                continue;
            }
            int variableStart = MyBehaviorText.skipWhitespace(text, open + 1);
            int scopeEnd = scopeEnd(text, variableStart);
            if (scopeEnd >= 0) {
                int afterScope = MyBehaviorText.skipWhitespace(text, scopeEnd);
                if (afterScope > scopeEnd) {
                    variableStart = afterScope;
                }
            }
            int variableEnd = variableEnd(text, variableStart);
            int equals = MyBehaviorText.skipWhitespace(text, variableEnd);
            if (variableEnd > variableStart && equals < text.length() && text.charAt(equals) == '=') {
                String variable = text.substring(variableStart, variableEnd);
                addConfigKey(SplitQueryType.SYSTEM_SETTING_WRITE, subToken(token, variableStart, variable), variable);
                searchFrom = variableEnd;
            } else {
                searchFrom = setVar + "SET_VAR".length();
            }
        }
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
        if (!(function instanceof CaseFunctionCallContext)) {
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
        boolean quotedIdentifier = token.getType() == MySqlParser.REVERSE_QUOTE_ID || token.getType() == MySqlParser.DOUBLE_QUOTE_ID;
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
    public Void visitColumnCreateTable(ColumnCreateTableContext ctx) {
        super.visitColumnCreateTable(ctx);
        addCreateColumns(ctx.tableName(), ctx.createDefinitions());
        return null;
    }

    @Override
    public Void visitQueryCreateTable(QueryCreateTableContext ctx) {
        super.visitQueryCreateTable(ctx);
        addCreateColumns(ctx.tableName(), ctx.createDefinitions());
        return null;
    }

    private void addCreateColumns(TableNameContext table, CreateDefinitionsContext definitions) {
        if (definitions == null) {
            return;
        }
        for (CreateDefinitionContext definition : definitions.createDefinition()) {
            if (!(definition instanceof ColumnDeclarationContext column) || column.columnDefinition().uid() == null) {
                continue;
            }
            addTableChild(SplitQueryType.CREATE_TABLE, TargetType.Column, false, table, column.columnDefinition().uid());
        }
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

    @Override
    public Void visitMultipleDeleteStatement(MultipleDeleteStatementContext ctx) {
        if (ctx.tableSources() == null) {
            return super.visitMultipleDeleteStatement(ctx);
        }
        visit(ctx.tableSources());
        List<AtomTableItemContext> tables = descendants(ctx.tableSources(), AtomTableItemContext.class).stream()
            .filter(table -> topLevelUpdateTable(table, ctx.tableSources()))
            .toList();
        for (TableNameContext deleteTarget : ctx.tableName()) {
            String qualifier = unquoteUpdateIdentifier(deleteTarget.getText());
            AtomTableItemContext resolved = tables.stream().filter(table -> matchesUpdateQualifier(table, qualifier)).findFirst().orElse(null);
            if (resolved == null) {
                addBehaviorResource(SplitQueryType.DELETE, TargetType.Table, true, deleteTarget, BehaviorAction.DELETE);
            } else {
                addBehaviorResource(SplitQueryType.DELETE, TargetType.Table, true, resolved.tableName(), BehaviorAction.DELETE);
            }
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
            return StringUtils.equalsIgnoreCase(unquoteUpdateIdentifier(table.aliasName().getText()), qualifier);
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
                parts.add(unquoteUpdateIdentifier(current.toString().strip()));
                current.setLength(0);
            } else {
                current.append(character);
            }
        }
        parts.add(unquoteUpdateIdentifier(current.toString().strip()));
        return parts;
    }

    private static String unquoteUpdateIdentifier(String value) {
        if (value.length() >= 2) {
            char quote = value.charAt(0);
            if ((quote == '`' || quote == '"') && value.charAt(value.length() - 1) == quote) {
                String delimiter = String.valueOf(quote);
                return value.substring(1, value.length() - 1).replace(delimiter + delimiter, delimiter);
            }
        }
        return value;
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
    public Void visitMultipleUpdateStatement(MultipleUpdateStatementContext ctx) {
        List<AtomTableItemContext> tables = descendants(ctx.tableSources(), AtomTableItemContext.class);
        if (tables.isEmpty()) {
            return null;
        }
        String qualifier = null;
        if (!ctx.updatedElement().isEmpty()) {
            FullColumnNameContext column = ctx.updatedElement(0).fullColumnName();
            if (!column.dottedId().isEmpty()) {
                qualifier = unquote(column.uid().getText());
            }
        }
        AtomTableItemContext target = resolveAlias(qualifier, tables);
        add(SplitQueryType.UPDATE, TargetType.Table, target.tableName());
        return null;
    }

    @Override
    public Void visitMultipleDeleteStatement(MultipleDeleteStatementContext ctx) {
        List<AtomTableItemContext> tables = descendants(ctx.tableSources(), AtomTableItemContext.class);
        for (TableNameContext deleteTarget : ctx.tableName()) {
            AtomTableItemContext target = resolveAlias(unquote(text(deleteTarget)), tables);
            if (target == null) {
                add(SplitQueryType.DELETE, TargetType.Table, deleteTarget);
            } else {
                add(SplitQueryType.DELETE, TargetType.Table, target.tableName());
            }
        }
        return null;
    }

    private AtomTableItemContext resolveAlias(String qualifier, List<AtomTableItemContext> tables) {
        if (qualifier != null) {
            for (AtomTableItemContext table : tables) {
                if (table.aliasName() != null && qualifier.equalsIgnoreCase(unquote(text(table.aliasName())))) {
                    return table;
                }
            }
        }
        return tables.isEmpty() ? null : tables.get(0);
    }

    private String text(ParserRuleContext context) {
        return parser.getTokenStream().getText(context.getStart(), context.getStop());
    }

    private String unquote(String value) {
        if (value != null && value.length() >= 2) {
            char quote = value.charAt(0);
            if ((quote == '`' || quote == '"') && value.charAt(value.length() - 1) == quote) {
                return value.substring(1, value.length() - 1);
            }
        }
        return value;
    }

    @Override
    public Void visitReferenceDefinition(ReferenceDefinitionContext ctx) {
        add(SplitQueryType.SELECT, TargetType.Table, ctx.tableName());
        return null;
    }

    @Override
    public Void visitPrimaryKeyTableConstraint(PrimaryKeyTableConstraintContext ctx) {
        addNamedOrUnnamed(SplitQueryType.ADD_CONSTRAINT, TargetType.Constraint, false, ctx.name, ctx);
        if (ctx.index != null) {
            add(SplitQueryType.ADD_INDEX, TargetType.Index, false, ctx.index);
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
    public Void visitTableSampleClause(TableSampleClauseContext ctx) {
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
}
