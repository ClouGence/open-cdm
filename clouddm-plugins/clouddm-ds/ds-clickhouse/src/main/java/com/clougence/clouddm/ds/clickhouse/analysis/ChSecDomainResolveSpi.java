package com.clougence.clouddm.ds.clickhouse.analysis;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.clickhouse.analysis.builder.ChBuilderFactory;
import com.clougence.clouddm.sdk.analysis.secrules.SecDomainResolveSpi;
import com.clougence.clouddm.sdk.analysis.split.SplitScript;
import com.clougence.clouddm.sdk.model.analysis.CodeInfo;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.ds.clickhouse.parser.ChSqlDslProvider;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.dslpaser.parse.AstSplitScript;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

public class ChSecDomainResolveSpi implements SecDomainResolveSpi {

    private final MetaService metaService;

    public ChSecDomainResolveSpi(MetaService metaService){
        this.metaService = metaService;
    }

    protected DslProvider dslProvider() {
        return ChSqlDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<Void> parserVisitor(ChBuilderFactory domainBuilder, Parser parser) {
        return new ChSQLParserVisitor(domainBuilder, parser);
    }

    @Override
    public List<RuleDomain> resolveDomain(DataSourceType dsType, CodeInfo codeInfo, ContextInfo ctxInfo) {
        com.clougence.dslpaser.ast.location.CodeLocation dslBase = //
                new com.clougence.dslpaser.ast.location.CodeLocation(codeInfo.getBaseLine(), codeInfo.getBaseColumn());
        List<RuleDomain> domainList = new ArrayList<>();

        List<AstSplitScript> scripts = DslHelper.splitDsl(dslProvider(), codeInfo.getQuery(), dslBase);
        for (AstSplitScript s : scripts) {
            SplitScript ss = new SplitScript();
            ss.setScript(s.getScript());
            ss.setBodyStartCodeLine(s.getBodyStartCodeLine());
            ss.setBodyEndCodeLine(s.getEndCodeLine());
            ss.setBodyStartCodeColumn(s.getBodyStartCodeColumn());
            ss.setBodyEndCodeColumn(s.getEndCodeColumn());

            //
            ChBuilderFactory builder = new ChBuilderFactory(this.metaService);
            DslHelper.doVisitor(dslProvider(), s.getScript(), (lexer, parser) -> this.parserVisitor(builder, parser));
            List<RuleDomain> build;
            if (ctxInfo.isDeepParser()) {
                build = builder.build(ctxInfo.getCuid(), ctxInfo.getDsId(), ctxInfo.getLevelsParam());
            } else {
                build = builder.build();
            }
            for (RuleDomain domain : build) {
                domain.setDsType(dsType);
                domain.setSplitScript(ss);
                domainList.add(domain);
            }
        }

        return domainList;
    }
}
//public class ClickHouseParserUtils extends CommonParserUtils {
//
//    // =============================================================== Create Table
//
//    public static RuleDomain parseCreateTable(ClickhouseCreateTableStatement statement) {
//        CreateRuleDomain ruleDomain = new CreateRuleDomain();
//        ruleDomain.setRuleTypeEnum(RuleTypeEnum.Create);
//        ruleDomain.setTargetType(ObjectType.Table);
//        ruleDomain = parseCreateTableName(ruleDomain, statement);
//        ruleDomain = parseCreateTableOptions(ruleDomain, statement);
//        // create table (...)
//        List<SQLTableElement> elementList = statement.getTableElementList();
//        if (elementList != null && !elementList.isEmpty()) {
//            ruleDomain.setValue("mode", "create");
//            parseCreateElement(ruleDomain, statement.getTableElementList());
//        }
//        // create table xxx like xxx
//        SQLExprTableSource like = statement.getLike();
//        if (like != null) {
//            ruleDomain.setValue("mode", "like");
//            parseTableResources(ruleDomain.getResourceTable(), like);
//        }
//        // create table xxx as select xxx
//        SQLSelect select = statement.getSelect();
//        if (select != null) {
//            ruleDomain.setValue("mode", "select");
//            parseTableResources(ruleDomain.getResourceTable(), select.getQueryBlock());
//        }
//        return ruleDomain;
//    }
//
//    private static void parseCreateElement(CreateRuleDomain ruleDomain, List<SQLTableElement> columns) {
//        for (SQLTableElement element : columns) {
//            if (element instanceof SQLColumnDefinition) {
//                parseColumnDefinition(ruleDomain, (SQLColumnDefinition) element);
//            }
//        }
//    }
//
//    private static void parsePrimaryKey(CreateRuleDomain ruleDomain, MySqlPrimaryKey element) {
//        NameInfo pkName = getString(element.getName());
//        List<SQLSelectOrderByItem> pkColumns = element.getColumns();
//        for (SQLSelectOrderByItem item : pkColumns) {
//            SQLExpr columnName = item.getExpr();
//            String columnNameStr = columnName instanceof SQLCharExpr ? ((SQLCharExpr) columnName).getText() : ((SQLIdentifierExpr) columnName).getName();
//            boolean columnNameQualifier = columnNameStr.charAt(0) == '`';
//            if (columnNameQualifier) {
//                columnNameStr = columnNameStr.substring(1, columnNameStr.length() - 1);
//            }
//            TreeNode columnNode = ruleDomain.findNode("columns." + columnNameStr);
//            columnNode.setValue("primary", String.valueOf(true));
//        }
//        if (StringUtils.isNotBlank(pkName.nameString)) {
//            ruleDomain.addValue("constraint.primary.name", pkName.nameString);
//        }
//    }
//
//    private static void parseColumnDefinition(CreateRuleDomain ruleDomain, SQLColumnDefinition element) {
//        String columnName = element.getName().getSimpleName();
//        SQLDataType columnType = element.getDataType();
//        boolean columnNameQualifier = columnName.charAt(0) == '`';
//        if (columnNameQualifier) {
//            columnName = columnName.substring(1, columnName.length() - 1);
//        }
//        ruleDomain.addValue("table.columnNames", columnName);
//        //
//        TreeNode columnInfo = ruleDomain.findOrCreateNode("columns").getOrNewSubNode(columnName);
//        NameInfo commentString = getString(element.getComment());
//        int size = -1;
//        if (columnType.getArguments() != null && !columnType.getArguments().isEmpty()) {
//            List<SQLExpr> arguments = columnType.getArguments();
//            for (SQLExpr argument : arguments) {
//                columnInfo.addValue("typeArgs", getString(argument).nameString);
//            }
//            if (columnType instanceof SQLCharacterDataType) {
//                size = ((SQLCharacterDataType) columnType).getLength();// varchar length
//            } else {
//                // else type args
//            }
//        }
//        //
//        columnInfo.setValue("type", columnType.getName());
//        columnInfo.setValue("size", String.valueOf(size));
//        columnInfo.setValue("qualifier", String.valueOf(columnNameQualifier));
//        columnInfo.setValue("primary", String.valueOf(false));
//        columnInfo.setValue("comment", commentString.nameString);
//        columnInfo.setValue("nullable", String.valueOf(true));
//        List<SQLColumnConstraint> constraints = element.getConstraints();
//        for (SQLColumnConstraint constraint : constraints) {
//            if (constraint instanceof SQLColumnPrimaryKey) {
//                columnInfo.setValue("primary", String.valueOf(true));
//            } else if (constraint instanceof SQLNotNullConstraint) {
//                columnInfo.setValue("nullable", String.valueOf(true));
//            }
//        }
//        columnInfo.setValue("collate", getString(element.getCollateExpr()).nameString);
//    }
//
//    private static CreateRuleDomain parseCreateTableName(CreateRuleDomain ruleDomain, ClickhouseCreateTableStatement statement) {
//        SQLName name = statement.getName();
//        NameGroupInfo groupInfo = getGroupInfo(DataSourceType.ClickHouse, name);
//        TreeNode tableNode = ruleDomain.findOrCreateNode("table");
//        configNameGroupInfo(tableNode, groupInfo);
//        return ruleDomain;
//    }
//
//    private static CreateRuleDomain parseCreateTableOptions(CreateRuleDomain ruleDomain, ClickhouseCreateTableStatement statement) {
//        if (statement.getComment() != null) {
//            NameInfo commentStr = getString(statement.getComment());
//            ruleDomain.setValue("table.comment", commentStr.nameString);
//        }
//        List<SQLAssignItem> tableOptions = statement.getTableOptions();
//        if (tableOptions != null) {
//            for (SQLAssignItem option : tableOptions) {
//                NameInfo optionKey = getString(option.getTarget());
//                NameInfo optionValue = getString(option.getValue());
//                ruleDomain.setValue("table.option." + optionKey.nameString.toLowerCase(), optionValue.nameString);
//            }
//        }
//        return ruleDomain;
//    }
//    // =============================================================== Drop
//
//    // =============================================================== Utils
//
//    private static void parseTableResources(QueryDomain queryDomain, SQLObject queryBlock) {
//        if (queryBlock instanceof SQLDeleteStatement) {
//            SQLTableSource tableSourceForm = ((SQLDeleteStatement) queryBlock).getFrom();
//            SQLTableSource tableSource = ((SQLDeleteStatement) queryBlock).getTableSource();
//            recursiveTableSource(queryDomain, tableSourceForm);
//            recursiveTableSource(queryDomain, tableSource);
//            SQLExpr where = ((SQLDeleteStatement) queryBlock).getWhere();
//            if (where != null) {
//                where.accept(new SqlAstCollectTableSqlVisitor(DataSourceType.ClickHouse, queryDomain));
//            }
//        } else if (queryBlock instanceof SQLTableSource) {
//            recursiveTableSource(queryDomain, (SQLTableSource) queryBlock);
//        } else {
//            queryBlock.accept(new SqlAstCollectTableSqlVisitor(DataSourceType.ClickHouse, queryDomain));
//        }
//    }
//
//    private static void recursiveTableSource(QueryDomain queryDomain, SQLTableSource queryBlock) {
//        if (queryBlock instanceof SQLJoinTableSource) {
//            recursiveTableSource(queryDomain, ((SQLJoinTableSource) queryBlock).getLeft());
//            recursiveTableSource(queryDomain, ((SQLJoinTableSource) queryBlock).getRight());
//            return;
//        }
//        if (queryBlock instanceof SQLExprTableSource) {
//            SQLName tableName = ((SQLExprTableSource) queryBlock).getName();
//            SqlAstCollectTableSqlVisitor.printTable(DataSourceType.ClickHouse, queryDomain, tableName);
//        }
//    }
//}
