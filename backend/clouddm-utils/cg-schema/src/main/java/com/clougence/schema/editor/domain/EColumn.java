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
package com.clougence.schema.editor.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2021/5/21 19:56
 */
@Getter
@Setter
public class EColumn extends EChange<EColumn> {

    private String              source;
    private String              name;
    private String              dbType;
    private Boolean             nullable;
    private Long                length;
    private Integer             numericPrecision;
    private Integer             numericScale;
    private boolean             numericUnsigned;
    private Integer             arrayDimension;
    private Integer             datetimePrecision;
    private String              defaultValue;
    private boolean             defaultValueIsFunc;
    private boolean             autoGenerate;
    private String              comment;
    private String              columnTypeWithDisplayWidth;
    private Map<String, String> attribute = new HashMap<>();

    public EColumn(String source){
        this.source = source;
    }

    public EColumn(){
    }

    @Override
    public EColumn clone() {
        EColumn eColumn = new EColumn(source);
        eColumn.name = this.name;
        eColumn.dbType = this.dbType;
        eColumn.nullable = this.nullable;
        eColumn.length = this.length;
        eColumn.numericPrecision = this.numericPrecision;
        eColumn.numericScale = this.numericScale;
        eColumn.numericUnsigned = this.numericUnsigned;
        eColumn.datetimePrecision = this.datetimePrecision;
        eColumn.arrayDimension = this.arrayDimension;
        eColumn.defaultValue = this.defaultValue;
        eColumn.defaultValueIsFunc = this.defaultValueIsFunc;
        eColumn.autoGenerate = this.autoGenerate;
        eColumn.comment = this.comment;
        eColumn.columnTypeWithDisplayWidth = this.columnTypeWithDisplayWidth;
        eColumn.attribute.putAll(this.attribute);
        return eColumn;
    }

    public boolean testChanged(EColumn initData) {
        if (initData == null) {
            return true;
        }

        // To identify extreme situations, for example:

        //      column1                      column2
        // sourceName    name         sourceName    name
        // id1           id1          null          id1

        // column1 -> column2, drop id1 first, then add a new column name id1, not rename
        // so if not have this determine, only compare name, cannot determine if there is a difference
        if (!Objects.equals(initData.source, initData.name)) {
            return true;
        }
        if (!Objects.equals(this.name, initData.name)) {
            return true;
        }
        if (!Objects.equals(this.dbType, initData.dbType)) {
            return true;
        }
        if (this.nullable != initData.nullable) {
            return true;
        }
        if (!Objects.equals(this.length, initData.length)) {
            return true;
        }
        if (!Objects.equals(this.numericPrecision, initData.numericPrecision)) {
            return true;
        }
        if (!Objects.equals(this.numericScale, initData.numericScale)) {
            return true;
        }
        if (!Objects.equals(this.numericUnsigned, initData.numericUnsigned)) {
            return true;
        }
        if (!Objects.equals(this.datetimePrecision, initData.datetimePrecision)) {
            return true;
        }
        if (!Objects.equals(this.defaultValue, initData.defaultValue)) {
            return true;
        }
        if (!Objects.equals(this.defaultValueIsFunc, initData.defaultValueIsFunc)) {
            return true;
        }
        if (!Objects.equals(this.autoGenerate, initData.autoGenerate)) {
            return true;
        }
        if (!Objects.equals(this.comment, initData.comment)) {
            return true;
        }
        if (!Objects.equals(this.columnTypeWithDisplayWidth, initData.columnTypeWithDisplayWidth)) {
            return true;
        }
        if (EditorUtils.testAttribute(this.attribute, initData.attribute)) {
            return true;
        }
        return false;
    }

    public boolean testChangedExcept(EColumn initData) {
        if (initData == null) {
            return true;
        }
        if (!Objects.equals(this.name, initData.name)) {
            return true;
        }
        if (!Objects.equals(this.dbType, initData.dbType)) {
            return true;
        }
        if (!Objects.equals(this.nullable, initData.nullable)) {
            return true;
        }
        if (!Objects.equals(this.length, initData.length)) {
            return true;
        }
        if (!Objects.equals(this.numericPrecision, initData.numericPrecision)) {
            return true;
        }
        if (!Objects.equals(this.numericScale, initData.numericScale)) {
            return true;
        }
        if (!Objects.equals(this.numericUnsigned, initData.numericUnsigned)) {
            return true;
        }
        if (!Objects.equals(this.datetimePrecision, initData.datetimePrecision)) {
            return true;
        }
        if (!Objects.equals(this.defaultValue, initData.defaultValue)) {
            return true;
        }
        if (!Objects.equals(this.defaultValueIsFunc, initData.defaultValueIsFunc)) {
            return true;
        }
        if (!Objects.equals(this.autoGenerate, initData.autoGenerate)) {
            return true;
        }
        if (!Objects.equals(this.comment, initData.comment)) {
            return true;
        }
        if (!Objects.equals(this.columnTypeWithDisplayWidth, initData.columnTypeWithDisplayWidth)) {
            return true;
        }
        if (EditorUtils.testAttribute(this.attribute, initData.attribute)) {
            return true;
        }
        return false;
    }
}
