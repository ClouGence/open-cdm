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

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.service.secrules.RuleQueryType;
import com.clougence.clouddm.sdk.service.secrules.SecQueryKind;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbTableDomain;
import com.clougence.sql.common.analysis.secrules.builder.enums.DomainSource;

public class FromDomainBuilder extends AbstractDomainBuilder {

    protected List<Domain> ruleDomains = new ArrayList<>();

    @Override
    public List<Domain> build() {
        return ruleDomains;
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.TABLE) {
            for (Domain ruleDomain : list) {
                if (ruleDomain instanceof RdbTableDomain rdbTableDomain) {
                    rdbTableDomain.setAuditKind(SecQueryKind.QUERY);
                    rdbTableDomain.setSqlType(RuleQueryType.SELECT);
                }
            }
            this.ruleDomains.addAll(list);
        } else if (source == DomainSource.JOIN) {
            this.ruleDomains.addAll(list);
        } else {
            super.handleSubDomain(list, source);
        }

    }

}
