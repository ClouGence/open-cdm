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
package com.clougence.clouddm.ds.oracle.analysis.builder;

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.ds.oracle.analysis.secrules.OraColumnDomain;
import com.clougence.clouddm.ds.oracle.analysis.secrules.OraTableDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.RenameBuilder;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbTableDomain;
import com.clougence.clouddm.sdk.model.analysis.TargetType;
import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.security.auth.SecQueryType;

public class OraRenameBuilder extends RenameBuilder {

    public OraRenameBuilder(TargetType targetType){
        super(targetType);
    }

    @Override
    protected RdbColumnDomain getColumnDomain() { return new OraColumnDomain(); }

    @Override
    protected RdbTableDomain getTableDomain() { return new OraTableDomain(); }

    @Override
    public List<Domain> build() {
        if (targetType == TargetType.Column) {
            RdbColumnDomain rdbColumnDomain = getColumnDomain();
            rdbColumnDomain.setSqlType(SecQueryType.ALTER_TABLE_RENAME_COLUMN);
            rdbColumnDomain.setAuditKind(SecQueryKind.ALTER);
            int size = nameList.size();
            switch (size) {
                case 4: {
                    rdbColumnDomain.setSchema(nameList.get(size - 4));
                }
                default: {
                    rdbColumnDomain.setTable(nameList.get(size - 3));
                    rdbColumnDomain.setColumn(nameList.get(size - 2));
                    rdbColumnDomain.setNewName(nameList.get(size - 1));
                }
            }
            return Collections.singletonList(rdbColumnDomain);
        } else if (targetType == TargetType.Table) {
            OraTableDomain rdbColumnDomain = new OraTableDomain();
            rdbColumnDomain.setSqlType(SecQueryType.ALTER_TABLE_RENAME);
            rdbColumnDomain.setAuditKind(SecQueryKind.ALTER);
            int size = nameList.size();
            switch (size) {
                case 3: {
                    rdbColumnDomain.setSchema(nameList.get(size - 3));
                }
                default: {
                    rdbColumnDomain.setTable(nameList.get(size - 2));
                    rdbColumnDomain.setNewName(nameList.get(size - 1));
                }
            }
            return Collections.singletonList(rdbColumnDomain);
        }

        throw new UnsupportedOperationException(targetType.name());
    }
}
