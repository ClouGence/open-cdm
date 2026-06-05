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

import java.util.*;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WhereDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WithSelectDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.*;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.schema.umi.struts.UmiTypes;

public class UpdateBuilder extends AbstractDomainBuilder {

    protected List<String>                  nameList;
    protected RdbUpdateDomain               updateDomain = getRdbUpdateDomain();
    protected Stack<List<WithSelectDomain>> selectStack;

    public UpdateBuilder(Stack<List<WithSelectDomain>> selectStack){
        this.selectStack = selectStack;
    }

    protected RdbUpdateDomain getRdbUpdateDomain() { return new RdbUpdateDomain(); }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            ObjNameDomain objNameDomain = (ObjNameDomain) list.get(0);
            nameList = objNameDomain.getNameList();
        } else if (source == DomainSource.SET_VALUE) {
            for (Domain domain : list) {
                if (domain instanceof RdbColumnDomain) {
                    // todo need modify
                    RdbColumnDomain rdbColumnDomain = (RdbColumnDomain) domain;
                    updateDomain.getSetColumns().add(rdbColumnDomain.getColumn());
                } else if (domain instanceof RdbSelectDomain) {
                    RdbSelectDomain rdbSelectDomain = (RdbSelectDomain) domain;
                    rdbSelectDomain.setMode(RdbQueryMode.SUB_SET);
                    updateDomain.addChild(rdbSelectDomain);
                    updateDomain.setSelectInSet(true);
                } else if (domain instanceof RdbCallDomain) {
                    RdbCallDomain rdbCallDomain = (RdbCallDomain) domain;
                    updateDomain.addChild(rdbCallDomain);
                } else {
                    super.handleSubDomain(list, source);
                }
            }
        } else if (source == DomainSource.UPDATE_COLUMN) {
            for (Domain domain : list) {
                updateDomain.getSetColumns().add(((RdbColumnDomain) domain).getColumn());
            }
        } else if (source == DomainSource.WHERE) {
            WhereDomain whereDomain = (WhereDomain) list.get(0);
            if (!whereDomain.isValidWhere()) {
                return;
            }
            for (Domain domain : whereDomain.getDomains()) {
                if (domain instanceof RdbColumnDomain) {
                    RdbColumnDomain rdbColumnDomain = (RdbColumnDomain) domain;
                    updateDomain.addWhereColumn(rdbColumnDomain.getColumn());
                } else if (domain instanceof RdbSelectDomain) {
                    RdbSelectDomain rdbSelectDomain = (RdbSelectDomain) domain;
                    rdbSelectDomain.setMode(RdbQueryMode.SUB_WHERE);
                    updateDomain.addChild(rdbSelectDomain);
                    updateDomain.setSelectInWhere(true);
                } else if (domain instanceof RdbCallDomain) {
                    RdbCallDomain rdbCallDomain = (RdbCallDomain) domain;
                    updateDomain.addChild(rdbCallDomain);
                }
            }
        } else {
            super.handleSubDomain(list, source);
        }
    }

    @Override
    public List<Domain> build() {
        if (!this.selectStack.peek().isEmpty()) {
            this.updateDomain.setHasWith(true);
        }
        Map<UmiTypes, String> map = BuilderUtil.parseTableName(nameList);
        this.updateDomain.setTable(map.get(UmiTypes.Table));
        this.updateDomain.setSchema(map.get(UmiTypes.Schema));
        this.updateDomain.setCatalog(map.get(UmiTypes.Catalog));
        updateDomain.setAuditKind(SecQueryKind.DML);
        updateDomain.setSqlType(SecQueryType.UPDATE);
        if (updateDomain.getWhereColumns() == null) {
            updateDomain.setWhereColumns(new ArrayList<>());
        }
        return Collections.singletonList(this.updateDomain);
    }
}
