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

import java.util.Arrays;

import com.clougence.clouddm.faker.config.OpsType;

import lombok.Getter;

/**
 * 一条生成的 SQL
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public class BoundQuery {

    @Getter
    private final FakerTable    tableInfo;
    @Getter
    private final OpsType       opsType;
    private final StringBuilder sqlString;
    private final SqlArg[]      paramArray;
    @Getter
    private final int           records;

    public BoundQuery(FakerTable tableInfo, OpsType opsType, StringBuilder sqlString, SqlArg[] paramArray, int records){
        this.tableInfo = tableInfo;
        this.opsType = opsType;
        this.sqlString = sqlString;
        this.paramArray = paramArray;
        this.records = records;
    }

    /** SQL 文本 */
    public String getSqlString() { return this.sqlString.toString(); }

    /** 执行参数 */
    public SqlArg[] getArgs() { return this.paramArray; }

    @Override
    public String toString() {
        return "{'" + sqlString + '\'' + ", args=" + Arrays.toString(paramArray) + '}';
    }
}
