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
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTree;

import com.clougence.clouddm.ds.dameng.sql.parser.DmDslProvider;
import com.clougence.clouddm.ds.dameng.sql.parser.antlr.DmSqlParser;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.sql.analysis.column.RealColumn;
import com.clougence.clouddm.sdk.sql.analysis.column.SelectColumnAnalysisSpi;
import com.clougence.clouddm.sdk.sql.analysis.column.SelectItem;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.utils.StringUtils;

public class DmSelectColumnAnalysisSpi implements SelectColumnAnalysisSpi {

    @Override
    public List<SelectItem> parseSelectColumn(String script, ContextInfo contextInfo) {
        List<SelectItem> result = new ArrayList<>();
        for (AstSplitScript splitScript : DslHelper.splitDsl(DmDslProvider.INSTANCE, script)) {
            if (!(splitScript.getAstTree() instanceof DmSqlParser.StatementContext statement)) {
                continue;
            }
            if (statement.selectStatement() != null) {
                result.addAll(selectStatementItems(statement.selectStatement(), new LinkedHashMap<>()));
            }
        }
        return result;
    }

    private List<SelectItem> selectStatementItems(DmSqlParser.SelectStatementContext ctx, Map<String, List<SelectItem>> outerDerived) {
        Map<String, List<SelectItem>> derived = new LinkedHashMap<>(outerDerived);
        collectCteColumns(ctx.withClause(), derived);

        List<SelectItem> result = selectOperandItems(ctx.selectOperand(), derived);
        for (DmSqlParser.QueryRemainderContext queryRemainderContext : ctx.queryRemainder()) {
            mergeByPosition(result, selectOperandItems(queryRemainderContext.selectOperand(), derived));
        }
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

        DmSqlParser.IdentifierContext alias = ctx.identifier();
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
            if (!addDerivedColumn(item, column, derived)) {
                NameParts table = resolveColumnTable(column, tables);
                item.addRealColumn(realColumn(table, column.name(), true));
            }
        }
        return item;
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
            List<SelectItem> items = selectStatementItems(cteDefinitionContext.selectStatement(), derived);
            items = applyDerivedColumnAliases(items, cteDefinitionContext.columnNameList());
            putDerived(derived, NameParts.clean(cteDefinitionContext.identifier().getText()), items);
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
        if (ctx.qualifiedName() != null) {
            NameParts name = NameParts.from(ctx.qualifiedName());
            if (name.name() != null) {
                List<SelectItem> derivedItems = name.schema() == null ? findDerived(name.name(), visibleDerived) : null;
                if (derivedItems != null) {
                    derivedItems = applyDerivedColumnAliases(derivedItems, ctx.derivedColumnList());
                    putDerived(derived, name.name(), derivedItems);
                    putDerived(derived, tableAlias(ctx.tableAlias()), derivedItems);
                    return;
                }
                tables.putIfAbsent(name.name(), name);
                if (ctx.tableAlias() != null) {
                    tables.putIfAbsent(NameParts.clean(ctx.tableAlias().identifier().getText()), name);
                }
            }
        } else if (ctx.selectStatement() != null) {
            String alias = tableAlias(ctx.tableAlias());
            if (alias != null) {
                List<SelectItem> items = selectStatementItems(ctx.selectStatement(), visibleDerived);
                putDerived(derived, alias, applyDerivedColumnAliases(items, ctx.derivedColumnList()));
            }
        }
        for (DmSqlParser.TableSourceContext tableSourceContext : ctx.tableSource()) {
            collectTables(tableSourceContext, tables, derived, visibleDerived);
        }
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
        if (tree instanceof DmSqlParser.QualifiedNameContext) {
            names.add((DmSqlParser.QualifiedNameContext) tree);
            return;
        }
        for (int i = 0; i < tree.getChildCount(); i++) {
            collectQualifiedNames(tree.getChild(i), names);
        }
    }

    private NameParts resolveColumnTable(NameParts column, Map<String, NameParts> tables) {
        if (column.schema() != null) {
            return resolveTable(column.schema(), tables);
        }
        if (tables.size() == 1) {
            return tables.values().iterator().next();
        }
        return new NameParts(null, null, null);
    }

    private NameParts resolveTable(String alias, Map<String, NameParts> tables) {
        if (alias == null) {
            return new NameParts(null, null, null);
        }
        for (Map.Entry<String, NameParts> entry : tables.entrySet()) {
            if (alias.equalsIgnoreCase(entry.getKey())) {
                return entry.getValue();
            }
        }
        return new NameParts(null, null, alias);
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
        return ctx == null ? null : NameParts.clean(ctx.identifier().getText());
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
