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

import java.util.ArrayList;
import java.util.List;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.*;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.*;
import com.clougence.clouddm.dsfamily.analysis.secrules.ref.MetaCollectTables;
import com.clougence.clouddm.dsfamily.analysis.secrules.ref.MetaTable;
import com.clougence.clouddm.dsfamily.db2.analysis.secrules.Db2SelectDomain;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.utils.StringUtils;

public class Db2QueryResolveHelper extends Db2AbstractResolveHelper {

    protected Db2CollectTableHelper helper;

    public Db2QueryResolveHelper(){
        this.helper = new Db2CollectTableHelper();
    }

    public List<RuleDomain> fromSelect(SQLSelect select) {
        List<RuleDomain> result = new ArrayList<>();
        fromSelect(result, new MetaCollectTables(), select, RdbQueryMode.NORMAL);
        return result;
    }

    protected void fromSelect(List<RuleDomain> result, MetaCollectTables ctxTabs, SQLSelect select, RdbQueryMode mode) {
        if (select == null) {
            return;
        }

        Db2SelectDomain domain = newQueryDomain(mode);
        result.add(domain);

        ctxTabs.createFrame();
        this.helper.collectTables(ctxTabs, select.getQuery(), select.getWithSubQuery());

        fromSelectTestSimpleQuery(domain, select.getQuery());
        fromSelectMainQuery(result, ctxTabs, domain, select.getQuery());
        fromWithQuery(result, ctxTabs, domain, select.getWithSubQuery());

        ctxTabs.dropFrame();
    }

    protected void fromSelectForUnion(List<RuleDomain> result, MetaCollectTables ctxTabs, SQLSelectQuery select) {
        if (select == null) {
            return;
        }

        Db2SelectDomain domain = newQueryDomain(RdbQueryMode.UNION);
        result.add(domain);

        fromSelectTestSimpleQuery(domain, select);
        fromSelectMainQuery(result, ctxTabs, domain, select);
    }

    protected void fromSelectForSource(List<RuleDomain> result, MetaCollectTables ctxTabs, SQLTableSource select, RdbQueryMode mode, SQLExpr whereExpr) {
        if (select == null) {
            return;
        }

        if (select instanceof SQLJoinTableSource) {
            fromSelectForSource(result, ctxTabs, ((SQLJoinTableSource) select).getLeft(), RdbQueryMode.JOIN, whereExpr);
            fromSelectForSource(result, ctxTabs, ((SQLJoinTableSource) select).getRight(), RdbQueryMode.JOIN, whereExpr);
        } else if (select instanceof SQLSubqueryTableSource) {
            String alias = select.getAlias();
            SQLSelect subSelect = ((SQLSubqueryTableSource) select).getSelect();
            fromSelectForSourceIsSub(result, ctxTabs, subSelect, alias);
        } else {
            if (select instanceof SQLExprTableSource) {
                String alias = select.getAlias();
                SQLExpr expr = ((SQLExprTableSource) select).getExpr();
                if (expr instanceof SQLQueryExpr) {
                    SQLSelect subSelect = ((SQLQueryExpr) expr).getSubQuery();
                    fromSelectForSourceIsSub(result, ctxTabs, subSelect, alias);
                    return;
                }
            }

            Db2SelectDomain domain = newQueryDomain(mode);
            fromSelectFrom(result, ctxTabs, domain, select, whereExpr);
            fromWhere(result, ctxTabs, domain, whereExpr);
            result.add(domain);
        }
    }

    protected void fromSelectForSourceIsSub(List<RuleDomain> result, MetaCollectTables ctxTabs, SQLSelect select, String alias) {
        Db2SelectDomain domain = newQueryDomain(RdbQueryMode.SUB_FROM);
        domain.setTable(alias);
        domain.setVirtual(true);
        result.add(domain);

        ctxTabs.createFrame();
        fromSelect(result, ctxTabs, select, RdbQueryMode.SUB_FROM);
        ctxTabs.dropFrame();
    }

    protected void fromSelectTestSimpleQuery(Db2SelectDomain domain, SQLSelectQuery query) {
        domain.setSimpleSelect(false);

        if (!(query instanceof SQLSelectQueryBlock)) {
            return;
        }

        SQLTableSource from = ((SQLSelectQueryBlock) query).getFrom();
        if (!(from == null || from instanceof SQLExprTableSource)) {
            return;
        }

        for (SQLSelectItem selectItem : ((SQLSelectQueryBlock) query).getSelectList()) {
            SQLExpr alias = selectItem.getExpr();
            if (alias instanceof SQLIdentifierExpr || alias instanceof SQLIntegerExpr || alias instanceof SQLCharExpr || alias instanceof SQLVariantRefExpr
                || alias instanceof SQLAllColumnExpr) {
                //
            } else if (alias instanceof SQLMethodInvokeExpr) {
                String methodName = ((SQLMethodInvokeExpr) alias).getMethodName();
                if (!SimpleQuery_WlMethods.contains(methodName.toUpperCase())) {
                    return;
                }
            } else if (alias instanceof SQLBinaryOpExpr) {
                SQLExpr left = ((SQLBinaryOpExpr) alias).getLeft();
                SQLExpr right = ((SQLBinaryOpExpr) alias).getRight();
                if (left instanceof SQLIntegerExpr && right instanceof SQLIntegerExpr) {
                    //
                } else if (left instanceof SQLCharExpr && right instanceof SQLCharExpr) {
                    //
                } else {
                    return;
                }
            } else {
                return;
            }
        }

        domain.setSimpleSelect(true);
    }

    protected void fromSelectMainQuery(List<RuleDomain> result, MetaCollectTables ctxTabs, Db2SelectDomain domain, SQLSelectQuery query) {
        if (query instanceof SQLUnionQuery) {
            markHasUnion(domain);
            for (SQLSelectQuery item : ((SQLUnionQuery) query).getRelations()) {
                fromSelectForUnion(result, ctxTabs, item);
            }
        } else if (query instanceof SQLSelectQueryBlock) {
            domain.setHasLimit(((SQLSelectQueryBlock) query).getLimit() != null);
            SQLExpr whereExpr = ((SQLSelectQueryBlock) query).getWhere();

            // select
            fromSelectSelect(result, ctxTabs, domain, ((SQLSelectQueryBlock) query).getSelectList());
            // from
            fromSelectFrom(result, ctxTabs, domain, ((SQLSelectQueryBlock) query).getFrom(), whereExpr);
            // where
            fromWhere(result, ctxTabs, domain, whereExpr);
        }
    }

    protected void fromSelectSelect(List<RuleDomain> result, MetaCollectTables ctxTabs, Db2SelectDomain domain, List<SQLSelectItem> items) {
        for (SQLSelectItem selectItem : items) {
            String selectName = selectItem.getAlias();
            SQLExpr selectExpr = selectItem.getExpr();
            if (selectExpr instanceof SQLAllColumnExpr) {
                domain.setHasSelectAll(true);
            } else if (selectExpr instanceof SQLMethodInvokeExpr) {
                fromSelectSelectArgs(result, ctxTabs, domain, selectExpr);

                // some function can use 'as xxx'
                boolean skipAs = Select_As_WlMethods.contains(((SQLMethodInvokeExpr) selectExpr).getMethodName().toUpperCase());
                if (!skipAs && StringUtils.isNotBlank(selectItem.getAlias2())) {
                    markHasAs(domain);
                }
            } else if (selectExpr instanceof SQLTextLiteralExpr || selectExpr instanceof SQLNumericLiteralExpr) {
                if (StringUtils.isBlank(selectName)) {
                    domain.addSelect(selectExpr.toString(), RdbQuerySelectType.Value);
                } else {
                    domain.addSelect(getColumnNameRemoveQualifier(selectName), RdbQuerySelectType.Value);
                }
            } else if (selectExpr instanceof SQLIdentifierExpr) {
                if (StringUtils.isNotBlank(selectName) && !StringUtils.equals(selectName, ((SQLIdentifierExpr) selectExpr).getName())) {
                    markHasAs(domain);
                }
                fromSelectSelectArgs(result, ctxTabs, domain, selectExpr);
            } else {
                fromSelectSelectArgs(result, ctxTabs, domain, selectExpr);
            }
        }
    }

    protected void fromSelectSelectArgs(List<RuleDomain> result, MetaCollectTables ctxTabs, Db2SelectDomain domain, SQLExpr sqlExpr) {
        if (sqlExpr instanceof SQLBinaryOpExpr) {
            SQLExpr left = ((SQLBinaryOpExpr) sqlExpr).getLeft();
            SQLExpr right = ((SQLBinaryOpExpr) sqlExpr).getRight();

            fromSelectSelectArgs(result, ctxTabs, domain, left);
            fromSelectSelectArgs(result, ctxTabs, domain, right);
        } else if (sqlExpr instanceof SQLQueryExpr) {
            markSelectInSelect(domain);
            fromSelect(result, ctxTabs, ((SQLQueryExpr) sqlExpr).getSubQuery(), RdbQueryMode.SUB_SELECT);
            //domain.addSelect(callDomain.getCallName(), RdbQuerySelectType.SubQuery);
        } else if (sqlExpr instanceof SQLAggregateExpr) {
            markFuncInSelect(domain);
            RdbCallDomain callDomain = toCallDomain((SQLAggregateExpr) sqlExpr);
            callDomain.setFunc(true);
            result.add(callDomain);
            for (SQLExpr expr : ((SQLMethodInvokeExpr) sqlExpr).getArguments()) {
                callDomain.addArgStr(expr.toString());
                fromSelectSelectArgs(result, ctxTabs, domain, expr);
            }
            domain.addSelect(callDomain.getCallName(), RdbQuerySelectType.Function);
        } else if (sqlExpr instanceof SQLMethodInvokeExpr) {
            markFuncInSelect(domain);
            RdbCallDomain callDomain = toCallDomain((SQLMethodInvokeExpr) sqlExpr);
            callDomain.setFunc(true);
            result.add(callDomain);
            for (SQLExpr expr : ((SQLMethodInvokeExpr) sqlExpr).getArguments()) {
                callDomain.addArgStr(expr.toString());
                fromSelectSelectArgs(result, ctxTabs, domain, expr);
            }
            domain.addSelect(callDomain.getCallName(), RdbQuerySelectType.Function);
        } else if (sqlExpr instanceof SQLCastExpr) {
            markFuncInSelect(domain);
            RdbCallDomain callDomain = toCallDomain("CAST", null);
            callDomain.setFunc(true);
            callDomain.setEmptyArg(false);
            result.add(callDomain);
            {
                SQLExpr expr = ((SQLCastExpr) sqlExpr).getExpr();
                callDomain.addArgStr(expr.toString());
                fromSelectSelectArgs(result, ctxTabs, domain, expr);
            }
            domain.addSelect(callDomain.getCallName(), RdbQuerySelectType.Function);
        } else if (sqlExpr instanceof SQLIdentifierExpr) {
            domain.addSelect(getString(sqlExpr), RdbQuerySelectType.Column);
        } else if (sqlExpr instanceof SQLVariantRefExpr) {
            domain.addSelect(getString(sqlExpr), RdbQuerySelectType.Variable);
        } else if (sqlExpr instanceof SQLTextLiteralExpr || sqlExpr instanceof SQLNumericLiteralExpr) {
            //domain.addSelect(getString(sqlExpr), RdbQuerySelectType.Value);
        } else if (sqlExpr instanceof SQLPropertyExpr) {
            domain.addSelect(getColumnNameRemoveQualifier(((SQLPropertyExpr) sqlExpr).getName()), RdbQuerySelectType.Column);
        } else if (sqlExpr instanceof SQLAllColumnExpr) {
            domain.setHasSelectAll(true);
        } else {
            throw new UnsupportedOperationException("unsupported SQL: " + sqlExpr.toString());
        }
    }

    protected void fromSelectFrom(List<RuleDomain> result, MetaCollectTables ctxTabs, Db2SelectDomain domain, SQLTableSource source, SQLExpr whereExpr) {
        if (source == null) {
            domain.setEmptyFrom(true);
            markVirtual(domain);
            return;
        } else {
            domain.setEmptyFrom(false);
        }

        if (source instanceof SQLExprTableSource) {
            configTargetTableName(ctxTabs, domain, source);
        } else if (source instanceof SQLJoinTableSource) {
            markJoin(domain, findJoinType(((SQLJoinTableSource) source)));
            markVirtual(domain);
            SQLExpr conditionExpr = ((SQLJoinTableSource) source).getCondition();

            fromSelectForSource(result, ctxTabs, ((SQLJoinTableSource) source).getLeft(), RdbQueryMode.JOIN, whereExpr);
            fromSelectForSource(result, ctxTabs, ((SQLJoinTableSource) source).getRight(), RdbQueryMode.JOIN, whereExpr);
        } else if (source instanceof SQLSubqueryTableSource) {
            markSelectInFrom(domain);
            markVirtual(domain);
            domain.setTable(source.getAlias());
            fromSelect(result, ctxTabs, ((SQLSubqueryTableSource) source).getSelect(), RdbQueryMode.SUB_FROM);
        } else if (source instanceof SQLUnionQueryTableSource) {
            // sub from
            Db2SelectDomain subFormDomain = newQueryDomain(RdbQueryMode.SUB_FROM);
            markHasUnion(subFormDomain);
            subFormDomain.setVirtual(true);
            subFormDomain.setTable(source.getAlias());
            result.add(subFormDomain);
            markSelectInFrom(domain);
            markVirtual(domain);
            domain.setTable(source.getAlias());
            SQLUnionQuery union = ((SQLUnionQueryTableSource) source).getUnion();
            for (SQLSelectQuery item : union.getRelations()) {
                fromSelectForUnion(result, ctxTabs, item);
            }
        } else {
            throw new UnsupportedOperationException("unsupported SQL: " + source.toString());
        }
    }

    //
    //
    //

    protected void fromWithQuery(List<RuleDomain> result, MetaCollectTables ctxTabs, RuleDomain domain, SQLWithSubqueryClause withQuery) {
        if (withQuery == null) {
            return;
        }

        markHasWith(domain);
        for (SQLWithSubqueryClause.Entry entry : withQuery.getEntries()) {
            String alias = entry.getAlias();

            Db2SelectDomain withDomain = newQueryDomain(RdbQueryMode.WITH);
            withDomain.setTable(alias);
            withDomain.setVirtual(true);
            result.add(withDomain);

            fromSelect(result, ctxTabs, entry.getSubQuery(), RdbQueryMode.WITH_BODY);
        }
    }

    protected void fromWhere(List<RuleDomain> result, MetaCollectTables ctxTabs, RdbWhereDomain domain, SQLExpr whereExpr) {
        if (whereExpr == null) {
            return;
        }

        if (whereExpr instanceof SQLInSubQueryExpr) {
            markSelectInWhere(domain);

            SQLExpr expr = ((SQLInSubQueryExpr) whereExpr).getExpr();
            fromWhereExpr(result, ctxTabs, domain, expr);
            fromSelect(result, ctxTabs, ((SQLInSubQueryExpr) whereExpr).getSubQuery(), RdbQueryMode.SUB_WHERE);
        } else if (whereExpr instanceof SQLBinaryOpExpr) {
            SQLExpr left = ((SQLBinaryOpExpr) whereExpr).getLeft();
            SQLExpr right = ((SQLBinaryOpExpr) whereExpr).getRight();
            SQLBinaryOperator operator = ((SQLBinaryOpExpr) whereExpr).getOperator();
            if (StringUtils.equals("=", operator.getName()) && StringUtils.equals(left.toString(), right.toString())) {
                return;
            }

            domain.setHasWhere(true);
            fromWhereExpr(result, ctxTabs, domain, whereExpr);
        } else {
            fromWhereExpr(result, ctxTabs, domain, whereExpr);
        }
    }

    protected void fromWhereExpr(List<RuleDomain> result, MetaCollectTables ctxTabs, RdbWhereDomain domain, SQLExpr sqlExpr) {
        if (sqlExpr instanceof SQLBinaryOpExpr) {
            fromWhereExprItem(result, ctxTabs, domain, ((SQLBinaryOpExpr) sqlExpr).getLeft());
            fromWhereExprItem(result, ctxTabs, domain, ((SQLBinaryOpExpr) sqlExpr).getRight());
        } else if (sqlExpr instanceof SQLInSubQueryExpr) {
            markSelectInWhere(domain);
            fromSelect(result, ctxTabs, ((SQLInSubQueryExpr) sqlExpr).getSubQuery(), RdbQueryMode.SUB_WHERE);
        } else if (sqlExpr instanceof SQLInListExpr) {
            for (SQLExpr inItem : ((SQLInListExpr) sqlExpr).getTargetList()) {
                fromWhereExprItem(result, ctxTabs, domain, inItem);
            }
            SQLExpr colExpr = ((SQLInListExpr) sqlExpr).getExpr();
            if (colExpr instanceof SQLIdentifierExpr) {
                domain.addWhereColumn(getColumnNameRemoveQualifier(((SQLIdentifierExpr) colExpr).getName()));
            } else if (colExpr instanceof SQLPropertyExpr) {
                domain.addWhereColumn(getColumnNameRemoveQualifier(((SQLPropertyExpr) colExpr).getName()));
            } else {
                throw new UnsupportedOperationException("unsupported SQL: " + colExpr.toString());
            }
        } else if (sqlExpr instanceof SQLQueryExpr) {
            markSelectInWhere(domain);
            fromSelect(result, ctxTabs, ((SQLQueryExpr) sqlExpr).getSubQuery(), RdbQueryMode.SUB_WHERE);
        } else if (sqlExpr instanceof SQLAggregateExpr) {
            RdbCallDomain callDomain = toCallDomain((SQLAggregateExpr) sqlExpr);
            callDomain.setFunc(true);
            result.add(callDomain);
            for (SQLExpr expr : ((SQLMethodInvokeExpr) sqlExpr).getArguments()) {
                callDomain.addArgStr(expr.toString());
                fromWhereExpr(result, ctxTabs, domain, expr);
            }
        } else if (sqlExpr instanceof SQLMethodInvokeExpr) {
            RdbCallDomain callDomain = toCallDomain((SQLMethodInvokeExpr) sqlExpr);
            callDomain.setFunc(true);
            result.add(callDomain);
            for (SQLExpr expr : ((SQLMethodInvokeExpr) sqlExpr).getArguments()) {
                callDomain.addArgStr(expr.toString());
                fromWhereExpr(result, ctxTabs, domain, expr);
            }
        } else if (sqlExpr instanceof SQLCastExpr) {
            RdbCallDomain callDomain = toCallDomain("CAST", null);
            callDomain.setFunc(true);
            callDomain.setEmptyArg(false);
            result.add(callDomain);
            {
                SQLExpr expr = ((SQLCastExpr) sqlExpr).getExpr();
                callDomain.addArgStr(expr.toString());

                boolean atom = expr instanceof SQLIdentifierExpr || expr instanceof SQLTextLiteralExpr || expr instanceof SQLNumericLiteralExpr;
                if (!atom) {
                    fromWhereExpr(result, ctxTabs, domain, expr);
                }
            }
        } else if (sqlExpr instanceof SQLIdentifierExpr) {
            domain.addWhereColumn(getString(sqlExpr));
        } else if (sqlExpr instanceof SQLPropertyExpr) {
            domain.addWhereColumn(getColumnNameRemoveQualifier(((SQLPropertyExpr) sqlExpr).getName()));
        } else {
            throw new UnsupportedOperationException("unsupported SQL: " + sqlExpr.toString());
        }
    }

    protected void fromWhereExprItem(List<RuleDomain> result, MetaCollectTables ctxTabs, RdbWhereDomain domain, SQLExpr itemExpr) {
        boolean hasColName = itemExpr instanceof SQLIdentifierExpr ||   //
                             itemExpr instanceof SQLPropertyExpr ||     //
                             itemExpr instanceof SQLInSubQueryExpr || //
                             itemExpr instanceof SQLInListExpr;
        boolean isBasic = itemExpr instanceof SQLTextLiteralExpr ||     //
                          itemExpr instanceof SQLNullExpr ||            //
                          itemExpr instanceof SQLNumericLiteralExpr;
        boolean needRecursion = itemExpr instanceof SQLBinaryOpExpr ||  //
                                itemExpr instanceof SQLCastExpr ||      //
                                itemExpr instanceof SQLMethodInvokeExpr || //
                                itemExpr instanceof SQLQueryExpr ||     //
                                itemExpr instanceof SQLInSubQueryExpr;

        if (hasColName) {
            SQLExpr columnName = itemExpr;
            if (itemExpr instanceof SQLInSubQueryExpr) {
                columnName = ((SQLInSubQueryExpr) itemExpr).getExpr();
            } else if (itemExpr instanceof SQLInListExpr) {
                columnName = ((SQLInListExpr) itemExpr).getExpr();
            }

            if (testBelong(domain, findOwnerTable(ctxTabs, columnName))) {
                if (columnName instanceof SQLIdentifierExpr) {
                    domain.addWhereColumn(getString(columnName));
                } else if (columnName instanceof SQLPropertyExpr) {
                    domain.addWhereColumn(getColumnNameRemoveQualifier(((SQLPropertyExpr) columnName).getName()));
                }
            }
        }

        if (needRecursion) {
            fromWhereExpr(result, ctxTabs, domain, itemExpr);
        }

        if (hasColName || isBasic || needRecursion) {
            return;
        }

        throw new UnsupportedOperationException("unsupported SQL: " + itemExpr.toString());
    }

    private static boolean testBelong(RuleDomain domain, MetaTable colOwner) {
        if (colOwner == null || colOwner.getTable() == null) {
            return true;// todo inaccuracy
        }

        String domainCatalog = null;
        String domainSchema = null;
        String domainTable = null;
        if (domain instanceof RdbCallDomain) {
            domainCatalog = ((RdbCallDomain) domain).getCatalog();
            domainSchema = ((RdbCallDomain) domain).getSchema();
            domainTable = ((RdbCallDomain) domain).getCallName();
        } else if (domain instanceof RdbDeleteDomain) {
            domainCatalog = ((RdbDeleteDomain) domain).getCatalog();
            domainSchema = ((RdbDeleteDomain) domain).getSchema();
            domainTable = ((RdbDeleteDomain) domain).getTable();
        } else if (domain instanceof RdbInsertDomain) {
            domainCatalog = ((RdbInsertDomain) domain).getCatalog();
            domainSchema = ((RdbInsertDomain) domain).getSchema();
            domainTable = ((RdbInsertDomain) domain).getTable();
        } else if (domain instanceof RdbSelectDomain) {
            domainCatalog = ((RdbSelectDomain) domain).getCatalog();
            domainSchema = ((RdbSelectDomain) domain).getSchema();
            domainTable = ((RdbSelectDomain) domain).getTable();
        } else if (domain instanceof RdbUpdateDomain) {
            domainCatalog = ((RdbUpdateDomain) domain).getCatalog();
            domainSchema = ((RdbUpdateDomain) domain).getSchema();
            domainTable = ((RdbUpdateDomain) domain).getTable();
        } else {
            throw new UnsupportedOperationException("unsupported testBelong of " + domain.getClass());
        }

        if (domainTable == null) {
            return true; // If you are not sure table name then included.
        }

        String colCatalog = colOwner.getCatalog();
        String colSchema = colOwner.getSchema();
        String colTable = colOwner.getTable();

        if (StringUtils.isNotBlank(domainCatalog) && StringUtils.isNotBlank(colCatalog)) {
            if (!colCatalog.equals(domainCatalog)) {
                return false;
            }
        }
        if (StringUtils.isNotBlank(domainSchema) && StringUtils.isNotBlank(colSchema)) {
            if (!colSchema.equals(domainSchema)) {
                return false;
            }
        }

        return colTable.equals(domainTable);
    }

    //
    //
    //

    protected void markHasWith(RuleDomain domain) {
        if (domain instanceof Db2SelectDomain) {
            ((Db2SelectDomain) domain).setHasWith(true);
            ((Db2SelectDomain) domain).setSimpleSelect(false);
        } else if (domain instanceof RdbWhereDomain) {
            ((RdbWhereDomain) domain).setHasWith(true);
        }
    }

    protected void markHasAs(RuleDomain domain) {
        if (domain instanceof Db2SelectDomain) {
            ((Db2SelectDomain) domain).setSimpleSelect(false);
            ((Db2SelectDomain) domain).setHasAs(true);
        }
    }

    protected void markHasUnion(RuleDomain domain) {
        if (domain instanceof Db2SelectDomain) {
            ((Db2SelectDomain) domain).setHasUnion(true);
            ((Db2SelectDomain) domain).setSimpleSelect(false);
        } else if (domain instanceof RdbWhereDomain) {
            ((RdbWhereDomain) domain).setHasUnion(true);
        }
    }

    protected void markSelectInWhere(RuleDomain domain) {
        if (domain instanceof RdbWhereDomain) {
            ((RdbWhereDomain) domain).setSelectInWhere(true);
        }
    }

    protected void markVirtual(RuleDomain domain) {
        if (domain instanceof RdbSelectDomain) {
            ((RdbSelectDomain) domain).setVirtual(true);
        }
    }

    protected void markSelectInSelect(RuleDomain domain) {
        if (domain instanceof RdbSelectDomain) {
            ((RdbSelectDomain) domain).setSelectInSelect(true);
        }
    }

    protected void markFuncInSelect(RuleDomain domain) {
        if (domain instanceof RdbSelectDomain) {
            ((RdbSelectDomain) domain).setFuncInSelect(true);
        }
    }

    protected void markSelectInFrom(RuleDomain domain) {
        if (domain instanceof RdbSelectDomain) {
            ((RdbSelectDomain) domain).setSelectInFrom(true);
        }
    }

    protected void markJoin(RuleDomain domain, SQLJoinTableSource.JoinType joinType) {
        if (!(domain instanceof RdbWhereDomain)) {
            return;
        }
        switch (joinType) {
            case JOIN:
            case INNER_JOIN:
            case NATURAL_JOIN:
            case NATURAL_INNER_JOIN:
                ((RdbWhereDomain) domain).setJoinType(RdbJoinType.INNER_JOIN);
                switch (joinType) {
                    case NATURAL_JOIN:
                    case NATURAL_INNER_JOIN:
                        domain.getOptions().put(OPT_JOIN_NATURAL, "true");
                        break;
                    default:
                        break;
                }
                break;
            case COMMA:
            case CROSS_JOIN:
            case NATURAL_CROSS_JOIN:
                ((RdbWhereDomain) domain).setJoinType(RdbJoinType.CROSS_JOIN);
                if (joinType == SQLJoinTableSource.JoinType.NATURAL_CROSS_JOIN) {
                    domain.getOptions().put(OPT_JOIN_NATURAL, "true");
                }
                break;
            case LEFT_OUTER_JOIN:
            case NATURAL_LEFT_JOIN:
                ((RdbWhereDomain) domain).setJoinType(RdbJoinType.LEFT_JOIN);
                if (joinType == SQLJoinTableSource.JoinType.NATURAL_LEFT_JOIN) {
                    domain.getOptions().put(OPT_JOIN_NATURAL, "true");
                }
                break;
            case RIGHT_OUTER_JOIN:
            case NATURAL_RIGHT_JOIN:
                ((RdbWhereDomain) domain).setJoinType(RdbJoinType.RIGHT_JOIN);
                if (joinType == SQLJoinTableSource.JoinType.NATURAL_RIGHT_JOIN) {
                    domain.getOptions().put(OPT_JOIN_NATURAL, "true");
                }
                break;
            default:
                // LEFT_SEMI_JOIN("LEFT SEMI JOIN"),
                // LEFT_ANTI_JOIN("LEFT ANTI JOIN"),
                // FULL_OUTER_JOIN("FULL JOIN"),
                // OUTER_APPLY("OUTER APPLY"),
                // CROSS_APPLY("CROSS APPLY");
                ((RdbWhereDomain) domain).setJoinType(RdbJoinType.OTHER_JOIN);
                break;
        }
    }
}
