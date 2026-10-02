/*
 * Copyright 2026 杭州开云集致科技有限公司
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.clougence.sql.hana.analysis.lineage;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import com.clougence.sql.common.analysis.lineage.model.*;
import com.clougence.sql.hana.analysis.HanaSqlFunctions;
import com.clougence.sql.hana.parser.antlr.HanaParser;

final class HanaLineageVisitor {
    private static final Set<String> SESSION_VALUES = Set.of("CURRENT_USER", "SESSION_USER", "CURRENT_SCHEMA", "CURRENT_DATE",
        "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_UTCDATE", "CURRENT_UTCTIME", "CURRENT_UTCTIMESTAMP", "CURRENT_CONNECTION");

    LineageQuery query(HanaParser.QueryContext context) {
        List<LineageCte> ctes = new ArrayList<>();
        if (context.withClause() != null) {
            for (var cte : context.withClause().cte()) {
                List<String> aliases = List.of();
                if (cte.columnNames() != null) aliases = cte.columnNames().identifier().stream().map(HanaLineageVisitor::name).toList();
                ctes.add(new LineageCte(name(cte.identifier()), aliases, query(cte.query())));
            }
        }
        List<LineageQueryBlock> branches = new ArrayList<>();
        for (var term : context.queryTerm()) {
            if (term.query() != null) {
                LineageQuery nested = query(term.query());
                nested.branches().forEach(branch -> branches.add(branch.withCtes(nested.ctes())));
            } else {
                branches.add(block(term.selectQuery()));
            }
        }
        return new LineageQuery(ctes, branches);
    }

    private LineageQueryBlock block(HanaParser.SelectQueryContext context) {
        if (context.intoClause() != null) throw new IllegalArgumentException("SELECT INTO does not expose a query result");
        List<LineageRelation> relations = new ArrayList<>();
        if (context.fromClause() != null) {
            for (var source : context.fromClause().tableSource()) {
                LineageRelation relation = relation(source.tablePrimary());
                for (var join : source.joinClause()) {
                    relation = new LineageJoinRelation(relation, relation(join.tablePrimary()), false, List.of());
                }
                relations.add(relation);
            }
        }
        List<LineageSelectItem> items = new ArrayList<>();
        for (var item : context.selectItem()) {
            if (item.STAR() != null) {
                String qualifier = "";
                if (item.qualifiedName() != null) {
                    qualifier = String.join(".", names(item.qualifiedName()));
                }
                items.add(new LineageSelectItem(null, qualifier, range(item), List.of()));
                continue;
            }
            List<LineageValue> values = new ArrayList<>();
            values(item.expression(), values);
            String output;
            if (item.identifier() != null) {
                output = name(item.identifier());
            } else if (values.size() == 1 && values.get(0) instanceof LineageColumnReference column &&
                item.expression().getText().equals(String.join(".", rawColumn(item.expression())))) {
                output = column.column();
            } else if (values.isEmpty()) {
                output = item.expression().getText();
            } else {
                output = "";
            }
            items.add(new LineageSelectItem(output, null, range(item), values));
        }
        return new LineageQueryBlock(items, relations);
    }

    private LineageRelation relation(HanaParser.TablePrimaryContext context) {
        String alias = null;
        if (context.alias() != null) alias = name(context.alias().identifier());
        if (context.query() != null) return new LineageDerivedRelation(query(context.query()), alias, List.of());
        if (context.functionCall() != null || context.COLON() != null) {
            throw new IllegalArgumentException("Table function or SQLScript variable result lineage is not available");
        }
        List<String> parts = names(context.qualifiedName());
        if (parts.size() > 2) throw new IllegalArgumentException("Cross-database lineage is not supported");
        String schema = null;
        if (parts.size() == 2) schema = parts.get(0);
        return new LineageNamedRelation(null, schema, parts.get(parts.size() - 1), alias, List.of());
    }

    private void values(ParseTree tree, List<LineageValue> result) {
        if (tree instanceof HanaParser.QueryContext query) {
            result.add(new LineageSubqueryValue(query(query)));
            return;
        }
        if (tree instanceof HanaParser.VariableContext || tree instanceof HanaParser.ParameterMarkerContext) {
            throw new IllegalArgumentException("External value provenance is not available");
        }
        if (tree instanceof HanaParser.FunctionCallContext function && (!HanaSqlFunctions.isBuiltIn(function.qualifiedName()) ||
            function.qualifiedName().getText().equalsIgnoreCase("SESSION_CONTEXT"))) {
            throw new IllegalArgumentException("Stored function result provenance is not available");
        }
        if (tree instanceof HanaParser.PrimaryContext primary && primary.qualifiedName() != null) {
            var qualified = primary.qualifiedName();
            List<String> parts = names(qualified);
            var last = qualified.identifier(qualified.identifier().size() - 1);
            if (last.QUOTED_IDENTIFIER() == null && ((parts.size() == 1 && SESSION_VALUES.contains(parts.get(0))) ||
                (parts.size() > 1 && Set.of("NEXTVAL", "CURRVAL").contains(name(last))))) return;
            if (parts.size() > 3) throw new IllegalArgumentException("Cross-database column lineage is not supported");
            String qualifier = null;
            String schema = null;
            if (parts.size() >= 2) qualifier = parts.get(parts.size() - 2);
            if (parts.size() == 3) schema = parts.get(0);
            result.add(new LineageColumnReference(null, schema, qualifier, parts.get(parts.size() - 1), range(qualified)));
            return;
        }
        for (int i = 0; i < tree.getChildCount(); i++) values(tree.getChild(i), result);
    }

    // Only a bare column can use its identifier as the JDBC result label without an alias.
    private List<String> rawColumn(ParseTree tree) {
        if (tree instanceof HanaParser.PrimaryContext primary && primary.qualifiedName() != null) {
            return primary.qualifiedName().identifier().stream().map(ParseTree::getText).toList();
        }
        if (tree.getChildCount() == 1) return rawColumn(tree.getChild(0));
        return List.of();
    }

    private static List<String> names(HanaParser.QualifiedNameContext context) {
        return context.identifier().stream().map(HanaLineageVisitor::name).toList();
    }

    private static String name(HanaParser.IdentifierContext context) {
        String text = context.getText();
        if (context.QUOTED_IDENTIFIER() != null) return text.substring(1, text.length() - 1).replace("\"\"", "\"");
        return text.toUpperCase(Locale.ROOT);
    }

    private static SourceRange range(ParserRuleContext context) {
        var start = context.getStart();
        var stop = context.getStop();
        int line = stop.getLine();
        int column = stop.getCharPositionInLine();
        String text = stop.getText();
        for (int index = 0; index < text.length();) {
            int cp = text.codePointAt(index);
            index += Character.charCount(cp);
            if (cp == '\n') { line++; column = 0; }
            else column++;
        }
        return new SourceRange(start.getLine(), start.getCharPositionInLine(), line, column);
    }
}
