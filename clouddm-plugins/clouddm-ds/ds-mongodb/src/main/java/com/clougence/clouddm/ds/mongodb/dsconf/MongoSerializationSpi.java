package com.clougence.clouddm.ds.mongodb.dsconf;

import java.lang.reflect.Type;

import com.clougence.clouddm.sdk.execute.dsconf.SerializationService;
import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.utils.JsonUtils;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import lombok.SneakyThrows;

public class MongoSerializationSpi implements SerializationService {

    public static final String PROVIDER_NAME = DataSourceType.MongoDB.getTypeName(); // ref DataSourceType.Redis
    private final ObjectMapper objectMapper;

    public MongoSerializationSpi(ClassLoader classLoader){
        TypeFactory typeFactory = TypeFactory.defaultInstance().withClassLoader(classLoader);
        this.objectMapper = new ObjectMapper();
        this.objectMapper.setTypeFactory(typeFactory);
    }

    @Override
    public String name() {
        return PROVIDER_NAME;
    }

    @Override
    public String encode(Object argData) {
        return JsonUtils.toJson(argData);
    }

    @SneakyThrows
    @Override
    public Object decode(String jsonData, Type tryType) {
        JavaType paramJavaType = objectMapper.getTypeFactory().constructType(tryType);
        return objectMapper.readValue(jsonData, paramJavaType);
    }
}
