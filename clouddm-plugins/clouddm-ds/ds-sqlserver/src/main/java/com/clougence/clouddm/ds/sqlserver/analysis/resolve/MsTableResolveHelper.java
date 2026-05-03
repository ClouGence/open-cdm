package com.clougence.clouddm.ds.sqlserver.analysis.resolve;

import java.util.*;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.*;
import com.alibaba.druid.sql.dialect.sqlserver.ast.stmt.SQLServerExecStatement;
import com.clougence.clouddm.ds.sqlserver.analysis.secrules.MsCatalogDomain;
import com.clougence.clouddm.ds.sqlserver.analysis.secrules.MsColumnDomain;
import com.clougence.clouddm.ds.sqlserver.analysis.secrules.MsTableDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstraintDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbIndexDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.SqlConstraintType;
import com.clougence.clouddm.dsfamily.analysis.secrules.ref.MetaCollectTables;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.utils.StringUtils;

public class MsTableResolveHelper extends MsAbstractResolveHelper {

    protected MsQueryResolveHelper queryHelper;

    public MsTableResolveHelper(){
        this.queryHelper = new MsQueryResolveHelper();
    }

    public List<RuleDomain> fromCreateTable(SQLCreateTableStatement statement) {
        List<RuleDomain> result = new ArrayList<>();

        MsTableDomain domain = new MsTableDomain();
        domain.setSqlType(SecQueryType.CREATE_TABLE);
        domain.setAuditKind(SecQueryKind.CREATE);
        domain.setOptions(Collections.emptyMap());

        SQLExprTableSource tableSource = statement.getTableSource();
        SQLName sourceName = tableSource.getName();

        domain.setCatalog(extractCatalogName(sourceName));
        domain.setSchema(extractSchemaName(sourceName));
        domain.setTable(extractName(sourceName));
        domain.setIfNotExists(statement.isIfNotExists());
        domain.setColumns(new ArrayList<>());

        // create table (...)
        List<SQLTableElement> elementList = statement.getTableElementList();
        if (elementList != null && !elementList.isEmpty()) {
            Map<String, MsColumnDomain> cacheColumns = new HashMap<>();
            domain.setSqlType(SecQueryType.CREATE_TABLE);
            for (SQLTableElement element : statement.getTableElementList()) {
                if (element instanceof SQLColumnDefinition) {
                    fromCreateTableColumn(result, domain, cacheColumns, (SQLColumnDefinition) element);
                } else if (element instanceof SQLPrimaryKeyImpl) {
                    fromCreateTablePrimary(result, domain, cacheColumns, (SQLPrimaryKeyImpl) element);
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

    private void fromCreateTableColumn(List<RuleDomain> result, MsTableDomain tableDomain, Map<String, MsColumnDomain> columns, SQLColumnDefinition columnDef) {
        MsColumnDomain domain = toColumnDomain(tableDomain, columnDef);
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

    private void fromCreateTablePrimary(List<RuleDomain> result, MsTableDomain tableDomain, Map<String, MsColumnDomain> columns, SQLPrimaryKeyImpl primaryKey) {
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
            boolean columnNameQualifier = columnNameStr.charAt(0) == '[';
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

    private void fromCreateTableUnique(List<RuleDomain> result, MsTableDomain tableDomain, Map<String, MsColumnDomain> columns, SQLUnique unique) {
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

    private void fromCreateTableForeignKey(List<RuleDomain> result, MsTableDomain tableDomain, Map<String, MsColumnDomain> columns, SQLForeignKeyConstraint foreignKey) {
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

    public List<RuleDomain> fromAlterColumnName(SQLServerExecStatement statement){
        MsCatalogDomain domain = new MsCatalogDomain();
        domain.setSqlType(SecQueryType.ALTER_TABLE_RENAME_COLUMN);
        domain.setAuditKind(SecQueryKind.ALTER);
        domain.setOptions(Collections.emptyMap());

        domain.setCatalog(extractName(statement.getParameters().get(0).getExpr().toString()));
        domain.setNewName(extractName(statement.getParameters().get(1).getExpr().toString()));

        return Collections.singletonList(domain);
    }

    public List<RuleDomain> fromAlterTable(SQLAlterTableStatement statement) {
        List<RuleDomain> result = new ArrayList<>();

        MsTableDomain domain = new MsTableDomain();
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
            Map<String, MsColumnDomain> cacheColumns = new HashMap<>();
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
                    fromAlterTableAlterColumn(result, domain, cacheColumns, columnName, newDef);
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

    private void fromAlterTableDropConstraint(List<RuleDomain> result, MsTableDomain tableDomain, SQLAlterTableDropConstraint constraint) {
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

    private void fromAlterTableAddUniqueKey(List<RuleDomain> result, MsTableDomain tableDomain, SQLUnique constraint) {
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

    private void fromAlterTableAddPrimaryKey(List<RuleDomain> result, MsTableDomain tableDomain, SQLPrimaryKey constraint) {
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

    private void fromAlterIndexAdd(List<RuleDomain> result, MsTableDomain domain, SQLAlterTableAddIndex element) {
        RdbIndexDomain MsColumnDomain = new RdbIndexDomain();
        MsColumnDomain.setSqlType(SecQueryType.ALTER_TABLE_ADD_INDEX);
        MsColumnDomain.setAuditKind(SecQueryKind.CREATE);
        MsColumnDomain.setOptions(new HashMap<>());

        MsColumnDomain.setCatalog(domain.getCatalog());
        MsColumnDomain.setSchema(domain.getSchema());
        MsColumnDomain.setTableName(domain.getTable());
        MsColumnDomain.setName(getString(element.getIndexDefinition().getName()));

        MsColumnDomain.setColumns(new ArrayList<>());
        if (element.getType() != null) {
            MsColumnDomain.setType(element.getType());
        } else {
            MsColumnDomain.setType("index");
        }
        for (SQLSelectOrderByItem column : element.getColumns()) {
            String name = getString(column.getExpr());
            MsColumnDomain.getColumns().add(name);
        }
        MsColumnDomain.setComment(getString(element.getComment()));

        //        //option
        //        SQLIndexOptions options = element.getIndexDefinition().getOptions();
        //        if (options.isVisible()) {
        //            MsColumnDomain.getOptions().put(OPT_INDEX_VISIBLE, "true");
        //        }

        result.add(MsColumnDomain);
    }

    private void fromAlterIndexName(List<RuleDomain> result, MsTableDomain domain, SQLAlterTableRenameIndex element) {
        RdbIndexDomain MsColumnDomain = new RdbIndexDomain();
        MsColumnDomain.setSqlType(SecQueryType.ALTER_INDEX);
        MsColumnDomain.setAuditKind(SecQueryKind.ALTER);
        MsColumnDomain.setOptions(Collections.emptyMap());

        MsColumnDomain.setCatalog(domain.getCatalog());
        MsColumnDomain.setSchema(domain.getSchema());
        MsColumnDomain.setTableName(domain.getTable());

        MsColumnDomain.setName(element.getName().getSimpleName());
        MsColumnDomain.setNewName(element.getTo().getSimpleName());

        result.add(MsColumnDomain);
    }

    private void fromAlterIndexVisible(List<RuleDomain> result, MsTableDomain domain, SQLAlterTableAlterIndex element) {
        RdbIndexDomain MsColumnDomain = new RdbIndexDomain();
        MsColumnDomain.setSqlType(SecQueryType.ALTER_INDEX);
        MsColumnDomain.setAuditKind(SecQueryKind.ALTER);
        MsColumnDomain.setOptions(new HashMap<>());

        MsColumnDomain.setCatalog(domain.getCatalog());
        MsColumnDomain.setSchema(domain.getSchema());
        MsColumnDomain.setTableName(domain.getTable());

        //        if (element.getIndexDefinition().getOptions().isVisible()) {
        //            MsColumnDomain.getOptions().put(MySecDomainOptionKeys.OPT_INDEX_VISIBLE, "true");
        //        }

        result.add(MsColumnDomain);
    }

    private void fromAlterRenameTable(List<RuleDomain> result, MsTableDomain domain, SQLAlterTableRename element) {
        MsTableDomain tableDomain = new MsTableDomain();
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

    private void fromAlterTableAddColumn(List<RuleDomain> result, MsTableDomain tableDomain, Map<String, MsColumnDomain> columns, SQLColumnDefinition columnDef) {
        MsColumnDomain domain = toColumnDomain(tableDomain, columnDef);
        domain.setSqlType(SecQueryType.ALTER_TABLE_ADD_COLUMN);
        domain.setAuditKind(SecQueryKind.CREATE);

        result.add(domain);

        tableDomain.getColumns().add(domain.getColumn());
        columns.put(domain.getColumn(), domain);
    }

    private void fromAlterTableAlterColumn(List<RuleDomain> result, MsTableDomain tableDomain, Map<String, MsColumnDomain> columns, SQLName columnName,
                                           SQLColumnDefinition columnDef) {
        MsColumnDomain domain = toColumnDomain(tableDomain, columnDef);
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

    private void fromAlterTableRenameColumn(List<RuleDomain> result, MsTableDomain tableDomain, Map<String, MsColumnDomain> columns, SQLName oldName, SQLName newName) {
        MsColumnDomain domain = new MsColumnDomain();
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

    private void fromAlterTableDropColumn(List<RuleDomain> result, MsTableDomain tableDomain, Map<String, MsColumnDomain> columns, SQLName columnDef) {
        MsColumnDomain domain = new MsColumnDomain();
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

    private void fromAlterTableAddForeignKey(List<RuleDomain> result, MsTableDomain tableDomain, Map<String, MsColumnDomain> columns, SQLForeignKeyConstraint foreignKey) {
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

            MsTableDomain domain = new MsTableDomain();
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
        MsTableDomain domain = new MsTableDomain();
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
        MsColumnDomain domain = new MsColumnDomain();
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
}
