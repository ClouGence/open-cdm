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
package com.clougence.clouddm.ds.maxcompute.analysis.builder;

import java.util.*;

import com.clougence.clouddm.ds.maxcompute.analysis.builder.utils.McBuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.SelectTableDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WithSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbQueryMode;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbTableDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.schema.umi.struts.UmiTypes;

public class McTableDomainBuilder extends SelectTableDomainBuilder {

    private final boolean schemaEnabled;

    public McTableDomainBuilder(boolean schemaEnabled, Stack<List<WithSelectDomain>> selectStack){
        super(selectStack);
        this.schemaEnabled = schemaEnabled;
    }

    @Override
    protected Map<UmiTypes, String> getNameMap(List<String> names) {
        return McBuilderUtil.parseTableName(names, schemaEnabled);
    }

    @Override
    protected void prepareHandle() {
        if (ruleDomainList.size() == 1 && ruleDomainList.get(0) instanceof RdbTableDomain && this.alias != null) {
            ((RdbTableDomain) ruleDomainList.get(0)).setAlias(this.alias);
            return;
        }

        RdbTableDomain rdbTableDomain = getRdbTableDomain();
        if (ruleDomainList.isEmpty()) {
            if ((schemaEnabled && rdbTableDomain.getSchema() == null) || (!schemaEnabled && rdbTableDomain.getCatalog() == null)) {
                WithSelectDomain domainBy = findDomainBy(rdbTableDomain.getTable());
                if (domainBy != null) {
                    rdbTableDomain.setVirtual(true);
                    RdbSelectDomain selectDomain = domainBy.getSelectDomain();
                    selectDomain.setMode(RdbQueryMode.SUB_FROM);
                    rdbTableDomain.addChild(selectDomain);
                }
            }
            ruleDomainList.add(rdbTableDomain);
        } else {
            List<RuleDomain> ruleDomains = new ArrayList<>();
            for (Domain ruleDomain : ruleDomainList) {
                if (ruleDomain instanceof RdbSelectDomain) {
                    ruleDomains.add((RdbSelectDomain) ruleDomain);
                }
            }
            if (ruleDomains.isEmpty()) {
                if (rdbTableDomain.getSchema() == null) {
                    WithSelectDomain domainBy = findDomainBy(rdbTableDomain.getTable());
                    if (domainBy != null) {
                        rdbTableDomain.setVirtual(true);
                        RdbSelectDomain selectDomain = domainBy.getSelectDomain();
                        selectDomain.setMode(RdbQueryMode.SUB_FROM);
                        rdbTableDomain.addChild(selectDomain);
                    }
                }
                if (rdbTableDomain.getTable() != null) {
                    ruleDomainList.add(0, rdbTableDomain);
                }

                return;
            }
            ruleDomainList.removeAll(ruleDomains);

            rdbTableDomain.setVirtual(true);
            for (Domain domain : ruleDomains) {
                RdbSelectDomain selectDomain = (RdbSelectDomain) domain;
                selectDomain.setMode(RdbQueryMode.SUB_FROM);
                rdbTableDomain.addChild(selectDomain);
            }
            ruleDomainList.add(0, rdbTableDomain);
        }
    }

    @Override
    public List<Domain> build() {
        prepareHandle();
        if (ruleDomainList.isEmpty()) {
            RdbTableDomain rdbTableDomain = getRdbTableDomain();
            if ((schemaEnabled && rdbTableDomain.getSchema() == null) || (!schemaEnabled && rdbTableDomain.getCatalog() == null)) {
                WithSelectDomain domainBy = findDomainBy(rdbTableDomain.getTable());
                if (domainBy != null) {
                    rdbTableDomain.setVirtual(true);
                    rdbTableDomain.addChild(domainBy.getSelectDomain());
                }
            }
            return Collections.singletonList(rdbTableDomain);
        } else {
            return ruleDomainList;
        }
    }

    protected RdbTableDomain getRdbTableDomain() {
        RdbTableDomain rdbTableDomain = new RdbTableDomain();
        Map<UmiTypes, String> nameMap = getNameMap(this.nameList);

        rdbTableDomain.setCatalog(nameMap.get(UmiTypes.Catalog));
        rdbTableDomain.setSchema(nameMap.get(UmiTypes.Schema));
        rdbTableDomain.setTable(nameMap.get(UmiTypes.Table));

        if (rdbTableDomain.getTable() == null) {
            rdbTableDomain.setTable(alias);
        }

        rdbTableDomain.setAlias(alias);
        return rdbTableDomain;
    }
}
