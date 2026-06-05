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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbTableDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

public abstract class DropTableBuilder<T extends RdbTableDomain> extends AbstractDomainBuilder {

    protected List<Domain> tableDomains = new ArrayList<>();

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            RdbTableDomain tableDomain = getTableDomain();

            tableDomain.setSqlType(SecQueryType.DROP_TABLE);
            tableDomain.setAuditKind(SecQueryKind.DROP);

            ObjNameDomain objNameDomain = (ObjNameDomain) list.get(0);

            List<String> nameList = objNameDomain.getNameList();
            Map<UmiTypes, String> map = BuilderUtil.parseTableName(nameList);
            tableDomain.setCatalog(map.get(UmiTypes.Catalog));
            tableDomain.setSchema(map.get(UmiTypes.Schema));
            tableDomain.setTable(map.get(UmiTypes.Table));

            this.tableDomains.add(tableDomain);
        } else {
            super.handleSubDomain(list, source);
        }
    }

    @Override
    public List<Domain> build() {
        return tableDomains;
    }

    protected abstract T getTableDomain();
}
