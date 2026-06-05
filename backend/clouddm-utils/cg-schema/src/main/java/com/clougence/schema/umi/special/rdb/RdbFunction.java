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

import java.util.Map;

import com.clougence.schema.umi.serializer.SerializerRoot;
import com.clougence.schema.umi.struts.RoutineUmiData;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonSerialize(using = SerializerRoot.JacksonSerializer.class)
@JsonDeserialize(using = SerializerRoot.JacksonDeserializer.class)
public class RdbFunction extends RoutineUmiData implements Value {

    private String              catalog;
    private String              schema;
    private UmiTypes            umiType;
    private RdbParam            returns;
    private String              sql;
    private Map<String, Object> features;

    public RdbFunction(){
        this.umiType = UmiTypes.Function;
    }

    @Override
    public String asValue() {
        return getName();
    }

    public void setReturns(RdbParam param) { this.returns = param; }

    public void setReturn(String param) {
        RdbParam rdbParam = new RdbParam();
        rdbParam.setCatalog(this.catalog);
        rdbParam.setSchema(this.schema);
        rdbParam.setName("RETURN");
        rdbParam.setType(param);
        rdbParam.setMode(RdbParamMode.OUT);
        this.returns = rdbParam;
    }

    public void setReturn(int pOrdinal, String pName, String pType, String pDesc) {
        RdbParam rdbParam = new RdbParam();
        rdbParam.setCatalog(this.catalog);
        rdbParam.setSchema(this.schema);
        rdbParam.setName(pName);
        rdbParam.setType(pType);
        rdbParam.setOrdinal(pOrdinal);
        rdbParam.setMode(RdbParamMode.OUT);
        rdbParam.setAttribute(RdbAttributeNames.OBJ_UI_TIPS, pDesc);
        this.returns = rdbParam;
    }
}
