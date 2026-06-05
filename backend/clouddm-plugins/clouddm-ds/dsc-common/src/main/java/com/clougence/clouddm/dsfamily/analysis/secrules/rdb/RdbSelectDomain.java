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
package com.clougence.clouddm.dsfamily.analysis.secrules.rdb;

import java.util.*;

import com.clougence.clouddm.dsfamily.analysis.secrules.column.QueryItem;
import com.clougence.clouddm.sdk.analysis.column.SelectItem;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.detectrule.lang.reflect.RuleIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: mode
 * @Date: 2024-11-20 11:00
 */
@Getter
@Setter
public class RdbSelectDomain extends RdbWhereDomain implements RdbConfigNames {

    @Deprecated
    private String            catalog;
    @Deprecated
    private String            schema;
    @Deprecated
    private String            table;
    @Deprecated
    private boolean           simpleSelect;
    private RdbQueryMode      mode;
    private boolean           virtual;

    // for select
    private boolean           hasAs;
    private boolean           hasSelectAll;
    private boolean           selectInSelect;
    private boolean           funcInSelect;
    // such as  select id + name
    private boolean           exprInSelect;

    private List<String>      selectColumns;
    private List<String>      selectVariables;
    private List<String>      selectFunc;
    private List<String>      selectValue;

    // for from
    private boolean           selectInFrom;
    private boolean           emptyFrom;

    private List<SelectItem>  selectItems  = new ArrayList<>();

    @RuleIgnore
    private List<QueryItem>   columns      = new ArrayList<>();
    @RuleIgnore
    private List<RuleDomain>  whereDomains = new ArrayList<>();

    private List<RdbJoinType> joinTypes    = new ArrayList<>();

    public void addSelect(String select, RdbQuerySelectType selectType) {
        if (select == null || select.isEmpty()) {
            return;
        }

        List<String> append = null;
        switch (selectType) {
            case Column:
                append = this.selectColumns;
                break;
            case Function:
                append = this.selectFunc;
                break;
            case Variable:
                append = this.selectVariables;
                break;
            case Value:
                append = this.selectValue;
                break;
            default:
                return;
        }
        if (!append.contains(select)) {
            append.add(select);
        }
    }

    @Override
    public void configName(String catalog, String schema, String name) {
        this.catalog = catalog;
        this.schema = schema;
        this.table = name;
    }

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        Map<TargetType, String> map = new HashMap<>();
        map.put(TargetType.Catalog, this.catalog);
        map.put(TargetType.Schema, this.schema);
        map.put(TargetType.Table, this.table);
        return Collections.singletonList(map);
    }
}
