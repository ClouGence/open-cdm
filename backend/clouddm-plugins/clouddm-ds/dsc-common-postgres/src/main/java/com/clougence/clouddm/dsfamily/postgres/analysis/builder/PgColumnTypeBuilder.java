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

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.ColumnTypeBuilder;
import com.clougence.clouddm.dsfamily.postgres.analysis.builder.enums.PgAttribute;
import com.clougence.clouddm.dsfamily.postgres.analysis.builder.mode.PgColumnTypeDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class PgColumnTypeBuilder extends ColumnTypeBuilder {

    int array = 0;

    public PgColumnTypeBuilder(String fullName){
        super(fullName);
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == PgAttribute.ARRAY_TYPE) {
            array++;
        } else {
            super.addAttr(attr, value);
        }
    }

    @Override
    public List<Domain> build() {
        return Collections.singletonList(new PgColumnTypeDomain(typeName, fullName, params.isEmpty() ? null : params.get(0), array != 0));
    }
}
