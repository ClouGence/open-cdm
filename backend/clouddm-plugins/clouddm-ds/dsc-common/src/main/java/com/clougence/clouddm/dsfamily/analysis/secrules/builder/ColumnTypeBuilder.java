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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ColumnTypeDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstantDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class ColumnTypeBuilder extends AbstractDomainBuilder {

    protected String       typeName;

    protected String       fullName;

    protected List<String> params = new ArrayList<>();

    public ColumnTypeBuilder(String fullName){
        this.fullName = fullName;
    }

    @Override
    public List<Domain> build() {
        return Collections.singletonList(new ColumnTypeDomain(typeName, fullName, params.isEmpty() ? null : params.get(0)));
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.CONSTANT) {
            RdbConstantDomain constant = (RdbConstantDomain) list.get(0);
            this.params.add(constant.getConstantValue());
        } else if (source == DomainSource.VALUES) {
            RdbConstantDomain rdbConstantDomain = (RdbConstantDomain) list.get(0);
            this.params.add(rdbConstantDomain.getConstantValue());
        } else if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain objNameDomain = (ObjNameDomain) list.get(0);
            this.typeName = objNameDomain.getName();
        } else {
            super.handleSubDomain(list, source);
        }
    }
}
