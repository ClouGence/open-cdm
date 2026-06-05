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
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.detectrule.lang.reflect.RuleIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdbTableDomain extends RuleDomain {

    private String                    catalog;
    private String                    schema;
    private String                    table;
    private String                    comment;

    private boolean                   hasPrimary;
    private boolean                   hasUnique;
    private boolean                   hasForeignKey;
    private boolean                   hasIndex;

    private List<String>              columns;

    // for create table like
    private String                    sourceSchema;
    private String                    sourceTable;

    // for table rename
    private String                    newName;

    // for select
    private boolean                   virtual;
    private String                    alias;

    @RuleIgnore
    private List<RdbColumnDomain>     columnDomains     = new ArrayList<>();
    @RuleIgnore
    private List<RdbConstraintDomain> constraintDomains = new ArrayList<>();
    @RuleIgnore
    private List<RdbIndexDomain>      indexDomains      = new ArrayList<>();

    @Override
    public List<Map<TargetType, String>> resolveResource() {
        if (virtual) {
            List<Map<TargetType, String>> result = new ArrayList<>();
            for (RuleDomain child : getChildren()) {
                result.addAll(child.resolveResource());
            }
            return result;
        } else {
            Map<TargetType, String> map = new HashMap<>();
            map.put(TargetType.Catalog, this.catalog);
            map.put(TargetType.Schema, this.schema);
            map.put(TargetType.Table, this.table);
            return Collections.singletonList(map);
        }
    }
}
