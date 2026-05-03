package com.clougence.schema;

import java.util.Map;
import java.util.function.Supplier;

import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.editor.provider.SqlBuilder;
import com.clougence.schema.metadata.FieldType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.function.EFunction;

/**
 * @version : 2013-4-10
 * @author 赵永春 (zyc@hasor.net)
 */
public interface SchemaService {

    // find its Dialect by DsType
    interface DialectService extends SchemaService, Supplier<Dialect> {
    }

    // find its SqlBuilder by DsType
    interface SqlBuilderService extends SchemaService, Supplier<SqlBuilder> {
    }

    // find its MainVersion by DsType and version info
    interface MainVersionService extends SchemaService, EFunction<String, MainVersion, Exception> {
    }

    // find its FieldType[] of DsType
    interface FieldTypeDefService extends SchemaService, Supplier<FieldType[]> {
    }

    // find its FieldType by name of DsType
    interface FieldTypeService extends SchemaService, EFunction<String, FieldType, Exception> {
    }

    // find its FieldType by name of DsType
    interface FieldTypeNumService extends SchemaService, EFunction<Integer, FieldType, Exception> {
    }

    // find its other DsType FieldType mapping by DsType
    interface TypeMappingService extends SchemaService, BasicMappingService<FieldType> {
    }

    // find its other DsType function mapping by DsType
    interface FunctionMappingService extends SchemaService, BasicMappingService<String> {
    }

    interface BasicMappingService<T> {

        Map<T, T> getMapping(DsType targetDsType, MainVersion targetVersion);

        T findMapping(T srcType, DsType targetDsType, MainVersion targetVersion);

        void addMapping(DsType targetDsType, T sourceType, T targetType, MainVersion targetVersion, Map<String, String> attr);
    }

}
