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
package com.clougence.sql.common.analysis.secrules.builder;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.sql.analysis.security.column.QueryItem;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbQueryMode;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbSelectDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbTableDomain;
import com.clougence.sql.common.analysis.secrules.builder.enums.Attribute;
import com.clougence.sql.common.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.sql.common.analysis.secrules.builder.enums.DomainSource;
import com.clougence.sql.common.analysis.secrules.builder.mode.WithSelectDomain;

public class WithSelectBuilder implements DomainBuilder {

    private final WithSelectDomain domain      = new WithSelectDomain();
    private List<String>           columnNames = Collections.emptyList();

    @Override
    public List<Domain> build() {
        RdbSelectDomain selectDomain = domain.getSelectDomain();
        selectDomain.setMode(RdbQueryMode.WITH_BODY);
        selectDomain.setSimpleSelect(false);
        if (!columnNames.isEmpty()) {
            RdbTableDomain source = new RdbTableDomain();
            source.setTable(domain.getName());
            source.setVirtual(true);
            source.addChild(selectDomain);

            RdbSelectDomain renamedDomain = new RdbSelectDomain();
            renamedDomain.setMode(RdbQueryMode.WITH);
            renamedDomain.setSimpleSelect(false);
            renamedDomain.addChild(source);
            for (String columnName : columnNames) {
                QueryItem queryItem = new QueryItem();
                queryItem.setColumn(columnName);
                renamedDomain.getColumns().add(queryItem);
            }
            domain.setSelectDomain(renamedDomain);
        }
        return Collections.singletonList(domain);
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source != DomainSource.SELECT) {
            throw new UnsupportedOperationException();
        }
        this.domain.setSelectDomain((RdbSelectDomain) list.get(0));
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.VALUE) {
            this.domain.setName((String) value);
        } else if (attr == CommonAttribute.CTE_COLUMN_NAMES) {
            this.columnNames = ((List<?>) value).stream().map(Object::toString).toList();
        }
    }

}
