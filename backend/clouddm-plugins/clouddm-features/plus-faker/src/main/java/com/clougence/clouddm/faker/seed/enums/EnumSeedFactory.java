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
package com.clougence.clouddm.faker.seed.enums;

import static com.clougence.utils.RandomUtils.nextFloat;
import static com.clougence.utils.RandomUtils.nextInt;

import java.io.Serializable;
import java.util.Set;
import java.util.function.Supplier;

import com.clougence.clouddm.faker.seed.SeedConfig;
import com.clougence.clouddm.faker.seed.SeedFactory;

/**
 * 枚举类型的 SeedFactory
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class EnumSeedFactory implements SeedFactory<EnumSeedConfig> {

    @Override
    public EnumSeedConfig newConfig(SeedConfig contextType) {
        return new EnumSeedConfig();
    }

    @Override
    public Supplier<Serializable> createSeed(EnumSeedConfig seedConfig) {
        boolean allowNullable = seedConfig.isAllowNullable();
        Float nullableRatio = seedConfig.getNullableRatio();
        if (allowNullable && nullableRatio == null) {
            throw new IllegalStateException("allowNullable is true but, nullableRatio missing.");
        }

        Set<String> dict = seedConfig.getDict();
        if (allowNullable) {
            dict.add(null);
        }

        String[] dictArrays = dict.toArray(new String[0]);
        int max = dictArrays.length;

        return () -> {
            if (allowNullable && nextFloat(0, 100) < nullableRatio) {
                return null;
            } else {
                return (dictArrays.length == 0) ? null : (dictArrays.length == 1) ? dictArrays[0] : dictArrays[nextInt(0, max)];
            }
        };
    }
}
