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
package com.clougence.clouddm.ds.starrocks.analysis.builder;

import java.util.List;

import com.clougence.clouddm.ds.starrocks.analysis.secrules.SrTableDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.DropTableBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class SrDropTableBuilder extends DropTableBuilder<SrTableDomain> {

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
        for (Domain tableDomain : this.tableDomains) {
            SrTableDomain tableDomain1 = (SrTableDomain) tableDomain;
            tableDomain1.setIfExists(this.ifExists);
        }
        return this.tableDomains;
    }

    @Override
    protected SrTableDomain getTableDomain() { return new SrTableDomain(); }
}
