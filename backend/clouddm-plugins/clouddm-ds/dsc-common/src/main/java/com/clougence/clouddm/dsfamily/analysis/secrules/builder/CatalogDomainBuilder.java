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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbCatalogDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public abstract class CatalogDomainBuilder<T extends RdbCatalogDomain> extends AbstractDomainBuilder {

    protected T rdbCatalogDomain = getCatalogDomain();

    protected abstract T getCatalogDomain();

    public CatalogDomainBuilder(SecQueryType secQueryType){
        rdbCatalogDomain.setSqlType(secQueryType);
        rdbCatalogDomain.setAuditKind(secQueryType.getAuditKind());
    }

    @Override
    public List<Domain> build() {
        if (rdbCatalogDomain.getOptions() == null) {
            rdbCatalogDomain.setOptions(new HashMap<>());
        }
        return Collections.singletonList(rdbCatalogDomain);
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.VALUE) {
            if (rdbCatalogDomain.getCatalog() != null) {
                return;
            }
            this.rdbCatalogDomain.setCatalog((String) value);
        }
    }

}
