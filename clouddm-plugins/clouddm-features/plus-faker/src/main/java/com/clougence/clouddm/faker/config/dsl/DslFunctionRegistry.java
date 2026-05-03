/*
 * Copyright 2015-2022 the original author or authors.
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
package com.clougence.clouddm.faker.config.dsl;

import java.util.Map;

import com.clougence.clouddm.faker.config.processor.PublicSpiRegistry;
import com.clougence.clouddm.faker.config.spi.HanaSpiRegistry;
import com.clougence.clouddm.faker.config.spi.PostgresqlSpiRegistry;
import com.clougence.utils.ref.LinkedCaseInsensitiveMap;

/**
 * DslFunction 注册器
 * @version : 2023-02-14
 * @author 赵永春 (zyc@hasor.net)
 */
public class DslFunctionRegistry {

    public static final DslFunctionRegistry DEFAULT     = new DslFunctionRegistry();
    private final Map<String, DslFunction>  functionMap = new LinkedCaseInsensitiveMap<>();

    private DslFunctionRegistry(){
    }

    static {
        new PublicSpiRegistry().loopUp(DEFAULT);
        new PostgresqlSpiRegistry().loopUp(DEFAULT);
        new HanaSpiRegistry().loopUp(DEFAULT);
    }

    /** 注册 DslFunction */
    public DslFunction findByName(String functionName) {
        return functionMap.get(functionName);
    }

    /** 注册 DslFunction */
    public void register(String functionName, DslFunction dslFunction) {
        functionMap.put(functionName, dslFunction);
    }
}
