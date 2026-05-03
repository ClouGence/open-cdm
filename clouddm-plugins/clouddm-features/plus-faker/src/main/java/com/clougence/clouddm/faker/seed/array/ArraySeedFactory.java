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
package com.clougence.clouddm.faker.seed.array;

import static com.clougence.utils.RandomUtils.nextFloat;
import static com.clougence.utils.RandomUtils.nextInt;

import java.io.Serializable;
import java.util.function.Supplier;

import com.clougence.clouddm.faker.seed.SeedConfig;
import com.clougence.clouddm.faker.seed.SeedFactory;

/**
 * 数组类型生成器 SeedFactory
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class ArraySeedFactory implements SeedFactory<ArraySeedConfig> {

    private SeedFactory elementFactory;

    public ArraySeedFactory(){
    }

    public ArraySeedFactory(SeedFactory elementFactory){
        this.elementFactory = elementFactory;
    }

    @Override
    public ArraySeedConfig newConfig(SeedConfig contextType) {
        return new ArraySeedConfig(this.elementFactory.newConfig());
    }

    @Override
    public Supplier<Serializable> createSeed(ArraySeedConfig seedConfig) {
        int minSize = seedConfig.getMinSize();
        int maxSize = seedConfig.getMaxSize();
        Supplier<Serializable> elementSeed = this.elementFactory.createSeed(seedConfig.getElementDef());

        boolean allowNullable = seedConfig.isAllowNullable();
        Float nullableRatio = seedConfig.getNullableRatio();
        if (allowNullable && nullableRatio == null) {
            throw new IllegalStateException("allowNullable is true but, nullableRatio missing.");
        }

        return () -> {
            if (allowNullable && nextFloat(0, 100) < nullableRatio) {
                return null;
            }

            return nextArray(nextInt(minSize, maxSize + 1), elementSeed);
        };
    }

    private Serializable[] nextArray(int size, Supplier<Serializable> elementSeed) {
        Serializable[] result = new Serializable[size];
        for (int i = 0; i < size; i++) {
            result[i] = elementSeed.get();
        }
        return result;
    }

    public SeedFactory getElementFactory() { return elementFactory; }

    public void setElementFactory(SeedFactory elementFactory) { this.elementFactory = elementFactory; }
}
