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
package com.clougence.clouddm.ds.clickhouse.analysis.builder;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.clickhouse.analysis.secrules.ChSchemaDomain;
import com.clougence.clouddm.ds.clickhouse.analysis.secrules.ChTableDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.AbstractDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

public class ChRenameBuilder extends AbstractDomainBuilder {

    protected TargetType    targetType;

    protected ObjNameDomain oldName;
    protected ObjNameDomain newName;

    public ChRenameBuilder(TargetType targetType){
        this.targetType = targetType;
    }

    @Override
    public List<Domain> build() {
        if (targetType == TargetType.Table) {
            ChTableDomain domain = new ChTableDomain();
            domain.setAuditKind(SecQueryKind.ALTER);
            domain.setSqlType(SecQueryType.RENAME_TABLE);

            Map<UmiTypes, String> map = BuilderUtil.parseTableName(oldName.getNameList());
            domain.setSchema(map.get(UmiTypes.Schema));
            domain.setTable(map.get(UmiTypes.Table));

            Map<UmiTypes, String> map1 = BuilderUtil.parseTableName(newName.getNameList());
            domain.setNewSchemaName(map1.get(UmiTypes.Schema));
            domain.setNewName(map1.get(UmiTypes.Table));

            return Collections.singletonList(domain);
        } else if (targetType == TargetType.Schema) {
            ChSchemaDomain domain = new ChSchemaDomain();
            domain.setAuditKind(SecQueryKind.ALTER);
            domain.setSqlType(SecQueryType.RENAME_SCHEMA);
            Map<UmiTypes, String> map = BuilderUtil.parseSchemaName(oldName.getNameList());
            domain.setSchema(map.get(UmiTypes.Schema));

            Map<UmiTypes, String> map1 = BuilderUtil.parseSchemaName(newName.getNameList());
            domain.setNewName(map1.get(UmiTypes.Schema));

            return Collections.singletonList(domain);
        }

        throw new UnsupportedOperationException(targetType.name());
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            if (oldName == null) {
                this.oldName = (ObjNameDomain) list.get(0);
            } else {
                this.newName = (ObjNameDomain) list.get(0);
            }
        } else {
            super.handleSubDomain(list, source);
        }
    }
}
