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
package com.clougence.adapter.kafka;

import java.sql.JDBCType;

import com.clougence.schema.DsType;
import com.clougence.schema.metadata.FieldType;

public enum KafkaTypes implements FieldType {

    String("String", JDBCType.VARCHAR),
    Bytes("Bytes", JDBCType.VARBINARY),;

    private final String   codeKey;
    private final JDBCType jdbcType;

    KafkaTypes(String codeKey, JDBCType jdbcType){
        this.codeKey = codeKey;
        this.jdbcType = jdbcType;
    }

    public static KafkaTypes valueOfCode(String code) {
        for (KafkaTypes type : KafkaTypes.values()) {
            if (type.codeKey.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new UnsupportedOperationException("Unsupported Kafka columnType " + code);
    }

    @Override
    public String getCodeKey() { return this.codeKey; }

    @Override
    public int getCodeNum() { return ordinal(); }

    @Override
    public DsType getDsType() { return DsType.Kafka; }

    @Override
    public boolean isReadOnly() { return false; }

    @Override
    public boolean hasApproximate() { return false; }

    @Override
    public boolean isArray() { return false; }

    @Override
    public boolean isStruct() { return false; }

    @Override
    public boolean isNumber() { return false; }

    @Override
    public boolean isBinary() { return this == Bytes; }

    @Override
    public boolean isString() { return this == String; }

    @Override
    public boolean isDataOrTime() { return false; }

    @Override
    public boolean isGeometry() { return false; }

    @Override
    public boolean isBoolean() { return false; }

    @Override
    public boolean hasFixedChar() { return false; }

    @Override
    public boolean hasDate() { return false; }

    @Override
    public boolean hasTime() { return false; }

    @Override
    public boolean hasZone() { return false; }

    @Override
    public boolean isAccurateDecimal() { return false; }

    @Override
    public Integer getJdbcType() { return this.jdbcType.getVendorTypeNumber(); }

    @Override
    public JDBCType toJDBCType() { return this.jdbcType; }
}
