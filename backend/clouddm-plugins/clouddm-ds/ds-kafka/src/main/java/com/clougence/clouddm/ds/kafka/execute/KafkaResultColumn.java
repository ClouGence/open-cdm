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
package com.clougence.clouddm.ds.kafka.execute;

import java.sql.JDBCType;

import com.clougence.clouddm.dsfamily.execute.AbstractColReader;
import com.clougence.clouddm.sdk.execute.session.result.fetcher.ValueFetcher;

import lombok.Getter;

@Getter
public enum KafkaResultColumn {
    TOPIC,
    TOPIC_ID,
    INTERNAL(JDBCType.BOOLEAN),
    PARTITION_COUNT(JDBCType.BIGINT),
    PARTITION(JDBCType.BIGINT),
    LEADER(JDBCType.BIGINT),
    REPLICAS,
    ISR,
    CONFIGS,
    OFFSET(JDBCType.BIGINT),
    TIMESTAMP(JDBCType.BIGINT),
    TIMESTAMP_TYPE,
    KEY,
    VALUE,
    STATUS,
    ERROR;

    private final JDBCType jdbcType;

    KafkaResultColumn(){
        this(JDBCType.VARCHAR);
    }

    KafkaResultColumn(JDBCType jdbcType){
        this.jdbcType = jdbcType;
    }

    public String columnName() {
        return name().replace('_', '-');
    }

    public ValueFetcher fetcher() {
        return switch (jdbcType) {
            case BIGINT -> AbstractColReader.LONG_VALUE_FETCHER;
            case BOOLEAN -> AbstractColReader.BOOLEAN_VALUE_FETCHER;
            default -> AbstractColReader.STRING_VALUE_FETCHER;
        };
    }
}
