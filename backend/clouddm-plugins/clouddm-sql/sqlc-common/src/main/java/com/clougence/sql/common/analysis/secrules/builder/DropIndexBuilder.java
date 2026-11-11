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
package com.clougence.sql.common.analysis.secrules.builder;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.service.secrules.RuleQueryType;
import com.clougence.clouddm.sdk.service.secrules.SecQueryKind;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbIndexDomain;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.secrules.builder.enums.Attribute;
import com.clougence.sql.common.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.sql.common.analysis.secrules.builder.enums.DomainSource;
import com.clougence.sql.common.analysis.secrules.builder.enums.NameType;
import com.clougence.sql.common.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.sql.common.analysis.secrules.builder.utils.BuilderUtil;

public class DropIndexBuilder extends AbstractDomainBuilder {

    RdbIndexDomain  indexDomain = new RdbIndexDomain();

    private boolean ifExists;

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
        indexDomain.setSqlType(RuleQueryType.DROP_INDEX);
        indexDomain.setAuditKind(SecQueryKind.DROP);
        indexDomain.setIfExists(this.ifExists);
        return Collections.singletonList(indexDomain);
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain domain = (ObjNameDomain) list.get(0);
            if (domain.getType() == NameType.INDEX) {
                List<String> names = domain.getNameList();
                int size = names.size();
                if (size > 3) {
                    throw new UnsupportedOperationException("unsupported index name, at most catalog.schema.index but got " + names);
                }
                if (size >= 3) {
                    indexDomain.setCatalog(names.get(size - 3));
                    indexDomain.setSchema(names.get(size - 2));
                } else if (size == 2) {
                    indexDomain.setSchema(names.get(0));
                }
                indexDomain.setName(names.get(size - 1));
            } else {
                Map<UmiTypes, String> map = BuilderUtil.parseTableName(domain.getNameList());
                indexDomain.setTableSchema(map.get(UmiTypes.Schema));
                indexDomain.setTableName(map.get(UmiTypes.Table));
            }
        } else {
            super.handleSubDomain(list, source);
        }
    }

}
