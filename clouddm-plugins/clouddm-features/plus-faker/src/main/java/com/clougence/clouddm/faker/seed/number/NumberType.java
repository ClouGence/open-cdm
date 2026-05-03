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
package com.clougence.clouddm.faker.seed.number;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.clougence.utils.StringUtils;

/**
 * 数值 具体类型
 * @version : 2022-07-25
 * @author 赵永春 (zyc@hasor.net)
 */
public enum NumberType {

    Bool(Boolean.class),
    Byte(Byte.class),
    Short(Short.class),
    Integer(Integer.class),
    Int(Integer.class),
    Long(Long.class),
    Float(Float.class),
    Double(Double.class),
    BigInt(BigInteger.class),
    Decimal(BigDecimal.class);

    private final Class<?> dateType;

    NumberType(Class<?> dateType){
        this.dateType = dateType;
    }

    public Class<?> getDateType() { return this.dateType; }

    public static NumberType valueOfCode(String name) {
        for (NumberType numberType : NumberType.values()) {
            if (StringUtils.equalsIgnoreCase(numberType.name(), name)) {
                return numberType;
            }
        }
        return null;
    }
}
