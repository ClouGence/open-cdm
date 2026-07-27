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
package com.clougence.clouddm.ds.starrocks.sql.analysis.security.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.starrocks.sql.analysis.security.domain.SrTableDomain;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbColumnDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbConstraintDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbIndexDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbTableDomain;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.secrules.builder.AbstractDomainBuilder;
import com.clougence.sql.common.analysis.secrules.builder.enums.DomainSource;
import com.clougence.sql.common.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.sql.common.analysis.secrules.builder.mode.OptionsDomain;
import com.clougence.sql.common.analysis.secrules.builder.utils.BuilderUtil;

public class SrAlterTableBuilder extends AbstractDomainBuilder {

    private List<String>   nameList;
    protected List<Domain> ruleDomains = new ArrayList<>();

    @Override
    public List<Domain> build() {
        Map<UmiTypes, String> map = BuilderUtil.parseTableName(nameList);
        String catalog = map.get(UmiTypes.Catalog);
        String schema = map.get(UmiTypes.Schema);
        String table = map.get(UmiTypes.Table);

        if (ruleDomains.isEmpty()) {
            SrTableDomain srTableDomain = new SrTableDomain();
            srTableDomain.setCatalog(catalog);
            srTableDomain.setSchema(schema);
            srTableDomain.setTable(table);
            srTableDomain.addSqlType(SplitQueryType.ALTER_TABLE);
            srTableDomain.setAuditKind(SecQueryKind.ALTER);
            ruleDomains.add(srTableDomain);
        }

        for (Domain ruleDomain : ruleDomains) {
            if (ruleDomain instanceof RdbColumnDomain columnDomain) {
                columnDomain.setTable(table);
                columnDomain.setSchema(schema);
                columnDomain.setCatalog(catalog);
            } else if (ruleDomain instanceof RdbConstraintDomain constraintDomain) {
                constraintDomain.setTableCatalog(catalog);
                constraintDomain.setTableSchema(schema);
                constraintDomain.setTableName(table);
            } else if (ruleDomain instanceof RdbTableDomain tableDomain) {
                tableDomain.setTable(table);
                tableDomain.setSchema(schema);
                tableDomain.setCatalog(catalog);
            } else if (ruleDomain instanceof RdbIndexDomain indexDomain) {
                indexDomain.setTableCatalog(catalog);
                indexDomain.setTableName(table);
                indexDomain.setTableSchema(schema);
            }
        }
        return ruleDomains;
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            Domain domain = list.get(0);
            ObjNameDomain objNameDomain = (ObjNameDomain) domain;
            this.nameList = objNameDomain.getNameList();
        } else if (source == DomainSource.ALTER_TABLE_ITEM) {
            this.ruleDomains.addAll(list);
        } else if (source == DomainSource.OPTIONS) {
            SrTableDomain srTableDomain = new SrTableDomain();
            srTableDomain.setAuditKind(SecQueryKind.ALTER);
            srTableDomain.addSqlType(SplitQueryType.ALTER_TABLE);
            OptionsDomain optionsDomain = (OptionsDomain) list.get(0);
            srTableDomain.setOptions(optionsDomain.getOptions());
            this.ruleDomains.add(srTableDomain);
        } else {
            super.handleSubDomain(list, source);
        }
    }
}
