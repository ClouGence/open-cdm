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

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbColumnDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbConstraintDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbIndexDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.SqlConstraintType;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.common.analysis.secrules.builder.enums.AlterTableType;
import com.clougence.sql.common.analysis.secrules.builder.enums.DomainSource;

public class AlterTableItemBuilder extends AbstractDomainBuilder {

    private final AlterTableType alterTableType;
    private final List<Domain>   domainList = new ArrayList<>();

    public AlterTableItemBuilder(AlterTableType type){
        this.alterTableType = type;
    }

    @Override
    public List<Domain> build() {
        return domainList;
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.COLUMN_DEF) {
            for (Domain ruleDomain : list) {
                RdbColumnDomain rdbColumnDomain = (RdbColumnDomain) ruleDomain;
                if (alterTableType == AlterTableType.ADD_COLUMN) {
                    rdbColumnDomain.setAuditKind(SecQueryKind.CREATE);
                    rdbColumnDomain.addSqlType(SplitQueryType.ADD_COLUMN);
                } else if (alterTableType == AlterTableType.DROP_COLUMN) {
                    rdbColumnDomain.setAuditKind(SecQueryKind.DROP);
                    rdbColumnDomain.addSqlType(SplitQueryType.DROP_COLUMN);
                } else if (alterTableType == AlterTableType.ALTER_COLUMN) {
                    rdbColumnDomain.setAuditKind(SecQueryKind.ALTER);
                    rdbColumnDomain.addSqlType(SplitQueryType.ALTER_COLUMN);
                }
            }
            domainList.addAll(list);
        } else if (source == DomainSource.CONSTRAINT) {
            for (Domain ruleDomain : list) {
                if (alterTableType == AlterTableType.DROP_CONSTRAINT) {
                    RdbConstraintDomain rdbConstraintDomain = (RdbConstraintDomain) ruleDomain;
                    rdbConstraintDomain.addSqlType(SplitQueryType.DROP_CONSTRAINT);
                    rdbConstraintDomain.setAuditKind(SecQueryKind.DROP);
                    if (rdbConstraintDomain.getType() == null) {
                        rdbConstraintDomain.setType(SqlConstraintType.ByName);
                    }
                } else {
                    RdbConstraintDomain rdbConstraintDomain = (RdbConstraintDomain) ruleDomain;
                    rdbConstraintDomain.addSqlType(SplitQueryType.ADD_CONSTRAINT);
                    rdbConstraintDomain.setAuditKind(SecQueryKind.CREATE);
                }
            }
            domainList.addAll(list);
        } else if (source == DomainSource.INDEX) {
            for (Domain domain : list) {
                if (alterTableType == AlterTableType.ADD_INDEX) {
                    RdbIndexDomain domain1 = (RdbIndexDomain) domain;
                    domain1.setAuditKind(SecQueryKind.CREATE);
                    domain1.addSqlType(SplitQueryType.ADD_INDEX);
                } else if (alterTableType == AlterTableType.DROP_INDEX) {
                    RdbIndexDomain domain1 = (RdbIndexDomain) domain;
                    domain1.setAuditKind(SecQueryKind.DROP);
                    domain1.addSqlType(SplitQueryType.DROP_INDEX);
                }
            }
            domainList.addAll(list);
        } else {
            super.handleSubDomain(domainList, source);
        }
    }
}
