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
package com.clougence.clouddm.ds.maxcompute.sql.analysis.security.builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.maxcompute.sql.analysis.security.builder.utils.McBuilderUtil;
import com.clougence.clouddm.ds.maxcompute.sql.analysis.security.domain.McColumnDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.service.secrules.RuleQueryType;
import com.clougence.clouddm.sdk.service.secrules.SecQueryKind;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbColumnDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbConstraintDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbTableDomain;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.secrules.builder.AbstractDomainBuilder;
import com.clougence.sql.common.analysis.secrules.builder.enums.DomainSource;
import com.clougence.sql.common.analysis.secrules.builder.mode.ColumnListDomain;
import com.clougence.sql.common.analysis.secrules.builder.mode.ObjNameDomain;

public class McAlterTableBuilder extends AbstractDomainBuilder {

    private Map<UmiTypes, String> map;
    private final List<Domain>    ruleDomains = new ArrayList<>();
    private final boolean         schemaEnables;

    public McAlterTableBuilder(boolean schemaEnables){
        this.schemaEnables = schemaEnables;
    }

    @Override
    public List<Domain> build() {
        String table = map.get(UmiTypes.Table);
        String schema = map.get(UmiTypes.Schema);
        String catalog = map.get(UmiTypes.Catalog);

        for (Domain ruleDomain : ruleDomains) {
            if (ruleDomain instanceof RdbColumnDomain columnDomain) {
                columnDomain.setTable(table);
                columnDomain.setSchema(schema);
                columnDomain.setCatalog(catalog);
            } else if (ruleDomain instanceof RdbConstraintDomain constraintDomain) {
                constraintDomain.setTableSchema(schema);
                constraintDomain.setTableName(table);
                constraintDomain.setTableCatalog(catalog);
            } else if (ruleDomain instanceof RdbTableDomain indexDomain) {
                indexDomain.setTable(table);
                indexDomain.setSchema(schema);
                indexDomain.setCatalog(catalog);
            }
        }
        return new ArrayList<>(ruleDomains);
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            Domain domain = list.get(0);
            ObjNameDomain objNameDomain = (ObjNameDomain) domain;
            this.map = McBuilderUtil.parseTableName(objNameDomain.getNameList(), schemaEnables);
        } else if (source == DomainSource.ALTER_TABLE_ITEM) {
            this.ruleDomains.addAll(list);
        } else if (source == DomainSource.COLUMN_DEF) {
            for (Domain domain : list) {
                RdbColumnDomain domainColumn = (RdbColumnDomain) domain;
                domainColumn.setAuditKind(SecQueryKind.CREATE);
                domainColumn.setSqlType(RuleQueryType.ADD_COLUMN);
            }
            this.ruleDomains.addAll(list);
        } else if (source == DomainSource.COLUMN_LIST) {
            ColumnListDomain domain = (ColumnListDomain) list.get(0);
            for (String column : domain.getColumns()) {
                McColumnDomain rdbColumnDomain = new McColumnDomain();
                rdbColumnDomain.setAuditKind(SecQueryKind.DROP);
                rdbColumnDomain.setSqlType(RuleQueryType.DROP_COLUMN);
                rdbColumnDomain.setColumn(column);
                this.ruleDomains.add(rdbColumnDomain);
            }
        } else {
            super.handleSubDomain(list, source);
        }
    }

}
