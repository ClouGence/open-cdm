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
package com.clougence.clouddm.faker.seed.guid;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.UUID;
import java.util.function.Supplier;

import com.clougence.clouddm.faker.seed.SeedConfig;
import com.clougence.clouddm.faker.seed.SeedFactory;
import com.clougence.utils.RandomUtils;

/**
 * GUID/UUID SeedFactory
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class GuidSeedFactory implements SeedFactory<GuidSeedConfig> {

    @Override
    public GuidSeedConfig newConfig(SeedConfig contextType) {
        return new GuidSeedConfig();
    }

    @Override
    public Supplier<Serializable> createSeed(GuidSeedConfig seedConfig) {
        GuidType guidType = seedConfig.getDateType();
        boolean allowNullable = seedConfig.isAllowNullable();
        Float nullableRatio = seedConfig.getNullableRatio();

        if (allowNullable && nullableRatio == null) {
            throw new IllegalStateException("allowNullable is true but, nullableRatio missing.");
        }

        return () -> {
            if (allowNullable && RandomUtils.nextFloat(0, 100) < nullableRatio) {
                return null;
            } else {
                UUID randomUUID = UUID.randomUUID();
                switch (guidType) {
                    case Timestamp:
                        return randomUUID.timestamp();
                    case Bytes: {
                        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
                        bb.putLong(randomUUID.getMostSignificantBits());
                        bb.putLong(randomUUID.getLeastSignificantBits());
                        return bb.array();
                    }
                    case String32:
                        return randomUUID.toString().replace("-", "");
                    case String36:
                    default:
                        return randomUUID.toString();
                }
            }
        };
    }
}
