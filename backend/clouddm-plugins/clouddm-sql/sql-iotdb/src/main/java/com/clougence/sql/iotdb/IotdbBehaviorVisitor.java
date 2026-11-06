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
package com.clougence.sql.iotdb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.Trees;

import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.sql.iotdb.antlr.IotdbParser;
import com.clougence.sql.iotdb.antlr.IotdbParserBaseVisitor;
import com.clougence.sql.iotdb.antlr.IotdbParser.*;

final class IotdbBehaviorVisitor extends IotdbParserBaseVisitor<Void> {

    private static final Pattern SIMPLE_TREE_NODE = Pattern.compile("[\\p{L}\\p{N}_]+");
    private static final Pattern NUMERIC_NODE     = Pattern.compile("[0-9]+(?:[Ee][0-9]+)?");

    private final String         model;
    private final String         database;
    private StatementBehavior    behavior;

    IotdbBehaviorVisitor(boolean treeModel, String database){
        this.model = treeModel ? "TREE" : "TABLE";
        this.database = database;
    }

    StatementBehavior analyze(ParserRuleContext statement) {
        behavior = new StatementBehavior();
        behavior.setStatementType(statement.accept(IotdbSplitVisitor.INSTANCE));
        visit(statement);
        return behavior;
    }

    @Override
    public Void visitTreeSelectStatement(TreeSelectStatementContext context) {
        TreeQueryContext query = context.treeQuery();
        List<TreeAbsolutePathContext> sources = query.treePathList().treeAbsolutePath();
        Collection<ParseTree> references = Trees.findAllRuleNodes(query, IotdbParser.RULE_treeSeriesReference);
        for (ParseTree node : references) {
            TreeSeriesReferenceContext reference = (TreeSeriesReferenceContext) node;
            if (reference.treeAbsolutePath() != null) {
                treeResource(BehaviorAction.READ, TargetType.Timeseries, path(reference.treeAbsolutePath()), reference);
            } else {
                List<String> suffix = path(reference.treeRelativePath());
                for (TreeAbsolutePathContext source : sources) {
                    List<String> target = path(source);
                    target.addAll(suffix);
                    treeResource(BehaviorAction.READ, TargetType.Timeseries, target, reference);
                }
            }
        }
        if (references.isEmpty()) {
            for (TreeAbsolutePathContext source : sources) {
                List<String> target = path(source);
                target.add("*");
                treeResource(BehaviorAction.READ, TargetType.Timeseries, target, source);
            }
        }
        return null;
    }

    @Override
    public Void visitTreeInsertStatement(TreeInsertStatementContext context) {
        List<String> device = path(context.treeAbsolutePath());
        for (TreeIdContext id : context.treeIdList().treeId()) {
            if (id.TIME() == null && id.TIMESTAMP() == null) {
                List<String> series = new ArrayList<>(device);
                series.add(treeNode(id.getText()));
                treeResource(BehaviorAction.INSERT, TargetType.Timeseries, series, id);
            }
        }
        return null;
    }

    @Override
    public Void visitTreeDeleteStatement(TreeDeleteStatementContext context) {
        return treeSeriesList(context.treePathList(), BehaviorAction.DELETE);
    }

    @Override
    public Void visitTreeDropSeriesStatement(TreeDropSeriesStatementContext context) {
        return treeSeriesList(context.treePathList(), BehaviorAction.DROP);
    }

    private Void treeSeriesList(TreePathListContext list, BehaviorAction action) {
        for (TreeAbsolutePathContext target : list.treeAbsolutePath()) {
            treeResource(action, TargetType.Timeseries, path(target), target);
        }
        return null;
    }

    @Override
    public Void visitTreeCreateSeriesStatement(TreeCreateSeriesStatementContext context) {
        var target = context.treeAbsolutePath();
        treeResource(BehaviorAction.CREATE, TargetType.Timeseries, path(target), target);
        return null;
    }

    @Override
    public Void visitTreeCreateAlignedStatement(TreeCreateAlignedStatementContext context) {
        List<String> device = path(context.treeAbsolutePath());
        for (TreeColumnContext column : context.treeColumn()) {
            TreeIdContext id = column.treeId(0);
            List<String> series = new ArrayList<>(device);
            series.add(treeNode(id.getText()));
            treeResource(BehaviorAction.CREATE, TargetType.Timeseries, series, id);
        }
        return null;
    }

    @Override
    public Void visitTreeCreateDatabaseStatement(TreeCreateDatabaseStatementContext context) {
        var target = context.treeAbsolutePath();
        treeResource(BehaviorAction.CREATE, TargetType.Schema, path(target), target);
        return null;
    }

    @Override
    public Void visitTreeDropDatabaseStatement(TreeDropDatabaseStatementContext context) {
        for (var target : context.treePathList().treeAbsolutePath()) {
            treeResource(BehaviorAction.DROP, TargetType.Schema, path(target), target);
        }
        return null;
    }

    @Override
    public Void visitTreeShowScopeStatement(TreeShowScopeStatementContext context) {
        add(BehaviorAction.READ, TargetType.Catalog, null, null, context, false);
        return null;
    }

    @Override
    public Void visitTreeShowPathStatement(TreeShowPathStatementContext context) {
        var target = context.treeAbsolutePath();
        if (target == null) {
            add(BehaviorAction.READ, TargetType.Catalog, null, null, context, false);
        } else {
            TargetType type = context.DEVICES() == null ? TargetType.Timeseries : TargetType.Device;
            List<String> nodes = path(target);
            String prefix = String.join(".", nodes);
            add(BehaviorAction.READ, type, treeDatabase(prefix), prefix, target, false);
        }
        return null;
    }

    private List<String> path(ParserRuleContext context) {
        List<String> nodes = new ArrayList<>();
        if (context instanceof TreeAbsolutePathContext) {
            nodes.add("root");
        }
        for (TreeNodeContext node : context.getRuleContexts(TreeNodeContext.class)) {
            nodes.add(node.treeId() == null ? node.getText() : treeNode(node.getText()));
        }
        return nodes;
    }

    private String treeNode(String token) {
        if (token.startsWith("`")) {
            String content = token.substring(1, token.length() - 1);
            if (SIMPLE_TREE_NODE.matcher(content).matches() && !NUMERIC_NODE.matcher(content).matches()) {
                return content;
            }
        }
        return token;
    }

    private void treeResource(BehaviorAction action, TargetType type, List<String> nodes, ParserRuleContext context) {
        String path = String.join(".", nodes);
        boolean wildcard = nodes.stream().anyMatch(node -> node.startsWith("*") || node.endsWith("*"));
        if (type == TargetType.Schema) {
            add(action, type, path, null, context, !wildcard);
            return;
        }
        String schema = treeDatabase(path);
        add(action, type, schema, path, context, !wildcard && schema != null);
    }

    private String treeDatabase(String path) {
        // The selected database can identify its descendants. Other paths remain unresolved.
        if (database != null && (path.equals(database) || path.startsWith(database + "."))) {
            return database;
        }
        return null;
    }

    @Override
    public Void visitTableRelation(TableRelationContext context) {
        if (context.tableName() != null) {
            tableResource(BehaviorAction.READ, TargetType.Table, context.tableName());
        }
        return visitChildren(context);
    }

    @Override
    public Void visitTableInsertStatement(TableInsertStatementContext context) {
        tableResource(BehaviorAction.INSERT, TargetType.Table, context.tableName());
        return visitChildren(context);
    }

    @Override
    public Void visitTableAddColumnStatement(TableAddColumnStatementContext context) {
        tableResource(BehaviorAction.ALTER, TargetType.Table, context.tableName());
        return null;
    }

    @Override
    public Void visitTableDropColumnStatement(TableDropColumnStatementContext context) {
        tableResource(BehaviorAction.ALTER, TargetType.Table, context.tableName());
        return null;
    }

    @Override
    public Void visitTableUpdateStatement(TableUpdateStatementContext context) {
        tableResource(BehaviorAction.UPDATE, TargetType.Table, context.tableName());
        return visitChildren(context);
    }

    @Override
    public Void visitTableDeleteStatement(TableDeleteStatementContext context) {
        tableResource(BehaviorAction.DELETE, TargetType.Table, context.tableName());
        return visitChildren(context);
    }

    @Override
    public Void visitTableCreateStatement(TableCreateStatementContext context) {
        tableResource(BehaviorAction.CREATE, TargetType.Table, context.tableName());
        return null;
    }

    @Override
    public Void visitTableDropStatement(TableDropStatementContext context) {
        tableResource(BehaviorAction.DROP, TargetType.Table, context.tableName());
        return null;
    }

    @Override
    public Void visitTableDropViewStatement(TableDropViewStatementContext context) {
        tableResource(BehaviorAction.DROP, TargetType.View, context.tableName());
        return null;
    }

    @Override
    public Void visitTableCreateDatabaseStatement(TableCreateDatabaseStatementContext context) {
        add(BehaviorAction.CREATE, TargetType.Schema, tableId(context.tableId()), null, context.tableId(), true);
        return null;
    }

    @Override
    public Void visitTableDropDatabaseStatement(TableDropDatabaseStatementContext context) {
        add(BehaviorAction.DROP, TargetType.Schema, tableId(context.tableId()), null, context.tableId(), true);
        return null;
    }

    @Override
    public Void visitTableShowScopeStatement(TableShowScopeStatementContext context) {
        add(BehaviorAction.READ, TargetType.Catalog, null, null, context, false);
        return null;
    }

    @Override
    public Void visitTableShowTablesStatement(TableShowTablesStatementContext context) {
        String schema = database;
        if (context.tableId() != null) {
            schema = tableId(context.tableId());
        }
        add(BehaviorAction.READ, TargetType.Schema, schema, null, context, true);
        return null;
    }

    @Override
    public Void visitTableDescribeStatement(TableDescribeStatementContext context) {
        tableResource(BehaviorAction.READ, TargetType.Table, context.tableName());
        return null;
    }

    @Override
    public Void visitTableShowCreateStatement(TableShowCreateStatementContext context) {
        tableResource(BehaviorAction.READ, context.VIEW() == null ? TargetType.Table : TargetType.View, context.tableName());
        return null;
    }

    private void tableResource(BehaviorAction action, TargetType type, TableNameContext context) {
        List<TableIdContext> identifiers = context.tableId();
        String schema = database;
        if (identifiers.size() == 2) {
            schema = tableId(identifiers.get(0));
        }
        add(action, type, schema, tableId(identifiers.get(identifiers.size() - 1)), context, schema != null);
    }

    private String tableId(TableIdContext context) {
        String text = context.getText();
        if (context.QUOTED_IDENTIFIER() != null) {
            text = text.substring(1, text.length() - 1).replace("\"\"", "\"");
        }
        return text.toLowerCase(Locale.ROOT);
    }

    private void add(BehaviorAction action, TargetType type, String schema, String name, ParserRuleContext context, boolean exact) {
        String scope = "/" + model + "/";
        // Until path encoding is shared with authorization, separators in names must not create resource levels.
        if (schema != null && !schema.isBlank() && !schema.contains("/") && (name != null || exact)) {
            scope += schema + "/";
            if (exact && name != null && !name.isBlank() && !name.contains("/")) {
                scope += name + "/";
            }
        }
        Token start = context.getStart();
        Token stop = context.getStop();
        String[] lines = stop.getText().split("\\n", -1);
        int endColumn = lines[lines.length - 1].length();
        if (lines.length == 1) {
            endColumn += stop.getCharPositionInLine();
        }

        BehaviorObject object = new BehaviorObject();
        object.setObjectType(type);
        object.setObjectName(new ObjectName(model, schema, name));
        object.setObjectPath(scope);
        object.setStartLine(start.getLine());
        object.setStartColumn(start.getCharPositionInLine());
        object.setEndLine(stop.getLine() + lines.length - 1);
        object.setEndColumn(endColumn);

        BehaviorRelation relation = new BehaviorRelation();
        relation.setSubject(object);
        relation.setAction(action);
        behavior.getRelations().add(relation);
    }
}
