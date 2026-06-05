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

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbFunctionDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CreateFunctionBuilder extends AbstractDomainBuilder {

    private RdbFunctionDomain rdbFunctionDomain = new RdbFunctionDomain();

    @Override
    public List<Domain> build() {
        rdbFunctionDomain.setSqlType(SecQueryType.CREATE_FUNCTION);
        rdbFunctionDomain.setAuditKind(SecQueryKind.CREATE);

        return Collections.singletonList(rdbFunctionDomain);
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain objNameDomain = (ObjNameDomain) list.get(0);
            Map<UmiTypes, String> map = BuilderUtil.parseFunctionName(objNameDomain.getNameList());
            rdbFunctionDomain.setCatalog(map.get(UmiTypes.Catalog));
            rdbFunctionDomain.setSchema(map.get(UmiTypes.Schema));
            rdbFunctionDomain.setName(map.get(UmiTypes.Function));
        } else {
            super.handleSubDomain(list, source);
        }
    }
}
