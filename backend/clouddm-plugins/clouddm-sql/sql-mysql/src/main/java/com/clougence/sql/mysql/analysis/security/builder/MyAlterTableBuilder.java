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
import java.util.Map;

import com.clougence.clouddm.sdk.security.auth.SecQueryKind;
import com.clougence.clouddm.sdk.service.secrules.Domain;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbColumnDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbConstraintDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbIndexDomain;
import com.clougence.clouddm.sdk.sql.analysis.security.rdb.RdbTableDomain;
import com.clougence.clouddm.sdk.sql.parser.SplitQueryType;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.sql.common.analysis.secrules.builder.AbstractDomainBuilder;
import com.clougence.sql.common.analysis.secrules.builder.enums.Attribute;
import com.clougence.sql.common.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.sql.common.analysis.secrules.builder.enums.DomainSource;
import com.clougence.sql.common.analysis.secrules.builder.enums.NameType;
import com.clougence.sql.common.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.sql.common.analysis.secrules.builder.utils.BuilderUtil;
import com.clougence.sql.mysql.analysis.security.builder.enums.MyAttribute;
import com.clougence.sql.mysql.analysis.security.domain.MyTableDomain;

public class MyAlterTableBuilder extends AbstractDomainBuilder {

    private String              newSchemaName;
    private String              newTableName;
    private final MyTableDomain myTableDomain = new MyTableDomain();
    private final List<Domain>  ruleDomains   = new ArrayList<>();

    private boolean             alterTable;

    @Override
    public List<Domain> build() {
        List<Domain> domains = new ArrayList<>();
        if (alterTable) {
            domains.add(myTableDomain);
        }

        if (newTableName != null) {
            MyTableDomain myTableDomain1 = new MyTableDomain();
            myTableDomain1.addSqlType(SplitQueryType.RENAME_TABLE);
            myTableDomain1.setAuditKind(SecQueryKind.ALTER);
            myTableDomain1.setTable(myTableDomain.getTable());
            myTableDomain1.setSchema(myTableDomain.getSchema());
            myTableDomain1.setNewName(newTableName);
            if (newSchemaName == null) {
                newSchemaName = myTableDomain.getSchema();
            }
            myTableDomain1.setNewSchemaName(newSchemaName);
            domains.add(myTableDomain1);
        }
        myTableDomain.addSqlType(SplitQueryType.ALTER_TABLE);
        myTableDomain.setAuditKind(SecQueryKind.ALTER);

        for (Domain ruleDomain : ruleDomains) {
            if (ruleDomain instanceof RdbColumnDomain columnDomain) {
                columnDomain.setTable(myTableDomain.getTable());
                columnDomain.setSchema(myTableDomain.getSchema());
            } else if (ruleDomain instanceof RdbConstraintDomain constraintDomain) {
                constraintDomain.setTableSchema(myTableDomain.getSchema());
                constraintDomain.setTableName(myTableDomain.getTable());
            } else if (ruleDomain instanceof RdbIndexDomain indexDomain) {
                indexDomain.setTableName(myTableDomain.getTable());
                indexDomain.setTableSchema(myTableDomain.getSchema());
            } else if (ruleDomain instanceof RdbTableDomain tableDomain) {
                tableDomain.setTable(myTableDomain.getTable());
                tableDomain.setSchema(myTableDomain.getSchema());
                tableDomain.setAuditKind(SecQueryKind.ALTER);
                tableDomain.addSqlType(SplitQueryType.ALTER_TABLE);
            }
        }
        domains.addAll(ruleDomains);
        return domains;
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource source) {
        if (source == DomainSource.OBJ_NAME) {
            Domain domain = list.get(0);
            ObjNameDomain objNameDomain = (ObjNameDomain) domain;
            if (objNameDomain.getType() == NameType.NEW_TABLE) {
                Map<UmiTypes, String> map = BuilderUtil.parseTableName(objNameDomain.getNameList());
                newSchemaName = map.get(UmiTypes.Schema);
                newTableName = map.get(UmiTypes.Table);
                myTableDomain.setNewName(newTableName);
            } else {
                Map<UmiTypes, String> map = BuilderUtil.parseTableName(objNameDomain.getNameList());
                myTableDomain.setSchema(map.get(UmiTypes.Schema));
                myTableDomain.setTable(map.get(UmiTypes.Table));

            }
        } else if (source == DomainSource.ALTER_TABLE_ITEM) {
            this.ruleDomains.addAll(list);
        } else {
            super.handleSubDomain(list, source);
        }
    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.COMMENT) {
            myTableDomain.setComment((String) value);
            alterTable = true;
        } else if (attr == MyAttribute.ENGINE) {
            myTableDomain.setEngine((String) value);
            alterTable = true;
        } else if (attr == MyAttribute.CHARACTER_SET) {
            myTableDomain.setCharacterSet((String) value);
            alterTable = true;
        } else if (attr == MyAttribute.COLLATE) {
            myTableDomain.setCollate((String) value);
            alterTable = true;
        } else {
            super.addAttr(attr, value);
        }
    }
}
