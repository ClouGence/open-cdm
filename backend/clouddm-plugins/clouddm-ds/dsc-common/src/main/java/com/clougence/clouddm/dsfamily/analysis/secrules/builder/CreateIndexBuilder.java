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
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.NameType;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ColumnListDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ConstraintTypeDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbIndexDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.SqlConstraintType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.utils.StringUtils;

public class CreateIndexBuilder extends AbstractDomainBuilder {

    protected RdbIndexDomain indexDomain = getIndexDomain();

    protected RdbIndexDomain getIndexDomain() { return new RdbIndexDomain(); }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.COMMENT) {
            indexDomain.setComment((String) value);
        } else if (attr == CommonAttribute.INDEX_TYPE) {
            indexDomain.setType((String) value);
        } else {
            super.addAttr(attr, value);
        }
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain objNameDomain = (ObjNameDomain) list.get(0);
            if (objNameDomain.getType() == NameType.INDEX) {
                indexDomain.setName(objNameDomain.getName());
            } else {
                Map<UmiTypes, String> map = BuilderUtil.parseTableName(objNameDomain.getNameList());
                indexDomain.setTableCatalog(map.get(UmiTypes.Catalog));
                indexDomain.setTableSchema(map.get(UmiTypes.Schema));
                indexDomain.setTableName(map.get(UmiTypes.Table));
            }
        } else if (source == DomainSource.COLUMN_LIST) {
            ColumnListDomain columnListDomain = (ColumnListDomain) list.get(0);
            indexDomain.setColumns(columnListDomain.getColumns());
        } else if (source == DomainSource.CONSTRAINT_TYPE) {
            ConstraintTypeDomain constraintTypeDomain = (ConstraintTypeDomain) list.get(0);
            if (constraintTypeDomain.getConstraintType() == SqlConstraintType.Unique) {
                indexDomain.setType("unique");
            } else {
                super.handleSubDomain(list, source);
            }
        } else {
            super.handleSubDomain(list, source);
        }
    }

    @Override
    public List<Domain> build() {
        indexDomain.setSqlType(SecQueryType.CREATE_INDEX);
        indexDomain.setAuditKind(SecQueryKind.CREATE);
        if (StringUtils.isEmpty(indexDomain.getType())) {
            indexDomain.setType("index");
        }
        return Collections.singletonList(indexDomain);
    }
}
