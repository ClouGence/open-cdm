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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.faker.config.dsl.DslFunction;
import com.clougence.clouddm.faker.config.dsl.DslFunctionRegistry;

/**
 * DSL 函数
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2023-02-14
 */
public class FunctionModel implements DataModel {

    private final String          name;
    private final List<DataModel> params;

    public FunctionModel(String name, List<DataModel> params){
        this.name = name;
        this.params = params;
    }

    @Override
    public Object recover(Map<String, Object> context) {
        DslFunction dslRule = DslFunctionRegistry.DEFAULT.findByName(this.name);
        if (dslRule == null) {
            throw new IllegalStateException("function " + name + " is not definition.");
        }

        List<Object> unwrap = new ArrayList<>(this.params.size());
        for (DataModel model : this.params) {
            unwrap.add(model.recover(context));
        }

        return dslRule.rule(unwrap, context);
    }
}
