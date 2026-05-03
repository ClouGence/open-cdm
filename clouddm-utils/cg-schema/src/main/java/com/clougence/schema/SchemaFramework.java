package com.clougence.schema;

import java.util.*;
import java.util.stream.Collectors;

import com.clougence.schema.SchemaService.*;
import com.clougence.schema.dialect.Dialect;
import com.clougence.schema.handlers.Handler;
import com.clougence.schema.handlers.Key;
import com.clougence.schema.metadata.FieldType;
import com.clougence.schema.metadata.MainVersion;
import com.clougence.utils.CollectionUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @version : 2013-4-10
 * @author 赵永春 (zyc@hasor.net)
 */
@Slf4j
public final class SchemaFramework {

    private static final Map<DsType, Map<Class<?>, SchemaService>> dsServicesMap = new HashMap<>();
    private static final Map<Key, List<Handler>>                   handlerMap    = new HashMap<>();

    private SchemaFramework(){
    }

    public static void reset() {
        dsServicesMap.clear();
        handlerMap.clear();
    }

    public static void install(SchemaPlugin plugin) {
        log.info("SchemaPlugin install " + plugin.getClass().getName());
        plugin.init(new SchemaBinder());
    }

    // ----------------------------------------------------------------------------------------------------------------

    protected static <T extends SchemaService> void addService(DsType dsType, Class<T> serviceType, T service) {
        Map<Class<?>, SchemaService> serviceMap = dsServicesMap.computeIfAbsent(dsType, type -> new HashMap<>());
        serviceMap.put(serviceType, service);
    }

    public static <T extends SchemaService> T getService(DsType dsType, Class<T> serviceType) {
        if (dsType == null) {
            return null;
        }
        Map<Class<?>, SchemaService> serviceMap = dsServicesMap.get(dsType);
        if (serviceMap == null) {
            return null;
        }
        return (T) serviceMap.get(serviceType);
    }

    public static Dialect getDialect(DsType dsType) {
        DialectService dialectService = getService(dsType, DialectService.class);
        Dialect dialect = (dialectService != null) ? dialectService.get() : null;
        return Objects.requireNonNull(dialect, dsType.getTypeName() + " Dialect undefined.");
    }

    public static MainVersion getMainVersion(DsType dsType, String version) {
        MainVersionService versionService = getService(dsType, MainVersionService.class);
        return (versionService != null) ? versionService.apply(version) : null;
    }

    public static FieldType getFieldType(DsType dsType, String typeName) {
        FieldTypeService fieldTypeService = getService(dsType, FieldTypeService.class);
        return (fieldTypeService != null) ? fieldTypeService.apply(typeName) : null;
    }

    public static FieldType getFieldType(DsType dsType, int typeNum) {
        FieldTypeNumService fieldTypeService = getService(dsType, FieldTypeNumService.class);
        return (fieldTypeService != null) ? fieldTypeService.apply(typeNum) : null;
    }

    public static TypeMappingService getTypeMappingService(DsType dsType) {
        return getService(dsType, TypeMappingService.class);
    }

    public static FunctionMappingService getFunctionMappingService(DsType dsType) {
        return getService(dsType, FunctionMappingService.class);
    }

    // ----------------------------------------------------------------------------------------------------------------

    protected static void addHandlers(Key key, Handler[] handlers) {
        handlerMap.computeIfAbsent(key, handlerKey -> new ArrayList<>()).addAll(Arrays.asList(handlers));
    }

    public static <T extends Handler> List<T> getHandlers(Key[] keys, Class<T> handlerType) {
        if (keys == null || keys.length == 0) {
            return Collections.emptyList();
        }

        List<T> resultList = new ArrayList<>();
        for (Key key : keys) {
            List<Handler> handlers = handlerMap.get(key);
            if (CollectionUtils.isNotEmpty(handlers)) {
                List<T> list = handlers.stream().filter(handlerType::isInstance).map(o -> (T) o).collect(Collectors.toList());
                for (T item : list) {
                    if (!resultList.contains(item)) {
                        resultList.add(item);
                    }
                }
            }
        }
        return resultList;
    }

}
