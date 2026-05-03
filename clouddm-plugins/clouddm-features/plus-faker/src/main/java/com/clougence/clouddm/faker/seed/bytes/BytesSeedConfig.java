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
package com.clougence.clouddm.faker.seed.bytes;

import org.apache.ibatis.type.TypeHandler;

import com.clougence.clouddm.faker.seed.SeedConfig;
import com.clougence.clouddm.faker.seed.SeedType;
import com.clougence.clouddm.faker.types.TypeHandlerRegistryUtils;

/**
 * byte[] 类型的 SeedConfig
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class BytesSeedConfig extends SeedConfig {

    private int minLength;
    private int maxLength;

    public final SeedType getSeedType() { return SeedType.Bytes; }

    @Override
    protected TypeHandler<?> defaultTypeHandler() {
        return TypeHandlerRegistryUtils.getTypeHandler(byte[].class);
    }

    public int getMinLength() { return minLength; }

    public void setMinLength(int minLength) { this.minLength = minLength; }

    public int getMaxLength() { return maxLength; }

    public void setMaxLength(int maxLength) { this.maxLength = maxLength; }
}
