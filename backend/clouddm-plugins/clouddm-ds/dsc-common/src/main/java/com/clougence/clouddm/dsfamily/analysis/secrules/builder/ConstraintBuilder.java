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
package com.clougence.clouddm.dsfamily.analysis.secrules.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.NameType;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ColumnListDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ConstraintTypeDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstraintDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.SqlConstraintType;
import com.clougence.clouddm.sdk.service.secrules.Domain;

public class ConstraintBuilder extends AbstractDomainBuilder {

    private String            name;

    private String            comment;

    private List<String>      columns = new ArrayList<>();

    private SqlConstraintType constraintType;

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.VALUE) {
            if (this.name == null) {
                this.name = (String) value;
            }
        } else if (attr == CommonAttribute.COMMENT) {
            this.comment = (String) value;
        } else {
            super.addAttr(attr, value);
        }
    }

    @Override
    public List<Domain> build() {
        RdbConstraintDomain constraintDomain = new RdbConstraintDomain();
        constraintDomain.setName(name);
        constraintDomain.setColumns(columns);
        constraintDomain.setType(constraintType);
        constraintDomain.setComment(comment);

        return Collections.singletonList(constraintDomain);
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource type) {
        if (constraintType == SqlConstraintType.Check) {
            if (type == DomainSource.COLUMN) {
                RdbColumnDomain columnDomain = (RdbColumnDomain) list.get(0);
                this.columns.add(columnDomain.getColumn());
            } else if (type == DomainSource.CONSTANT) {
            }
        } else if (type == DomainSource.COLUMN_LIST) {
            ColumnListDomain domain = (ColumnListDomain) list.get(0);
            this.columns.addAll(domain.getColumns());
        } else if (type == DomainSource.OBJ_NAME) {
            // foreign table
            ObjNameDomain objNameDomain = (ObjNameDomain) list.get(0);
            if (objNameDomain.getType() == NameType.INDEX) {
                this.name = objNameDomain.getName();
            }
        } else if (type == DomainSource.CONSTRAINT_TYPE) {
            ConstraintTypeDomain constraintTypeDomain = (ConstraintTypeDomain) list.get(0);
            constraintType = constraintTypeDomain.getConstraintType();
        } else {
            super.handleSubDomain(list, type);
        }
    }
}
