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
package com.clougence.schema.umi.special.rdb;

import com.clougence.schema.metadata.FieldType;
import com.clougence.schema.umi.serializer.SerializerRoot;
import com.clougence.schema.umi.struts.ConstraintUmiData;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;

/**
 * column
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2020-05-21
 */
@Getter
@Setter
@JsonSerialize(using = SerializerRoot.JacksonSerializer.class)
@JsonDeserialize(using = SerializerRoot.JacksonDeserializer.class)
public class RdbColumn extends ConstraintUmiData implements Value {

    private String    catalog;
    private String    schema;
    private String    table;
    private String    name;
    private String    comment;
    private UmiTypes  umiType;

    private FieldType sqlType;
    private Integer   index;
    private Long      charLength;
    private Long      byteLength;
    private Integer   numericPrecision;
    private Integer   numericScale;
    private Boolean   numericUnsigned;
    private Integer   arrayDimension;

    private Integer   datetimePrecision;
    private String    defaultValue;
    private Boolean   defaultValueIsFunc;

    public RdbColumn(){
        this.umiType = UmiTypes.Column;
    }

    @Override
    public String asValue() {
        return getName();
    }

    @Override
    public String toString() {
        return this.name;
    }
}
