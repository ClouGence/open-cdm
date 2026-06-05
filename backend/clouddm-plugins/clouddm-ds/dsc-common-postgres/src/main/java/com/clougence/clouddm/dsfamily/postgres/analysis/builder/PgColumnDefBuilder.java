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
package com.clougence.clouddm.dsfamily.postgres.analysis.builder;

import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.ColumnDefBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.postgres.analysis.builder.mode.PgColumnTypeDomain;
import com.clougence.clouddm.dsfamily.postgres.analysis.secrules.PgColumnDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class PgColumnDefBuilder extends ColumnDefBuilder<PgColumnDomain> {

    @Override
    protected PgColumnDomain getColumnDomain() {
        PgColumnDomain pgColumnDomain = new PgColumnDomain();
        pgColumnDomain.setNullable(true);
        return pgColumnDomain;
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource type) {
        if (type == DomainSource.COLUMN_TYPE) {
            PgColumnTypeDomain columnTypeDomain = (PgColumnTypeDomain) list.get(0);
            this.columnDomain.setTypeDesc(columnTypeDomain.getFullName());
            this.columnDomain.setTypeName(columnTypeDomain.getType());
            this.columnDomain.setLength(columnTypeDomain.getLength());
            this.columnDomain.setArray(columnTypeDomain.isArray());
        }
        super.handleSubDomain(list, type);
    }
}
