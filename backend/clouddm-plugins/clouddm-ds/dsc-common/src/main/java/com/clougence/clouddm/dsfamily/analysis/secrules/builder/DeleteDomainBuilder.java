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
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.WhereDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.*;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.schema.umi.struts.UmiTypes;

public class DeleteDomainBuilder extends AbstractDomainBuilder {

    private List<String>      nameList        = new ArrayList<>();
    protected RdbDeleteDomain rdbDeleteDomain = getDeleteDomain();

    @Override
    public List<Domain> build() {
        rdbDeleteDomain.setAuditKind(SecQueryKind.DML);
        rdbDeleteDomain.setSqlType(SecQueryType.DELETE);
        if (rdbDeleteDomain.getWhereColumns() == null) {
            rdbDeleteDomain.setWhereColumns(new ArrayList<>());
        }
        rdbDeleteDomain.setJoinType(RdbJoinType.NONE);

        Map<UmiTypes, String> map = BuilderUtil.parseTableName(nameList);
        rdbDeleteDomain.setTable(map.get(UmiTypes.Table));
        rdbDeleteDomain.setSchema(map.get(UmiTypes.Schema));
        rdbDeleteDomain.setCatalog(map.get(UmiTypes.Catalog));

        return Collections.singletonList(rdbDeleteDomain);
    }

    protected RdbDeleteDomain getDeleteDomain() { return new RdbDeleteDomain(); }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource type) {
        if (type == DomainSource.OBJ_NAME) {
            Domain domain = list.get(0);
            ObjNameDomain objNameDomain = (ObjNameDomain) domain;
            this.nameList = objNameDomain.getNameList();
        } else if (type == DomainSource.WHERE) {
            WhereDomain whereDomain = (WhereDomain) list.get(0);
            if (!whereDomain.isValidWhere()) {
                return;
            }
            for (Domain domain : whereDomain.getDomains()) {
                if (domain instanceof RdbColumnDomain) {
                    RdbColumnDomain rdbColumnDomain = (RdbColumnDomain) domain;
                    rdbDeleteDomain.addWhereColumn(rdbColumnDomain.getColumn());
                } else if (domain instanceof RdbSelectDomain) {
                    RdbSelectDomain rdbSelectDomain = (RdbSelectDomain) domain;
                    rdbSelectDomain.setMode(RdbQueryMode.SUB_WHERE);
                    rdbDeleteDomain.addChild(rdbSelectDomain);
                    rdbDeleteDomain.setSelectInWhere(true);
                }
            }
        } else if (type == DomainSource.SELECT) {
            for (Domain domain : list) {
                RdbSelectDomain rdbSelectDomain = (RdbSelectDomain) domain;
                rdbSelectDomain.setMode(RdbQueryMode.SUB_WHERE);
                rdbDeleteDomain.addChild(rdbSelectDomain);
                rdbDeleteDomain.setSelectInWhere(true);
            }
        } else if (type == DomainSource.COLUMN) {
            for (Domain domain : list) {
                RdbColumnDomain rdbColumnDomain = (RdbColumnDomain) domain;
                rdbDeleteDomain.addWhereColumn(rdbColumnDomain.getColumn());
            }
        } else {
            super.handleSubDomain(list, type);
        }
    }
}
