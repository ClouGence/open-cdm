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

import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.Attribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.CommonAttribute;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.enums.DomainSource;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ColumnTypeDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ConstraintTypeDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.builder.mode.ObjNameDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbColumnDomain;
import com.clougence.clouddm.dsfamily.analysis.secrules.rdb.RdbConstantDomain;
import com.clougence.clouddm.sdk.service.secrules.Domain;

/**
 * for create column
 * @param <T>
 */
public abstract class ColumnDefBuilder<T extends RdbColumnDomain> extends AbstractDomainBuilder {

    protected T columnDomain = getColumnDomain();

    public void setType(String typeDesc, String type, String length) {
        columnDomain.setTypeName(type);
        columnDomain.setLength(length);
        columnDomain.setTypeDesc(typeDesc);
    }

    @Override
    public List<Domain> build() {
        return Collections.singletonList(columnDomain);
    }

    @Override
    public void handleSubDomain(List<Domain> list, DomainSource type) {
        if (type == DomainSource.COLUMN_DEFAULT_VALUE) {
            if (list.isEmpty()) {
                return;
            }
            String constantValue = ((RdbConstantDomain) list.get(0)).getConstantValue();
            if (constantValue.equals("null")) {
                return;
            }
            if (constantValue.startsWith("'")) {
                constantValue = constantValue.substring(1, constantValue.length() - 1);
            }
            this.columnDomain.setDefaultValue(constantValue);
        } else if (type == DomainSource.COLUMN_TYPE) {
            ColumnTypeDomain columnTypeDomain = (ColumnTypeDomain) list.get(0);
            columnDomain.setTypeName(columnTypeDomain.getType());
            columnDomain.setTypeDesc(columnTypeDomain.getFullName());
            columnDomain.setLength(columnTypeDomain.getLength());
        } else if (type == DomainSource.OBJ_NAME) {
            if (columnDomain.getColumn() != null) {
                return;
            }
            ObjNameDomain objNameDomain = (ObjNameDomain) list.get(0);
            columnDomain.setColumn(objNameDomain.getNameList().get(0));
        } else if (type == DomainSource.CONSTRAINT_TYPE) {
            ConstraintTypeDomain constraintTypeDomain = (ConstraintTypeDomain) list.get(0);
            switch (constraintTypeDomain.getConstraintType()) {
                case Unique:
                    this.columnDomain.setUnique(true);
                    break;
                case ForeignKey:
                    this.columnDomain.setForeign(true);
                    break;
                case Primary:
                    this.columnDomain.setPrimary(true);
                    break;
            }
        } else {
            super.handleSubDomain(list, type);
        }

    }

    @Override
    public void addAttr(Attribute attr, Object value) {
        if (attr == CommonAttribute.NOT_NULL) {
            this.columnDomain.setNullable(false);
        } else if (attr == CommonAttribute.COLUMN_ALLOW_NULL) {
            this.columnDomain.setNullable(true);
        } else if (attr == CommonAttribute.VALUE) {
            if (columnDomain.getColumn() == null) {
                this.columnDomain.setColumn((String) value);
            }
        } else if (attr == CommonAttribute.COMMENT) {
            this.columnDomain.setComment((String) value);
        }
    }

    protected abstract T getColumnDomain();

}
