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
package com.clougence.sql.postgres.builder;

import java.util.List;

import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.sql.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.sdk.sql.secrules.rdb.RdbConstraintDomain;
import com.clougence.sql.common.analysis.secrules.builder.CreateSchemaBuilder;
import com.clougence.sql.common.analysis.secrules.builder.enums.Attribute;
import com.clougence.sql.common.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.sql.common.analysis.secrules.builder.enums.DomainSource;
import com.clougence.sql.common.analysis.secrules.builder.mode.StringDomain;
import com.clougence.sql.postgres.security.domain.PgSchemaDomain;
import com.clougence.sql.postgres.security.domain.PgTableDomain;

public class PgCreateSchemaBuilder extends CreateSchemaBuilder<PgSchemaDomain> {

    private boolean ifNotExists;

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.ROLE_SPEC) {
            StringDomain domain = (StringDomain) list.get(0);
            this.schemaDomain.setOwner(domain.getVal());
            if (schemaDomain.getSchema() == null) {
                this.schemaDomain.setSchema(domain.getVal());
            }
        } else if (source == DomainSource.CREATE_TABLE) {
            PgTableDomain domain = (PgTableDomain) list.get(0);
            domain.setSchema(schemaDomain.getSchema());
            for (RdbColumnDomain columnDomain : domain.getColumnDomains()) {
                columnDomain.setSchema(schemaDomain.getSchema());
            }
            for (RdbColumnDomain columnDomain : domain.getColumnDomains()) {
                columnDomain.setSchema(schemaDomain.getSchema());
            }
            for (RdbConstraintDomain constraintDomain : domain.getConstraintDomains()) {
                constraintDomain.setTableSchema(schemaDomain.getSchema());
            }
            schemaDomain.addChild(domain);
        } else {
            super.handleSubDomain(list, source);
        }
    }

    @Override
    public List<Domain> build() {
        schemaDomain.setIfNotExists(ifNotExists);
        return super.build();
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.IF_NOT_EXISTS) {
            this.ifNotExists = true;
        } else {
            super.addAttr(attr, value);
        }
    }

    @Override
    protected PgSchemaDomain getSchemaDomain() { return new PgSchemaDomain(); }
}
