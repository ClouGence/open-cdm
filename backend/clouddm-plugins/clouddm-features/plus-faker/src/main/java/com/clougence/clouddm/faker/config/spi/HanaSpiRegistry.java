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
package com.clougence.clouddm.faker.config.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.clougence.clouddm.faker.config.UseFor;
import com.clougence.clouddm.faker.config.dsl.DslFunction;
import com.clougence.clouddm.faker.config.dsl.DslFunctionLoopUp;
import com.clougence.clouddm.faker.config.dsl.DslFunctionRegistry;
import com.clougence.clouddm.faker.config.parameter.ParameterProcessorLookUp;
import com.clougence.clouddm.faker.config.parameter.ParameterRegistry;
import com.clougence.utils.StringUtils;

/**
 * @author chunlin
 * @date 2024/4/10
 */
public class HanaSpiRegistry implements DslFunctionLoopUp, ParameterProcessorLookUp {

    @Override
    public void loopUp(DslFunctionRegistry registry) {
        registry.register("hanaIgnoreActSupport", hanaIgnoreActSupport());
    }

    @Override
    public void loopUp(ParameterRegistry registry) {
    }

    private static DslFunction hanaIgnoreActSupport() {
        return (args, context) -> {
            List<UseFor> ignoreAct = new ArrayList<>();
            Map attributes = (Map) context.get("attributes");
            if (attributes == null || attributes.isEmpty()) {
                return ignoreAct;
            }
            String rdbAuto = (String) attributes.get("rdb_auto");
            if (!StringUtils.isBlank(rdbAuto) && "true".equals(rdbAuto)) {
                ignoreAct.add(UseFor.Insert);
                ignoreAct.add(UseFor.UpdateSet);
            }

            return ignoreAct;
        };
    }
}
