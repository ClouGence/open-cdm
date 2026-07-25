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
package com.clougence.sql.doris.analysis.security.builder;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.secrules.builder.DropSchemaBuilder;
import com.clougence.sql.common.analysis.secrules.builder.enums.Attribute;
import com.clougence.sql.common.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.sql.common.analysis.secrules.builder.enums.DomainSource;
import com.clougence.sql.common.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.sql.common.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.sql.doris.analysis.security.builder.enums.DrAttribute;
import com.clougence.sql.doris.analysis.security.domain.DrSchemaDomain;

public class DrDropSchemaBuilder extends DropSchemaBuilder<DrSchemaDomain> {

    private boolean ifExists;
    private boolean force;

    @Override
    protected DrSchemaDomain getSchemaDomain() { return new DrSchemaDomain(); }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.IF_EXISTS) {
            this.ifExists = true;
        } else if (attr == DrAttribute.FORCE) {
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
            DrSchemaDomain schemaDomain = getSchemaDomain();
            schemaDomain.setSchema(map.get(UmiTypes.Schema));
            schemaDomain.setCatalog(map.get(UmiTypes.Catalog));
            schemaDomain.setAuditKind(SecQueryKind.DROP);
            schemaDomain.setSqlType(SplitQueryType.DROP_SCHEMA);
            schemaDomain.setIfExists(ifExists);
            schemaDomain.setForce(force);
            domains.add(schemaDomain);
        } else {
            super.handleSubDomain(list, source);
        }
    }

}
