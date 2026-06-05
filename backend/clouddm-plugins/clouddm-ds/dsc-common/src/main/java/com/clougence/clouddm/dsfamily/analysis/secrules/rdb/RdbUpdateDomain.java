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

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.detectrule.lang.reflect.RuleIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdbUpdateDomain extends RdbWhereDomain implements RdbConfigNames {

    @Deprecated
    private String                 catalog;
    @Deprecated
    private String                 schema;
    @Deprecated
    private String                 table;

    private List<String>           setColumns;
    private boolean                selectInSet;

    private boolean                multiUpdate;

    private List<RdbJoinType>      joinTypes = new ArrayList<>();
    @RuleIgnore
    protected List<RdbTableDomain> tables    = new ArrayList<>();

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        HashMap<TargetType, String> map = new HashMap<>();
        map.put(TargetType.Catalog, this.catalog);
        map.put(TargetType.Schema, this.schema);
        map.put(TargetType.Table, this.table);
        return Collections.singletonList(map);
    }

    @Override
    public void configName(String catalog, String schema, String name) {
        this.catalog = catalog;
        this.schema = schema;
        this.table = name;
    }
}
