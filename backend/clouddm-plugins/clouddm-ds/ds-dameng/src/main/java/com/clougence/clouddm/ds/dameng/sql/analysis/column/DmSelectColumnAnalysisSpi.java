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
package com.clougence.clouddm.ds.dameng.sql.analysis.column;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.dameng.sql.parser.DmDslProvider;
import com.clougence.clouddm.ds.dameng.sql.parser.antlr.DmSqlParser;
import com.clougence.clouddm.sdk.sql.analysis.column.ContextInfo;
import com.clougence.clouddm.sdk.sql.analysis.column.RealColumn;
import com.clougence.clouddm.sdk.sql.analysis.column.SelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.column.SelectItem;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

public class DmSelectColumnAnalysisSpi implements SelectColumnAnalysisSpi {

    private static final Set<String> NON_COLUMN_NAMES = Set.of(
        "DBTIMEZONE", "SESSIONTIMEZONE", "LEVEL", "CONNECT_BY_ISLEAF",
        "CONNECT_BY_ISCYCLE", "ROWNUM");
    private static final Set<String> DATE_PART_FUNCTIONS = Set.of(
        "BIGDATEDIFF", "DATEADD", "DATEDIFF", "DATEPART", "TIMESTAMPADD", "TIMESTAMPDIFF");

    @Override
    public List<SelectItem> parseSelectColumn(String script, ContextInfo contextInfo) {
        List<SelectItem> result = new ArrayList<>();
        Object catalogLevel = contextInfo == null || contextInfo.getLevelsParam() == null
            ? null
            : contextInfo.getLevelsParam().get(UmiTypes.Catalog);
        String defaultCatalog = catalogLevel == null ? null : String.valueOf(catalogLevel);
        for (AstSplitScript splitScript : DslHelper.splitDsl(DmDslProvider.INSTANCE, script)) {
            if (!(splitScript.getAstTree() instanceof DmSqlParser.StatementContext statement)) {
                continue;
            }
            if (statement.selectStatement() != null) {
                result.addAll(selectStatementItems(statement.selectStatement(), new LinkedHashMap<>()));
            } else if (statement.createStatement() != null) {
                collectDefinitionSelectItems(statement.createStatement(), result, defaultCatalog);
            } else if (statement.explainStatement() != null) {
                collectSelectItems(statement.explainStatement(), result);
            }
        }
        return result;
    }

    private void collectDefinitionSelectItems(ParseTree tree, List<SelectItem> result, String defaultCatalog) {
        if (tree instanceof DmSqlParser.SchemaCreateContext schemaCreate) {
            List<SelectItem> schemaItems = new ArrayList<>();
            for (int i = 0; i < tree.getChildCount(); i++) {
                collectDefinitionSelectItems(tree.getChild(i), schemaItems, defaultCatalog);
            }
            applyDefaultScope(schemaItems, defaultCatalog, schemaName(schemaCreate));
            result.addAll(schemaItems);
            return;
        }
        if (tree instanceof DmSqlParser.ViewCreateContext viewCreate) {
            List<SelectItem> items = selectStatementItems(viewCreate.selectStatement(), new LinkedHashMap<>());
            result.addAll(applyColumnAliases(items, viewCreate.columnNameList()));
            return;
        }
        if (tree instanceof DmSqlParser.TableCreateContext) {
            collectSelectItems(tree, result);
            return;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectDefinitionSelectItems(tree.getChild(i), result, defaultCatalog);
        }
    }

    private void collectSelectItems(ParseTree tree, List<SelectItem> result) {
        if (tree instanceof DmSqlParser.SelectStatementContext selectStatement) {
            result.addAll(selectStatementItems(selectStatement, new LinkedHashMap<>()));
            return;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectSelectItems(tree.getChild(i), result);
        }
    }

    private List<SelectItem> selectStatementItems(DmSqlParser.SelectStatementContext ctx, Map<String, List<SelectItem>> outerDerived) {
        Map<String, List<SelectItem>> derived = new LinkedHashMap<>(outerDerived);
        collectCteColumns(ctx.withClause(), derived);

        List<SelectItem> result = selectOperandItems(ctx.selectOperand(), derived);
        for (DmSqlParser.QueryRemainderContext queryRemainderContext : ctx.queryRemainder()) {
            mergeByPosition(result, selectOperandItems(queryRemainderContext.selectOperand(), derived));
        }
        restrictCorrespondingColumns(result, ctx.queryRemainder());
        return result;
    }

    private List<SelectItem> selectOperandItems(DmSqlParser.SelectOperandContext ctx, Map<String, List<SelectItem>> derived) {
        if (ctx.selectQuery() != null) {
            return selectQueryItems(ctx.selectQuery(), derived);
        }
        return selectStatementItems(ctx.selectStatement(), derived);
    }

    private List<SelectItem> selectQueryItems(DmSqlParser.SelectQueryContext ctx, Map<String, List<SelectItem>> visibleDerived) {
        Map<String, NameParts> tables = new LinkedHashMap<>();
        Map<String, List<SelectItem>> derived = new LinkedHashMap<>();
        if (ctx.fromClause() != null) {
            for (DmSqlParser.TableSourceContext tableSourceContext : ctx.fromClause().tableSource()) {
                collectTables(tableSourceContext, tables, derived, visibleDerived);
            }
        }

        List<SelectItem> result = new ArrayList<>();
        for (DmSqlParser.SelectItemContext selectItemContext : ctx.selectList().selectItem()) {
            result.add(selectItem(selectItemContext, tables, derived));
        }
        return result;
    }

    private SelectItem selectItem(DmSqlParser.SelectItemContext ctx, Map<String, NameParts> tables, Map<String, List<SelectItem>> derived) {
        SelectItem item = new SelectItem();
        if (ctx.STAR() != null) {
            item.setItemAlias("*");
            for (NameParts table : tables.values()) {
                item.addRealColumn(realColumn(table, "*", false));
            }
            for (List<SelectItem> items : uniqueDerivedColumns(derived)) {
                addProjectedColumns(item, items);
            }
            return item;
        }
        if (ctx.qualifiedName() != null) {
            NameParts name = NameParts.from(ctx.qualifiedName());
            item.setItemAlias("*");
            item.setTableAlias(name.name());
            List<SelectItem> derivedItems = findDerived(name.name(), derived);
            if (derivedItems != null) {
                addProjectedColumns(item, derivedItems);
                return item;
            }
            item.addRealColumn(realColumn(resolveTable(name.name(), tables), "*", false));
            return item;
        }

        DmSqlParser.IdentifierContext alias = null;
        if (ctx.aliasIdentifier() != null) {
            alias = ctx.aliasIdentifier().identifier();
        }
        DmSqlParser.QualifiedNameContext directColumn = directColumn(ctx.expression());
        if (alias != null) {
            item.setItemAlias(NameParts.clean(alias.getText()));
        } else if (directColumn != null) {
            item.setItemAlias(NameParts.from(directColumn).name());
        } else {
            item.setItemAlias(ctx.expression().getText());
        }

        if (directColumn != null) {
            NameParts column = NameParts.from(directColumn);
            item.setTableAlias(column.schema());
            addColumn(item, column, tables, derived, true);
        } else {
            List<DmSqlParser.QualifiedNameContext> columns = new ArrayList<>();
            collectQualifiedNames(ctx.expression(), columns);
            for (DmSqlParser.QualifiedNameContext columnContext : columns) {
                addColumn(item, NameParts.from(columnContext), tables, derived, false);
            }
        }
        return item;
    }

    private void addColumn(SelectItem item, NameParts column, Map<String, NameParts> tables,
                           Map<String, List<SelectItem>> derived, boolean onlyOneColumn) {
        String upperName = column.name() == null ? null : column.name().toUpperCase(Locale.ROOT);
        if (upperName != null && ("NEXTVAL".equals(upperName) || "CURRVAL".equals(upperName))) {
            return;
        }
        if (!addDerivedColumn(item, column, derived)) {
            if (column.catalog() != null) {
                NameParts attributeTable = findTable(column.catalog(), tables);
                if (attributeTable != null) {
                    item.addRealColumn(realColumn(attributeTable, column.schema(), onlyOneColumn));
                    return;
                }
            }
            NameParts table = resolveColumnTable(column, tables);
            RealColumn realColumn = realColumn(table, column.name(), onlyOneColumn);
            item.addRealColumn(realColumn);
        }
    }

    private void mergeByPosition(List<SelectItem> target, List<SelectItem> branch) {
        int size = Math.min(target.size(), branch.size());
        for (int i = 0; i < size; i++) {
            target.get(i).addAllRealColumns(branch.get(i).getColumns());
        }
    }

    private void collectCteColumns(DmSqlParser.WithClauseContext ctx, Map<String, List<SelectItem>> derived) {
        if (ctx == null || ctx.cteDefinitionList() == null) {
            return;
        }
        for (DmSqlParser.CteDefinitionContext cteDefinitionContext : ctx.cteDefinitionList().cteDefinition()) {
            String cteName = NameParts.clean(cteDefinitionContext.identifier().getText());
            List<SelectItem> items = selectStatementItems(cteDefinitionContext.selectStatement(), derived);
            for (SelectItem item : items) {
                item.getColumns().removeIf(column -> column.getCatalog() == null
                    && column.getSchema() == null
                    && cteName.equalsIgnoreCase(column.getTable()));
            }
            items = applyDerivedColumnAliases(items, cteDefinitionContext.columnNameList());
            putDerived(derived, cteName, items);
        }
    }

    private void collectTables(DmSqlParser.TableSourceContext ctx, Map<String, NameParts> tables, Map<String, List<SelectItem>> derived,
                               Map<String, List<SelectItem>> visibleDerived) {
        collectTables(ctx.tablePrimary(), tables, derived, visibleDerived);
        for (DmSqlParser.JoinClauseContext joinClauseContext : ctx.joinClause()) {
            if (joinClauseContext.tablePrimary() != null) {
                collectTables(joinClauseContext.tablePrimary(), tables, derived, visibleDerived);
            } else if (joinClauseContext.applyJoinClause() != null) {
                collectTables(joinClauseContext.applyJoinClause().tablePrimary(), tables, derived, visibleDerived);
            }
        }
    }

    private void collectTables(DmSqlParser.TablePrimaryContext ctx, Map<String, NameParts> tables, Map<String, List<SelectItem>> derived,
                               Map<String, List<SelectItem>> visibleDerived) {
        if (ctx.arrayTableExpression() != null) {
            SelectItem item = new SelectItem();
            item.setItemAlias("COLUMN_VALUE");
            String alias = tableAlias(ctx.tableAlias());
            putDerived(derived, alias == null ? "__ARRAY_" + derived.size() : alias, List.of(item));
        } else if (ctx.tableCollectionExpression() != null) {
            SelectItem item = new SelectItem();
            item.setItemAlias("COLUMN_VALUE");
            DmSqlParser.TableCollectionExpressionContext collection = ctx.tableCollectionExpression();
            if (collection.selectStatement() != null) {
                addProjectedColumns(item, selectStatementItems(collection.selectStatement(), visibleDerived));
            } else {
                List<DmSqlParser.QualifiedNameContext> sourceColumns = new ArrayList<>();
                collectQualifiedNames(collection.expression(), sourceColumns);
                for (DmSqlParser.QualifiedNameContext sourceColumn : sourceColumns) {
                    addColumn(item, NameParts.from(sourceColumn), tables, derived, false);
                }
            }
            String alias = tableAlias(ctx.tableAlias());
            putDerived(derived, alias == null ? "__COLLECTION_" + derived.size() : alias, List.of(item));
        } else if (ctx.xmlTableExpression() != null) {
            List<DmSqlParser.QualifiedNameContext> sourceColumns = new ArrayList<>();
            if (ctx.xmlTableExpression().xmlPassingClause() != null) {
                collectQualifiedNames(ctx.xmlTableExpression().xmlPassingClause(), sourceColumns);
            }
            List<SelectItem> projected = new ArrayList<>();
            if (ctx.xmlTableExpression().xmlTableColumnsClause() != null) {
                for (DmSqlParser.XmlTableColumnContext columnContext
                    : ctx.xmlTableExpression().xmlTableColumnsClause().xmlTableColumn()) {
                    SelectItem item = new SelectItem();
                    item.setItemAlias(NameParts.clean(columnContext.identifier().getText()));
                    for (DmSqlParser.QualifiedNameContext sourceColumn : sourceColumns) {
                        addColumn(item, NameParts.from(sourceColumn), tables, derived, false);
                    }
                    projected.add(item);
                }
            }
            putDerived(derived, tableAlias(ctx.tableAlias()), projected);
        } else if (ctx.qualifiedName() != null) {
            NameParts name = NameParts.from(ctx.qualifiedName());
            if (name.name() != null) {
                List<SelectItem> derivedItems = name.schema() == null ? findDerived(name.name(), visibleDerived) : null;
                if (derivedItems != null) {
                    derivedItems = applyDerivedColumnAliases(derivedItems, ctx.derivedColumnList());
                    derivedItems = applyPivotColumns(ctx, derivedItems, tables, derived);
                    putDerived(derived, name.name(), derivedItems);
                    putDerived(derived, tableAlias(ctx.tableAlias()), derivedItems);
                    return;
                }
                tables.putIfAbsent(name.name(), name);
                if (ctx.tableAlias() != null) {
                    tables.putIfAbsent(
                        NameParts.clean(ctx.tableAlias().aliasIdentifier().identifier().getText()), name);
                }
                List<SelectItem> pivotItems = applyPivotColumns(ctx, null, tables, derived);
                if (pivotItems != null) {
                    putDerived(derived, name.name(), pivotItems);
                    putDerived(derived, tableAlias(ctx.tableAlias()), pivotItems);
                }
            }
        } else if (ctx.selectStatement() != null) {
            String alias = tableAlias(ctx.tableAlias());
            if (alias != null) {
                List<SelectItem> items = selectStatementItems(ctx.selectStatement(), visibleDerived);
                items = applyPivotColumns(ctx, items, tables, derived);
                putDerived(derived, alias, applyDerivedColumnAliases(items, ctx.derivedColumnList()));
            }
        }
        for (DmSqlParser.TableSourceContext tableSourceContext : ctx.tableSource()) {
            collectTables(tableSourceContext, tables, derived, visibleDerived);
        }
    }

    private List<SelectItem> applyPivotColumns(DmSqlParser.TablePrimaryContext ctx, List<SelectItem> sourceItems,
                                               Map<String, NameParts> tables, Map<String, List<SelectItem>> derived) {
        if (ctx.tablePivotClause().isEmpty()) {
            return sourceItems;
        }
        List<SelectItem> items = sourceItems == null ? new ArrayList<>() : copySelectItems(sourceItems);
        for (DmSqlParser.TablePivotClauseContext tablePivot : ctx.tablePivotClause()) {
            DmSqlParser.PivotClauseContext pivot = tablePivot.pivotClause();
            if (pivot.PIVOT() != null) {
                items = pivotColumns(pivot, items, sourceItems != null, tables, derived);
            } else {
                items = unpivotColumns(pivot, items, sourceItems != null, tables, derived);
            }
        }
        return items;
    }

    private List<SelectItem> pivotColumns(DmSqlParser.PivotClauseContext ctx, List<SelectItem> sourceItems,
                                          boolean sourceColumnsKnown, Map<String, NameParts> tables,
                                          Map<String, List<SelectItem>> derived) {
        List<NameParts> groupingInputs = qualifiedNames(ctx.pivotForClause());
        List<NameParts> aggregateInputs = new ArrayList<>();
        for (DmSqlParser.PivotExpressionContext expression : ctx.pivotExpressionList().pivotExpression()) {
            aggregateInputs.addAll(qualifiedNames(expression.functionCall()));
        }

        List<SelectItem> result = copyUnconsumedItems(sourceItems, groupingInputs, aggregateInputs);
        if (ctx.pivotInClauseList() == null) {
            SelectItem xmlItem = new SelectItem();
            StringBuilder alias = new StringBuilder();
            for (NameParts groupingInput : groupingInputs) {
                if (!alias.isEmpty()) {
                    alias.append('_');
                }
                alias.append(groupingInput.name());
            }
            xmlItem.setItemAlias(alias.append("_XML").toString());
            addSourceColumns(xmlItem, groupingInputs, sourceItems, sourceColumnsKnown, tables, derived);
            addSourceColumns(xmlItem, aggregateInputs, sourceItems, sourceColumnsKnown, tables, derived);
            result.add(xmlItem);
            return result;
        }
        List<DmSqlParser.PivotExpressionContext> expressions = ctx.pivotExpressionList().pivotExpression();
        for (DmSqlParser.PivotInClauseContext inClause : ctx.pivotInClauseList().pivotInClause()) {
            String valueAlias = pivotValueAlias(inClause);
            for (DmSqlParser.PivotExpressionContext expression : expressions) {
                SelectItem item = new SelectItem();
                String aggregateAlias = expression.identifier() == null
                    ? null
                    : NameParts.clean(expression.identifier().getText());
                if (aggregateAlias == null) {
                    item.setItemAlias(valueAlias);
                } else {
                    item.setItemAlias(valueAlias + "_" + aggregateAlias);
                }
                addSourceColumns(item, groupingInputs, sourceItems, sourceColumnsKnown, tables, derived);
                addSourceColumns(item, qualifiedNames(expression.functionCall()), sourceItems,
                    sourceColumnsKnown, tables, derived);
                result.add(item);
            }
        }
        return result;
    }

    private List<SelectItem> unpivotColumns(DmSqlParser.PivotClauseContext ctx, List<SelectItem> sourceItems,
                                            boolean sourceColumnsKnown, Map<String, NameParts> tables,
                                            Map<String, List<SelectItem>> derived) {
        List<List<NameParts>> inputGroups = new ArrayList<>();
        List<NameParts> allInputs = new ArrayList<>();
        for (DmSqlParser.UnpivotInClauseContext inClause : ctx.unpivotInClauseList().unpivotInClause()) {
            List<NameParts> inputs = qualifiedNames(inClause);
            inputGroups.add(inputs);
            allInputs.addAll(inputs);
        }
        List<SelectItem> result = copyUnconsumedItems(sourceItems, allInputs);
        for (DmSqlParser.IdentifierContext identifier : unpivotIdentifiers(ctx.unpivotForClause())) {
            SelectItem item = new SelectItem();
            item.setItemAlias(NameParts.clean(identifier.getText()));
            result.add(item);
        }
        List<DmSqlParser.IdentifierContext> values = unpivotIdentifiers(ctx.unpivotValueClause());
        for (int valueIndex = 0; valueIndex < values.size(); valueIndex++) {
            SelectItem item = new SelectItem();
            item.setItemAlias(NameParts.clean(values.get(valueIndex).getText()));
            for (List<NameParts> inputGroup : inputGroups) {
                if (valueIndex < inputGroup.size()) {
                    addSourceColumns(item, List.of(inputGroup.get(valueIndex)), sourceItems,
                        sourceColumnsKnown, tables, derived);
                }
            }
            result.add(item);
        }
        return result;
    }

    @SafeVarargs
    private final List<SelectItem> copyUnconsumedItems(List<SelectItem> sourceItems, List<NameParts>... consumedGroups) {
        List<SelectItem> result = new ArrayList<>();
        for (SelectItem sourceItem : sourceItems) {
            boolean consumed = false;
            for (List<NameParts> consumedGroup : consumedGroups) {
                for (NameParts consumedColumn : consumedGroup) {
                    if (consumedColumn.name() != null
                        && consumedColumn.name().equalsIgnoreCase(sourceItem.getItemAlias())) {
                        consumed = true;
                        break;
                    }
                }
                if (consumed) {
                    break;
                }
            }
            if (!consumed) {
                result.add(copySelectItem(sourceItem));
            }
        }
        return result;
    }

    private void addSourceColumns(SelectItem target, List<NameParts> columns, List<SelectItem> sourceItems,
                                  boolean sourceColumnsKnown, Map<String, NameParts> tables,
                                  Map<String, List<SelectItem>> derived) {
        for (NameParts column : columns) {
            boolean matched = false;
            for (SelectItem sourceItem : sourceItems) {
                if (column.name() != null && column.name().equalsIgnoreCase(sourceItem.getItemAlias())) {
                    target.addAllRealColumns(sourceItem.getColumns());
                    matched = true;
                    break;
                }
            }
            if (!matched && !sourceColumnsKnown) {
                addColumn(target, column, tables, derived, false);
            }
        }
    }

    private List<NameParts> qualifiedNames(ParseTree tree) {
        List<DmSqlParser.QualifiedNameContext> contexts = new ArrayList<>();
        collectQualifiedNames(tree, contexts);
        List<NameParts> names = new ArrayList<>();
        for (DmSqlParser.QualifiedNameContext context : contexts) {
            names.add(NameParts.from(context));
        }
        return names;
    }

    private List<DmSqlParser.IdentifierContext> unpivotIdentifiers(ParseTree tree) {
        List<DmSqlParser.IdentifierContext> identifiers = new ArrayList<>();
        collectIdentifiers(tree, identifiers);
        return identifiers;
    }

    private void collectIdentifiers(ParseTree tree, List<DmSqlParser.IdentifierContext> identifiers) {
        if (tree instanceof DmSqlParser.IdentifierContext identifier) {
            identifiers.add(identifier);
            return;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectIdentifiers(tree.getChild(i), identifiers);
        }
    }

    private String pivotValueAlias(DmSqlParser.PivotInClauseContext ctx) {
        if (ctx.identifier() != null) {
            return NameParts.clean(ctx.identifier().getText());
        }
        String text = ctx.expression() == null ? ctx.expressionList().getText() : ctx.expression().getText();
        if (text.length() >= 2 && text.startsWith("'") && text.endsWith("'")) {
            return text.substring(1, text.length() - 1).replace("''", "'");
        }
        return text;
    }

    private List<SelectItem> copySelectItems(List<SelectItem> items) {
        List<SelectItem> result = new ArrayList<>();
        for (SelectItem item : items) {
            result.add(copySelectItem(item));
        }
        return result;
    }

    private DmSqlParser.QualifiedNameContext directColumn(DmSqlParser.ExpressionContext expression) {
        List<DmSqlParser.QualifiedNameContext> names = new ArrayList<>();
        collectQualifiedNames(expression, names);
        if (names.size() != 1) {
            return null;
        }
        DmSqlParser.QualifiedNameContext name = names.get(0);
        return StringUtils.equals(expression.getText(), name.getText()) ? name : null;
    }

    private void collectQualifiedNames(ParseTree tree, List<DmSqlParser.QualifiedNameContext> names) {
        if (tree instanceof DmSqlParser.FunctionNameContext || tree instanceof DmSqlParser.DataTypeContext) {
            return;
        }
        if (tree instanceof DmSqlParser.QualifiedNameContext qualifiedName) {
            if (isDatePartArgument(qualifiedName)
                || NON_COLUMN_NAMES.contains(qualifiedName.getText().toUpperCase(Locale.ROOT))
                    && !qualifiedName.getText().startsWith("\"")) {
                return;
            }
            names.add(qualifiedName);
            return;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectQualifiedNames(tree.getChild(i), names);
        }
    }

    private boolean isDatePartArgument(DmSqlParser.QualifiedNameContext ctx) {
        ParseTree parent = ctx.getParent();
        while (parent != null && !(parent instanceof DmSqlParser.FunctionCallContext)) {
            parent = parent.getParent();
        }
        if (!(parent instanceof DmSqlParser.FunctionCallContext functionCall)
            || functionCall.functionArguments() == null
            || functionCall.functionArguments().functionArgument().isEmpty()) {
            return false;
        }
        DmSqlParser.FunctionArgumentContext firstArgument = functionCall.functionArguments().functionArgument(0);
        return firstArgument.getText().equals(ctx.getText())
            && DATE_PART_FUNCTIONS.contains(functionCall.functionName().getText().toUpperCase(Locale.ROOT));
    }

    private NameParts resolveColumnTable(NameParts column, Map<String, NameParts> tables) {
        if (column.schema() != null) {
            return resolveTable(column.schema(), tables);
        }
        NameParts onlyTable = null;
        for (NameParts table : tables.values()) {
            if (onlyTable == null) {
                onlyTable = table;
            } else if (!onlyTable.equals(table)) {
                return new NameParts(null, null, null);
            }
        }
        if (onlyTable != null) {
            return onlyTable;
        }
        return new NameParts(null, null, null);
    }

    private NameParts resolveTable(String alias, Map<String, NameParts> tables) {
        if (alias == null) {
            return new NameParts(null, null, null);
        }
        NameParts table = findTable(alias, tables);
        if (table != null) {
            return table;
        }
        return new NameParts(null, null, alias);
    }

    private NameParts findTable(String alias, Map<String, NameParts> tables) {
        if (alias == null) {
            return null;
        }
        for (Map.Entry<String, NameParts> entry : tables.entrySet()) {
            if (alias.equalsIgnoreCase(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    private void restrictCorrespondingColumns(List<SelectItem> items, List<DmSqlParser.QueryRemainderContext> remainders) {
        for (DmSqlParser.QueryRemainderContext remainder : remainders) {
            if (remainder.setCorrespondingClause() == null
                || remainder.setCorrespondingClause().columnNameList() == null) {
                continue;
            }
            Set<String> names = new java.util.HashSet<>();
            for (DmSqlParser.IdentifierContext identifier
                : remainder.setCorrespondingClause().columnNameList().identifierList().identifier()) {
                names.add(NameParts.clean(identifier.getText()).toUpperCase(Locale.ROOT));
            }
            items.removeIf(item -> item.getItemAlias() == null
                || !names.contains(item.getItemAlias().toUpperCase(Locale.ROOT)));
            return;
        }
    }

    private String schemaName(DmSqlParser.SchemaCreateContext ctx) {
        if (ctx.schemaName != null) {
            return NameParts.from(ctx.schemaName).name();
        }
        if (ctx.schemaAuthorizationOnly() != null) {
            return NameParts.clean(ctx.schemaAuthorizationOnly().schemaOwner.getText());
        }
        return null;
    }

    private void applyDefaultScope(List<SelectItem> items, String catalog, String schema) {
        for (SelectItem item : items) {
            for (RealColumn column : item.getColumns()) {
                if (column.getTable() == null) {
                    continue;
                }
                if (column.getCatalog() == null) {
                    column.setCatalog(catalog);
                }
                if (column.getSchema() == null) {
                    column.setSchema(schema);
                }
            }
        }
    }

    private boolean addDerivedColumn(SelectItem item, NameParts column, Map<String, List<SelectItem>> derived) {
        List<SelectItem> items;
        if (column.schema() != null) {
            items = findDerived(column.schema(), derived);
        } else {
            List<List<SelectItem>> unique = uniqueDerivedColumns(derived);
            items = unique.size() == 1 ? unique.get(0) : null;
        }
        if (items == null) {
            return false;
        }
        for (SelectItem selectItem : items) {
            if (column.name().equalsIgnoreCase(selectItem.getItemAlias())) {
                item.addAllRealColumns(selectItem.getColumns());
                return true;
            }
        }
        return false;
    }

    private void addProjectedColumns(SelectItem item, List<SelectItem> items) {
        for (SelectItem selectItem : items) {
            item.addAllRealColumns(selectItem.getColumns());
        }
    }

    private List<SelectItem> applyDerivedColumnAliases(List<SelectItem> items, DmSqlParser.DerivedColumnListContext columnList) {
        if (columnList == null) {
            return items;
        }
        return applyColumnAliases(items, columnList.columnNameList());
    }

    private List<SelectItem> applyDerivedColumnAliases(List<SelectItem> items, DmSqlParser.ColumnNameListContext columnList) {
        if (columnList == null) {
            return items;
        }
        return applyColumnAliases(items, columnList);
    }

    private List<SelectItem> applyColumnAliases(List<SelectItem> items, DmSqlParser.ColumnNameListContext columnList) {
        if (columnList == null) {
            return items;
        }
        List<SelectItem> result = new ArrayList<>();
        List<DmSqlParser.IdentifierContext> aliases = columnList.identifierList().identifier();
        for (int i = 0; i < items.size(); i++) {
            SelectItem item = copySelectItem(items.get(i));
            if (i < aliases.size()) {
                item.setItemAlias(NameParts.clean(aliases.get(i).getText()));
            }
            result.add(item);
        }
        return result;
    }

    private SelectItem copySelectItem(SelectItem item) {
        SelectItem copy = new SelectItem();
        copy.setItemAlias(item.getItemAlias());
        copy.setTableAlias(item.getTableAlias());
        copy.addAllRealColumns(item.getColumns());
        return copy;
    }

    private void putDerived(Map<String, List<SelectItem>> derived, String alias, List<SelectItem> items) {
        if (alias != null) {
            derived.putIfAbsent(alias, items);
        }
    }

    private List<SelectItem> findDerived(String alias, Map<String, List<SelectItem>> derived) {
        if (alias == null) {
            return null;
        }
        for (Map.Entry<String, List<SelectItem>> entry : derived.entrySet()) {
            if (alias.equalsIgnoreCase(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    private List<List<SelectItem>> uniqueDerivedColumns(Map<String, List<SelectItem>> derived) {
        List<List<SelectItem>> result = new ArrayList<>();
        for (List<SelectItem> items : derived.values()) {
            if (!result.contains(items)) {
                result.add(items);
            }
        }
        return result;
    }

    private String tableAlias(DmSqlParser.TableAliasContext ctx) {
        if (ctx == null) {
            return null;
        }
        return NameParts.clean(ctx.aliasIdentifier().identifier().getText());
    }

    private RealColumn realColumn(NameParts table, String column, boolean onlyOneColumn) {
        RealColumn realColumn = new RealColumn();
        realColumn.setCatalog(table.catalog());
        realColumn.setSchema(table.schema());
        realColumn.setTable(table.name());
        realColumn.setColumn(column);
        realColumn.setOnlyOneColumn(onlyOneColumn);
        return realColumn;
    }

    private record NameParts(String catalog, String schema, String name) {

        private static NameParts from(DmSqlParser.QualifiedNameContext ctx) {
            if (ctx == null) {
                return new NameParts(null, null, null);
            }
            List<String> parts = new ArrayList<>();
            parts.add(clean(ctx.dottedName().identifier().getText()));
            for (DmSqlParser.DottedNamePartContext partContext : ctx.dottedName().dottedNamePart()) {
                parts.add(clean(partContext.getText()));
            }
            return fromParts(parts);
        }

        private static NameParts fromParts(List<String> parts) {
            if (parts.isEmpty()) {
                return new NameParts(null, null, null);
            }
            String name = parts.get(parts.size() - 1);
            String schema = parts.size() > 1 ? parts.get(parts.size() - 2) : null;
            String catalog = parts.size() > 2 ? parts.get(parts.size() - 3) : null;
            return new NameParts(catalog, schema, name);
        }

        private static String clean(String text) {
            if (text == null || text.length() < 2) {
                return text;
            }
            if (text.startsWith("\"") && text.endsWith("\"")) {
                return text.substring(1, text.length() - 1).replace("\"\"", "\"");
            }
            if (text.startsWith("[") && text.endsWith("]")) {
                return text.substring(1, text.length() - 1);
            }
            return text;
        }
    }
}
