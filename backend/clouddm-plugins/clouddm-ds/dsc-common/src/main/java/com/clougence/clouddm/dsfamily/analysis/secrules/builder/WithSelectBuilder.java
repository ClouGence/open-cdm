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

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WithSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSelectDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class WithSelectBuilder implements DomainBuilder {

    private WithSelectDomain domain = new WithSelectDomain();

    @Override
    public List<Domain> build() {
        domain.getSelectDomain().setMode(RdbQueryMode.WITH_BODY);
        domain.getSelectDomain().setSimpleSelect(false);
        return Collections.singletonList(domain);
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source != DomainSource.SELECT) {
            throw new UnsupportedOperationException();
        }
        this.domain.setSelectDomain((RdbSelectDomain) list.get(0));
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.VALUE) {
            this.domain.setName((String) value);
        }
    }

}
