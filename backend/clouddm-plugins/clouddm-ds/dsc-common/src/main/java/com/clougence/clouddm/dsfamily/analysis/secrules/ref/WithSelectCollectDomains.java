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
package com.clougence.clouddm.dsfamily.analysis.secrules.ref;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSelectDomain;
import com.clougence.utils.StringUtils;

public class WithSelectCollectDomains {

    protected Stack<List<RdbSelectDomain>> selectStack = new Stack<>();

    public RdbSelectDomain pushDomain(RdbSelectDomain value) {
        this.selectStack.peek().add(value);
        return value;
    }

    public void createFrame() {
        this.selectStack.push(new ArrayList<>());
    }

    public void dropFrame() {
        this.selectStack.pop();
    }

    public RdbSelectDomain findDomainBy(String catalog, String schema, String table) {
        List<RdbSelectDomain> filtered = this.selectStack.stream()
            .flatMap((Function<List<RdbSelectDomain>, Stream<RdbSelectDomain>>) Collection::stream)
            .filter(t -> StringUtils.equals(t.getTable(), table))
            .collect(Collectors.toList());

        Collections.reverse(filtered);

        if (StringUtils.isNotBlank(catalog)) {
            filtered = filtered.stream().filter(t -> {
                return t.getCatalog() == null || StringUtils.equals(t.getCatalog(), catalog);
            }).collect(Collectors.toList());
        }
        if (StringUtils.isNotBlank(schema)) {
            filtered = filtered.stream().filter(t -> {
                return t.getSchema() == null || StringUtils.equals(t.getSchema(), schema);
            }).collect(Collectors.toList());
        }

        if (filtered.isEmpty()) {
            return null;
        } else if (filtered.size() == 1) {
            return filtered.get(0);
        } else {
            RdbSelectDomain defaultTab = filtered.get(0);
            List<RdbSelectDomain> filteredOnlyName = filtered.stream().filter(t -> {
                return !StringUtils.equals(t.getTable(), table);
            }).collect(Collectors.toList());
            if (filteredOnlyName.isEmpty()) {
                return defaultTab;
            } else {
                return filteredOnlyName.get(0);
            }
        }
    }
}
