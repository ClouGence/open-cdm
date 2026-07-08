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
package com.clougence.clouddm.ds.clickhouse.sql.builder;

import java.util.List;
import java.util.Map;

import com.clougence.clouddm.ds.clickhouse.sql.security.domain.ChDeleteDomain;
import com.clougence.clouddm.ds.clickhouse.sql.security.domain.ChUpdateDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.sql.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.sdk.sql.secrules.rdb.RdbConstraintDomain;
import com.clougence.clouddm.sdk.sql.secrules.rdb.RdbIndexDomain;
import com.clougence.clouddm.sdk.sql.secrules.rdb.RdbTableDomain;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.secrules.builder.AlterTableBuilder;
import com.clougence.sql.common.analysis.secrules.builder.enums.DomainSource;
import com.clougence.sql.common.analysis.secrules.builder.utils.BuilderUtil;

public class ChAlterTableBuilder extends AlterTableBuilder {

    @Override
    public List<Domain> build() {

        Map<UmiTypes, String> map = BuilderUtil.parseTableName(nameList);
        String catalog = map.get(UmiTypes.Catalog);
        String schema = map.get(UmiTypes.Schema);
        String table = map.get(UmiTypes.Table);

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
            } else if (ruleDomain instanceof ChUpdateDomain updateDomain) {
                updateDomain.setTable(table);
                updateDomain.setSchema(schema);
                updateDomain.setCatalog(catalog);
            } else if (ruleDomain instanceof RdbIndexDomain indexDomain) {
                indexDomain.setTableCatalog(catalog);
                indexDomain.setTableSchema(schema);
                indexDomain.setTableName(table);
            } else if (ruleDomain instanceof ChDeleteDomain deleteDomain) {
                deleteDomain.setTable(table);
                deleteDomain.setSchema(schema);
                deleteDomain.setCatalog(catalog);
            }
        }
        return ruleDomains;
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.UPDATE) {
            this.ruleDomains.addAll(list);
        } else if (source == DomainSource.DELETE) {
            this.ruleDomains.addAll(list);
        } else {
            super.handleSubDomain(list, source);
        }
    }
}
