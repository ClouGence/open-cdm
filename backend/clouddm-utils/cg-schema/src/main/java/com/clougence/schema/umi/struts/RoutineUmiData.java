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
package com.clougence.schema.umi.struts;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.clougence.schema.umi.serializer.SerializerRoot;
import com.clougence.schema.umi.special.rdb.RdbParam;
import com.clougence.schema.umi.special.rdb.RdbParamMode;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonSerialize(using = SerializerRoot.JacksonSerializer.class)
@JsonDeserialize(using = SerializerRoot.JacksonDeserializer.class)
public abstract class RoutineUmiData extends AttributeUmiData {

    private String         name;

    private String         comment;

    private List<RdbParam> rdbParams;

    public RoutineUmiData(){
        this.rdbParams = new ArrayList<>();
    }

    public void setRdbParams(List<RdbParam> rdbParams) {
        this.rdbParams.addAll(rdbParams);
    }

    public void addParam(int ordinal, String name, String dataType, RdbParamMode mode) {
        RdbParam rdbParam = new RdbParam();
        rdbParam.setName(StringUtils.isEmpty(name) ? ("arg" + ordinal) : name);
        rdbParam.setType(dataType);
        rdbParam.setOrdinal(ordinal);
        rdbParam.setMode(mode);
        this.rdbParams.add(rdbParam);
    }

    @Deprecated //  is incomplete
    public void addParam(String parameterName, String dataType, String maximumLength, Integer ordinal) {
        RdbParam rdbParam = new RdbParam();
        rdbParam.setName(parameterName);
        rdbParam.setType(dataType);
        rdbParam.setCharacterMaximumLength(maximumLength);
        rdbParam.setOrdinal(ordinal);
        rdbParams.add(rdbParam);
    }

    public String getParamString() {
        if (!rdbParams.isEmpty()) {
            StringBuilder paramBuilder = new StringBuilder();
            rdbParams.stream()
                // concat params like [name varchar(32)], [age int],
                .sorted(Comparator.comparingInt(RdbParam::getOrdinal))
                .forEach(rdbParam -> {
                    paramBuilder.append("[").append(rdbParam.getName()).append(" ").append(rdbParam.getType().toUpperCase());
                    if (StringUtils.isNotBlank(rdbParam.getCharacterMaximumLength())) {
                        paramBuilder.append("(").append(rdbParam.getCharacterMaximumLength()).append(")");
                    }
                    paramBuilder.append("], ");
                });
            paramBuilder.delete(paramBuilder.length() - 2, paramBuilder.length());
            return paramBuilder.toString();
        }
        return null;
    }

}
