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
package com.clougence.clouddm.ds.doris.analysis.builder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.clougence.clouddm.ds.doris.analysis.secrules.DrCatalogDomain;
import com.clougence.clouddm.ds.doris.analysis.secrules.DrSchemaDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.RenameBuilder;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class DrRenameBuilder extends RenameBuilder {

    public DrRenameBuilder(TargetType targetType){
        super(targetType);
    }

    @Override
    public List<Domain> build() {
        if (targetType == TargetType.Schema) {
            DrSchemaDomain schemaDomain = new DrSchemaDomain();
            schemaDomain.setAuditKind(SecQueryKind.ALTER);
            schemaDomain.setSqlType(SecQueryType.RENAME_SCHEMA);
            int size = nameList.size();
            if (size == 3) {
                schemaDomain.setCatalog(nameList.get(0));
            }
            schemaDomain.setSchema(nameList.get(size - 2));
            schemaDomain.setNewName(nameList.get(size - 1));
            return Collections.singletonList(schemaDomain);
        } else if (targetType == TargetType.Catalog) {
            DrCatalogDomain catalogDomain = new DrCatalogDomain();
            catalogDomain.setAuditKind(SecQueryKind.ALTER);
            catalogDomain.setSqlType(SecQueryType.RENAME_CATALOG);
            catalogDomain.setCatalog(nameList.get(0));
            catalogDomain.setNewName(nameList.get(1));
            catalogDomain.setOptions(new HashMap<>());
            return Collections.singletonList(catalogDomain);
        }

        throw new UnsupportedOperationException(targetType.name());
    }

}
