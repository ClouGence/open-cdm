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
package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.*;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.JoinDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.SelectItemDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WhereDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WithSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.column.QueryItem;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.*;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.utils.StringUtils;

public abstract class SelectDomainBuilder<T extends RdbSelectDomain> extends AbstractDomainBuilder {

    protected T                           selectDomain;

    protected List<Domain>                domains = new ArrayList<>();

    private boolean                       union;

    private Stack<List<WithSelectDomain>> selectStack;

    public SelectDomainBuilder(Stack<List<WithSelectDomain>> selectStack){
        this.selectStack = selectStack;

        this.selectDomain = getSelectDomain();

        selectDomain.setSqlType(SecQueryType.SELECT);
        selectDomain.setAuditKind(SecQueryKind.QUERY);
        selectDomain.setMode(RdbQueryMode.NORMAL);
        selectDomain.setOptions(new HashMap<>());
        selectDomain.setSelectColumns(new ArrayList<>());
        selectDomain.setSelectVariables(new ArrayList<>());
        selectDomain.setSelectFunc(new ArrayList<>());
        selectDomain.setSelectValue(new ArrayList<>());
        selectDomain.setWhereColumns(new ArrayList<>());
        selectDomain.setJoinType(RdbJoinType.NONE);
        selectDomain.setSimpleSelect(true);
        selectDomain.setEmptyFrom(true);
    }

    protected abstract T getSelectDomain();

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.UNION) {
            this.union = (boolean) value;
        }
    }

    public List<Domain> build() {

        for (QueryItem column : selectDomain.getColumns()) {
            for (RuleDomain columnColumn : column.getColumns()) {
                if (columnColumn instanceof RdbColumnDomain) {
                    RdbColumnDomain columnDomain = (RdbColumnDomain) columnColumn;
                    if (columnDomain.getCatalog() == null) {
                        columnDomain.setCatalog(selectDomain.getCatalog());
                    }
                    if (columnDomain.getSchema() == null) {
                        columnDomain.setSchema(selectDomain.getSchema());
                    }
                    if (columnDomain.getTable() == null) {
                        columnDomain.setTable(selectDomain.getTable());
                    }
                }
            }
        }
        if (union) {
            selectDomain.setHasUnion(true);
            List<Domain> result = new ArrayList<>();
            for (Domain domain : domains) {
                if (domain instanceof RdbSelectDomain) {
                    RdbSelectDomain selectDomain1 = (RdbSelectDomain) domain;
                    selectDomain1.setMode(RdbQueryMode.UNION);
                    selectDomain1.setHasUnion(true);
                    result.add(selectDomain1);
                }
            }
            return result;
        }
        if (domains.isEmpty()) {
            return Collections.singletonList(selectDomain);
        } else if (domains.size() == 1) {
            boolean with = !selectStack.peek().isEmpty();
            if (with) {
                ((RdbSelectDomain) domains.get(0)).setHasWith(with);
            }
            return domains;
        }

        return domains;
    }

    public void addSelectColumn(SelectItemDomain selectItemDomain) {
        QueryItem queryItem = new QueryItem();
        queryItem.setItemAlias(selectItemDomain.getAlias());
        queryItem.setColumn(selectItemDomain.getName());
        if (queryItem.getItemAlias() != null && !queryItem.getColumn().equals(queryItem.getItemAlias())) {
            selectDomain.setHasAs(true);
        }
        selectDomain.getColumns().add(queryItem);
        for (RuleDomain domain : selectItemDomain.getDomainList()) {
            queryItem.addDomain(domain);
            parseQueryColumnDomain(selectItemDomain, domain, queryItem);
        }
    }

    private void parseQueryColumnDomain(SelectItemDomain selectItemDomain, RuleDomain domain, QueryItem queryItem) {
        if (domain instanceof RdbColumnDomain) {
            RdbColumnDomain rdbColumnDomain = (RdbColumnDomain) domain;
            if (queryItem.getColumns().size() == 1) {
                queryItem.setColumn(rdbColumnDomain.getColumn());
            }

            if (StringUtils.isNotEmpty(selectItemDomain.getAlias()) && !rdbColumnDomain.getColumn().equals(selectItemDomain.getAlias())) {
                selectDomain.setHasAs(true);
                selectDomain.setSimpleSelect(false);
            }
            if (rdbColumnDomain.getColumn().equals("*")) {
                queryItem.setSelectAll(true);
                selectDomain.setHasSelectAll(true);
                queryItem.setTable(rdbColumnDomain.getTable());
                return;
            }
            selectDomain.addSelect(rdbColumnDomain.getColumn(), RdbQuerySelectType.Column);
        } else if (domain instanceof RdbCallDomain) {
            RdbCallDomain rdbCallDomain = (RdbCallDomain) domain;
            selectDomain.setFuncInSelect(true);
            selectDomain.setSimpleSelect(false);
            for (RuleDomain child : rdbCallDomain.getChildren()) {
                if (child instanceof RdbColumnDomain) {
                    RdbColumnDomain columnDomain = (RdbColumnDomain) child;
                    if (columnDomain.getColumn().equals("*")) {
                        continue;
                    }
                    selectDomain.addSelect(columnDomain.getColumn(), RdbQuerySelectType.Column);
                } else if (child instanceof RdbCallDomain) {
                    RdbCallDomain callDomain = (RdbCallDomain) child;
                    parseQueryColumnDomain(selectItemDomain, callDomain, queryItem);
                }
            }
            selectDomain.addSelect(rdbCallDomain.getCallName(), RdbQuerySelectType.Function);
        } else if (domain instanceof RdbSelectDomain) {
            selectDomain.setSelectInSelect(true);
            selectDomain.setSimpleSelect(false);
            ((RdbSelectDomain) domain).setMode(RdbQueryMode.SUB_SELECT);
        } else if (domain instanceof RdbConstantDomain) {
            RdbConstantDomain rdbConstantDomain = (RdbConstantDomain) domain;
            selectDomain.addSelect(rdbConstantDomain.getConstantValue(), RdbQuerySelectType.Value);
        } else if (domain instanceof RdbVariableDomain) {
            RdbVariableDomain rdbVariableDomain = (RdbVariableDomain) domain;
            selectDomain.addSelect(rdbVariableDomain.getVariable(), RdbQuerySelectType.Variable);
        }
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource type) {
        if (type == DomainSource.SELECT_ITEM) {
            if (list.isEmpty()) {
                selectDomain.setHasSelectAll(true);
            } else {
                SelectItemDomain selectItemDomain = (SelectItemDomain) list.get(0);
                addSelectColumn(selectItemDomain);
            }
        } else if (type == DomainSource.FROM) {
            for (Domain ruleDomain : list) {
                if (ruleDomain instanceof RdbTableDomain) {
                    RdbTableDomain rdbTableDomain = (RdbTableDomain) ruleDomain;
                    selectDomain.addChild(rdbTableDomain);
                    if (rdbTableDomain.getChildren() != null) {
                        for (RuleDomain child : rdbTableDomain.getChildren()) {
                            if (child instanceof RdbSelectDomain) {
                                selectDomain.setSelectInFrom(true);
                                selectDomain.setSimpleSelect(false);
                                break;
                            }
                        }
                    }
                } else if (ruleDomain instanceof JoinDomain) {
                    JoinDomain joinDomain = (JoinDomain) ruleDomain;
                    if (joinDomain.getOptions() != null) {
                        selectDomain.getOptions().putAll(joinDomain.getOptions());
                    }
                    selectDomain.getJoinTypes().add(joinDomain.getJoinType());
                    selectDomain.setSimpleSelect(false);

                }
            }
            if (selectDomain.getJoinTypes().isEmpty() && list.size() > 1) {
                selectDomain.getJoinTypes().add(RdbJoinType.CROSS_JOIN);
                selectDomain.setSimpleSelect(false);
            }

            selectDomain.setEmptyFrom(false);
        } else if (type == DomainSource.WHERE) {
            WhereDomain whereDomain = (WhereDomain) list.get(0);
            if (!whereDomain.isValidWhere()) {
                return;
            }
            for (Domain ruleDomain : whereDomain.getDomains()) {
                if (ruleDomain instanceof RdbColumnDomain) {
                    RdbColumnDomain rdbColumnDomain = (RdbColumnDomain) ruleDomain;
                    selectDomain.getWhereDomains().add(rdbColumnDomain);
                    selectDomain.addWhereColumn(rdbColumnDomain.getColumn());
                } else if (ruleDomain instanceof RdbCallDomain) {
                    RdbCallDomain rdbCallDomain = (RdbCallDomain) ruleDomain;
                    selectDomain.getWhereDomains().add(rdbCallDomain);
                    for (RuleDomain arg : rdbCallDomain.getChildren()) {
                        if (arg instanceof RdbColumnDomain) {
                            selectDomain.addWhereColumn(((RdbColumnDomain) arg).getColumn());
                        }
                    }
                } else if (ruleDomain instanceof RdbSelectDomain) {
                    RdbSelectDomain rdbSelectDomain = (RdbSelectDomain) ruleDomain;
                    rdbSelectDomain.setMode(RdbQueryMode.SUB_WHERE);
                    selectDomain.setSelectInWhere(true);
                    selectDomain.setSimpleSelect(false);
                    selectDomain.getWhereDomains().add(rdbSelectDomain);
                }
            }
            selectDomain.setHasWhere(true);
        } else if (type == DomainSource.SELECT) {
            domains.addAll(list);
        } else {
            super.handleSubDomain(list, type);
        }
    }

}
