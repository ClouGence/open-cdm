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
package com.clougence.clouddm.faker.config.dsl.model;

import java.util.Map;

/**
 * 值类型
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2023-02-14
 */
public class ValueModel implements DataModel {

    public static ValueModel NULL  = new ValueModel(null);
    public static ValueModel TRUE  = new ValueModel(true);
    public static ValueModel FALSE = new ValueModel(false);
    private Object           value = null;

    public ValueModel(Object value){
        this.value = value;
    }

    @Override
    public Object recover(Map<String, Object> context) {
        return this.value;
    }

    public Object getValue() { return this.value; }

    public void setValue(Object value) { this.value = value; }
}
