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

import java.util.List;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbColumnDomain;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.sql.common.analysis.secrules.builder.AlterTableItemBuilder;
import com.clougence.sql.common.analysis.secrules.builder.enums.AlterTableType;
import com.clougence.sql.common.analysis.secrules.builder.enums.Attribute;
import com.clougence.sql.common.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.sql.mysql.analysis.security.domain.MyColumnDomain;

public class MyColumnAlterTableItemBuilder extends AlterTableItemBuilder {

    private String oldColumnName;

    public MyColumnAlterTableItemBuilder(AlterTableType type){
        super(type);
    }

    @Override
    public List<Domain> build() {
        List<Domain> build = super.build();
        Domain domain = build.get(0);
        if (domain instanceof RdbColumnDomain rdbColumn) {
            if (oldColumnName != null && !rdbColumn.getColumn().equals(oldColumnName)) {
                MyColumnDomain rdbColumnDomain = new MyColumnDomain();
                rdbColumnDomain.addSqlType(SplitQueryType.RENAME_COLUMN);
                rdbColumnDomain.setAuditKind(SecQueryKind.ALTER);
                rdbColumnDomain.setColumn(oldColumnName);
                rdbColumnDomain.setNewName(rdbColumn.getColumn());
                build.add(rdbColumnDomain);
                rdbColumn.setColumn(oldColumnName);
            }

        }
        return build;

    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.VALUE) {
            this.oldColumnName = (String) value;
        } else {
            super.addAttr(attr, value);
        }
    }
}
