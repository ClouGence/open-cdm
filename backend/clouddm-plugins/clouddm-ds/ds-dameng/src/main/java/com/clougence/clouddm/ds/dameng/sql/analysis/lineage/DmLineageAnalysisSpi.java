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
package com.clougence.clouddm.ds.dameng.sql.analysis.lineage;

import java.util.*;

import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.dameng.sql.parser.DmDslProvider;
import com.clougence.clouddm.ds.dameng.sql.parser.antlr.DmSqlParser;
import com.clougence.clouddm.sdk.sql.analysis.lineage.ColumnLineage;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.lineage.LineageContext;
import com.clougence.clouddm.sdk.sql.analysis.lineage.SourceName;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

import lombok.Getter;
import lombok.Setter;

public class DmLineageAnalysisSpi implements LineageAnalysisSpi {

    private static final Set<String> NON_COLUMN_NAMES    = Set.of("DBTIMEZONE", "SESSIONTIMEZONE", "LEVEL", "CONNECT_BY_ISLEAF", "CONNECT_BY_ISCYCLE", "ROWNUM");
    private static final Set<String> DATE_PART_FUNCTIONS = Set.of("BIGDATEDIFF", "DATEADD", "DATEDIFF", "DATEPART", "TIMESTAMPADD", "TIMESTAMPDIFF");

    @Getter
    @Setter
    private static class MutableColumnLineage {

        private final List<SourceName> columns = new ArrayList<>();
        private String                 itemAlias;
        private String                 tableAlias;

        public void addSource(SourceName column) {
            this.columns.add(column);
        }

        public void addAllSources(List<SourceName> columns) {
            this.columns.addAll(columns);
        }
    }

    @Override
    public List<ColumnLineage> analyze(String sql, LineageContext lineageContext) {
        List<MutableColumnLineage> result = new ArrayList<>();
        Object catalogLevel = lineageContext == null || lineageContext.getLevelsParam() == null ? null : lineageContext.getLevelsParam().get(UmiTypes.Catalog);
        String defaultCatalog = catalogLevel == null ? null : String.valueOf(catalogLevel);
        for (AstSplitScript splitScript : DslHelper.splitDsl(DmDslProvider.INSTANCE, sql)) {
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
        return result.stream().map(column -> new ColumnLineage(column.getItemAlias(), column.getColumns())).toList();
    }

    private void collectDefinitionSelectItems(ParseTree tree, List<MutableColumnLineage> result, String defaultCatalog) {
        if (tree instanceof DmSqlParser.SchemaCreateContext schemaCreate) {
            List<MutableColumnLineage> schemaItems = new ArrayList<>();
            for (int i = 0; i < tree.getChildCount(); i++) {
                collectDefinitionSelectItems(tree.getChild(i), schemaItems, defaultCatalog);
            }
            applyDefaultScope(schemaItems, defaultCatalog, schemaName(schemaCreate));
            result.addAll(schemaItems);
            return;
        }
        if (tree instanceof DmSqlParser.ViewCreateContext viewCreate) {
            List<MutableColumnLineage> items = selectStatementItems(viewCreate.selectStatement(), new LinkedHashMap<>());
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

    private void collectSelectItems(ParseTree tree, List<MutableColumnLineage> result) {
        if (tree instanceof DmSqlParser.SelectStatementContext selectStatement) {
            result.addAll(selectStatementItems(selectStatement, new LinkedHashMap<>()));
            return;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectSelectItems(tree.getChild(i), result);
        }
    }

    private List<MutableColumnLineage> selectStatementItems(DmSqlParser.SelectStatementContext ctx, Map<String, List<MutableColumnLineage>> outerDerived) {
        Map<String, List<MutableColumnLineage>> derived = new LinkedHashMap<>(outerDerived);
        collectCteColumns(ctx.withClause(), derived);

        List<MutableColumnLineage> result = selectOperandItems(ctx.selectOperand(), derived);
        for (DmSqlParser.QueryRemainderContext queryRemainderContext : ctx.queryRemainder()) {
            mergeByPosition(result, selectOperandItems(queryRemainderContext.selectOperand(), derived));
        }
        restrictCorrespondingColumns(result, ctx.queryRemainder());
        return result;
    }

    private List<MutableColumnLineage> selectOperandItems(DmSqlParser.SelectOperandContext ctx, Map<String, List<MutableColumnLineage>> derived) {
        if (ctx.selectQuery() != null) {
            return selectQueryItems(ctx.selectQuery(), derived);
        }
        return selectStatementItems(ctx.selectStatement(), derived);
    }

    private List<MutableColumnLineage> selectQueryItems(DmSqlParser.SelectQueryContext ctx, Map<String, List<MutableColumnLineage>> visibleDerived) {
        Map<String, NameParts> tables = new LinkedHashMap<>();
        Map<String, List<MutableColumnLineage>> derived = new LinkedHashMap<>();
        if (ctx.fromClause() != null) {
            for (DmSqlParser.TableSourceContext tableSourceContext : ctx.fromClause().tableSource()) {
                collectTables(tableSourceContext, tables, derived, visibleDerived);
            }
        }

        List<MutableColumnLineage> result = new ArrayList<>();
        for (DmSqlParser.SelectItemContext selectItemContext : ctx.selectList().selectItem()) {
            result.add(selectItem(selectItemContext, tables, derived));
        }
        return result;
    }

    private MutableColumnLineage selectItem(DmSqlParser.SelectItemContext ctx, Map<String, NameParts> tables, Map<String, List<MutableColumnLineage>> derived) {
        MutableColumnLineage item = new MutableColumnLineage();
        if (ctx.STAR() != null) {
            item.setItemAlias("*");
            for (NameParts table : tables.values()) {
                item.addSource(realColumn(table, "*"));
            }
            for (List<MutableColumnLineage> items : uniqueDerivedColumns(derived)) {
                addProjectedColumns(item, items);
            }
            return item;
        }
        if (ctx.qualifiedName() != null) {
            NameParts name = NameParts.from(ctx.qualifiedName());
            item.setItemAlias("*");
            item.setTableAlias(name.name());
            List<MutableColumnLineage> derivedItems = findDerived(name.name(), derived);
            if (derivedItems != null) {
                addProjectedColumns(item, derivedItems);
                return item;
            }
            item.addSource(realColumn(resolveTable(name.name(), tables), "*"));
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
            addColumn(item, column, tables, derived);
        } else {
            List<DmSqlParser.QualifiedNameContext> columns = new ArrayList<>();
            collectQualifiedNames(ctx.expression(), columns);
            for (DmSqlParser.QualifiedNameContext columnContext : columns) {
                addColumn(item, NameParts.from(columnContext), tables, derived);
            }
        }
        return item;
    }

    private void addColumn(MutableColumnLineage item, NameParts column, Map<String, NameParts> tables, Map<String, List<MutableColumnLineage>> derived) {
        if (StringUtils.equalsIgnoreCase(column.name(), "NEXTVAL") || StringUtils.equalsIgnoreCase(column.name(), "CURRVAL")) {
            return;
        }
        if (!addDerivedColumn(item, column, derived)) {
            if (column.catalog() != null) {
                NameParts attributeTable = findTable(column.catalog(), tables);
                if (attributeTable != null) {
                    item.addSource(realColumn(attributeTable, column.schema()));
                    return;
                }
            }
            NameParts table = resolveColumnTable(column, tables);
            SourceName realColumn = realColumn(table, column.name());
            item.addSource(realColumn);
        }
    }

    private void mergeByPosition(List<MutableColumnLineage> target, List<MutableColumnLineage> branch) {
        int size = Math.min(target.size(), branch.size());
        for (int i = 0; i < size; i++) {
            target.get(i).addAllSources(branch.get(i).getColumns());
        }
    }

    private void collectCteColumns(DmSqlParser.WithClauseContext ctx, Map<String, List<MutableColumnLineage>> derived) {
        if (ctx == null || ctx.cteDefinitionList() == null) {
            return;
        }
        for (DmSqlParser.CteDefinitionContext cteDefinitionContext : ctx.cteDefinitionList().cteDefinition()) {
            String cteName = NameParts.clean(cteDefinitionContext.identifier().getText());
            List<MutableColumnLineage> items = selectStatementItems(cteDefinitionContext.selectStatement(), derived);
            for (MutableColumnLineage item : items) {
                item.getColumns().removeIf(column -> column.catalog() == null && column.schema() == null && cteName.equalsIgnoreCase(column.table()));
            }
            items = applyDerivedColumnAliases(items, cteDefinitionContext.columnNameList());
            putDerived(derived, cteName, items);
        }
    }

    private void collectTables(DmSqlParser.TableSourceContext ctx, Map<String, NameParts> tables, Map<String, List<MutableColumnLineage>> derived,
                               Map<String, List<MutableColumnLineage>> visibleDerived) {
        collectTables(ctx.tablePrimary(), tables, derived, visibleDerived);
        for (DmSqlParser.JoinClauseContext joinClauseContext : ctx.joinClause()) {
            if (joinClauseContext.tablePrimary() != null) {
                collectTables(joinClauseContext.tablePrimary(), tables, derived, visibleDerived);
            } else if (joinClauseContext.applyJoinClause() != null) {
                collectTables(joinClauseContext.applyJoinClause().tablePrimary(), tables, derived, visibleDerived);
            }
        }
    }

    private void collectTables(DmSqlParser.TablePrimaryContext ctx, Map<String, NameParts> tables, Map<String, List<MutableColumnLineage>> derived,
                               Map<String, List<MutableColumnLineage>> visibleDerived) {
        if (ctx.arrayTableExpression() != null) {
            MutableColumnLineage item = new MutableColumnLineage();
            item.setItemAlias("COLUMN_VALUE");
            String alias = tableAlias(ctx.tableAlias());
            putDerived(derived, alias == null ? "__ARRAY_" + derived.size() : alias, List.of(item));
        } else if (ctx.tableCollectionExpression() != null) {
            MutableColumnLineage item = new MutableColumnLineage();
            item.setItemAlias("COLUMN_VALUE");
            DmSqlParser.TableCollectionExpressionContext collection = ctx.tableCollectionExpression();
            if (collection.selectStatement() != null) {
                addProjectedColumns(item, selectStatementItems(collection.selectStatement(), visibleDerived));
            } else {
                List<DmSqlParser.QualifiedNameContext> sourceColumns = new ArrayList<>();
                collectQualifiedNames(collection.expression(), sourceColumns);
                for (DmSqlParser.QualifiedNameContext sourceColumn : sourceColumns) {
                    addColumn(item, NameParts.from(sourceColumn), tables, derived);
                }
            }
            String alias = tableAlias(ctx.tableAlias());
            putDerived(derived, alias == null ? "__COLLECTION_" + derived.size() : alias, List.of(item));
        } else if (ctx.xmlTableExpression() != null) {
            List<DmSqlParser.QualifiedNameContext> sourceColumns = new ArrayList<>();
            if (ctx.xmlTableExpression().xmlPassingClause() != null) {
                collectQualifiedNames(ctx.xmlTableExpression().xmlPassingClause(), sourceColumns);
            }
            List<MutableColumnLineage> projected = new ArrayList<>();
            if (ctx.xmlTableExpression().xmlTableColumnsClause() != null) {
                for (DmSqlParser.XmlTableColumnContext columnContext : ctx.xmlTableExpression().xmlTableColumnsClause().xmlTableColumn()) {
                    MutableColumnLineage item = new MutableColumnLineage();
                    item.setItemAlias(NameParts.clean(columnContext.identifier().getText()));
                    for (DmSqlParser.QualifiedNameContext sourceColumn : sourceColumns) {
                        addColumn(item, NameParts.from(sourceColumn), tables, derived);
                    }
                    projected.add(item);
                }
            }
            putDerived(derived, tableAlias(ctx.tableAlias()), projected);
        } else if (ctx.qualifiedName() != null) {
            NameParts name = NameParts.from(ctx.qualifiedName());
            if (name.name() != null) {
                List<MutableColumnLineage> derivedItems = name.schema() == null ? findDerived(name.name(), visibleDerived) : null;
                if (derivedItems != null) {
                    derivedItems = applyDerivedColumnAliases(derivedItems, ctx.derivedColumnList());
                    derivedItems = applyPivotColumns(ctx, derivedItems, tables, derived);
                    putDerived(derived, name.name(), derivedItems);
                    putDerived(derived, tableAlias(ctx.tableAlias()), derivedItems);
                    return;
                }
                tables.putIfAbsent(name.name(), name);
                if (ctx.tableAlias() != null) {
                    tables.putIfAbsent(NameParts.clean(ctx.tableAlias().aliasIdentifier().identifier().getText()), name);
                }
                List<MutableColumnLineage> pivotItems = applyPivotColumns(ctx, null, tables, derived);
                if (pivotItems != null) {
                    putDerived(derived, name.name(), pivotItems);
                    putDerived(derived, tableAlias(ctx.tableAlias()), pivotItems);
                }
            }
        } else if (ctx.selectStatement() != null) {
            String alias = tableAlias(ctx.tableAlias());
            if (alias != null) {
                List<MutableColumnLineage> items = selectStatementItems(ctx.selectStatement(), visibleDerived);
                items = applyPivotColumns(ctx, items, tables, derived);
                putDerived(derived, alias, applyDerivedColumnAliases(items, ctx.derivedColumnList()));
            }
        }
        for (DmSqlParser.TableSourceContext tableSourceContext : ctx.tableSource()) {
            collectTables(tableSourceContext, tables, derived, visibleDerived);
        }
    }

    private List<MutableColumnLineage> applyPivotColumns(DmSqlParser.TablePrimaryContext ctx, List<MutableColumnLineage> sourceItems, Map<String, NameParts> tables,
                                                         Map<String, List<MutableColumnLineage>> derived) {
        if (ctx.tablePivotClause().isEmpty()) {
            return sourceItems;
        }
        List<MutableColumnLineage> items = sourceItems == null ? new ArrayList<>() : copySelectItems(sourceItems);
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

    private List<MutableColumnLineage> pivotColumns(DmSqlParser.PivotClauseContext ctx, List<MutableColumnLineage> sourceItems, boolean sourceColumnsKnown,
                                                    Map<String, NameParts> tables, Map<String, List<MutableColumnLineage>> derived) {
        List<NameParts> groupingInputs = qualifiedNames(ctx.pivotForClause());
        List<NameParts> aggregateInputs = new ArrayList<>();
        for (DmSqlParser.PivotExpressionContext expression : ctx.pivotExpressionList().pivotExpression()) {
            aggregateInputs.addAll(qualifiedNames(expression.functionCall()));
        }

        List<MutableColumnLineage> result = copyUnconsumedItems(sourceItems, groupingInputs, aggregateInputs);
        if (ctx.pivotInClauseList() == null) {
            MutableColumnLineage xmlItem = new MutableColumnLineage();
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
                MutableColumnLineage item = new MutableColumnLineage();
                String aggregateAlias = expression.identifier() == null ? null : NameParts.clean(expression.identifier().getText());
                if (aggregateAlias == null) {
                    item.setItemAlias(valueAlias);
                } else {
                    item.setItemAlias(valueAlias + "_" + aggregateAlias);
                }
                addSourceColumns(item, groupingInputs, sourceItems, sourceColumnsKnown, tables, derived);
                addSourceColumns(item, qualifiedNames(expression.functionCall()), sourceItems, sourceColumnsKnown, tables, derived);
                result.add(item);
            }
        }
        return result;
    }

    private List<MutableColumnLineage> unpivotColumns(DmSqlParser.PivotClauseContext ctx, List<MutableColumnLineage> sourceItems, boolean sourceColumnsKnown,
                                                      Map<String, NameParts> tables, Map<String, List<MutableColumnLineage>> derived) {
        List<List<NameParts>> inputGroups = new ArrayList<>();
        List<NameParts> allInputs = new ArrayList<>();
        for (DmSqlParser.UnpivotInClauseContext inClause : ctx.unpivotInClauseList().unpivotInClause()) {
            List<NameParts> inputs = qualifiedNames(inClause);
            inputGroups.add(inputs);
            allInputs.addAll(inputs);
        }
        List<MutableColumnLineage> result = copyUnconsumedItems(sourceItems, allInputs);
        for (DmSqlParser.IdentifierContext identifier : unpivotIdentifiers(ctx.unpivotForClause())) {
            MutableColumnLineage item = new MutableColumnLineage();
            item.setItemAlias(NameParts.clean(identifier.getText()));
            result.add(item);
        }
        List<DmSqlParser.IdentifierContext> values = unpivotIdentifiers(ctx.unpivotValueClause());
        for (int valueIndex = 0; valueIndex < values.size(); valueIndex++) {
            MutableColumnLineage item = new MutableColumnLineage();
            item.setItemAlias(NameParts.clean(values.get(valueIndex).getText()));
            for (List<NameParts> inputGroup : inputGroups) {
                if (valueIndex < inputGroup.size()) {
                    addSourceColumns(item, List.of(inputGroup.get(valueIndex)), sourceItems, sourceColumnsKnown, tables, derived);
                }
            }
            result.add(item);
        }
        return result;
    }

    @SafeVarargs
    private final List<MutableColumnLineage> copyUnconsumedItems(List<MutableColumnLineage> sourceItems, List<NameParts>... consumedGroups) {
        List<MutableColumnLineage> result = new ArrayList<>();
        for (MutableColumnLineage sourceItem : sourceItems) {
            boolean consumed = false;
            for (List<NameParts> consumedGroup : consumedGroups) {
                for (NameParts consumedColumn : consumedGroup) {
                    if (consumedColumn.name() != null && consumedColumn.name().equalsIgnoreCase(sourceItem.getItemAlias())) {
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

    private void addSourceColumns(MutableColumnLineage target, List<NameParts> columns, List<MutableColumnLineage> sourceItems, boolean sourceColumnsKnown,
                                  Map<String, NameParts> tables, Map<String, List<MutableColumnLineage>> derived) {
        for (NameParts column : columns) {
            boolean matched = false;
            for (MutableColumnLineage sourceItem : sourceItems) {
                if (column.name() != null && column.name().equalsIgnoreCase(sourceItem.getItemAlias())) {
                    target.addAllSources(sourceItem.getColumns());
                    matched = true;
                    break;
                }
            }
            if (!matched && !sourceColumnsKnown) {
                addColumn(target, column, tables, derived);
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

    private List<MutableColumnLineage> copySelectItems(List<MutableColumnLineage> items) {
        List<MutableColumnLineage> result = new ArrayList<>();
        for (MutableColumnLineage item : items) {
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
            if (isDatePartArgument(qualifiedName) || NON_COLUMN_NAMES.contains(qualifiedName.getText().toUpperCase(Locale.ROOT)) && !qualifiedName.getText().startsWith("\"")) {
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
        if (!(parent instanceof DmSqlParser.FunctionCallContext functionCall) || functionCall.functionArguments() == null
            || functionCall.functionArguments().functionArgument().isEmpty()) {
            return false;
        }
        DmSqlParser.FunctionArgumentContext firstArgument = functionCall.functionArguments().functionArgument(0);
        return firstArgument.getText().equals(ctx.getText()) && DATE_PART_FUNCTIONS.contains(functionCall.functionName().getText().toUpperCase(Locale.ROOT));
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

    private void restrictCorrespondingColumns(List<MutableColumnLineage> items, List<DmSqlParser.QueryRemainderContext> remainders) {
        for (DmSqlParser.QueryRemainderContext remainder : remainders) {
            if (remainder.setCorrespondingClause() == null || remainder.setCorrespondingClause().columnNameList() == null) {
                continue;
            }
            Set<String> names = new java.util.HashSet<>();
            for (DmSqlParser.IdentifierContext identifier : remainder.setCorrespondingClause().columnNameList().identifierList().identifier()) {
                names.add(NameParts.clean(identifier.getText()).toUpperCase(Locale.ROOT));
            }
            items.removeIf(item -> item.getItemAlias() == null || !names.contains(item.getItemAlias().toUpperCase(Locale.ROOT)));
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

    private void applyDefaultScope(List<MutableColumnLineage> items, String catalog, String schema) {
        for (MutableColumnLineage item : items) {
            item.getColumns().replaceAll(column -> {
                if (column.table() == null) {
                    return column;
                }
                String sourceCatalog = column.catalog() == null ? catalog : column.catalog();
                String sourceSchema = column.schema() == null ? schema : column.schema();
                return new SourceName(sourceCatalog, sourceSchema, column.table(), column.column());
            });
        }
    }

    private boolean addDerivedColumn(MutableColumnLineage item, NameParts column, Map<String, List<MutableColumnLineage>> derived) {
        List<MutableColumnLineage> items;
        if (column.schema() != null) {
            items = findDerived(column.schema(), derived);
        } else {
            List<List<MutableColumnLineage>> unique = uniqueDerivedColumns(derived);
            items = unique.size() == 1 ? unique.get(0) : null;
        }
        if (items == null) {
            return false;
        }
        for (MutableColumnLineage selectItem : items) {
            if (column.name().equalsIgnoreCase(selectItem.getItemAlias())) {
                item.addAllSources(selectItem.getColumns());
                return true;
            }
        }
        return false;
    }

    private void addProjectedColumns(MutableColumnLineage item, List<MutableColumnLineage> items) {
        for (MutableColumnLineage selectItem : items) {
            item.addAllSources(selectItem.getColumns());
        }
    }

    private List<MutableColumnLineage> applyDerivedColumnAliases(List<MutableColumnLineage> items, DmSqlParser.DerivedColumnListContext columnList) {
        if (columnList == null) {
            return items;
        }
        return applyColumnAliases(items, columnList.columnNameList());
    }

    private List<MutableColumnLineage> applyDerivedColumnAliases(List<MutableColumnLineage> items, DmSqlParser.ColumnNameListContext columnList) {
        if (columnList == null) {
            return items;
        }
        return applyColumnAliases(items, columnList);
    }

    private List<MutableColumnLineage> applyColumnAliases(List<MutableColumnLineage> items, DmSqlParser.ColumnNameListContext columnList) {
        if (columnList == null) {
            return items;
        }
        List<MutableColumnLineage> result = new ArrayList<>();
        List<DmSqlParser.IdentifierContext> aliases = columnList.identifierList().identifier();
        for (int i = 0; i < items.size(); i++) {
            MutableColumnLineage item = copySelectItem(items.get(i));
            if (i < aliases.size()) {
                item.setItemAlias(NameParts.clean(aliases.get(i).getText()));
            }
            result.add(item);
        }
        return result;
    }

    private MutableColumnLineage copySelectItem(MutableColumnLineage item) {
        MutableColumnLineage copy = new MutableColumnLineage();
        copy.setItemAlias(item.getItemAlias());
        copy.setTableAlias(item.getTableAlias());
        copy.addAllSources(item.getColumns());
        return copy;
    }

    private void putDerived(Map<String, List<MutableColumnLineage>> derived, String alias, List<MutableColumnLineage> items) {
        if (alias != null) {
            derived.putIfAbsent(alias, items);
        }
    }

    private List<MutableColumnLineage> findDerived(String alias, Map<String, List<MutableColumnLineage>> derived) {
        if (alias == null) {
            return null;
        }
        for (Map.Entry<String, List<MutableColumnLineage>> entry : derived.entrySet()) {
            if (alias.equalsIgnoreCase(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    private List<List<MutableColumnLineage>> uniqueDerivedColumns(Map<String, List<MutableColumnLineage>> derived) {
        List<List<MutableColumnLineage>> result = new ArrayList<>();
        for (List<MutableColumnLineage> items : derived.values()) {
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

    private SourceName realColumn(NameParts table, String column) {
        return new SourceName(table.catalog(), table.schema(), table.name(), column);
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
