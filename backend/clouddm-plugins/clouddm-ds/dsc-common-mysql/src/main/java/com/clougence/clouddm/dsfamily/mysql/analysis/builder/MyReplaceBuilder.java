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
package com.clougence.clouddm.dsfamily.mysql.analysis.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.AbstractDomainBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.*;
import com.clougence.clouddm.dsfamily.mysql.analysis.secrules.MyReplaceDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.schema.umi.struts.UmiTypes;

public class MyReplaceBuilder extends AbstractDomainBuilder {

    private List<String>      nameList     = new ArrayList<>();
    protected MyReplaceDomain insertDomain = new MyReplaceDomain();


    @Override
    public List<Domain> build() {
        insertDomain.setAuditKind(SecQueryKind.DML);
        insertDomain.setSqlType(SecQueryType.INSERT);

        Map<UmiTypes, String> map = BuilderUtil.parseTableName(nameList);
        insertDomain.setCatalog(map.get(UmiTypes.Catalog));
        insertDomain.setSchema(map.get(UmiTypes.Schema));
        insertDomain.setTable(map.get(UmiTypes.Table));


        return Collections.singletonList(this.insertDomain);
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource type) {
        if (type == DomainSource.OBJ_NAME) {
            Domain domain = list.get(0);
            ObjNameDomain objNameDomain = (ObjNameDomain) domain;
            this.nameList = objNameDomain.getNameList();
        } else if (type == DomainSource.SELECT) {
            for (Domain ruleDomain : list) {
                if (ruleDomain instanceof RdbSelectDomain) {
                    RdbSelectDomain rdbSelectDomain = (RdbSelectDomain) ruleDomain;
                    insertDomain.addChild(rdbSelectDomain);
                    RdbSelectDomain selectDomain = (RdbSelectDomain) ruleDomain;
                    selectDomain.setMode(RdbQueryMode.INSERT);
                    insertDomain.setFromSelect(true);
                }
            }

        } else if (type == DomainSource.VALUES) {
            for (Domain ruleDomain : list) {
                if (ruleDomain instanceof RdbSelectDomain) {
                    RdbSelectDomain selectDomain = (RdbSelectDomain) ruleDomain;
                    selectDomain.setMode(RdbQueryMode.INSERT);
                    insertDomain.addChild(selectDomain);
                    insertDomain.setHasSubQuery(true);
                } else if (ruleDomain instanceof RdbConstantDomain) {
                    RdbConstantDomain rdbConstantDomain = (RdbConstantDomain) ruleDomain;
                    if (rdbConstantDomain.getConstantValue().equals("null")) {
                        insertDomain.setHasNullValue(true);
                    }
                } else if (ruleDomain instanceof RdbCallDomain) {
                    RdbCallDomain rdbCallDomain = (RdbCallDomain) ruleDomain;
                    insertDomain.addChild(rdbCallDomain);
                }
            }
        } else if (type == DomainSource.INSERT_COLUMN) {
            for (Domain ruleDomain : list) {
                RdbConstantDomain rdbConstantDomain = (RdbConstantDomain) ruleDomain;
                insertDomain.getColumns().add(rdbConstantDomain.getConstantValue());
            }
        } else if (type == DomainSource.SET_VALUE) {
            for (Domain domain : list) {
                if (domain instanceof RdbColumnDomain) {
                    RdbColumnDomain rdbColumnDomain = (RdbColumnDomain) domain;
                    insertDomain.getSetColumns().add(rdbColumnDomain.getColumn());
                } else if (domain instanceof RdbSelectDomain) {
                    RdbSelectDomain rdbSelectDomain = (RdbSelectDomain) domain;
                    rdbSelectDomain.setMode(RdbQueryMode.SUB_SET);
                    insertDomain.addChild(rdbSelectDomain);
                    insertDomain.setSelectInSet(true);
                } else if (domain instanceof RdbCallDomain) {
                    RdbCallDomain rdbCallDomain = (RdbCallDomain) domain;
                    insertDomain.addChild(rdbCallDomain);
                } else {
                    super.handleSubDomain(list, type);
                }
            }
        } else {
            super.handleSubDomain(list, type);
        }
    }


}
