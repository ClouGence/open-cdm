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

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.starrocks.analysis.enums.SrAttribute;
import com.clougence.clouddm.ds.starrocks.analysis.secrules.SrSchemaDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.DropSchemaBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

public class SrDropSchemaBuilder extends DropSchemaBuilder<SrSchemaDomain> {

    private boolean ifExists;
    private boolean force;

    @Override
    protected SrSchemaDomain getSchemaDomain() { return new SrSchemaDomain(); }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.IF_EXISTS) {
            this.ifExists = true;
        } else if (attr == SrAttribute.FORCE) {
            this.force = true;
        } else {
            super.addAttr(attr, value);
        }
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain objNameDomain = (ObjNameDomain) list.get(0);
            Map<UmiTypes, String> map = BuilderUtil.parseSchemaName(objNameDomain.getNameList());
            SrSchemaDomain schemaDomain = getSchemaDomain();
            schemaDomain.setSchema(map.get(UmiTypes.Schema));
            schemaDomain.setCatalog(map.get(UmiTypes.Catalog));
            schemaDomain.setAuditKind(SecQueryKind.DROP);
            schemaDomain.setSqlType(SecQueryType.DROP_SCHEMA);
            schemaDomain.setIfExists(ifExists);
            schemaDomain.setForce(force);
            domains.add(schemaDomain);
        } else {
            super.handleSubDomain(list, source);
        }
    }
}
