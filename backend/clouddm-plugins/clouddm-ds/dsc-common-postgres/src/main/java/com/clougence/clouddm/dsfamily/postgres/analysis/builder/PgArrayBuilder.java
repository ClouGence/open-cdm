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

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.AbstractDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class PgArrayBuilder extends AbstractDomainBuilder {

    private List<Domain> ruleDomains = new ArrayList<>();

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.COLUMN_TYPE) {
            return;
        }
        if (source != DomainSource.CONSTANT && source != DomainSource.SELECT && source != DomainSource.FUNCTION && source != DomainSource.ARRAY && source != DomainSource.COLUMN) {
            super.handleSubDomain(list, source);
        }
        ruleDomains.addAll(list);
    }

    @Override
    public List<Domain> build() {
        return this.ruleDomains;
    }
}
