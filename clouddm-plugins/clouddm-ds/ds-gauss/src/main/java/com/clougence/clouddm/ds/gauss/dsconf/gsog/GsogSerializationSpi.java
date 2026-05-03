package com.clougence.clouddm.ds.gauss.dsconf.gsog;

import java.lang.reflect.Type;

import com.clougence.clouddm.sdk.execute.dsconf.SerializationService;
import com.clougence.utils.JsonUtils;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import lombok.SneakyThrows;

public class GsogSerializationSpi implements SerializationService {

    public static final String PROVIDER_NAME = "GaussDBForOpenGauss"; // ref DataSourceType.GaussDBForOpenGauss
    private final ObjectMapper objectMapper;

    public GsogSerializationSpi(ClassLoader classLoader){
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
