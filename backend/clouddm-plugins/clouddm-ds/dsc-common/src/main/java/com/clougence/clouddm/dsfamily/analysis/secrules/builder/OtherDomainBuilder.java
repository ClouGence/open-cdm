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
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.service.secrules.ModeDomain;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;

public class OtherDomainBuilder extends AbstractDomainBuilder {

    private List<Domain> domains = new ArrayList<>();

    @Override
    public List<Domain> build() {
        for (Domain domain : domains) {
            RuleDomain ruleDomain = (RuleDomain) domain;
            if (ruleDomain.getAuditKind() == null) {
                ruleDomain.setAuditKind(SecQueryKind.OTHER);
            }
            if (ruleDomain.getSqlType() == null) {
                ruleDomain.setSqlType(SecQueryType.UNKNOWN);
            }
        }
        return this.domains;
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        for (Domain domain : list) {
            if (domain instanceof ModeDomain) {
                super.handleSubDomain(list, source);
            }
        }
        if (source == DomainSource.SELECT || source == DomainSource.FUNCTION) {
            this.domains.addAll(list);
        }
    }
}
