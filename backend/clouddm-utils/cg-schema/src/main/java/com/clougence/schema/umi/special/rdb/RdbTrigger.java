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

import java.util.List;
import java.util.Map;

import com.clougence.schema.umi.struts.RoutineUmiData;
import com.clougence.schema.umi.struts.UmiTypes;
import com.clougence.schema.umi.struts.Value;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdbTrigger extends RoutineUmiData implements Value {

    private List<String>        triggerEvent;
    private String              triggerTableCatalog;
    private String              triggerTableSchema;
    private String              triggerTableName;
    private List<String>        triggerTableColumns;
    private String              triggerTime;

    private String              sql;
    private Map<String, Object> features;

    private UmiTypes            umiType;

    public RdbTrigger(){
        this.umiType = UmiTypes.Trigger;
    }

    @Override
    public String asValue() {
        return this.getName();
    }
}
