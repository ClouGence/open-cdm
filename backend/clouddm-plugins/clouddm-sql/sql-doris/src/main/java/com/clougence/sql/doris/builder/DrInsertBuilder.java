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
package com.clougence.sql.doris.builder;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.service.secrules.RuleDomain;
import com.clougence.clouddm.sdk.sql.secrules.column.QueryItem;
import com.clougence.clouddm.sdk.sql.secrules.rdb.*;
import com.clougence.sql.common.analysis.secrules.builder.InsertBuilder;
import com.clougence.sql.common.analysis.secrules.builder.enums.DomainSource;
import com.clougence.sql.doris.security.domain.DrInsertDomain;
import com.clougence.sql.doris.security.domain.DrSelectDomain;

public class DrInsertBuilder extends InsertBuilder {

    @Override
    protected RdbInsertDomain getInsertDomain() {
        DrInsertDomain myInsertDomain = new DrInsertDomain();
        myInsertDomain.setColumns(new ArrayList<>());
        myInsertDomain.setConflict(RdbInsertConflictStrategy.NONE);
        return myInsertDomain;
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource type) {
        if (type == DomainSource.SELECT) {
            for (Domain ruleDomain : list) {
                if (ruleDomain instanceof RdbSelectDomain selectDomain) {
                    DrSelectDomain rdbSelectDomain = (DrSelectDomain) ruleDomain;
                    if (rdbSelectDomain.isValuesSelect()) {
                        for (QueryItem column : rdbSelectDomain.getColumns()) {
                            for (RuleDomain columnColumn : column.getColumns()) {
                                if (columnColumn instanceof RdbConstantDomain) {
                                    String constantValue = ((RdbConstantDomain) columnColumn).getConstantValue();
                                    if (constantValue.equalsIgnoreCase("null")) {
                                        insertDomain.setHasNullValue(true);
                                    }
                                } else if (columnColumn instanceof DrSelectDomain drSelectDomain) {
                                    drSelectDomain.setMode(RdbQueryMode.INSERT);
                                    insertDomain.setHasSubQuery(true);
                                    insertDomain.addChild(columnColumn);
                                } else {
                                    insertDomain.addChild(columnColumn);
                                }
                            }
                        }
                    } else {
                        selectDomain.setMode(RdbQueryMode.INSERT);
                        insertDomain.addChild(rdbSelectDomain);
                        insertDomain.setFromSelect(true);
                    }
                }
            }
        } else {
            super.handleSubDomain(list, type);
        }
    }
}
