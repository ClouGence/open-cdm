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
package com.clougence.clouddm.ds.maxcompute.analysis.builder;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.maxcompute.analysis.secrules.McTableDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.AbstractDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.NameType;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

public class McRenameBuilder extends AbstractDomainBuilder {

    protected TargetType   targetType;

    protected List<String> oldNameList;
    protected List<String> newNameList;

    public McRenameBuilder(TargetType targetType){
        this.targetType = (targetType);
    }

    @Override
    public List<Domain> build() {
        if (targetType == TargetType.Table) {
            McTableDomain tableDomain = new McTableDomain();
            tableDomain.setSqlType(SecQueryType.RENAME_TABLE);
            tableDomain.setAuditKind(SecQueryKind.ALTER);
            Map<UmiTypes, String> map = BuilderUtil.parseTableName(oldNameList);
            tableDomain.setSchema(map.get(UmiTypes.Schema));
            tableDomain.setTable(map.get(UmiTypes.Table));
            Map<UmiTypes, String> newName = BuilderUtil.parseTableName(newNameList);
            tableDomain.setNewName(newName.get(UmiTypes.Table));
            if (newName.get(UmiTypes.Schema) == null) {
                tableDomain.setNewSchemaName(map.get(UmiTypes.Schema));
            } else {
                tableDomain.setNewSchemaName(newName.get(UmiTypes.Schema));
            }
            return Collections.singletonList(tableDomain);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            Domain domain = list.get(0);
            ObjNameDomain objNameDomain = (ObjNameDomain) domain;
            if (objNameDomain.getType() == NameType.TABLE) {
                this.oldNameList = objNameDomain.getNameList();
            } else if (objNameDomain.getType() == NameType.NEW_TABLE) {
                this.newNameList = objNameDomain.getNameList();
            } else {
                super.handleSubDomain(list, source);
            }
        } else {
            super.handleSubDomain(list, source);
        }
    }
}
