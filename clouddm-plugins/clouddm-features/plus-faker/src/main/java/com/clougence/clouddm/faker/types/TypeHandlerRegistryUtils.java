/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
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
package com.clougence.clouddm.faker.types;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;

/**
 * @author 赵永春 (zyc@hasor.net)
 * @version : 2020-09-12
 */
@Slf4j
public class TypeHandlerRegistryUtils {

    public static final TypeHandlerRegistry   DEFAULT        = new TypeHandlerRegistry();
    public static final Map<String, Class<?>> typeHandlerMap = new ConcurrentHashMap<>();

    static {
        typeHandlerMap.put(replaceMysqlName(MySqlBitAsStringTypeHandler.class.getName()), MySqlBitAsStringTypeHandler.class);
        typeHandlerMap.put(replaceMysqlName(OffsetDateTimeAsZonedDateTimeTypeHandler.class.getName()), OffsetDateTimeAsZonedDateTimeTypeHandler.class);
        typeHandlerMap.put(replacePgName(PgArrayTypeHandler.class.getName()), PgArrayTypeHandler.class);
        typeHandlerMap.put(replacePgName(PgArrayTypeHandlerFactory.class.getName()), PgArrayTypeHandlerFactory.class);
        typeHandlerMap.put(replacePgName(PgMoneyAsBigDecimalTypeHandler.class.getName()), PgMoneyAsBigDecimalTypeHandler.class);
        typeHandlerMap.put(replacePgName(SqlTimestampAsLocalDateTypeHandler.class.getName()), SqlTimestampAsLocalDateTypeHandler.class);
        typeHandlerMap.put(replacePgName(SqlTimestampAsLocalTimeTypeHandler.class.getName()), SqlTimestampAsLocalTimeTypeHandler.class);
        typeHandlerMap.put(replaceMysqlName(StringAsBigDecimalTypeHandler.class.getName()), StringAsBigDecimalTypeHandler.class);
        typeHandlerMap.put(replaceMysqlName(JtsGeometryWktAsWkbTypeHandler.class.getName()), JtsGeometryWktAsWkbTypeHandler.class);
        typeHandlerMap.put(replaceMysqlName(JtsGeometryWktAsWkbTypeHandler.class.getName()), JtsGeometryWktAsWkbTypeHandler.class);
    }

    private static String replaceMysqlName(String oldName) {
        return oldName.replace(".clouddm", "");
    }

    private static String replacePgName(String oldName) {
        String replace = oldName.replace(".clouddm", "");
        return replace.replace("faker.types", "faker.utils.types"); // for pg SqlTimestampAsLocalDateTypeHandler
    }

    public static TypeHandler<?> getTypeHandler(Class<?> type) {
        return DEFAULT.getTypeHandler(type);
    }

    public static TypeHandler<?> getDefaultTypeHandler() { return DEFAULT.getUnknownTypeHandler(); }
}
