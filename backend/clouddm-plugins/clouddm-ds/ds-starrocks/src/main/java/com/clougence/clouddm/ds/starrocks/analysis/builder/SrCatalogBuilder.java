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
package com.clougence.clouddm.ds.starrocks.analysis.builder;

import com.clougence.clouddm.ds.starrocks.analysis.secrules.SrCatalogDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.CatalogDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.OptionsDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

import java.util.List;

public class SrCatalogBuilder  extends CatalogDomainBuilder<SrCatalogDomain> {

    public SrCatalogBuilder(SecQueryType secQueryType){
        super(secQueryType);
    }

    @Override
    protected SrCatalogDomain getCatalogDomain() { return new SrCatalogDomain(); }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain domain = (ObjNameDomain) list.get(0);
            rdbCatalogDomain.setCatalog(domain.getName());
        } else if (source == DomainSource.OPTIONS) {
            OptionsDomain domain = (OptionsDomain) list.get(0);
            rdbCatalogDomain.setOptions(domain.getOptions());
        } else {
            super.handleSubDomain(list, source);
        }
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.COMMENT) {
            rdbCatalogDomain.setComment((String) value);
        } else if (attr == CommonAttribute.IF_EXISTS) {
            rdbCatalogDomain.setIfExists(true);
        } else {
            super.addAttr(attr, value);
        }
    }
}