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
package com.clougence.clouddm.faker.generator;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import org.apache.ibatis.type.TypeHandler;

import com.clougence.clouddm.faker.config.UseFor;
import com.clougence.clouddm.faker.seed.SeedConfig;
import com.clougence.clouddm.faker.seed.SeedFactory;
import com.clougence.utils.BeanUtils;

/**
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class TypeProcessor {

    // seed
    private final SeedFactory<SeedConfig> seedFactory;
    private final SeedConfig              seedConfig;
    private final Set<UseFor>             defaultIgnoreAct;
    // read/write
    private Integer                       jdbcType;
    private TypeHandler<?>                typeHandler;
    private Supplier<Serializable>        valueSeed;

    public TypeProcessor(SeedFactory<? extends SeedConfig> seedFactory, SeedConfig seedConfig, Integer jdbcType){
        this.seedFactory = (SeedFactory<SeedConfig>) seedFactory;
        this.seedConfig = seedConfig;
        this.jdbcType = jdbcType;
        this.defaultIgnoreAct = new HashSet<>();
    }

    public SeedConfig getSeedConfig() { return this.seedConfig; }

    public Class<?> getConfigType() { return this.seedConfig.getClass(); }

    public Set<UseFor> getDefaultIgnoreAct() { return defaultIgnoreAct; }

    public void putConfig(String property, Object writeValue) {
        this.seedConfig.getConfigMap().put(property, writeValue);
    }

    public void writeProperty(String property, Object writeValue) {
        BeanUtils.writeProperty(this.seedConfig, property, writeValue);
    }

    /** 生成 value 值 */
    public SqlArg buildData(String columnName) {
        return new SqlArg(columnName, this.jdbcType, this.typeHandler, this.valueSeed.get());
    }

    /** 从 ResultSet value 值 */
    public SqlArg buildData(ResultSet rs, String columnName) throws SQLException {
        Object result = this.typeHandler.getResult(rs, columnName);
        return new SqlArg(columnName, this.jdbcType, this.typeHandler, result);
    }

    public void applyConfig() {
        this.typeHandler = this.seedConfig.getTypeHandler();
        this.valueSeed = this.seedFactory.createSeed(this.seedConfig);
    }

    @Override
    public String toString() {
        String typeHandlerStr = this.typeHandler == null ? "null" : this.typeHandler.getClass().getSimpleName();
        return "jdbcType= " + this.jdbcType + ", typeHandler= " + typeHandlerStr;
    }
}
