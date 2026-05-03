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
package com.clougence.clouddm.faker.seed;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.type.TypeHandler;

import com.clougence.utils.ClassUtils;

/**
 * SeedConfig
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public abstract class SeedConfig {

    private boolean                   allowNullable;
    private Float                     nullableRatio;
    private TypeHandler<?>            typeHandler;
    private final Map<String, Object> configMap = new HashMap<>();

    public Map<String, Object> getConfigMap() { return configMap; }

    public boolean isAllowNullable() { return allowNullable; }

    public void setAllowNullable(boolean allowNullable) { this.allowNullable = allowNullable; }

    public Float getNullableRatio() { return nullableRatio; }

    public void setNullableRatio(Float nullableRatio) { this.nullableRatio = nullableRatio; }

    public TypeHandler<?> getTypeHandler() {
        if (this.typeHandler == null) {
            this.typeHandler = defaultTypeHandler();
        }
        return this.typeHandler;
    }

    public void setTypeHandler(TypeHandler<?> typeHandler) { this.typeHandler = typeHandler; }

    public void setTypeHandlerType(String typeHandlerType) throws Exception {
        Class<?> typeHandlerClass = ClassUtils.getClass(typeHandlerType);
        this.typeHandler = (TypeHandler<?>) typeHandlerClass.newInstance();
    }

    protected abstract TypeHandler<?> defaultTypeHandler();

    public abstract SeedType getSeedType();
}
