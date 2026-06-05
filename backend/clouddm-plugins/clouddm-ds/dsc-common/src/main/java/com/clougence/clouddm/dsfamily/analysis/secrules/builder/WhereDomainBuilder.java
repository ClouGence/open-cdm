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
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WhereDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstantDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class WhereDomainBuilder extends AbstractDomainBuilder {

    private WhereDomain whereDomain = new WhereDomain();

    @Override
    public List<Domain> build() {
        // such as 2>1 ...
        if (allConstant()) {
            whereDomain.setValidWhere(false);
        }
        return Collections.singletonList(whereDomain);
    }

    private boolean allConstant() {
        for (Domain domain : whereDomain.getDomains()) {
            if (domain instanceof RdbConstantDomain) {
                continue;
            } else {
                return false;
            }
        }
        return !whereDomain.getDomains().isEmpty();
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource status) {
        // for in
        if (status == DomainSource.VALUES) {
            return;
        }
        if (status != DomainSource.COLUMN && status != DomainSource.CONSTANT && status != DomainSource.FUNCTION && status != DomainSource.SELECT) {
            super.handleSubDomain(list, status);
        }

        this.whereDomain.getDomains().addAll(list);
    }

    public void setValid(boolean valid) {
        this.whereDomain.setValidWhere(valid);
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.VALID_WHERE) {
            this.whereDomain.setValidWhere((Boolean) value);
        }
    }
}
