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
package com.clougence.sql.mysql.analysis.lineage.antlr;

import static com.clougence.sql.mysql.parser.antlr.MySqlParser.*;

import java.util.*;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.sql.common.analysis.lineage.model.*;
import com.clougence.sql.mysql.parser.antlr.MySqlParserBaseVisitor;

public final class MyLineageCstVisitor extends MySqlParserBaseVisitor<Void> {

    private final Parser parser;
    private LineageQuery query;

    public MyLineageCstVisitor(Parser parser){
        this.parser = parser;
    }

    public LineageQuery query() {
        if (query == null) {
            throw new IllegalArgumentException("SQL does not contain a supported MySQL query");
        }
        return query;
    }

    @Override
    public Void visitQuerySpecificationSelect(QuerySpecificationSelectContext context) {
        if (query == null) {
            query = buildSelectStatement(context);
        }
        return null;
    }

    @Override
    public Void visitQueryExpressionSelect(QueryExpressionSelectContext context) {
        if (query == null) {
            query = buildSelectStatement(context);
        }
        return null;
    }

    @Override
    public Void visitWithSelectStatement(WithSelectStatementContext context) {
        if (query == null) {
            query = buildWithSelect(context);
        }
        return null;
    }

    @Override
    public Void visitUnionTableValueSelect(UnionTableValueSelectContext context) {
        if (query == null) {
            query = buildSelectStatement(context);
        }
        return null;
    }

    @Override
    public Void visitTableStatement(TableStatementContext context) {
        if (query == null) {
            query = buildTableStatement(context);
        }
        return null;
    }

    @Override
    public Void visitValuesStatement(ValuesStatementContext context) {
        if (query == null) {
            query = buildValuesStatement(context);
        }
        return null;
    }

    private LineageQuery buildSelectStatement(SelectStatementContext context) {
        if (context instanceof QuerySpecificationSelectContext select) {
            List<LineageQueryBlock> branches = new ArrayList<>();
            branches.add(buildQueryBlock(select.querySpecification()));
            for (UnionStatementContext union : select.querySpecificationSelectTail().unionStatement()) {
                addScopedBranches(branches, buildUnion(union));
            }
            return new LineageQuery(List.of(), branches);
        }
        if (context instanceof QueryExpressionSelectContext select) {
            LineageQuery first = buildParenthesizedSelect(select.parenthesizedSelect());
            List<LineageQueryBlock> branches = new ArrayList<>();
            addScopedBranches(branches, first);
            for (UnionStatementContext union : select.queryExpressionSelectTail().unionStatement()) {
                addScopedBranches(branches, buildUnion(union));
            }
            return new LineageQuery(List.of(), branches);
        }
        if (context instanceof UnionTableValueSelectContext select) {
            LineageQuery first;
            if (select.tableStatement() != null) {
                first = buildTableStatement(select.tableStatement());
            } else {
                first = buildValuesStatement(select.valuesStatement());
            }
            List<LineageQueryBlock> branches = new ArrayList<>();
            addScopedBranches(branches, first);
            for (UnionStatementContext union : select.unionStatement()) {
                addScopedBranches(branches, buildUnion(union));
            }
            return new LineageQuery(List.of(), branches);
        }
        throw unsupported(context);
    }

    private void addScopedBranches(List<LineageQueryBlock> target, LineageQuery operand) {
        operand.branches().forEach(branch -> target.add(branch.withCtes(operand.ctes())));
    }

    private LineageQuery buildWithSelect(WithSelectStatementContext context) {
        List<LineageCte> ctes = new ArrayList<>();
        boolean recursive = context.withClause().RECURSIVE() != null;
        for (WithSelectExprContext cte : context.withClause().withSelectExpr()) {
            LineageQuery cteQuery;
            if (cte.withSelectStatement() != null) {
                cteQuery = buildWithSelect(cte.withSelectStatement());
            } else if (cte.selectStatement() != null) {
                cteQuery = buildSelectStatement(cte.selectStatement());
            } else if (cte.tableStatement() != null) {
                cteQuery = buildTableStatement(cte.tableStatement());
            } else if (cte.valuesStatement() != null) {
                cteQuery = buildValuesStatement(cte.valuesStatement());
            } else {
                throw unsupported(cte);
            }
            ctes.add(new LineageCte(identifier(cte.uid()), identifiers(cte.uidList()), cteQuery, recursive));
        }

        LineageQuery body;
        if (context.selectStatement() != null) {
            body = buildSelectStatement(context.selectStatement());
        } else if (context.tableStatement() != null) {
            body = buildTableStatement(context.tableStatement());
        } else if (context.valuesStatement() != null) {
            body = buildValuesStatement(context.valuesStatement());
        } else {
            throw unsupported(context);
        }
        List<LineageCte> allCtes = new ArrayList<>(ctes);
        allCtes.addAll(body.ctes());
        return body.withCtes(allCtes);
    }

    private LineageQuery buildUnion(UnionStatementContext context) {
        if (context.querySpecificationUnionOperand() != null) {
            return new LineageQuery(List.of(), List.of(buildQueryBlock(context.querySpecificationUnionOperand())));
        }
        if (context.querySpecification() != null) {
            return new LineageQuery(List.of(), List.of(buildQueryBlock(context.querySpecification())));
        }
        if (context.queryExpression() != null) {
            return buildQueryExpression(context.queryExpression());
        }
        if (context.legacyQueryExpression() != null) {
            return buildLegacyQueryExpression(context.legacyQueryExpression());
        }
        if (context.tableStatement() != null) {
            return buildTableStatement(context.tableStatement());
        }
        if (context.valuesStatement() != null) {
            return buildValuesStatement(context.valuesStatement());
        }
        throw unsupported(context);
    }

    private LineageQuery buildParenthesizedSelect(ParenthesizedSelectContext context) {
        if (context.queryExpression() != null) {
            return buildQueryExpression(context.queryExpression());
        }
        return buildLegacyQueryExpression(context.legacyQueryExpression());
    }

    private LineageQuery buildQueryExpression(QueryExpressionContext context) {
        if (context.selectStatement() != null) {
            return buildSelectStatement(context.selectStatement());
        }
        if (context.withSelectStatement() != null) {
            return buildWithSelect(context.withSelectStatement());
        }
        if (context.tableStatement() != null) {
            return buildTableStatement(context.tableStatement());
        }
        if (context.valuesStatement() != null) {
            return buildValuesStatement(context.valuesStatement());
        }
        throw unsupported(context);
    }

    private LineageQuery buildLegacyQueryExpression(LegacyQueryExpressionContext context) {
        if (context.querySpecification() != null) {
            return new LineageQuery(List.of(), List.of(buildQueryBlock(context.querySpecification())));
        }
        return buildLegacyQueryExpression(context.legacyQueryExpression());
    }

    private LineageQuery buildSubquery(SubqueryStatementContext context) {
        if (context.withSelectStatement() != null) {
            return buildWithSelect(context.withSelectStatement());
        }
        if (context.selectStatement() != null) {
            return buildSelectStatement(context.selectStatement());
        }
        if (context.tableStatement() != null) {
            return buildTableStatement(context.tableStatement());
        }
        if (context.valuesStatement() != null) {
            return buildValuesStatement(context.valuesStatement());
        }
        throw unsupported(context);
    }

    private LineageQuery buildTableStatement(TableStatementContext context) {
        LineageNamedRelation relation = namedRelation(context.tableName(), null);
        LineageSelectItem wildcard = new LineageSelectItem("*", "", MyLineageTokenRangeFactory.from(context.tableName()), List.of());
        return new LineageQuery(List.of(), List.of(new LineageQueryBlock(List.of(wildcard), List.of(relation))));
    }

    private LineageQuery buildValuesStatement(ValuesStatementContext context) {
        List<LineageQueryBlock> rows = new ArrayList<>();
        for (ExplicitValuesRowContext row : context.explicitValuesRow()) {
            List<LineageSelectItem> items = new ArrayList<>();
            ExpressionsWithDefaultsContext expressions = row.expressionsWithDefaults();
            if (expressions != null) {
                List<ExpressionOrDefaultContext> values = expressions.expressionOrDefault();
                for (int index = 0; index < values.size(); index++) {
                    ExpressionOrDefaultContext value = values.get(index);
                    List<LineageValue> sources = new ArrayList<>();
                    if (value.expression() != null) {
                        collectExpressionValues(value.expression(), sources);
                    }
                    items.add(new LineageSelectItem("column_" + index, null, MyLineageTokenRangeFactory.from(value), sources));
                }
            }
            rows.add(new LineageQueryBlock(items, List.of()));
        }
        return new LineageQuery(List.of(), rows);
    }

    private LineageQueryBlock buildQueryBlock(ParserRuleContext context) {
        SelectElementsContext selectElements;
        FromClauseContext fromClause;
        WindowClauseContext windowClause;
        if (context instanceof QuerySpecificationContext specification) {
            selectElements = specification.selectElements();
            fromClause = specification.fromClause();
            windowClause = specification.windowClause();
        } else if (context instanceof QuerySpecificationUnionOperandContext specification) {
            selectElements = specification.selectElements();
            fromClause = specification.fromClause();
            windowClause = specification.windowClause();
        } else {
            throw unsupported(context);
        }

        List<LineageSelectItem> items = buildSelectItems(selectElements, namedWindows(windowClause));
        List<LineageRelation> relations = fromClause == null ? List.of() : buildTableSources(fromClause.tableSources());
        return new LineageQueryBlock(items, relations);
    }

    private Map<String, Window_specificationContext> namedWindows(WindowClauseContext windowClause) {
        Map<String, Window_specificationContext> windows = new LinkedHashMap<>();
        if (windowClause != null) {
            for (WindowDefinitionContext definition : windowClause.windowDefinition()) {
                windows.put(identifier(definition.uid()).toLowerCase(Locale.ROOT), definition.window_specification());
            }
        }
        return Map.copyOf(windows);
    }

    private List<LineageSelectItem> buildSelectItems(SelectElementsContext context, Map<String, Window_specificationContext> namedWindows) {
        List<LineageSelectItem> items = new ArrayList<>();
        if (context.star != null) {
            items.add(new LineageSelectItem("*", "", MyLineageTokenRangeFactory.from(context.star), List.of()));
        }
        for (SelectElementContext element : context.selectElement()) {
            if (element instanceof SelectStarElementContext star) {
                String qualifier = identifier(star.table);
                if (star.schema != null) {
                    qualifier = identifier(star.schema) + "." + qualifier;
                }
                items.add(new LineageSelectItem("*", qualifier, MyLineageTokenRangeFactory.from(star), List.of()));
                continue;
            }

            SelectExpressionElementContext expressionElement = (SelectExpressionElementContext) element;
            List<LineageValue> values = new ArrayList<>();
            collectExpressionValues(expressionElement.expression(), values, namedWindows);

            String outputName;
            if (expressionElement.selectAlias() != null) {
                outputName = identifier(expressionElement.selectAlias());
            } else {
                outputName = text(expressionElement.expression());
                if (values.size() == 1 && values.get(0) instanceof LineageColumnReference reference && sameText(expressionElement.expression(), reference)) {
                    outputName = reference.column();
                }
            }
            items.add(new LineageSelectItem(outputName, null, MyLineageTokenRangeFactory.from(expressionElement), values));
        }
        return List.copyOf(items);
    }

    private boolean sameText(ExpressionContext expression, LineageColumnReference reference) {
        String expressionText = removeWhitespaceAndBackticks(text(expression));
        StringBuilder expected = new StringBuilder();
        if (reference.catalog() != null) {
            expected.append(reference.catalog()).append('.');
        }
        if (reference.schema() != null) {
            expected.append(reference.schema()).append('.');
        }
        if (reference.qualifier() != null) {
            expected.append(reference.qualifier()).append('.');
        }
        expected.append(reference.column());
        return expressionText.equalsIgnoreCase(expected.toString());
    }

    private String removeWhitespaceAndBackticks(String value) {
        StringBuilder normalized = new StringBuilder(value.length());
        for (int index = 0; index < value.length(); index++) {
            char character = value.charAt(index);
            if (character != '`' && !isAsciiWhitespace(character)) {
                normalized.append(character);
            }
        }
        return normalized.toString();
    }

    private boolean isAsciiWhitespace(char character) {
        return character == ' ' || character == '\t' || character == '\n' || character == '\u000B' || character == '\f' || character == '\r';
    }

    private void collectExpressionValues(ParseTree node, List<LineageValue> values) {
        collectExpressionValues(node, values, Map.of());
    }

    private void collectExpressionValues(ParseTree node, List<LineageValue> values, Map<String, Window_specificationContext> namedWindows) {
        collectExpressionValues(node, values, false, namedWindows, new HashSet<>());
    }

    private void collectExpressionValues(ParseTree node, List<LineageValue> values, boolean selectAliasVisible, Map<String, Window_specificationContext> namedWindows,
                                         Set<String> resolvingWindows) {
        if (node instanceof FullColumnNameContext column) {
            values.add(columnReference(column, selectAliasVisible));
            return;
        }
        if (node instanceof SubqueryStatementContext subquery) {
            values.add(new LineageSubqueryValue(buildSubquery(subquery)));
            return;
        }
        if (node instanceof NonKeywordFunctionCallContext function && (function.intervalType() != null || function.intervalTypeBase() != null)) {
            function.expression().forEach(expression -> {
                collectExpressionValues(expression, values, selectAliasVisible, namedWindows, resolvingWindows);
            });
            return;
        }
        if (node instanceof OverClauseContext overClause) {
            if (overClause.window_specification() != null) {
                collectExpressionValues(overClause.window_specification(), values, true, namedWindows, resolvingWindows);
            } else if (overClause.uid() != null) {
                collectNamedWindow(identifier(overClause.uid()), values, namedWindows, resolvingWindows);
            }
            return;
        }
        if (node instanceof Window_specificationContext window) {
            if (window.uid() != null) {
                collectNamedWindow(identifier(window.uid()), values, namedWindows, resolvingWindows);
            }
            for (int i = 0; i < node.getChildCount(); i++) {
                collectExpressionValues(node.getChild(i), values, true, namedWindows, resolvingWindows);
            }
            return;
        }
        boolean childAliasVisible = selectAliasVisible || node instanceof Window_specificationContext;
        for (int i = 0; i < node.getChildCount(); i++) {
            collectExpressionValues(node.getChild(i), values, childAliasVisible, namedWindows, resolvingWindows);
        }
    }

    private void collectNamedWindow(String name, List<LineageValue> values, Map<String, Window_specificationContext> namedWindows, Set<String> resolvingWindows) {
        String key = name.toLowerCase(Locale.ROOT);
        Window_specificationContext window = namedWindows.get(key);
        if (window == null || !resolvingWindows.add(key)) {
            return;
        }
        try {
            collectExpressionValues(window, values, true, namedWindows, resolvingWindows);
        } finally {
            resolvingWindows.remove(key);
        }
    }

    private LineageColumnReference columnReference(FullColumnNameContext context, boolean selectAliasVisible) {
        List<String> parts = identifierPath(text(context));
        String catalog = null;
        String schema = null;
        String qualifier = null;
        String column = parts.get(parts.size() - 1);
        if (parts.size() == 2) {
            qualifier = parts.get(0);
        } else if (parts.size() == 3) {
            schema = parts.get(0);
            qualifier = parts.get(1);
        } else if (parts.size() >= 4) {
            catalog = parts.get(parts.size() - 4);
            schema = parts.get(parts.size() - 3);
            qualifier = parts.get(parts.size() - 2);
        }
        return new LineageColumnReference(catalog, schema, qualifier, column, MyLineageTokenRangeFactory.from(context), selectAliasVisible);
    }

    private List<LineageRelation> buildTableSources(TableSourcesContext context) {
        return context.tableSource().stream().map(this::buildTableSource).toList();
    }

    private LineageRelation buildTableSource(TableSourceContext context) {
        if (context instanceof TableSourceOdbcContext odbc) {
            return buildTableSource(odbc.tableSource());
        }
        if (!(context instanceof TableSourceBaseContext base)) {
            throw unsupported(context);
        }

        LineageRelation current = buildTableItem(base.tableSourceItem());
        for (JoinPartContext join : base.joinPart()) {
            LineageRelation right = buildJoinRight(join);
            UidListContext using = join.getRuleContext(UidListContext.class, 0);
            current = new LineageJoinRelation(current, right, join instanceof NaturalJoinContext, identifiers(using));
        }
        return current;
    }

    private LineageRelation buildJoinRight(JoinPartContext context) {
        for (int i = 0; i < context.getChildCount(); i++) {
            ParseTree child = context.getChild(i);
            if (child instanceof TableSourceItemContext item) {
                return buildTableItem(item);
            }
            if (child instanceof TableSourceContext tableSource) {
                return buildTableSource(tableSource);
            }
        }
        throw unsupported(context);
    }

    private LineageRelation buildTableItem(TableSourceItemContext context) {
        if (context instanceof AtomTableItemContext table) {
            return namedRelation(table.tableName(), identifier(table.aliasName()));
        }
        if (context instanceof SubqueryTableItemContext subquery) {
            return new LineageDerivedRelation(buildQueryExpression(subquery.queryExpression()), identifier(subquery.aliasName()), identifiers(subquery.uidList()));
        }
        if (context instanceof LateralTableItemContext lateral) {
            return new LineageDerivedRelation(buildSubquery(lateral.subqueryStatement()), identifier(lateral.aliasName()), identifiers(lateral.uidList()), true);
        }
        if (context instanceof TableSourcesItemContext nested) {
            List<LineageRelation> relations = buildTableSources(nested.tableSources());
            if (relations.isEmpty()) {
                throw unsupported(context);
            }
            LineageRelation group = relations.get(0);
            for (int i = 1; i < relations.size(); i++) {
                group = new LineageJoinRelation(group, relations.get(i), false, List.of());
            }
            return group;
        }
        if (context instanceof JsonTableItemContext jsonTable) {
            List<LineageValue> values = new ArrayList<>();
            collectExpressionValues(jsonTable.jsonTableFunction().expression(), values);
            List<LineageTableFunctionColumn> columns = new ArrayList<>();
            for (JsonTableColumnContext column : jsonTable.jsonTableFunction().jsonTableColumn()) {
                collectJsonTableColumns(column, values, columns);
            }
            return new LineageTableFunctionRelation(identifier(jsonTable.aliasName()), columns);
        }
        throw unsupported(context);
    }

    private LineageNamedRelation namedRelation(TableNameContext context, String alias) {
        List<String> parts = identifierPath(text(context));
        String catalog = null;
        String schema = null;
        String name = parts.get(parts.size() - 1);
        if (parts.size() == 2) {
            schema = parts.get(0);
        } else if (parts.size() >= 3) {
            catalog = parts.get(parts.size() - 3);
            schema = parts.get(parts.size() - 2);
        }
        return new LineageNamedRelation(catalog, schema, name, alias, List.of());
    }

    private void collectJsonTableColumns(JsonTableColumnContext context, List<LineageValue> values, List<LineageTableFunctionColumn> columns) {
        if (context instanceof JsonTableNestedColumnContext nested) {
            for (JsonTableColumnContext child : nested.jsonTableColumn()) {
                collectJsonTableColumns(child, values, columns);
            }
            return;
        }
        for (int i = 0; i < context.getChildCount(); i++) {
            if (context.getChild(i) instanceof UidContext uid) {
                columns.add(new LineageTableFunctionColumn(identifier(uid), values));
                return;
            }
        }
    }

    private List<String> identifiers(UidListContext context) {
        if (context == null) {
            return List.of();
        }
        return context.uid().stream().map(this::identifier).toList();
    }

    private String identifier(ParserRuleContext context) {
        if (context == null) {
            return null;
        }
        return unquoteIdentifier(text(context));
    }

    private List<String> identifierPath(String value) {
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        char quote = 0;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if ((c == '`' || c == '"') && (quote == 0 || quote == c)) {
                if (quote == c && i + 1 < value.length() && value.charAt(i + 1) == c) {
                    current.append(c);
                    i++;
                } else {
                    quote = quote == 0 ? c : 0;
                }
            } else if (c == '.' && quote == 0) {
                if (!current.isEmpty()) {
                    parts.add(current.toString());
                    current.setLength(0);
                }
            } else {
                current.append(c);
            }
        }
        if (!current.isEmpty()) {
            parts.add(current.toString());
        }
        return parts;
    }

    private String unquoteIdentifier(String value) {
        String result = value.strip();
        if (result.length() >= 2) {
            char first = result.charAt(0);
            char last = result.charAt(result.length() - 1);
            if ((first == '`' && last == '`') || (first == '"' && last == '"') || (first == '\'' && last == '\'')) {
                result = result.substring(1, result.length() - 1);
                result = result.replace(String.valueOf(first) + first, String.valueOf(first));
            }
        }
        return result;
    }

    private String text(ParserRuleContext context) {
        return parser.getTokenStream().getText(context.getStart(), context.getStop());
    }

    private IllegalArgumentException unsupported(ParserRuleContext context) {
        return new IllegalArgumentException("Unsupported MySQL lineage context " + context.getClass().getSimpleName() + ": " + text(context));
    }
}
