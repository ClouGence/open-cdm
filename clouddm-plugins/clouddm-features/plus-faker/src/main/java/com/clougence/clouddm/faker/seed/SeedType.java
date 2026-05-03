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

import java.util.function.Supplier;

import com.clougence.clouddm.faker.seed.array.ArraySeedFactory;
import com.clougence.clouddm.faker.seed.array.ArraySeedUiFactory;
import com.clougence.clouddm.faker.seed.bool.BooleanSeedFactory;
import com.clougence.clouddm.faker.seed.bool.BooleanSeedUiFactory;
import com.clougence.clouddm.faker.seed.bytes.BytesSeedFactory;
import com.clougence.clouddm.faker.seed.bytes.BytesSeedUiFactory;
import com.clougence.clouddm.faker.seed.date.DateSeedFactory;
import com.clougence.clouddm.faker.seed.date.DateSeedUiFactory;
import com.clougence.clouddm.faker.seed.enums.EnumSeedFactory;
import com.clougence.clouddm.faker.seed.enums.EnumSeedUiFactory;
import com.clougence.clouddm.faker.seed.geometry.GeometrySeedFactory;
import com.clougence.clouddm.faker.seed.geometry.GeometrySeedUiFactory;
import com.clougence.clouddm.faker.seed.guid.GuidSeedFactory;
import com.clougence.clouddm.faker.seed.guid.GuidSeedUiFactory;
import com.clougence.clouddm.faker.seed.mysqltime.MySqlTimeSeedFactory;
import com.clougence.clouddm.faker.seed.mysqltime.MySqlTimeSeedUiFactory;
import com.clougence.clouddm.faker.seed.number.NumberSeedFactory;
import com.clougence.clouddm.faker.seed.number.NumberSeedUiFactory;
import com.clougence.clouddm.faker.seed.string.StringSeedFactory;
import com.clougence.clouddm.faker.seed.string.StringSeedUiFactory;
import com.clougence.utils.StringUtils;

/**
 * 类型
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public enum SeedType {

    Boolean(BooleanSeedFactory::new, BooleanSeedUiFactory::new),
    Date(DateSeedFactory::new, DateSeedUiFactory::new),
    String(StringSeedFactory::new, StringSeedUiFactory::new),
    Number(NumberSeedFactory::new, NumberSeedUiFactory::new),
    Enums(EnumSeedFactory::new, EnumSeedUiFactory::new),
    Bytes(BytesSeedFactory::new, BytesSeedUiFactory::new),
    GID(GuidSeedFactory::new, GuidSeedUiFactory::new),
    Array(ArraySeedFactory::new, ArraySeedUiFactory::new),
    Geometry(GeometrySeedFactory::new, GeometrySeedUiFactory::new),
    Time(MySqlTimeSeedFactory::new, MySqlTimeSeedUiFactory::new),
    //    Struts,
    //    RelationId,
    Custom(null, null);

    private final Supplier<SeedFactory<? extends SeedConfig>> supplier;

    private final Supplier<SeedUiFactory>                     uiSupplier;

    SeedType(Supplier<SeedFactory<? extends SeedConfig>> supplier, Supplier<SeedUiFactory> uiSupplier){
        this.supplier = supplier;
        this.uiSupplier = uiSupplier;
    }

    public SeedFactory<? extends SeedConfig> newFactory() {
        return this.supplier != null ? this.supplier.get() : null;
    }

    public SeedUiFactory newUiFactory() {
        return this.uiSupplier != null ? this.uiSupplier.get() : null;
    }

    public static SeedType valueOfCode(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (SeedType seedType : SeedType.values()) {
            if (StringUtils.equalsIgnoreCase(seedType.name(), name)) {
                return seedType;
            }
        }
        return null;
    }
}
