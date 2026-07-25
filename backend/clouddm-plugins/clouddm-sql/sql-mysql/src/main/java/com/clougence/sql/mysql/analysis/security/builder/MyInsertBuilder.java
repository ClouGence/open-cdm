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
package com.clougence.sql.mysql.analysis.security.builder;

import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryType;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbInsertConflictStrategy;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbInsertDomain;
import com.clougence.sql.common.analysis.secrules.builder.InsertBuilder;
import com.clougence.sql.common.analysis.secrules.builder.enums.Attribute;
import com.clougence.sql.common.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.sql.mysql.analysis.security.domain.MyInsertDomain;

public class MyInsertBuilder extends InsertBuilder {

    @Override
    public List<Domain> build() {
        List<Domain> domains = super.build();
        if (insertDomain.getConflict() == RdbInsertConflictStrategy.UPDATE) {
            insertDomain.setSqlType(SecQueryType.MERGE);
        }
        return domains;
    }

    @Override
    protected RdbInsertDomain getInsertDomain() {
        MyInsertDomain myInsertDomain = new MyInsertDomain();
        myInsertDomain.setColumns(new ArrayList<>());
        myInsertDomain.setConflict(RdbInsertConflictStrategy.NONE);
        return myInsertDomain;
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.IGNORE) {
            insertDomain.setConflict(RdbInsertConflictStrategy.IGNORE);
        } else {
            super.addAttr(attr, value);
        }
    }
}
