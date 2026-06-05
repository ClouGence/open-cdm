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
package com.clougence.clouddm.dsfamily.db2.analysis.resolve;

import java.util.*;

import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2CreateTableStatement;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2RenameTableStatement;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstraintDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbIndexDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.SqlConstraintType;
import com.clougence.clouddm.dsfamily.analysis.secrules.ref.MetaCollectTables;
import com.clougence.clouddm.dsfamily.db2.analysis.secrules.Db2ColumnDomain;
import com.clougence.clouddm.dsfamily.db2.analysis.secrules.Db2TableDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.utils.StringUtils;

public class Db2TableResolveHelper extends Db2AbstractResolveHelper {

    protected Db2QueryResolveHelper queryHelper;

    public Db2TableResolveHelper(){
        this.queryHelper = new Db2QueryResolveHelper();
    }

    public List<RuleDomain> fromCreateTable(DB2CreateTableStatement statement) {
        List<RuleDomain> result = new ArrayList<>();

        Db2TableDomain domain = new Db2TableDomain();
        domain.setSqlType(SecQueryType.CREATE_TABLE);
        domain.setAuditKind(SecQueryKind.CREATE);
        domain.setOptions(Collections.emptyMap());

        SQLExprTableSource tableSource = statement.getTableSource();
        SQLName sourceName = tableSource.getName();

        domain.setCatalog(extractCatalogName(sourceName));
        domain.setSchema(extractSchemaName(sourceName));
        domain.setTable(extractName(sourceName));
        //domain.setIfNotExists(statement.isIfNotExists());
        //domain.setTemporary(statement.isTemporary());
        domain.setColumns(new ArrayList<>());

        //        if (statement.getInherits() != null) {
        //            SQLExprTableSource inherits = statement.getInherits();
        //            SQLName inheritName = inherits.getName();
        //
        //            domain.setInherits(true);
        //            domain.setInheritTables(Collections.singletonList(extractName(inheritName)));
        //            domain.setTable(extractName(sourceName));
        //        } else {
        //            domain.setInherits(false);
        //            domain.setInheritTables(Collections.emptyList());
        //        }

        // create table (...)
        List<SQLTableElement> elementList = statement.getTableElementList();
        if (elementList != null && !elementList.isEmpty()) {
            Map<String, Db2ColumnDomain> cacheColumns = new HashMap<>();
            domain.setSqlType(SecQueryType.CREATE_TABLE);
            for (SQLTableElement element : statement.getTableElementList()) {
                if (element instanceof SQLColumnDefinition) {
                    fromCreateTableColumn(result, domain, cacheColumns, (SQLColumnDefinition) element);
                } else if (element instanceof SQLPrimaryKey) {
                    fromCreateTablePrimary(result, domain, cacheColumns, (SQLPrimaryKey) element);
                } else if (element instanceof SQLUnique) {
                    fromCreateTableUnique(result, domain, cacheColumns, (SQLUnique) element);
                } else if (element instanceof SQLForeignKeyConstraint) {
                    fromCreateTableForeignKey(result, domain, cacheColumns, (SQLForeignKeyConstraint) element);
                }
            }
        }

        //        // create table xxx like xxx
        //        SQLExprTableSource like = statement.getLike();
        //        if (like != null) {
        //            domain.setSqlType(SecQueryType.CREATE_TABLE_LIKE);
        //            domain.setSourceSchema(extractSchemaName(like.getName()));
        //            domain.setSourceTable(extractName(like.getName()));
        //        }

        // create table xxx as select xxx
        SQLSelect select = statement.getSelect();
        if (select != null) {
            domain.setSqlType(SecQueryType.CREATE_TABLE_SELECT);
            this.queryHelper.fromSelect(result, new MetaCollectTables(), select, RdbQueryMode.CREATE);
        }

        result.add(0, domain);
        return result;
    }

    private void fromCreateTableColumn(List<RuleDomain> result, Db2TableDomain tableDomain, Map<String, Db2ColumnDomain> columns, SQLColumnDefinition columnDef) {
        Db2ColumnDomain domain = toColumnDomain(tableDomain, columnDef, null);
        domain.setSqlType(SecQueryType.CREATE_TABLE_ADD_COLUMN);
        domain.setAuditKind(SecQueryKind.CREATE);

        if (domain.isPrimary()) {
            tableDomain.setHasPrimary(true);
            domain.setPrimary(true);
        }
        if (domain.isUnique()) {
            tableDomain.setHasUnique(true);
            domain.setUnique(true);
        }

        result.add(domain);

        tableDomain.getColumns().add(domain.getColumn());
        columns.put(domain.getColumn(), domain);
    }

    private void fromCreateTablePrimary(List<RuleDomain> result, Db2TableDomain tableDomain, Map<String, Db2ColumnDomain> columns, SQLPrimaryKey primaryKey) {
        RdbConstraintDomain domain = new RdbConstraintDomain();
        domain.setSqlType(SecQueryType.CREATE_TABLE_ADD_CONSTRAINT);
        domain.setAuditKind(SecQueryKind.CREATE);
        domain.setOptions(Collections.emptyMap());

        domain.setTableCatalog(tableDomain.getCatalog());
        domain.setTableSchema(tableDomain.getSchema());
        domain.setTableName(tableDomain.getTable());
        domain.setType(SqlConstraintType.Primary);
        domain.setCatalog(tableDomain.getCatalog());
        domain.setSchema(tableDomain.getSchema());
        domain.setName(getString(primaryKey.getName()));
        //domain.setComment(getString(primaryKey.getComment()));

        domain.setColumns(new ArrayList<>());

        for (SQLSelectOrderByItem item : primaryKey.getColumns()) {
            SQLExpr columnName = item.getExpr();
            String columnNameStr = columnName instanceof SQLCharExpr ? ((SQLCharExpr) columnName).getText() : ((SQLIdentifierExpr) columnName).getName();
            boolean columnNameQualifier = columnNameStr.charAt(0) == '`';
            if (columnNameQualifier) {
                columnNameStr = columnNameStr.substring(1, columnNameStr.length() - 1);
            }

            domain.getColumns().add(columnNameStr);
            tableDomain.setHasPrimary(true);
            if (columns.containsKey(columnNameStr)) {
                columns.get(columnNameStr).setPrimary(true);
            }
        }

        result.add(domain);
    }

    private void fromCreateTableUnique(List<RuleDomain> result, Db2TableDomain tableDomain, Map<String, Db2ColumnDomain> columns, SQLUnique unique) {
        RdbConstraintDomain domain = new RdbConstraintDomain();
        domain.setSqlType(SecQueryType.CREATE_TABLE_ADD_CONSTRAINT);
        domain.setAuditKind(SecQueryKind.CREATE);
        domain.setOptions(Collections.emptyMap());

        domain.setTableCatalog(tableDomain.getCatalog());
        domain.setTableSchema(tableDomain.getSchema());
        domain.setTableName(tableDomain.getTable());
        domain.setType(SqlConstraintType.Unique);

        domain.setCatalog(tableDomain.getCatalog());
        domain.setSchema(tableDomain.getSchema());
        domain.setName(getString(unique.getName()));
        //domain.setComment(getString(unique.getComment()));

        domain.setColumns(new ArrayList<>());

        for (SQLSelectOrderByItem item : unique.getColumns()) {
            String columnName = getString(item.getExpr());

            domain.getColumns().add(columnName);
            tableDomain.setHasUnique(true);
            if (columns.containsKey(columnName)) {
                columns.get(columnName).setUnique(true);
            }
        }

        result.add(domain);
    }

    private void fromCreateTableForeignKey(List<RuleDomain> result, Db2TableDomain tableDomain, Map<String, Db2ColumnDomain> columns, SQLForeignKeyConstraint foreignKey) {
        RdbConstraintDomain domain = new RdbConstraintDomain();
        domain.setSqlType(SecQueryType.CREATE_TABLE_ADD_CONSTRAINT);
        domain.setAuditKind(SecQueryKind.CREATE);
        domain.setOptions(Collections.emptyMap());

        domain.setTableCatalog(tableDomain.getCatalog());
        domain.setTableSchema(tableDomain.getSchema());
        domain.setTableName(tableDomain.getTable());
        domain.setType(SqlConstraintType.ForeignKey);
        domain.setCatalog(tableDomain.getCatalog());
        domain.setSchema(tableDomain.getSchema());
        domain.setName(getString(foreignKey.getName()));
        domain.setComment(getString(foreignKey.getComment()));

        domain.setColumns(new ArrayList<>());

        tableDomain.setHasForeignKey(true);
        for (SQLName item : foreignKey.getReferencingColumns()) {
            String columnName = getString(item);

            domain.getColumns().add(columnName);
            if (columns.containsKey(columnName)) {
                columns.get(columnName).setForeign(true);
            }
        }

        result.add(domain);
    }

    public List<RuleDomain> fromAlterTable(SQLAlterTableStatement statement) {
        List<RuleDomain> result = new ArrayList<>();

        Db2TableDomain domain = new Db2TableDomain();
        domain.setSqlType(SecQueryType.ALTER_TABLE);
        domain.setAuditKind(SecQueryKind.ALTER);
        domain.setOptions(Collections.emptyMap());

        SQLExprTableSource tableSource = statement.getTableSource();
        SQLName sourceName = tableSource.getName();

        domain.setCatalog(extractCatalogName(sourceName));
        domain.setSchema(extractSchemaName(sourceName));
        domain.setTable(extractName(sourceName));
        domain.setIfExists(statement.isIfExists());
        domain.setColumns(new ArrayList<>());

        List<SQLAlterTableItem> elementList = statement.getItems();
        if (elementList != null && !elementList.isEmpty()) {
            Map<String, Db2ColumnDomain> cacheColumns = new HashMap<>();
            for (SQLAlterTableItem element : elementList) {
                if (element instanceof SQLAlterTableAddColumn) {
                    for (SQLColumnDefinition columnDef : ((SQLAlterTableAddColumn) element).getColumns()) {
                        fromAlterTableAddColumn(result, domain, cacheColumns, columnDef);
                    }
                    continue;
                } else if (element instanceof SQLAlterTableAlterColumn) {
                    SQLName columnName = ((SQLAlterTableAlterColumn) element).getOriginColumn();
                    SQLColumnDefinition newDef = ((SQLAlterTableAlterColumn) element).getColumn();
                    if (columnName == null) {
                        columnName = newDef.getName();
                    }
                    SQLDataType dataType = ((SQLAlterTableAlterColumn) element).getDataType();
                    fromAlterTableAlterColumn(result, domain, cacheColumns, columnName, newDef, dataType);
                    continue;
                } else if (element instanceof SQLAlterTableRenameColumn) {
                    SQLName oldName = ((SQLAlterTableRenameColumn) element).getColumn();
                    SQLName newName = ((SQLAlterTableRenameColumn) element).getTo();
                    fromAlterTableRenameColumn(result, domain, cacheColumns, oldName, newName);
                    continue;
                } else if (element instanceof SQLAlterTableDropColumnItem) {
                    for (SQLName columnName : ((SQLAlterTableDropColumnItem) element).getColumns()) {
                        fromAlterTableDropColumn(result, domain, cacheColumns, columnName);
                    }
                    continue;
                } else if (element instanceof SQLAlterTableAddConstraint) {
                    SQLConstraint constraint = ((SQLAlterTableAddConstraint) element).getConstraint();
                    if (constraint instanceof SQLForeignKeyConstraint) {
                        fromAlterTableAddForeignKey(result, domain, cacheColumns, (SQLForeignKeyConstraint) constraint);
                        continue;
                    } else if (constraint instanceof SQLPrimaryKey) {
                        fromAlterTableAddPrimaryKey(result, domain, (SQLPrimaryKey) constraint);
                        continue;
                    } else if (constraint instanceof SQLUnique) {
                        fromAlterTableAddUniqueKey(result, domain, (SQLUnique) constraint);
                        continue;
                    }
                } else if (element instanceof SQLAlterTableDropConstraint) {
                    fromAlterTableDropConstraint(result, domain, (SQLAlterTableDropConstraint) element);
                    continue;
                } else if (element instanceof SQLAlterTableRename) {
                    fromAlterRenameTable(result, domain, (SQLAlterTableRename) element);
                    continue;
                } else if (element instanceof SQLAlterTableAlterIndex) {
                    fromAlterIndexVisible(result, domain, (SQLAlterTableAlterIndex) element);
                    continue;
                } else if (element instanceof SQLAlterTableRenameIndex) {
                    fromAlterIndexName(result, domain, (SQLAlterTableRenameIndex) element);
                    continue;
                } else if (element instanceof SQLAlterTableAddIndex) {
                    fromAlterIndexAdd(result, domain, (SQLAlterTableAddIndex) element);
                    continue;
                }

                throw new UnsupportedOperationException("unsupported SQL: " + statement.toString());
            }
        }

        //if (hasOptions) {
        //    result.add(0, domain);
        //}
        return result;
    }

    private void fromAlterTableDropConstraint(List<RuleDomain> result, Db2TableDomain tableDomain, SQLAlterTableDropConstraint constraint) {
        RdbConstraintDomain domain = new RdbConstraintDomain();
        domain.setSqlType(SecQueryType.ALTER_TABLE_DROP_CONSTRAINT);
        domain.setAuditKind(SecQueryKind.DROP);
        domain.setOptions(Collections.emptyMap());

        domain.setTableCatalog(tableDomain.getCatalog());
        domain.setTableSchema(tableDomain.getSchema());
        domain.setTableName(tableDomain.getTable());
        domain.setType(SqlConstraintType.ByName);
        domain.setCatalog(tableDomain.getCatalog());
        domain.setSchema(tableDomain.getSchema());
        domain.setName(getString(constraint.getConstraintName()));

        domain.setColumns(new ArrayList<>());
        tableDomain.setHasPrimary(false);
        result.add(domain);
    }

    private void fromAlterTableAddUniqueKey(List<RuleDomain> result, Db2TableDomain tableDomain, SQLUnique constraint) {
        RdbConstraintDomain domain = new RdbConstraintDomain();
        domain.setSqlType(SecQueryType.ALTER_TABLE_ADD_CONSTRAINT);
        domain.setAuditKind(SecQueryKind.CREATE);
        domain.setOptions(Collections.emptyMap());

        domain.setTableCatalog(tableDomain.getCatalog());
        domain.setTableSchema(tableDomain.getSchema());
        domain.setTableName(tableDomain.getTable());
        domain.setType(SqlConstraintType.Unique);
        domain.setCatalog(tableDomain.getCatalog());
        domain.setSchema(tableDomain.getSchema());
        domain.setName(getString(constraint.getName()));
        domain.setComment(getString(constraint.getComment()));

        domain.setColumns(new ArrayList<>());

        for (SQLSelectOrderByItem item : constraint.getColumns()) {
            String columnName = getString(item.getExpr());
            domain.getColumns().add(columnName);
        }

        tableDomain.setHasUnique(true);
        result.add(domain);
    }

    private void fromAlterTableAddPrimaryKey(List<RuleDomain> result, Db2TableDomain tableDomain, SQLPrimaryKey constraint) {
        RdbConstraintDomain domain = new RdbConstraintDomain();
        domain.setSqlType(SecQueryType.ALTER_TABLE_ADD_CONSTRAINT);
        domain.setAuditKind(SecQueryKind.CREATE);
        domain.setOptions(Collections.emptyMap());

        domain.setTableCatalog(tableDomain.getCatalog());
        domain.setTableSchema(tableDomain.getSchema());
        domain.setTableName(tableDomain.getTable());
        domain.setType(SqlConstraintType.Primary);
        domain.setCatalog(tableDomain.getCatalog());
        domain.setSchema(tableDomain.getSchema());
        domain.setName(getString(constraint.getName()));
        domain.setComment(getString(constraint.getComment()));

        domain.setColumns(new ArrayList<>());

        for (SQLSelectOrderByItem item : constraint.getColumns()) {
            String columnName = getString(item.getExpr());
            domain.getColumns().add(columnName);
        }

        tableDomain.setHasPrimary(true);
        result.add(domain);
    }

    private void fromAlterIndexAdd(List<RuleDomain> result, Db2TableDomain domain, SQLAlterTableAddIndex element) {
        RdbIndexDomain Db2ColumnDomain = new RdbIndexDomain();
        Db2ColumnDomain.setSqlType(SecQueryType.ALTER_TABLE_ADD_INDEX);
        Db2ColumnDomain.setAuditKind(SecQueryKind.CREATE);
        Db2ColumnDomain.setOptions(new HashMap<>());

        Db2ColumnDomain.setCatalog(domain.getCatalog());
        Db2ColumnDomain.setSchema(domain.getSchema());
        Db2ColumnDomain.setTableName(domain.getTable());
        Db2ColumnDomain.setName(getString(element.getIndexDefinition().getName()));

        Db2ColumnDomain.setColumns(new ArrayList<>());
        if (element.getType() != null) {
            Db2ColumnDomain.setType(element.getType());
        } else {
            Db2ColumnDomain.setType("index");
        }
        for (SQLSelectOrderByItem column : element.getColumns()) {
            String name = getString(column.getExpr());
            Db2ColumnDomain.getColumns().add(name);
        }
        Db2ColumnDomain.setComment(getString(element.getComment()));

        //        //option
        //        SQLIndexOptions options = element.getIndexDefinition().getOptions();
        //        if (options.isVisible()) {
        //            Db2ColumnDomain.getOptions().put(OPT_INDEX_VISIBLE, "true");
        //        }

        result.add(Db2ColumnDomain);
    }

    private void fromAlterIndexName(List<RuleDomain> result, Db2TableDomain domain, SQLAlterTableRenameIndex element) {
        RdbIndexDomain Db2ColumnDomain = new RdbIndexDomain();
        Db2ColumnDomain.setSqlType(SecQueryType.ALTER_INDEX);
        Db2ColumnDomain.setAuditKind(SecQueryKind.ALTER);
        Db2ColumnDomain.setOptions(Collections.emptyMap());

        Db2ColumnDomain.setCatalog(domain.getCatalog());
        Db2ColumnDomain.setSchema(domain.getSchema());
        Db2ColumnDomain.setTableName(domain.getTable());

        Db2ColumnDomain.setName(element.getName().getSimpleName());
        Db2ColumnDomain.setNewName(element.getTo().getSimpleName());

        result.add(Db2ColumnDomain);
    }

    private void fromAlterIndexVisible(List<RuleDomain> result, Db2TableDomain domain, SQLAlterTableAlterIndex element) {
        RdbIndexDomain Db2ColumnDomain = new RdbIndexDomain();
        Db2ColumnDomain.setSqlType(SecQueryType.ALTER_INDEX);
        Db2ColumnDomain.setAuditKind(SecQueryKind.ALTER);
        Db2ColumnDomain.setOptions(new HashMap<>());

        Db2ColumnDomain.setCatalog(domain.getCatalog());
        Db2ColumnDomain.setSchema(domain.getSchema());
        Db2ColumnDomain.setTableName(domain.getTable());

        //        if (element.getIndexDefinition().getOptions().isVisible()) {
        //            Db2ColumnDomain.getOptions().put(MySecDomainOptionKeys.OPT_INDEX_VISIBLE, "true");
        //        }

        result.add(Db2ColumnDomain);
    }

    private void fromAlterRenameTable(List<RuleDomain> result, Db2TableDomain domain, SQLAlterTableRename element) {
        Db2TableDomain tableDomain = new Db2TableDomain();
        tableDomain.setSqlType(SecQueryType.ALTER_TABLE_RENAME);
        tableDomain.setAuditKind(SecQueryKind.ALTER);
        tableDomain.setOptions(Collections.emptyMap());

        tableDomain.setCatalog(domain.getCatalog());
        tableDomain.setSchema(domain.getSchema());
        tableDomain.setTable(domain.getTable());
        tableDomain.setIfExists(domain.isIfExists());
        tableDomain.setColumns(new ArrayList<>());

        tableDomain.setNewName(extractName(element.getToName()));

        result.add(tableDomain);
    }

    private void fromAlterTableAddColumn(List<RuleDomain> result, Db2TableDomain tableDomain, Map<String, Db2ColumnDomain> columns, SQLColumnDefinition columnDef) {
        Db2ColumnDomain domain = toColumnDomain(tableDomain, columnDef, null);
        domain.setSqlType(SecQueryType.ALTER_TABLE_ADD_COLUMN);
        domain.setAuditKind(SecQueryKind.CREATE);

        result.add(domain);

        tableDomain.getColumns().add(domain.getColumn());
        columns.put(domain.getColumn(), domain);
    }

    private void fromAlterTableAlterColumn(List<RuleDomain> result, Db2TableDomain tableDomain, Map<String, Db2ColumnDomain> columns, SQLName columnName,
                                           SQLColumnDefinition columnDef, SQLDataType dataType) {
        Db2ColumnDomain domain = toColumnDomain(tableDomain, columnDef, dataType);
        domain.setSqlType(SecQueryType.ALTER_TABLE_ALTER_COLUMN);
        domain.setAuditKind(SecQueryKind.ALTER);

        String oldName = getString(columnName);
        String newName = domain.getColumn();

        domain.setColumn(oldName);
        if (!StringUtils.equals(oldName, newName)) {
            domain.setNewName(newName);
        }

        result.add(domain);
        tableDomain.getColumns().add(domain.getColumn());
        columns.put(domain.getColumn(), domain);
    }

    private void fromAlterTableRenameColumn(List<RuleDomain> result, Db2TableDomain tableDomain, Map<String, Db2ColumnDomain> columns, SQLName oldName, SQLName newName) {
        Db2ColumnDomain domain = new Db2ColumnDomain();
        domain.setSqlType(SecQueryType.ALTER_TABLE_RENAME_COLUMN);
        domain.setAuditKind(SecQueryKind.ALTER);
        domain.setOptions(new HashMap<>());

        domain.setCatalog(tableDomain.getCatalog());
        domain.setSchema(tableDomain.getSchema());
        domain.setTable(tableDomain.getTable());
        domain.setColumn(getString(oldName));
        domain.setNewName(getString(newName));

        result.add(domain);
        tableDomain.getColumns().remove(domain.getColumn());
        tableDomain.getColumns().add(domain.getColumn());
        columns.put(domain.getColumn(), domain);
        columns.remove(domain.getColumn());
    }

    private void fromAlterTableDropColumn(List<RuleDomain> result, Db2TableDomain tableDomain, Map<String, Db2ColumnDomain> columns, SQLName columnDef) {
        Db2ColumnDomain domain = new Db2ColumnDomain();
        domain.setSqlType(SecQueryType.ALTER_TABLE_DROP_COLUMN);
        domain.setAuditKind(SecQueryKind.DROP);
        domain.setOptions(Collections.emptyMap());

        domain.setCatalog(tableDomain.getCatalog());
        domain.setSchema(tableDomain.getSchema());
        domain.setTable(tableDomain.getTable());
        domain.setColumn(getString(columnDef));

        result.add(domain);
        tableDomain.getColumns().remove(domain.getColumn());
        columns.remove(domain.getColumn());
    }

    private void fromAlterTableAddForeignKey(List<RuleDomain> result, Db2TableDomain tableDomain, Map<String, Db2ColumnDomain> columns, SQLForeignKeyConstraint foreignKey) {
        RdbConstraintDomain domain = new RdbConstraintDomain();
        domain.setSqlType(SecQueryType.ALTER_TABLE_ADD_CONSTRAINT);
        domain.setAuditKind(SecQueryKind.CREATE);
        domain.setOptions(Collections.emptyMap());

        domain.setTableCatalog(tableDomain.getCatalog());
        domain.setTableSchema(tableDomain.getSchema());
        domain.setTableName(tableDomain.getTable());
        domain.setType(SqlConstraintType.ForeignKey);
        domain.setCatalog(tableDomain.getCatalog());
        domain.setSchema(tableDomain.getSchema());
        domain.setName(getString(foreignKey.getName()));
        //domain.setComment(getString(foreignKey.getComment()));

        domain.setColumns(new ArrayList<>());

        for (SQLName item : foreignKey.getReferencingColumns()) {
            String columnName = getString(item);

            domain.getColumns().add(columnName);
            tableDomain.setHasUnique(true);
            if (columns.containsKey(columnName)) {
                columns.get(columnName).setUnique(true);
            }
        }

        tableDomain.setHasForeignKey(true);
        result.add(domain);
    }

    public List<RuleDomain> fromDropTable(SQLDropTableStatement statement) {
        List<RuleDomain> dropList = new ArrayList<>();

        for (SQLExprTableSource tableSource : statement.getTableSources()) {
            SQLName tabName = tableSource.getName();

            Db2TableDomain domain = new Db2TableDomain();
            domain.setSqlType(SecQueryType.DROP_TABLE);
            domain.setAuditKind(SecQueryKind.DROP);
            domain.setOptions(Collections.emptyMap());

            domain.setCatalog(extractCatalogName(tabName));
            domain.setSchema(extractSchemaName(tabName));
            domain.setTable(extractName(tabName));
            domain.setIfExists(statement.isIfExists());
            domain.setColumns(new ArrayList<>());

            dropList.add(domain);
        }

        return dropList;
    }

    public List<RuleDomain> fromAlterTableComment(SQLCommentStatement statement) {
        Db2TableDomain domain = new Db2TableDomain();
        domain.setSqlType(SecQueryType.COMMENT_TABLE);
        domain.setAuditKind(SecQueryKind.ALTER);
        domain.setOptions(Collections.emptyMap());

        SQLExprTableSource tableSource = statement.getOn();
        SQLName sourceName = tableSource.getName();
        domain.setComment(((SQLCharExpr) statement.getComment()).getValue().toString());

        domain.setCatalog(extractCatalogName(sourceName));
        domain.setSchema(extractSchemaName(sourceName));
        domain.setTable(extractName(sourceName));
        domain.setColumns(Collections.emptyList());

        return Collections.singletonList(domain);
    }

    public List<RuleDomain> fromAlterColumnComment(SQLCommentStatement statement) {
        Db2ColumnDomain domain = new Db2ColumnDomain();
        domain.setSqlType(SecQueryType.COMMENT_COLUMN);
        domain.setAuditKind(SecQueryKind.ALTER);
        domain.setOptions(Collections.emptyMap());

        SQLExprTableSource tableSource = statement.getOn();
        SQLPropertyExpr colName = (SQLPropertyExpr) tableSource.getName();
        SQLName owner = (SQLName) colName.getOwner();

        domain.setCatalog(extractCatalogName(owner));
        domain.setSchema(extractSchemaName(owner));
        domain.setTable(extractName(owner));
        domain.setColumn(extractName(colName));

        domain.setComment(((SQLCharExpr) statement.getComment()).getValue().toString());

        return Collections.singletonList(domain);
    }

    public List<RuleDomain> fromRenameTable(DB2RenameTableStatement statement) {
        Db2TableDomain domain = new Db2TableDomain();
        domain.setSqlType(SecQueryType.RENAME_TABLE);
        domain.setAuditKind(SecQueryKind.ALTER);
        domain.setOptions(Collections.emptyMap());

        domain.setCatalog(extractCatalogName(statement.getName()));
        domain.setSchema(extractSchemaName(statement.getName()));
        domain.setTable(extractName(statement.getName()));

        domain.setNewName(extractName(statement.getTo()));

        return Collections.singletonList(domain);
    }
}
