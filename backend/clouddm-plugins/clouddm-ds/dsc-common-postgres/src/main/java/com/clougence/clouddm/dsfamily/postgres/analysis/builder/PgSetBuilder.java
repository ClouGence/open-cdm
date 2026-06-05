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
package com.clougence.clouddm.dsfamily.postgres.analysis.builder;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.AbstractDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstantDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.builder.mode.OptDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class PgSetBuilder extends AbstractDomainBuilder {

    private String key;
    private String value;

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.CONSTANT) {
            value = ((RdbConstantDomain) list.get(0)).getConstantValue();
            if (value.startsWith("'")) {
                value = value.substring(1, value.length() - 1);
            }
        } else {
            super.handleSubDomain(list, source);
        }
    }

    @Override
    public List<Domain> build() {
        return Collections.singletonList(new OptDomain(key, value));
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.DEFAULT) {
            this.value = "default";
        } else if (attr == CommonAttribute.VALUE) {
            this.key = (String) value;
        }
    }
}
