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
package com.clougence.clouddm.dsfamily.mysql.analysis;

import java.util.List;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;

import com.clougence.clouddm.dsfamily.analysis.column.AbstractSelectColumnAnalysisSpi;
import com.clougence.clouddm.dsfamily.analysis.secrules.column.QueryItem;
import com.clougence.clouddm.dsfamily.mysql.analysis.builder.MyBuilderFactory;
import com.clougence.clouddm.sdk.analysis.column.RealColumn;
import com.clougence.clouddm.sdk.analysis.column.SelectItem;
import com.clougence.clouddm.sdk.model.analysis.ContextInfo;
import com.clougence.clouddm.sdk.service.execute.MetaService;
import com.clougence.clouddm.dsfamily.mysql.parser.MyDslProvider;
import com.clougence.dslpaser.antlr.DslHelper;
import com.clougence.dslpaser.antlr.DslProvider;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

public class MySelectColumnAnalysisSpi extends AbstractSelectColumnAnalysisSpi {

    public MySelectColumnAnalysisSpi(MetaService metaService){
        super(metaService);
    }

    protected DslProvider dslProvider() {
        return MyDslProvider.INSTANCE;
    }

    protected AbstractParseTreeVisitor<Void> parserVisitor(MyBuilderFactory domainBuilder, Parser parser) {
        return new MySQLParserVisitor(domainBuilder, parser);
    }

    @Override
    protected boolean needAlias(QueryItem queryItem) {
        return false;
    }

    @Override
    public List<SelectItem> parseSelectColumn(String script, ContextInfo contextInfo) {
        MyBuilderFactory builder = new MyBuilderFactory(this.metaService);
        DslHelper.doVisitor(dslProvider(), script, (lexer, parser) -> this.parserVisitor(builder, parser));

        List<SelectItem> selectItems = analyzeRealColumn(contextInfo.getCuid(), contextInfo.getDsId(), contextInfo.getLevelsParam(), builder.buildKeepOrigin());
        for (SelectItem selectItem : selectItems) {
            for (RealColumn column : selectItem.getColumns()) {
                if (StringUtils.isEmpty(column.getSchema())) {
                    column.setSchema(contextInfo.getLevelsParam().get(UmiTypes.Schema).toString());
                }
            }
        }
        return selectItems;
    }

    @Override
    public boolean supportParseSelectColumn() {
        return true;
    }
    //
    //    private List<SelectItem> analyzeRealColumn(String uid, long dsID, Map<UmiTypes, Object> levelsParam, RdbSelectDomain selectDomain) {
    //        if (CollectionUtils.isNotEmpty(selectDomain.getChildren())) {
    //            List<SelectItem> selectItems = new ArrayList<>();
    //
    //            if (selectDomain.isHasUnion()) {
    //                return parseUnion(selectDomain, uid, dsID, levelsParam);
    //            }
    //
    //            for (RuleDomain child : selectDomain.getChildren()) {
    //                selectItems.addAll(analyzeRealColumn(uid, dsID, levelsParam, (RdbSelectDomain) child));
    //            }
    //
    //            if (selectDomain.isSelectInFrom()) {
    //                for (SelectItem selectItem : selectItems) {
    //                    selectItem.setTableAlias(selectDomain.getTable());
    //                }
    //            }
    //            // <columnName,<tableName,SelectColumn>>
    //            Map<String, Map<String, SelectItem>> columnMap = new LinkedHashMap<>();
    //
    //            for (SelectItem selectItem : selectItems) {
    //                columnMap.computeIfAbsent(selectItem.getItemAlias(), k -> new LinkedHashMap<>()).put(selectItem.getTableAlias(), selectItem);
    //            }
    //
    //            if (CollectionUtils.isNotEmpty(selectDomain.getColumns())) {
    //                if (selectDomain.getMode() == RdbQueryMode.WITH) {
    //                    if (selectDomain.getColumns().size() != selectItems.size()) {
    //                        throw new RuntimeException();
    //                    } else {
    //                        for (int i = 0; i < selectItems.size(); i++) {
    //                            SelectItem selectItem = selectItems.get(i);
    //                            QueryItem queryItem = selectDomain.getColumns().get(i);
    //                            selectItem.setItemAlias(queryItem.getColumn());
    //                            selectItem.setTableAlias(queryItem.getTable());
    //                        }
    //                    }
    //                    return selectItems;
    //                } else {
    //                    return parseColumns(uid, dsID, levelsParam, selectDomain, selectItems, columnMap);
    //                }
    //
    //            } else {
    //                for (SelectItem selectItem : selectItems) {
    //                    selectItem.setTableAlias(selectDomain.getTable());
    //                }
    //                return selectItems;
    //            }
    //        } else {
    //            return realTableHandle(uid, dsID, levelsParam, selectDomain);
    //        }
    //    }
    //
    //    private List<SelectItem> parseColumns(String uid, long dsID, Map<UmiTypes, Object> levelsParam, RdbSelectDomain selectDomain, List<SelectItem> selectItems,
    //                                          Map<String, Map<String, SelectItem>> columnMap) {
    //        List<SelectItem> result = new ArrayList<>();
    //        for (QueryItem queryItem : selectDomain.getColumns()) {
    //            if (queryItem.isSelectAll()) {
    //                if (StringUtils.isEmpty(queryItem.getTable())) {
    //                    result.addAll(selectItems);
    //                } else {
    //                    for (SelectItem column : selectItems) {
    //                        if (column.getTableAlias().equals(queryItem.getTable())) {
    //                            result.add(column);
    //                        }
    //                    }
    //                }
    //                continue;
    //            }
    //            for (RuleDomain selectColumn : queryItem.getColumns()) {
    //                if (selectColumn instanceof RdbColumnDomain) {
    //                    RdbColumnDomain rdbColumnDomain = (RdbColumnDomain) selectColumn;
    //
    //                    SelectItem column = findColumn(columnMap, rdbColumnDomain);
    //
    //                    if (StringUtils.isNotEmpty(queryItem.getItemAlias())) {
    //                        column.setItemAlias(queryItem.getItemAlias());
    //                    }
    //
    //                    result.add(column);
    //
    //                } else if (selectColumn instanceof RdbCallDomain) {
    //                    SelectItem column = parseCallDomain(uid, dsID, levelsParam, (RdbCallDomain) selectColumn, queryItem, selectDomain, columnMap);
    //                    result.add(column);
    //                } else if (selectColumn instanceof RdbSelectDomain) {
    //                    List<SelectItem> columnList = analyzeRealColumn(uid, dsID, levelsParam, (RdbSelectDomain) selectColumn);
    //                    if (columnList.size() > 1) {
    //                        throw new RuntimeException("The query statement in the selection column should only return one column");
    //                    }
    //                    SelectItem column = columnList.get(0);
    //                    column.setItemAlias(((RdbSelectDomain) selectColumn).getTableAlias());
    //                    result.add(column);
    //                }
    //            }
    //
    //        }
    //        return result;
    //    }
    //
    //    private static SelectItem findColumn(Map<String, Map<String, SelectItem>> columnMap, RdbColumnDomain rdbColumnDomain) {
    //        Map<String, SelectItem> tableMap = columnMap.get(rdbColumnDomain.getColumn());
    //        if (tableMap == null) {
    //            throw new RuntimeException("Can't find such table: " + rdbColumnDomain.getTable());
    //        }
    //        SelectItem column;
    //        if (StringUtils.isEmpty(rdbColumnDomain.getTable())) {
    //            if (tableMap.size() != 1) {
    //                throw new RuntimeException("Column '" + rdbColumnDomain.getColumn() + "' in field list is ambiguous");
    //            }
    //            column = tableMap.values().iterator().next();
    //        } else {
    //            column = tableMap.get(rdbColumnDomain.getTable());
    //        }
    //        if (column == null) {
    //            throw new RuntimeException("Can't find such column '" + rdbColumnDomain.getColumn() + "' at: " + rdbColumnDomain.getTable());
    //        }
    //        return column;
    //    }
    //
    //    private List<SelectItem> parseUnion(RdbSelectDomain selectDomain, String uid, long dsID, Map<UmiTypes, Object> levelsParam) {
    //
    //        int count = 0;
    //        List<SelectItem> tempList = new ArrayList<>();
    //        for (int i = 0; i < selectDomain.getChildren().size(); i++) {
    //            RuleDomain ruleDomain = selectDomain.getChildren().get(i);
    //            List<SelectItem> selectItems = analyzeRealColumn(uid, dsID, levelsParam, (RdbSelectDomain) ruleDomain);
    //            if (i == 0) {
    //                count = selectItems.size();
    //            } else if (count != selectItems.size()) {
    //                throw new RuntimeException("The number of fields returned by the query statements on both sides of union is not the same");
    //            }
    //            tempList.addAll(selectItems);
    //        }
    //
    //        List<SelectItem> result = new ArrayList<>();
    //        for (int i = 0; i < count; i++) {
    //            SelectItem selectItem = tempList.get(i);
    //            selectItem.addAllRealColumns(tempList.get(i + count).getColumns());
    //            selectItem.setTableAlias(null);
    //            result.add(selectItem);
    //        }
    //        return result;
    //    }
    //
    //    private List<SelectItem> realTableHandle(String uid, long dsID, Map<UmiTypes, Object> levelsParam, RdbSelectDomain rdbSelectDomain) {
    //        if (CollectionUtils.isEmpty(rdbSelectDomain.getColumns())) {
    //            return fetchTableColumns(uid, dsID, levelsParam, rdbSelectDomain);
    //        }
    //        List<SelectItem> result = new ArrayList<>();
    //        for (QueryItem column : rdbSelectDomain.getColumns()) {
    //            if (column.isSelectAll()) {
    //                result.addAll(fetchTableColumns(uid, dsID, levelsParam, rdbSelectDomain));
    //            } else {
    //                result.add(this.convertToSelectColumn(uid, dsID, levelsParam, column, rdbSelectDomain));
    //            }
    //        }
    //        return result;
    //    }
    //
    //    private List<SelectItem> fetchTableColumns(String uid, long dsID, Map<UmiTypes, Object> levelsParam, RdbSelectDomain rdbSelectDomain) {
    //        Map<UmiTypes, Object> levels = new HashMap<>(levelsParam);
    //        if (rdbSelectDomain.getSchema() != null) {
    //            levels.put(UmiTypes.Schema, rdbSelectDomain.getSchema());
    //        }
    //        List<SelectItem> selectItems = this.metaService.fetchTableColumns(uid, dsID, levels, rdbSelectDomain.getTable());
    //        for (SelectItem selectItem : selectItems) {
    //            if (StringUtils.isNotEmpty(rdbSelectDomain.getTableAlias())) {
    //                selectItem.setTableAlias(rdbSelectDomain.getTableAlias());
    //            } else {
    //                selectItem.setTableAlias(rdbSelectDomain.getTable());
    //            }
    //        }
    //        return selectItems;
    //    }
    //
    //    private SelectItem convertToSelectColumn(String uid, long dsID, Map<UmiTypes, Object> levelsParam, QueryItem queryItem, RdbSelectDomain selectDomain) {
    //        SelectItem selectItem = new SelectItem();
    //        if (queryItem.getItemAlias() != null) {
    //            selectItem.setItemAlias(queryItem.getItemAlias());
    //        } else {
    //            selectItem.setItemAlias(queryItem.getColumn());
    //        }
    //        selectItem.setTableAlias(queryItem.getTable());
    //
    //        for (RuleDomain domain : queryItem.getColumns()) {
    //            if (domain instanceof RdbColumnDomain) {
    //                RdbColumnDomain rdbColumnDomain = (RdbColumnDomain) domain;
    //
    //                RealColumn realColumn = new RealColumn();
    //                realColumn.setColumn(rdbColumnDomain.getColumn());
    //                if (StringUtils.isNotEmpty(rdbColumnDomain.getTable())) {
    //                    realColumn.setTable(rdbColumnDomain.getTable());
    //                } else {
    //                    realColumn.setTable(selectDomain.getTable());
    //                }
    //
    //                if (rdbColumnDomain.getSchema() != null) {
    //                    realColumn.setSchema(rdbColumnDomain.getSchema());
    //                } else {
    //                    realColumn.setSchema(selectDomain.getSchema());
    //                }
    //                selectItem.addRealColumn(realColumn);
    //            } else if (domain instanceof RdbCallDomain) {
    //                SelectItem column = parseCallDomain(uid, dsID, levelsParam, (RdbCallDomain) domain, queryItem, selectDomain, null);
    //                selectItem.addAllRealColumns(column.getColumns());
    //            } else if (domain instanceof RdbSelectDomain) {
    //                List<SelectItem> selectItems = analyzeRealColumn(uid, dsID, levelsParam, (RdbSelectDomain) domain);
    //                if (selectItems.size() != 1) {
    //                    throw new RuntimeException("The query statement in the selection column should only return one column");
    //                }
    //                selectItem.addAllRealColumns(selectItems.get(0).getColumns());
    //            } else {
    //                throw new UnsupportedOperationException();
    //            }
    //        }
    //        return selectItem;
    //    }
    //
    //    private SelectItem parseCallDomain(String uid, long dsID, Map<UmiTypes, Object> levelsParam, RdbCallDomain callDomain, QueryItem queryItem, RdbSelectDomain selectDomain,
    //                                       Map<String, Map<String, SelectItem>> columnMap) {
    //        SelectItem selectItem = new SelectItem();
    //        selectItem.setItemAlias(queryItem.getItemAlias());
    //
    //        if (StringUtils.isNotBlank(selectDomain.getTableAlias())) {
    //            selectItem.setTableAlias(selectDomain.getTableAlias());
    //        } else {
    //            selectItem.setTableAlias(selectDomain.getTable());
    //        }
    //        if (CollectionUtils.isEmpty(callDomain.getChildren())) {
    //            return selectItem;
    //        }
    //
    //        for (RuleDomain child : callDomain.getChildren()) {
    //            if (child instanceof RdbSelectDomain) {
    //                List<SelectItem> selectItems = analyzeRealColumn(uid, dsID, levelsParam, (RdbSelectDomain) child);
    //                if (selectItems.size() > 1) {
    //                    throw new RuntimeException("The query statement in the function should only return one column");
    //                } else {
    //                    selectItem.addAllRealColumns(selectItems.get(0).getColumns());
    //                }
    //            } else if (child instanceof RdbCallDomain) {
    //                SelectItem callColumn = parseCallDomain(uid, dsID, levelsParam, (RdbCallDomain) child, queryItem, selectDomain, columnMap);
    //                selectItem.addAllRealColumns(callColumn.getColumns());
    //            } else if (child instanceof RdbColumnDomain) {
    //                RdbColumnDomain columnDomain = (RdbColumnDomain) child;
    //                if (columnMap != null) {
    //                    SelectItem column = findColumn(columnMap, columnDomain);
    //                    selectItem.addAllRealColumns(column.getColumns());
    //                } else {
    //                    RealColumn realColumn = new RealColumn();
    //                    realColumn.setColumn(columnDomain.getColumn());
    //                    realColumn.setTable(selectDomain.getTable());
    //                    realColumn.setSchema(selectDomain.getSchema());
    //                    selectItem.addRealColumn(realColumn);
    //                }
    //            }
    //        }
    //        return selectItem;
    //    }
}
