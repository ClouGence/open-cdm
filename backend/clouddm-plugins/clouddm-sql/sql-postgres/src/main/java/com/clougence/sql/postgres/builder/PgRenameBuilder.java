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

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.sql.secrules.rdb.RdbCatalogDomain;
import com.clougence.clouddm.sdk.sql.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.sdk.sql.secrules.rdb.RdbSchemaDomain;
import com.clougence.clouddm.sdk.sql.secrules.rdb.RdbTableDomain;
import com.clougence.sql.common.analysis.secrules.builder.RenameBuilder;
import com.clougence.sql.common.analysis.secrules.builder.enums.Attribute;
import com.clougence.sql.common.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.sql.postgres.security.domain.PgColumnDomain;
import com.clougence.sql.postgres.security.domain.PgSchemaDomain;
import com.clougence.sql.postgres.security.domain.PgTableDomain;

public class PgRenameBuilder extends RenameBuilder {

    protected boolean ifExists;

    public PgRenameBuilder(TargetType targetType){
        super(targetType);
    }

    @Override
    protected RdbColumnDomain getColumnDomain() { return new PgColumnDomain(); }

    @Override
    protected RdbTableDomain getTableDomain() { return new PgTableDomain(); }

    protected RdbSchemaDomain getSchemaDomain() { return new PgSchemaDomain(); }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.IF_EXISTS) {
            this.ifExists = true;
        } else {
            super.addAttr(attr, value);
        }
    }

    @Override
    public List<Domain> build() {
        if (targetType == TargetType.Column) {
            RdbColumnDomain rdbColumnDomain = getColumnDomain();
            rdbColumnDomain.setSqlType(SecQueryType.ALTER_TABLE_RENAME_COLUMN);
            rdbColumnDomain.setAuditKind(SecQueryKind.ALTER);
            int size = nameList.size();
            switch (size) {
                case 5: {
                    rdbColumnDomain.setCatalog(nameList.get(0));
                }
                case 4: {
                    rdbColumnDomain.setSchema(nameList.get(size - 4));
                }
                default: {
                    rdbColumnDomain.setTable(nameList.get(size - 3));
                    rdbColumnDomain.setColumn(nameList.get(size - 2));
                    rdbColumnDomain.setNewName(nameList.get(size - 1));
                }
            }
            return Collections.singletonList(rdbColumnDomain);
        } else if (targetType == TargetType.Table) {
            PgTableDomain rdbColumnDomain = new PgTableDomain();
            rdbColumnDomain.setSqlType(SecQueryType.ALTER_TABLE_RENAME);
            rdbColumnDomain.setAuditKind(SecQueryKind.ALTER);
            int size = nameList.size();
            switch (size) {
                case 4: {
                    rdbColumnDomain.setCatalog(nameList.get(0));
                }
                case 3: {
                    rdbColumnDomain.setSchema(nameList.get(size - 3));
                }
                default: {
                    rdbColumnDomain.setTable(nameList.get(size - 2));
                    rdbColumnDomain.setNewName(nameList.get(size - 1));
                }
            }
            if (ifExists) {
                rdbColumnDomain.setIfExists(true);
            }
            return Collections.singletonList(rdbColumnDomain);
        } else if (targetType == TargetType.Catalog) {
            RdbCatalogDomain catalogDomain = new RdbCatalogDomain();
            catalogDomain.setAuditKind(SecQueryKind.ALTER);
            catalogDomain.setSqlType(SecQueryType.RENAME_CATALOG);
            catalogDomain.setCatalog(nameList.get(0));
            catalogDomain.setNewName(nameList.get(1));
            catalogDomain.setOptions(new HashMap<>());
            return Collections.singletonList(catalogDomain);
        } else if (targetType == TargetType.Schema) {
            RdbSchemaDomain schemaDomain = getSchemaDomain();
            schemaDomain.setAuditKind(SecQueryKind.ALTER);
            schemaDomain.setSqlType(SecQueryType.RENAME_SCHEMA);
            int size = nameList.size();
            if (size == 3) {
                schemaDomain.setCatalog(nameList.get(0));
            }
            schemaDomain.setSchema(nameList.get(size - 2));
            schemaDomain.setNewName(nameList.get(size - 1));
            return Collections.singletonList(schemaDomain);
        }

        throw new UnsupportedOperationException(targetType.name());
    }
}
