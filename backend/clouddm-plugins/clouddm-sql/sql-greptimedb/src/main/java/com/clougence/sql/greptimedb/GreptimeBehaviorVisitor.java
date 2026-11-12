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

package com.clougence.sql.greptimedb;

import static com.clougence.sql.greptimedb.GreptimeDslProvider.identifier;

import java.util.*;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;

import com.clougence.clouddm.sdk.sql.analysis.behavior.*;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.dslpaser.antlr.AntlerSyntaxException;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.greptimedb.antlr.GreptimeSqlBaseVisitor;
import com.clougence.sql.greptimedb.antlr.GreptimeSqlParser.*;
import com.clougence.utils.StringUtils;

final class GreptimeBehaviorVisitor extends GreptimeSqlBaseVisitor<Void> {

    private static final String               DEFAULT_CATALOG = "greptime";

    private final String                      instancePath;
    private final String                      database;
    private final List<StatementBehavior>     statements;
    private final Map<String, BehaviorObject> reads           = new LinkedHashMap<>();
    private StatementBehavior                 behavior;
    private BehaviorRelation                  writeWithReads;

    GreptimeBehaviorVisitor(Map<UmiTypes, Object> levels, List<StatementBehavior> statements){
        String instance = null;
        String database = null;
        if (levels != null) {
            instance = StringUtils.trimToNull(Objects.toString(levels.get(UmiTypes.Instance), null));
            database = StringUtils.trimToNull(Objects.toString(levels.get(UmiTypes.Schema), null));
        }
        this.instancePath = StringUtils.defaultString(StringUtils.strip(instance, "/"));
        this.database = database;
        this.statements = statements;
    }

    @Override
    public Void visitStatement(StatementContext context) {
        reads.clear();
        writeWithReads = null;
        behavior = new StatementBehavior();
        behavior.setStatementType(context.accept(GreptimeSplitVisitor.INSTANCE));

        visitChildren(context);

        if (writeWithReads != null) {
            writeWithReads.getTarget().addAll(reads.values());
        } else {
            reads.values().forEach(source -> add(BehaviorAction.READ, source));
        }

        if (behavior.getRelations().isEmpty() && behavior.getStatementType() == SplitQueryType.SELECT) {
            add(BehaviorAction.READ, resource(TargetType.Query, null, context));
        }
        statements.add(behavior);
        return null;
    }

    @Override
    public Void visitTableSource(TableSourceContext context) {
        var table = context.objectName();
        if (table != null && !isCommonTable(table)) {
            readTable(table);
        }
        return visitChildren(context);
    }

    @Override
    public Void visitInsertStatement(InsertStatementContext context) {
        writeWithReads = add(BehaviorAction.INSERT, relation(TargetType.Table, context.objectName()));
        if (context.VALUES() != null) {
            writeWithReads.setInsertRows((long) context.valuesRow().size());
        }
        return visitChildren(context);
    }

    @Override
    public Void visitDeleteStatement(DeleteStatementContext context) {
        writeWithReads = add(BehaviorAction.DELETE, relation(TargetType.Table, context.objectName()));
        return visitChildren(context);
    }

    @Override
    public Void visitCreateDatabase(CreateDatabaseContext context) {
        return schema(BehaviorAction.CREATE, context.identifier());
    }

    @Override
    public Void visitAlterDatabase(AlterDatabaseContext context) {
        return schema(BehaviorAction.ALTER, context.identifier());
    }

    @Override
    public Void visitDropDatabase(DropDatabaseContext context) {
        return schema(BehaviorAction.DROP, context.identifier());
    }

    @Override
    public Void visitUseDatabase(UseDatabaseContext context) {
        return schema(BehaviorAction.SWITCH, context.identifier());
    }

    @Override
    public Void visitCreateTable(CreateTableContext context) {
        writeWithReads = add(BehaviorAction.CREATE, relation(TargetType.Table, context.target));
        if (context.source != null) {
            readTable(context.source);
        }
        return visitChildren(context);
    }

    @Override
    public Void visitAlterTable(AlterTableContext context) {
        BehaviorObject table = relation(TargetType.Table, context.objectName());
        if (context.newName != null) {
            ObjectName sourceName = table.getObjectName();
            ObjectName destinationName = new ObjectName(sourceName.getCatalog(), sourceName.getSchema(), identifier(context.newName));
            BehaviorObject destination = resource(TargetType.Table, destinationName, context.newName);
            add(BehaviorAction.RENAME, table).getTarget().add(destination);
        } else {
            add(BehaviorAction.ALTER, table);
        }
        return visitChildren(context);
    }

    @Override
    public Void visitDropTable(DropTableContext context) {
        context.objectName().forEach(table -> add(BehaviorAction.DROP, relation(TargetType.Table, table)));
        return null;
    }

    @Override
    public Void visitTruncateTable(TruncateTableContext context) {
        add(BehaviorAction.ALTER, relation(TargetType.Table, context.objectName()));
        return null;
    }

    @Override
    public Void visitCreateView(CreateViewContext context) {
        writeWithReads = add(BehaviorAction.CREATE, relation(TargetType.View, context.objectName()));
        return visitChildren(context);
    }

    @Override
    public Void visitDropView(DropViewContext context) {
        context.objectName().forEach(view -> add(BehaviorAction.DROP, relation(TargetType.View, view)));
        return null;
    }

    @Override
    public Void visitShowDatabases(ShowDatabasesContext context) {
        add(BehaviorAction.READ, resource(TargetType.Instance, null, context));
        return visitChildren(context);
    }

    @Override
    public Void visitShowRelations(ShowRelationsContext context) {
        if (context.schemaClause() == null) {
            add(BehaviorAction.READ, resource(TargetType.Schema, new ObjectName(null, database, null), context));
        } else {
            schema(BehaviorAction.READ, context.schemaClause().identifier());
        }
        return visitChildren(context);
    }

    @Override
    public Void visitShowCreateDatabase(ShowCreateDatabaseContext context) {
        return schema(BehaviorAction.READ, context.identifier());
    }

    @Override
    public Void visitShowCreateRelation(ShowCreateRelationContext context) {
        TargetType type = context.VIEW() == null ? TargetType.Table : TargetType.View;
        add(BehaviorAction.READ, relation(type, context.objectName()));
        return null;
    }

    @Override
    public Void visitShowRelationMetadata(ShowRelationMetadataContext context) {
        var table = context.showTable();
        String schema = database;
        if (table.schemaClause() != null) {
            schema = identifier(table.schemaClause().identifier());
        }
        var name = new ObjectName(null, schema, identifier(table.identifier()));
        add(BehaviorAction.READ, resource(TargetType.Table, name, table.identifier()));
        return visitChildren(context);
    }

    @Override
    public Void visitDescribeStatement(DescribeStatementContext context) {
        add(BehaviorAction.READ, relation(TargetType.Table, context.objectName()));
        return null;
    }

    private void readTable(ObjectNameContext context) {
        BehaviorObject source = relation(TargetType.Table, context);
        reads.putIfAbsent(source.getObjectPath(), source);
    }

    private boolean isCommonTable(ObjectNameContext table) {
        if (table.identifier().size() != 1) {
            return false;
        }
        String name = identifier(table.identifier(0));
        int position = table.getStart().getTokenIndex();
        for (ParserRuleContext parent = table.getParent(); parent != null; parent = parent.getParent()) {
            if (!(parent instanceof QueryContext query) || query.withClause() == null) {
                continue;
            }

            WithClauseContext clause = query.withClause();
            for (CommonTableContext common : clause.commonTable()) {
                boolean visible = common.getStop().getTokenIndex() < position || (clause.RECURSIVE() != null && common.getStart().getTokenIndex() < position);
                if (visible && name.equals(identifier(common.identifier()))) {
                    return true;
                }
            }
        }
        return false;
    }

    private Void schema(BehaviorAction action, IdentifierContext identifier) {
        add(action, resource(TargetType.Schema, new ObjectName(null, identifier(identifier), null), identifier));
        return null;
    }

    private BehaviorObject relation(TargetType type, ObjectNameContext context) {
        List<IdentifierContext> parts = context.identifier();
        int size = parts.size();
        String catalog = null;
        String schema = database;
        if (size == 3) {
            catalog = identifier(parts.get(0));
            // The built-in catalog uses the same paths as schema-based browse and authorization.
            if (DEFAULT_CATALOG.equals(catalog)) {
                catalog = null;
            }
        }
        if (size >= 2) {
            schema = identifier(parts.get(size - 2));
        }
        String name = identifier(parts.get(size - 1));
        return resource(type, new ObjectName(catalog, schema, name), context);
    }

    private BehaviorObject resource(TargetType type, ObjectName name, ParserRuleContext context) {
        Token start = context.getStart();
        Token stop = context.getStop();
        StringBuilder path = new StringBuilder("/");
        if (!instancePath.isEmpty()) {
            path.append(instancePath).append('/');
        }
        if (name != null) {
            for (String part : new String[] { name.getCatalog(), name.getSchema(), name.getObjectName() }) {
                if (part == null) {
                    continue;
                }
                if (part.isBlank() || part.indexOf('/') >= 0) {
                    throw new AntlerSyntaxException(start.getLine(), start.getCharPositionInLine(), "Invalid GreptimeDB resource name");
                }
                path.append(part).append('/');
            }
        }

        String[] stopLines = stop.getText().split("\n", -1);
        int endColumn = stopLines[stopLines.length - 1].length();
        if (stopLines.length == 1) {
            endColumn += stop.getCharPositionInLine();
        }

        BehaviorObject object = new BehaviorObject();
        object.setObjectType(type);
        object.setObjectName(name);
        object.setObjectPath(path.toString());
        object.setStartLine(start.getLine());
        object.setStartColumn(start.getCharPositionInLine());
        object.setEndLine(stop.getLine() + stopLines.length - 1);
        object.setEndColumn(endColumn);
        return object;
    }

    private BehaviorRelation add(BehaviorAction action, BehaviorObject subject) {
        BehaviorRelation relation = new BehaviorRelation();
        relation.setAction(action);
        relation.setSubject(subject);
        behavior.getRelations().add(relation);
        return relation;
    }
}
