package com.clougence.clouddm.ds.maxcompute.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;

import com.clougence.clouddm.ds.maxcompute.analysis.builder.McBuilderFactory;
import com.clougence.clouddm.ds.maxcompute.analysis.builder.utils.McBuilderUtil;
import com.clougence.clouddm.ds.maxcompute.analysis.secrules.McColumnDomain;
import com.clougence.clouddm.ds.maxcompute.analysis.secrules.McTableDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.AlterTableType;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ConstraintTypeDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.KeyValueDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.*;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.ds.maxcompute.parser.antlr.McParserBaseVisitor;
import com.clougence.clouddm.ds.maxcompute.parser.antlr.McParserParser;
import com.clougence.schema.umi.struts.UmiTypes;

public class McSQLParserVisitor extends McParserBaseVisitor<Void> {

    private final McBuilderFactory builder;
    private final Parser           parser;

    public McSQLParserVisitor(McBuilderFactory builder, Parser parser){
        this.builder = builder;
        this.parser = parser;
    }

    private String getText(ParserRuleContext context) {
        return parser.getTokenStream().getText(context.getStart(), context.getStop());
    }

    private String getText(Token start, Token stop) {
        return parser.getTokenStream().getText(start, stop);
    }

    private String getName(String name) {
        if (name.startsWith("`")) {
            return name.substring(1, name.length() - 1);
        }
        return name;
    }

    @Override
    public Void visitShowTables(McParserParser.ShowTablesContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setNeedSupply(true);
        rdbResourceDomain.setTarget(TargetType.Table);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowRoles(McParserParser.ShowRolesContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setNeedSupply(true);
        rdbResourceDomain.setTarget(TargetType.UserOrRole);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowUsers(McParserParser.ShowUsersContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setNeedSupply(true);
        rdbResourceDomain.setTarget(TargetType.UserOrRole);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowTrustProjects(McParserParser.ShowTrustProjectsContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setNeedSupply(true);
        rdbResourceDomain.setTarget(TargetType.Catalog);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowTableColumnStatics(McParserParser.ShowTableColumnStaticsContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setNeedSupply(true);
        rdbResourceDomain.setTarget(TargetType.Table);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        List<String> list = parserQident(ctx.table_name().qident());
        Map<UmiTypes, String> map = McBuilderUtil.parseTableName(list, this.builder.getSchemaEnabled());

        rdbResourceDomain.setName(map.get(UmiTypes.Table));
        rdbResourceDomain.setCatalog(map.get(UmiTypes.Catalog));
        rdbResourceDomain.setSchema(map.get(UmiTypes.Schema));

        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowHistoryTables(McParserParser.ShowHistoryTablesContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setNeedSupply(true);
        rdbResourceDomain.setTarget(TargetType.Table);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowHistoryTable(McParserParser.ShowHistoryTableContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setNeedSupply(true);
        rdbResourceDomain.setTarget(TargetType.Table);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        List<String> list = parserQident(ctx.table_name().qident());
        Map<UmiTypes, String> map = McBuilderUtil.parseTableName(list, this.builder.getSchemaEnabled());

        rdbResourceDomain.setCatalog(map.get(UmiTypes.Catalog));
        rdbResourceDomain.setSchema(map.get(UmiTypes.Schema));
        rdbResourceDomain.setName(map.get(UmiTypes.Table));

        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowTablePartitions(McParserParser.ShowTablePartitionsContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setNeedSupply(true);
        rdbResourceDomain.setTarget(TargetType.Table);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        List<String> list = parserQident(ctx.table_name().qident());
        Map<UmiTypes, String> map = McBuilderUtil.parseTableName(list, this.builder.getSchemaEnabled());

        rdbResourceDomain.setCatalog(map.get(UmiTypes.Catalog));
        rdbResourceDomain.setSchema(map.get(UmiTypes.Schema));
        rdbResourceDomain.setName(map.get(UmiTypes.Table));

        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitShowCreateTable(McParserParser.ShowCreateTableContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setNeedSupply(true);
        rdbResourceDomain.setTarget(TargetType.Table);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        List<String> list = parserQident(ctx.table_name().qident());
        Map<UmiTypes, String> map = McBuilderUtil.parseTableName(list, this.builder.getSchemaEnabled());

        rdbResourceDomain.setCatalog(map.get(UmiTypes.Catalog));
        rdbResourceDomain.setSchema(map.get(UmiTypes.Schema));
        rdbResourceDomain.setName(map.get(UmiTypes.Table));

        builder.addDomain(rdbResourceDomain);
        return null;
    }

    private List<String> parserQident(McParserParser.QidentContext ctx) {
        List<String> list = new ArrayList<>();
        for (McParserParser.IdentContext identContext : ctx.ident()) {
            list.add(getName(getText(identContext)));
        }
        return list;
    }

    @Override
    public Void visitDropView(McParserParser.DropViewContext ctx) {
        builder.enterDropView();
        if (ctx.T_EXISTS() != null) {
            builder.addAttr(CommonAttribute.IF_EXISTS, true);
        }
        ctx.table_name().accept(this);
        builder.exitDropView();
        return null;
    }

    @Override
    public Void visitDropMView(McParserParser.DropMViewContext ctx) {
        builder.enterDropView();
        if (ctx.T_EXISTS() != null) {
            builder.addAttr(CommonAttribute.IF_EXISTS, true);
        }
        builder.enterObjName();
        ctx.ident().accept(this);
        builder.exitObjName();

        builder.exitDropView();
        return null;
    }

    @Override
    public Void visitCreate_view_stmt(McParserParser.Create_view_stmtContext ctx) {
        builder.enterCreateView();
        ctx.table_name().accept(this);
        builder.exitCreateView();
        return null;
    }

    @Override
    public Void visitTbprops(McParserParser.TbpropsContext ctx) {
        builder.enterOptionsBuilder();
        dmVisitChildren(ctx);
        builder.exitOptionsBuilder();
        return null;
    }

    @Override
    public Void visitProp(McParserParser.PropContext ctx) {
        KeyValueDomain domain = new KeyValueDomain(getString(ctx.key.getText()), getString(ctx.value.getText()));
        builder.handleDomain(domain, DomainSource.KEY_VALUE);
        return null;
    }

    @Override
    public Void visitAlterTablePartition(McParserParser.AlterTablePartitionContext ctx) {
        McTableDomain mcTableDomain = new McTableDomain();
        mcTableDomain.setAuditKind(SecQueryKind.ALTER);
        mcTableDomain.setSqlType(SecQueryType.ALTER_TABLE);
        builder.handleDomain(mcTableDomain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitAlterTableProp(McParserParser.AlterTablePropContext ctx) {
        McTableDomain mcTableDomain = new McTableDomain();
        mcTableDomain.setAuditKind(SecQueryKind.ALTER);
        mcTableDomain.setSqlType(SecQueryType.ALTER_TABLE);
        builder.handleDomain(mcTableDomain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitAssignment_stmt(McParserParser.Assignment_stmtContext ctx) {
        if ("odps.namespace.schema".equalsIgnoreCase(getText(ctx.key))) {
            throw new UnsupportedOperationException("unsupported config: " + this.getText(ctx.key));
        }
        RdbConfigDomain rdbConfigDomain = new RdbConfigDomain(getText(ctx.key));
        rdbConfigDomain.setAuditKind(SecQueryKind.OTHER);
        rdbConfigDomain.setSqlType(SecQueryType.CONFIG_WRITE);
        builder.addDomain(rdbConfigDomain);
        return null;
    }

    @Override
    public Void visitAssignment_stmt_single_item(McParserParser.Assignment_stmt_single_itemContext ctx) {
        builder.enterSetColumnValue();
        dmVisitChildren(ctx);
        builder.exitSetColumnValue();
        return null;
    }

    @Override
    public Void visitCreate_table_stmt(McParserParser.Create_table_stmtContext ctx) {
        if (ctx.create_table_definition() instanceof McParserParser.CreateTableColumnContext) {
            builder.enterCreateTable(SecQueryType.CREATE_TABLE);
        } else if (ctx.create_table_definition() instanceof McParserParser.CreateTableLikeContext) {
            builder.enterCreateTable(SecQueryType.CREATE_TABLE_LIKE);
        } else {
            builder.enterCreateTable(SecQueryType.CREATE_TABLE_SELECT);
        }
        if (ctx.T_EXISTS() != null) {
            builder.addAttr(CommonAttribute.IF_NOT_EXISTS, true);
        }
        dmVisitChildren(ctx);
        builder.exitCreateTable();
        return null;
    }

    @Override
    public Void visitAlterTableCompctMajor(McParserParser.AlterTableCompctMajorContext ctx) {
        McTableDomain mcTableDomain = new McTableDomain();
        mcTableDomain.setAuditKind(SecQueryKind.ALTER);
        mcTableDomain.setSqlType(SecQueryType.ALTER_TABLE);
        builder.handleDomain(mcTableDomain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitCreate_table_partitions(McParserParser.Create_table_partitionsContext ctx) {
        return null;
    }

    @Override
    public Void visitAlterTableDropColumns(McParserParser.AlterTableDropColumnsContext ctx) {
        builder.enterColumnList();
        dmVisitChildren(ctx);
        builder.exitColumnList();
        return null;
    }

    @Override
    public Void visitCreate_table_columns_item(McParserParser.Create_table_columns_itemContext ctx) {
        builder.enterColumnDef();
        if (ctx.T_NOT() != null) {
            builder.addAttr(CommonAttribute.NOT_NULL, true);
        }
        if (ctx.T_DEFAULT() != null) {
            builder.handleDomain(new RdbConstantDomain(getText(ctx.default_value())), DomainSource.COLUMN_DEFAULT_VALUE);
        }
        if (ctx.T_PRIMARY() != null) {
            builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.Primary), DomainSource.CONSTRAINT_TYPE);
        }

        if (ctx.comment != null) {
            String text = ctx.comment.getText();
            builder.addAttr(CommonAttribute.COMMENT, text.substring(1, text.length() - 1));
        }

        ctx.column_name().accept(this);
        ctx.column_type().accept(this);

        builder.exitColumnDef();
        return null;
    }

    @Override
    public Void visitCreate_table_primary(McParserParser.Create_table_primaryContext ctx) {
        builder.enterConstraint();
        builder.handleDomain(new ConstraintTypeDomain(SqlConstraintType.Primary), DomainSource.CONSTRAINT_TYPE);
        if (ctx.name != null) {
            ctx.name.accept(this);
        }

        builder.enterColumnList();
        for (McParserParser.IdentContext column : ctx.columns) {
            column.accept(this);
        }
        builder.exitColumnList();
        builder.exitConstraint();
        return null;
    }

    @Override
    public Void visitComment_clause(McParserParser.Comment_clauseContext ctx) {
        builder.addAttr(CommonAttribute.COMMENT, getString(ctx.L_S_STRING().getText()));
        return null;
    }

    private String getString(String text) {
        if (text.startsWith("'") || text.startsWith("\"")) {
            return text.substring(1, text.length() - 1);
        }
        return text;
    }

    @Override
    public Void visitAlter_table_stmt(McParserParser.Alter_table_stmtContext ctx) {
        builder.enterAlterTable();
        dmVisitChildren(ctx);
        builder.exitAlterTable();
        return null;
    }

    @Override
    public Void visitAlterTableComment(McParserParser.AlterTableCommentContext ctx) {
        McTableDomain mcTableDomain = new McTableDomain();
        mcTableDomain.setAuditKind(SecQueryKind.ALTER);
        mcTableDomain.setSqlType(SecQueryType.ALTER_TABLE);
        mcTableDomain.setComment(getString(ctx.comment.getText()));
        builder.handleDomain(mcTableDomain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitAlterTableLifeCycle(McParserParser.AlterTableLifeCycleContext ctx) {
        McTableDomain mcTableDomain = new McTableDomain();
        mcTableDomain.setAuditKind(SecQueryKind.ALTER);
        mcTableDomain.setSqlType(SecQueryType.ALTER_TABLE);
        builder.handleDomain(mcTableDomain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitAlterTableEnableLifeCycle(McParserParser.AlterTableEnableLifeCycleContext ctx) {
        McTableDomain mcTableDomain = new McTableDomain();
        mcTableDomain.setAuditKind(SecQueryKind.ALTER);
        mcTableDomain.setSqlType(SecQueryType.ALTER_TABLE);
        builder.handleDomain(mcTableDomain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitAlterTableClustered(McParserParser.AlterTableClusteredContext ctx) {
        McTableDomain mcTableDomain = new McTableDomain();
        mcTableDomain.setAuditKind(SecQueryKind.ALTER);
        mcTableDomain.setSqlType(SecQueryType.ALTER_TABLE);
        builder.handleDomain(mcTableDomain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitAlterTableTouch(McParserParser.AlterTableTouchContext ctx) {
        McTableDomain mcTableDomain = new McTableDomain();
        mcTableDomain.setAuditKind(SecQueryKind.ALTER);
        mcTableDomain.setSqlType(SecQueryType.ALTER_TABLE);
        builder.handleDomain(mcTableDomain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitAlterTableReanme(McParserParser.AlterTableReanmeContext ctx) {
        McTableDomain mcTableDomain = new McTableDomain();
        mcTableDomain.setAuditKind(SecQueryKind.ALTER);
        mcTableDomain.setSqlType(SecQueryType.ALTER_TABLE_RENAME);
        String text = getText(ctx.ident());
        if (text.startsWith("`")) {
            text = text.substring(1, text.length() - 1);
        }
        mcTableDomain.setNewName(text);
        builder.handleDomain(mcTableDomain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitDtype(McParserParser.DtypeContext ctx) {
        builder.enterObjName();
        builder.addAttr(CommonAttribute.VALUE, getText(ctx));
        builder.exitObjName();
        return null;
    }

    @Override
    public Void visitAlterTableChangeColumn(McParserParser.AlterTableChangeColumnContext ctx) {
        builder.enterAlterTableItem(AlterTableType.ALTER_COLUMN);
        builder.addAttr(CommonAttribute.VALUE, ctx.oldname.getText());

        builder.enterColumnDef();
        builder.enterObjName();
        ctx.newname.accept(this);
        builder.exitObjName();

        if (ctx.comment_clause() != null) {
            ctx.comment_clause().accept(this);
        }

        ctx.column_type().accept(this);

        builder.exitColumnDef();
        builder.exitAlterTableItem();
        return null;
    }

    @Override
    public Void visitAlterTableChangeColumnName(McParserParser.AlterTableChangeColumnNameContext ctx) {
        builder.enterAlterTableItem(AlterTableType.ALTER_COLUMN);
        builder.addAttr(CommonAttribute.VALUE, ctx.oldname.getText());

        builder.enterColumnDef();
        builder.enterObjName();
        ctx.newname.accept(this);
        builder.exitObjName();

        builder.exitColumnDef();
        builder.exitAlterTableItem();
        return null;
    }

    @Override
    public Void visitAlterTableChangeColumnNull(McParserParser.AlterTableChangeColumnNullContext ctx) {
        McColumnDomain mcColumnDomain = new McColumnDomain();
        mcColumnDomain.setColumn(getName(getText(ctx.oldname)));
        mcColumnDomain.setAuditKind(SecQueryKind.ALTER);
        mcColumnDomain.setSqlType(SecQueryType.ALTER_TABLE_ALTER_COLUMN);
        mcColumnDomain.setNullable(true);
        builder.handleDomain(mcColumnDomain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitAlterTableChangeColumnComment(McParserParser.AlterTableChangeColumnCommentContext ctx) {
        McColumnDomain mcColumnDomain = new McColumnDomain();
        mcColumnDomain.setColumn(getName(getText(ctx.oldname)));
        mcColumnDomain.setAuditKind(SecQueryKind.ALTER);
        mcColumnDomain.setSqlType(SecQueryType.ALTER_TABLE_ALTER_COLUMN);
        mcColumnDomain.setComment(getString(ctx.comment_clause().L_S_STRING().getText()));
        builder.handleDomain(mcColumnDomain, DomainSource.ALTER_TABLE_ITEM);
        return null;
    }

    @Override
    public Void visitColumn_type(McParserParser.Column_typeContext ctx) {
        builder.enterColumnType(getText(ctx));
        dmVisitChildren(ctx);
        builder.exitColumnType();
        return null;
    }

    @Override
    public Void visitDtype_len(McParserParser.Dtype_lenContext ctx) {
        builder.handleDomain(new RdbConstantDomain(ctx.L_INT(0).getText()), DomainSource.CONSTANT);
        return null;
    }

    @Override
    public Void visitCreate_database_stmt(McParserParser.Create_database_stmtContext ctx) {
        builder.enterCreateSchema();
        if (ctx.T_EXISTS() != null) {
            builder.addAttr(CommonAttribute.IF_NOT_EXISTS, true);
        }
        dmVisitChildren(ctx);
        builder.exitCreateSchema();
        return null;
    }

    @Override
    public Void visitDropTable(McParserParser.DropTableContext ctx) {
        builder.enterDropTable();
        if (ctx.T_EXISTS() != null) {
            builder.addAttr(CommonAttribute.IF_EXISTS, true);
        }
        dmVisitChildren(ctx);
        builder.exitDropTable();
        return null;
    }

    @Override
    public Void visitDropSchema(McParserParser.DropSchemaContext ctx) {
        builder.enterDropSchema();
        if (ctx.T_EXISTS() != null) {
            builder.addAttr(CommonAttribute.IF_EXISTS, true);
        }
        dmVisitChildren(ctx);
        builder.exitDropSchema();
        return null;
    }

    @Override
    public Void visitInsert_stmt(McParserParser.Insert_stmtContext ctx) {
        builder.enterInsertBuilder();
        dmVisitChildren(ctx);
        builder.exitInsertBuilder();
        return null;
    }

    @Override
    public Void visitPt_spec(McParserParser.Pt_specContext ctx) {
        return null;
    }

    @Override
    public Void visitZorder_stmt(McParserParser.Zorder_stmtContext ctx) {
        return null;
    }

    @Override
    public Void visitInsert_stmt_cols(McParserParser.Insert_stmt_colsContext ctx) {
        builder.enterInsertColumnBuilder();
        dmVisitChildren(ctx);
        builder.exitInsertColumnBuilder();
        return null;
    }

    @Override
    public Void visitInsert_stmt_rows(McParserParser.Insert_stmt_rowsContext ctx) {
        if (ctx.insert_stmt_row().size() > 1) {
            builder.addAttr(CommonAttribute.MULTI_VALUE, true);
        }
        builder.enterValuesBuilder();
        dmVisitChildren(ctx);
        builder.exitValuesBuilder();
        return null;
    }

    @Override
    public Void visitTruncate_stmt(McParserParser.Truncate_stmtContext ctx) {
        builder.enterTruncate();
        ctx.table_name().accept(this);
        builder.exitTruncate();
        return null;
    }

    @Override
    public Void visitSelect_stmt(McParserParser.Select_stmtContext ctx) {
        builder.enterSelectDomain();
        dmVisitChildren(ctx);
        builder.exitSelectDomain();
        return null;
    }

    @Override
    public Void visitCte_select_stmt_item(McParserParser.Cte_select_stmt_itemContext ctx) {
        builder.enterWithSelect();
        builder.addAttr(CommonAttribute.VALUE, getText(ctx.ident()));
        dmVisitChildren(ctx);
        builder.exitWithSelect();
        return null;
    }

    // todo
    //    @Override
    //    public Void visitFullselect_stmt(McParserParser.Fullselect_stmtContext ctx) {
    //        throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx));
    //    }

    @Override
    public Void visitFullselect_stmt_item(McParserParser.Fullselect_stmt_itemContext ctx) {
        builder.enterSelectDomain();
        dmVisitChildren(ctx);
        builder.exitSelectDomain();
        return null;
    }

    @Override
    public Void visitFullselect_set_clause(McParserParser.Fullselect_set_clauseContext ctx) {
        builder.addAttr(CommonAttribute.UNION, true);
        return null;
    }

    @Override
    public Void visitSubselect_stmt(McParserParser.Subselect_stmtContext ctx) {
        builder.enterSelectDomain();
        if (ctx.T_LIMIT() != null) {
            builder.addAttr(CommonAttribute.LIMIT, true);
        }
        dmVisitChildren(ctx);
        builder.exitSelectDomain();
        return null;
    }

    @Override
    public Void visitSelect_list_set(McParserParser.Select_list_setContext ctx) {
        return null;
    }

    @Override
    public Void visitSelect_list_item(McParserParser.Select_list_itemContext ctx) {
        builder.enterBuildSelectItem();
        if (ctx.expr() != null) {
            builder.addAttr(CommonAttribute.VALUE, getText(ctx.expr()));
        } else if (ctx.bool_expr() != null) {
            builder.addAttr(CommonAttribute.VALUE, getText(ctx.bool_expr()));
        }

        dmVisitChildren(ctx);
        builder.exitBuildSelectItem();
        return null;
    }

    @Override
    public Void visitSelect_list_alias(McParserParser.Select_list_aliasContext ctx) {
        builder.addAttr(CommonAttribute.ALIAS, getName(getText(ctx.ident())));
        return null;
    }

    @Override
    public Void visitSelect_list_asterisk(McParserParser.Select_list_asteriskContext ctx) {
        RdbColumnDomain rdbColumnDomain = new RdbColumnDomain();
        rdbColumnDomain.setColumn("*");
        if (ctx.L_ID() != null) {
            rdbColumnDomain.setTable(ctx.L_ID().getText());
        }
        builder.handleDomain(rdbColumnDomain, DomainSource.COLUMN);
        return null;
    }

    @Override
    public Void visitDistribute_clause(McParserParser.Distribute_clauseContext ctx) {
        return null;
    }

    @Override
    public Void visitSort_clause(McParserParser.Sort_clauseContext ctx) {
        return null;
    }

    @Override
    public Void visitFrom_clause(McParserParser.From_clauseContext ctx) {
        builder.enterSelectFromBuilder();
        dmVisitChildren(ctx);
        builder.exitSelectFromBuilder();
        return null;
    }

    @Override
    public Void visitFrom_table_clause(McParserParser.From_table_clauseContext ctx) {
        builder.enterSelectTableBuilder();
        dmVisitChildren(ctx);
        builder.exitSelectTableBuilder();
        return null;
    }

    @Override
    public Void visitFrom_join_clause(McParserParser.From_join_clauseContext ctx) {
        for (ParseTree child : ctx.children) {
            if (child instanceof McParserParser.Bool_exprContext) {
                continue;
            } else {
                child.accept(this);
            }
        }
        return null;
    }

    @Override
    public Void visitFrom_join_type_clause(McParserParser.From_join_type_clauseContext ctx) {
        builder.enterJoin(ctx.getText());
        builder.exitJoin();
        return null;
    }

    @Override
    public Void visitFrom_alias_clause(McParserParser.From_alias_clauseContext ctx) {
        builder.addAttr(CommonAttribute.ALIAS, getName(getText(ctx.ident())));
        return null;
    }

    @Override
    public Void visitWhere_clause(McParserParser.Where_clauseContext ctx) {
        builder.enterWhere();
        dmVisitChildren(ctx);
        builder.exitWhere();
        return null;
    }

    @Override
    public Void visitGroup_by_clause(McParserParser.Group_by_clauseContext ctx) {
        //        throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx));
        return null;
    }

    @Override
    public Void visitHaving_clause(McParserParser.Having_clauseContext ctx) {
        return null;
    }

    @Override
    public Void visitOrder_by_clause(McParserParser.Order_by_clauseContext ctx) {
        return null;
    }

    @Override
    public Void visitUpdate_stmt(McParserParser.Update_stmtContext ctx) {
        builder.enterUpdate();
        dmVisitChildren(ctx);
        builder.exitUpdate();
        return null;
    }

    @Override
    public Void visitUpdate_table(McParserParser.Update_tableContext ctx) {
        if (ctx.table_name() == null && ctx.getChildCount() > 1) {
            throw new UnsupportedOperationException("unsupported SQL: " + this.getText(ctx));
        }
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitDelete_stmt(McParserParser.Delete_stmtContext ctx) {
        builder.enterDelete();
        dmVisitChildren(ctx);
        builder.exitDelete();
        return null;
    }

    @Override
    public Void visitDescribe_stmt(McParserParser.Describe_stmtContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setNeedSupply(true);
        rdbResourceDomain.setTarget(TargetType.Table);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        List<String> list = parserQident(ctx.table_name().qident());
        Map<UmiTypes, String> map = McBuilderUtil.parseTableName(list, builder.getSchemaEnabled());

        rdbResourceDomain.setCatalog(map.get(UmiTypes.Catalog));
        rdbResourceDomain.setSchema(map.get(UmiTypes.Schema));
        rdbResourceDomain.setName(map.get(UmiTypes.Table));

        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitBool_expr_unary(McParserParser.Bool_expr_unaryContext ctx) {
        builder.addAttr(CommonAttribute.VALID_WHERE, true);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitBool_expr_single_in(McParserParser.Bool_expr_single_inContext ctx) {
        builder.addAttr(CommonAttribute.VALID_WHERE, true);
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitBool_expr_binary(McParserParser.Bool_expr_binaryContext ctx) {
        String left = getText(ctx.expr(0));
        String right = getText(ctx.expr(1));
        if (left.startsWith("(")) {
            left = left.substring(1, left.length() - 1);
        }
        if (right.startsWith("(")) {
            right = right.substring(1, right.length() - 1);
        }
        if (!left.equals(right)) {
            builder.addAttr(CommonAttribute.VALID_WHERE, true);
        }

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitExpr_cast(McParserParser.Expr_castContext ctx) {
        builder.handleDomain(new RdbConstantDomain(getText(ctx)), DomainSource.CONSTANT);
        return null;
    }

    @Override
    public Void visitExpr_atom(McParserParser.Expr_atomContext ctx) {
        if (ctx.getChild(0) instanceof McParserParser.QidentContext) {
            builder.enterSelectColumn();
            dmVisitChildren(ctx);
            builder.exitSelectColumn();
        } else {
            dmVisitChildren(ctx);
        }
        return null;
    }

    @Override
    public Void visitAnalyze_table_stmt(McParserParser.Analyze_table_stmtContext ctx) {
        RdbResourceDomain rdbResourceDomain = new RdbResourceDomain();
        rdbResourceDomain.setNeedSupply(true);
        rdbResourceDomain.setTarget(TargetType.Table);
        rdbResourceDomain.setSqlType(SecQueryType.SHOW);
        rdbResourceDomain.setAuditKind(SecQueryKind.QUERY);
        List<String> list = parserQident(ctx.tableName);
        Map<UmiTypes, String> map = McBuilderUtil.parseTableName(list, builder.getSchemaEnabled());

        rdbResourceDomain.setCatalog(map.get(UmiTypes.Catalog));
        rdbResourceDomain.setSchema(map.get(UmiTypes.Schema));
        rdbResourceDomain.setName(map.get(UmiTypes.Table));

        builder.addDomain(rdbResourceDomain);
        return null;
    }

    @Override
    public Void visitExpr_agg_window_func(McParserParser.Expr_agg_window_funcContext ctx) {
        builder.enterCall();

        ObjNameDomain objNameDomain = new ObjNameDomain(ctx.getChild(0).getText(), null);
        builder.handleDomain(objNameDomain, DomainSource.OBJ_NAME);

        if (ctx.T_MUL() != null) {
            builder.enterFunctionArgs();
            builder.addAttr(CommonAttribute.FUNC_ARG_NAME, "*");
            builder.exitFunctionArgs();
        }

        for (McParserParser.ExprContext exprContext : ctx.expr()) {
            builder.enterFunctionArgs();
            builder.addAttr(CommonAttribute.FUNC_ARG_NAME, getText(exprContext));
            exprContext.accept(this);
            builder.exitFunctionArgs();
        }
        builder.exitCall();
        return null;
    }

    @Override
    public Void visitExpr_spec_func(McParserParser.Expr_spec_funcContext ctx) {
        builder.enterCall();

        ObjNameDomain objNameDomain = new ObjNameDomain(ctx.getChild(0).getText(), null);
        builder.handleDomain(objNameDomain, DomainSource.OBJ_NAME);

        for (McParserParser.ExprContext exprContext : ctx.expr()) {
            builder.enterFunctionArgs();
            builder.addAttr(CommonAttribute.FUNC_ARG_NAME, getText(exprContext));
            exprContext.accept(this);
            builder.exitFunctionArgs();
        }

        builder.exitCall();
        return null;
    }

    @Override
    public Void visitExpr_func(McParserParser.Expr_funcContext ctx) {
        builder.enterCall();

        builder.enterObjName();
        ctx.ident().accept(this);
        builder.exitObjName();

        if (ctx.expr_func_params() != null) {
            ctx.expr_func_params().accept(this);
        }

        builder.exitCall();
        return null;
    }

    @Override
    public Void visitExpr_func_params(McParserParser.Expr_func_paramsContext ctx) {
        builder.enterFunctionArgs();
        dmVisitChildren(ctx);
        builder.exitFunctionArgs();
        return null;
    }

    @Override
    public Void visitFunc_param(McParserParser.Func_paramContext ctx) {
        builder.addAttr(CommonAttribute.FUNC_ARG_NAME, getText(ctx));
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitAlter_materialized_view_stmt(McParserParser.Alter_materialized_view_stmtContext ctx) {
        builder.enterAlterView();
        builder.enterObjName();
        ctx.name.accept(this);
        builder.exitObjName();
        builder.addAttr(CommonAttribute.MATERIALIZED, true);
        builder.exitAlterView();
        return null;
    }

    @Override
    public Void visitCreate_materialized_view_stmt(McParserParser.Create_materialized_view_stmtContext ctx) {
        builder.enterCreateView();
        builder.enterObjName();
        ctx.name.accept(this);
        builder.exitObjName();
        builder.addAttr(CommonAttribute.MATERIALIZED, true);
        builder.exitCreateView();
        return null;
    }

    @Override
    public Void visitTimestamp_literal(McParserParser.Timestamp_literalContext ctx) {

        builder.handleDomain(new RdbConstantDomain(getText(ctx)), DomainSource.CONSTANT);
        return null;
    }

    @Override
    public Void visitIdent(McParserParser.IdentContext ctx) {
        builder.addAttr(CommonAttribute.VALUE, getText(ctx));
        return null;
    }

    @Override
    public Void visitQident(McParserParser.QidentContext ctx) {
        builder.enterObjName();
        dmVisitChildren(ctx);
        builder.exitObjName();
        return null;
    }

    @Override
    public Void visitStmt(McParserParser.StmtContext ctx) {
        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitAlter_materialized_view_item(McParserParser.Alter_materialized_view_itemContext ctx) {

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitAssignment_stmt_item(McParserParser.Assignment_stmt_itemContext ctx) {

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCreate_partition_column(McParserParser.Create_partition_columnContext ctx) {

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCreateTableSelect(McParserParser.CreateTableSelectContext ctx) {

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCreateTableColumn(McParserParser.CreateTableColumnContext ctx) {

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCreateTableLike(McParserParser.CreateTableLikeContext ctx) {

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCreate_table_columns(McParserParser.Create_table_columnsContext ctx) {

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitDefault_value(McParserParser.Default_valueContext ctx) {

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitColumn_name(McParserParser.Column_nameContext ctx) {

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitAlterTableAddColumns(McParserParser.AlterTableAddColumnsContext ctx) {

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitPartition_spec(McParserParser.Partition_specContext ctx) {

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitPartition_spec_filter(McParserParser.Partition_spec_filterContext ctx) {

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitInsert_stmt_row(McParserParser.Insert_stmt_rowContext ctx) {

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitCte_select_stmt(McParserParser.Cte_select_stmtContext ctx) {

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitFullselect_stmt(McParserParser.Fullselect_stmtContext ctx) {

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSelect_list(McParserParser.Select_listContext ctx) {

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitFrom_table_name_clause(McParserParser.From_table_name_clauseContext ctx) {

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitFrom_subselect_clause(McParserParser.From_subselect_clauseContext ctx) {

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitNest_from_clause(McParserParser.Nest_from_clauseContext ctx) {

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitFrom_body_clause(McParserParser.From_body_clauseContext ctx) {

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitTable_name(McParserParser.Table_nameContext ctx) {

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitUpdate_assignment(McParserParser.Update_assignmentContext ctx) {

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitBool_expr(McParserParser.Bool_exprContext ctx) {

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitBool_expr_atom(McParserParser.Bool_expr_atomContext ctx) {

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitBool_expr_logical_operator(McParserParser.Bool_expr_logical_operatorContext ctx) {

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitBool_expr_binary_operator(McParserParser.Bool_expr_binary_operatorContext ctx) {

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitExpr(McParserParser.ExprContext ctx) {

        dmVisitChildren(ctx);
        return null;
    }

    @Override
    public Void visitSingle_quotedString(McParserParser.Single_quotedStringContext ctx) {
        String text = getText(ctx);
        builder.handleDomain(new RdbConstantDomain(text.substring(1, text.length() - 1)), DomainSource.CONSTANT);
        //        builder.addAttr(CommonAttribute.VALUE, text.substring(1, text.length() - 1));
        return null;
    }

    @Override
    public Void visitInt_number(McParserParser.Int_numberContext ctx) {
        builder.handleDomain(new RdbConstantDomain(getText(ctx)), DomainSource.CONSTANT);
        return null;
    }

    @Override
    public Void visitCreate_table_clustere(McParserParser.Create_table_clustereContext ctx) {
        return null;
    }

    @Override
    public Void visitDec_number(McParserParser.Dec_numberContext ctx) {
        builder.handleDomain(new RdbConstantDomain(getText(ctx)), DomainSource.CONSTANT);
        return null;
    }

    @Override
    public Void visitBool_literal(McParserParser.Bool_literalContext ctx) {
        builder.handleDomain(new RdbConstantDomain(getText(ctx)), DomainSource.CONSTANT);
        return null;
    }

    @Override
    public Void visitNull_const(McParserParser.Null_constContext ctx) {
        builder.handleDomain(new RdbConstantDomain("null"), DomainSource.CONSTANT);
        return null;
    }

    @Override
    public Void visitStore_as(McParserParser.Store_asContext ctx) {
        return null;
    }

    @Override
    public Void visitChildren(RuleNode node) {
        if (node instanceof ParserRuleContext) {
            throw new UnsupportedOperationException("unsupported SQL: " + this.getText((ParserRuleContext) node));
        }
        throw new UnsupportedOperationException("unsupported SQL: " + node.getText());
    }

    public void dmVisitChildren(RuleNode node) {
        int n = node.getChildCount();

        for (int i = 0; i < n; ++i) {
            ParseTree c = node.getChild(i);
            c.accept(this);
        }

    }
}
