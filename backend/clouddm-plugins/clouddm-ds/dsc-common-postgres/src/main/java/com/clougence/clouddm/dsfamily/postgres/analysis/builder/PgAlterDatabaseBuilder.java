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
package com.clougence.clouddm.dsfamily.postgres.analysis.builder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.AbstractDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.postgres.analysis.PgSecDomainOptionKeys;
import com.clougence.clouddm.dsfamily.postgres.analysis.builder.enums.PgAttribute;
import com.clougence.clouddm.dsfamily.postgres.analysis.builder.mode.OptDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgCatalogDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class PgAlterDatabaseBuilder extends AbstractDomainBuilder {

    private String          name;

    private PgCatalogDomain domain;

    public PgAlterDatabaseBuilder(){
        domain = new PgCatalogDomain();
        domain.setOptions(new HashMap<>());
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.SET_OPT) {
            OptDomain optDomain = (OptDomain) list.get(0);
            if (optDomain.getValue() == null) {
                domain.getOptions().put(PgSecDomainOptionKeys.OPT_CATALOG_RESET, optDomain.getKey());
            } else {
                domain.getOptions().put(PgSecDomainOptionKeys.OPT_CATALOG_CONF_NAME, optDomain.getKey());
                domain.getOptions().put(PgSecDomainOptionKeys.OPT_CATALOG_CONF_VALUE, optDomain.getValue());
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public List<Domain> build() {
        domain.setSqlType(SecQueryType.ALTER_CATALOG);
        domain.setAuditKind(SecQueryKind.ALTER);
        domain.setCatalog(name);
        return Collections.singletonList(domain);
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == PgAttribute.TABLESPACE) {
            this.domain.getOptions().put(PgSecDomainOptionKeys.OPT_CATALOG_TABLESPACE, (String) value);
        } else if (attr == PgAttribute.REFRESH_COLLATION_VERSION) {
            this.domain.getOptions().put(PgSecDomainOptionKeys.OPT_CATALOG_RCV, "true");
        } else if (attr == CommonAttribute.VALUE) {
            if (this.name == null) {
                this.name = (String) value;
            }
        } else {
            super.addAttr(attr, value);
        }
    }
}
