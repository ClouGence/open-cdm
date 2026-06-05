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
package com.clougence.schema;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.clougence.schema.SchemaService.*;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.handlers.Handler;
import com.clougence.schema.handlers.Key;
import com.clougence.schema.handlers.KeyUtils;
import com.clougence.schema.mapping.FuncMapping;
import com.clougence.schema.mapping.TypeMapping;
import com.clougence.schema.metadata.FieldType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.function.EFunction;

/**
 * @version : 2013-4-10
 * @author 赵永春 (zyc@hasor.net)
 */
public class SchemaBinder {

    SchemaBinder(){
    }

    public void bindSqlBuilder(DsType dsType, SqlBuilder sqlBuilder) {
        SchemaFramework.addService(dsType, SqlBuilderService.class, () -> sqlBuilder);
    }

    public void bindTypes(DsType dsType, FieldType[] types, EFunction<String, FieldType, Exception> typeValueOfCode) {
        Map<String, FieldType> typeMap = new HashMap<>();
        Map<Integer, FieldType> codeNumMap = new HashMap<>();
        for (FieldType type : types) {
            typeMap.put(type.getCodeKey(), type);
            codeNumMap.put(type.getCodeNum(), type);
        }

        SchemaFramework.addService(dsType, FieldTypeDefService.class, () -> types);
        SchemaFramework.addService(dsType, FieldTypeNumService.class, codeNumMap::get);
        SchemaFramework.addService(dsType, FieldTypeService.class, s -> {
            FieldType fieldType = typeMap.get(s);
            if (fieldType == null) {
                return typeValueOfCode.eApply(s);
            } else {
                return fieldType;
            }
        });
    }

    public MappingBindingBuilder<FieldType> bindTypeMapping(final DsType sourceDs, final DsType targetDs) {
        return (sourceType, targetType, targetVersion) -> {
            TypeMappingService mappingService = SchemaFramework.getService(sourceDs, TypeMappingService.class);
            if (mappingService == null) {
                SchemaFramework.addService(sourceDs, TypeMappingService.class, new TypeMapping());
                mappingService = SchemaFramework.getService(sourceDs, TypeMappingService.class);
            }

            Map<String, String> attr = new HashMap<>();
            mappingService.addMapping(targetDs, sourceType, targetType, targetVersion, attr);
            return (AdditionInfoBindingBuilder) attr::putAll;
        };
    }

    public MappingBindingBuilder<String> bindFuncMapping(final DsType sourceDs, final DsType targetDs) {
        return (sourceFunc, targetFunc, targetVersion) -> {
            FunctionMappingService mappingService = SchemaFramework.getService(sourceDs, FunctionMappingService.class);
            if (mappingService == null) {
                SchemaFramework.addService(sourceDs, FunctionMappingService.class, new FuncMapping());
                mappingService = SchemaFramework.getService(sourceDs, FunctionMappingService.class);
            }

            Map<String, String> attr = new HashMap<>();
            sourceFunc = sourceFunc.toUpperCase();
            mappingService.addMapping(targetDs, sourceFunc, targetFunc, targetVersion, attr);
            return (AdditionInfoBindingBuilder) attr::putAll;
        };
    }

    public HandlerBinding bindTableHandler(DsType srcDsType, DsType dstDsType) {
        return bindTableHandler(srcDsType, dstDsType, null);
    }

    public HandlerBinding bindTableHandler(DsType srcDsType, DsType dstDsType, MainVersion mainVersion) {
        return handlers -> {
            for (Key key : KeyUtils.buildMappingKey(srcDsType, dstDsType, mainVersion)) {
                SchemaFramework.addHandlers(key, handlers);
            }
        };
    }

    public void initMappingService(DsType dsType) {
        TypeMappingService typeService = SchemaFramework.getService(dsType, TypeMappingService.class);
        FunctionMappingService funService = SchemaFramework.getService(dsType, FunctionMappingService.class);

        if (typeService == null) {
            SchemaFramework.addService(dsType, TypeMappingService.class, new TypeMapping());
        }
        if (funService == null) {
            SchemaFramework.addService(dsType, FunctionMappingService.class, new FuncMapping());
        }
    }

    public static interface HandlerBinding {

        void to(Handler... handlers);
    }

    public static interface MappingBindingBuilder<T> {

        default AdditionInfoBindingBuilder mapping(T source, T target) {
            return mapping(source, target, null);
        }

        AdditionInfoBindingBuilder mapping(T source, T target, MainVersion targetVersion);

    }

    public static interface AdditionInfoBindingBuilder {

        default void additionInfo(String key, String value) {
            additionInfo(Collections.singletonMap(key, value));
        }

        void additionInfo(Map<String, String> data);
    }

}
