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
package com.clougence.detectrule.lang.reflect;

import com.clougence.detectrule.lang.CollectionAccess;
import com.clougence.detectrule.lang.KeyPairAccess;

import lombok.Getter;

@Getter
public enum TypeType {

    Boolean(true, true, false, java.lang.Boolean.class, java.lang.Boolean.TYPE),

    Float(true, true, false, java.lang.Float.class, java.lang.Float.TYPE, java.lang.Double.class, java.lang.Double.TYPE),

    Integer(true, true, false, java.lang.Byte.class, java.lang.Byte.TYPE, java.lang.Short.class, java.lang.Short.TYPE,//
            java.lang.Integer.class, java.lang.Integer.TYPE, java.lang.Long.class, java.lang.Long.TYPE, //
            java.time.Year.class, java.time.Month.class),

    Decimal(true, true, false, java.math.BigInteger.class, java.math.BigDecimal.class),

    String(true, true, false, java.lang.Character.class, Character.TYPE, java.lang.String.class, java.time.YearMonth.class,//
            java.net.URL.class, java.net.URI.class, java.io.File.class),

    Date(true, true, false, java.sql.Date.class, java.time.LocalDate.class),

    Time(true, true, false, java.sql.Time.class, java.time.LocalTime.class, java.time.OffsetTime.class),

    Datetime(true, true, false, java.util.Date.class, java.sql.Timestamp.class, java.time.Instant.class, java.time.LocalDateTime.class,//
            java.time.chrono.JapaneseDate.class, java.time.OffsetDateTime.class, java.time.ZonedDateTime.class),

    Null(true, true, false, java.lang.Void.class),

    Collection(false, true, true, java.lang.reflect.Array.class, java.util.List.class, CollectionAccess.class),

    KeyPair(false, true, true, java.util.Map.class, KeyPairAccess.class),

    // -- other
    Func(false, false, false),
    Type(false, false, false),
    Arg(false, false, false),
    Field(false, false, false),
    Ref(false, false, false),;

    private final boolean    atomic;
    private final boolean    selfType;
    private final boolean    allowSubType;
    private final Class<?>[] refTypes;

    TypeType(boolean atomic, boolean selfType, boolean allowSubType, Class<?>... refTypes){
        this.atomic = atomic;
        this.selfType = selfType;
        this.allowSubType = allowSubType;
        this.refTypes = refTypes;
    }

    public static TypeType valueOfType(Class<?> javaType) {
        if (javaType.isArray()) {
            return TypeType.Collection;
        }

        for (TypeType type : TypeType.values()) {
            if (type.isSelfType()) {
                for (Class<?> refType : type.getRefTypes()) {
                    boolean test = !type.isAllowSubType() ? (refType == javaType) : refType.isAssignableFrom(javaType);
                    if (test) {
                        return type;
                    }
                }
            }
        }

        return Type;
    }
}
