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
package com.clougence.clouddm.sdk.execute.session;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryArg implements Cloneable {

    private int     index;
    private String  value;
    private int     jdbcType;
    private String  dataType;
    private boolean isOutParam;

    public QueryArg(){
    }

    public QueryArg(int index, String value, int jdbcType, String dataType, boolean isOutParam){
        this.index = index;
        this.value = value;
        this.jdbcType = jdbcType;
        this.dataType = dataType;
        this.isOutParam = isOutParam;
    }

    @Override
    public QueryArg clone() {
        QueryArg arg = new QueryArg();
        arg.index = this.index;
        arg.value = this.value;
        arg.jdbcType = this.jdbcType;
        arg.dataType = this.dataType;
        arg.isOutParam = this.isOutParam;
        return arg;
    }

    @Override
    public String toString() {
        return this.value + "[" + this.dataType + "]";
    }
}
