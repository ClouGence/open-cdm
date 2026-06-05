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
 */package com.clougence.clouddm.worker.component.rsocket;

import java.lang.reflect.Type;

import com.clougence.clouddm.comm.RSocketSerialization;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.dsconf.SerializationService;
import com.clougence.utils.JsonUtils;
import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

public class RSocketSerializationImpl implements RSocketSerialization {

    public static final RSocketSerialization DEFAULT      = new RSocketSerializationImpl();
    private final ObjectMapper               objectMapper = new ObjectMapper();

    private RSocketSerializationImpl(){
    }

    @Override
    public String encode(String provider, Object argData) {
        if (StringUtils.isBlank(provider)) {
            return JsonUtils.toJson(argData);
        } else {
            SerializationService service = findSerializationService(provider);
            return service.encode(argData);
        }
    }

    @SneakyThrows
    @Override
    public Object decode(String provider, String jsonData, Type tryType) {
        if (StringUtils.isBlank(provider)) {
            JavaType paramJavaType = objectMapper.getTypeFactory().constructType(tryType);
            return this.objectMapper.readValue(jsonData, paramJavaType);
        } else {
            SerializationService service = findSerializationService(provider);
            return service.decode(jsonData, tryType);
        }
    }

    private SerializationService findSerializationService(String provider) {
        SerializationService service = PluginManager.findDsSpi(SerializationService.class, provider);
        if (service == null) {
            throw new IllegalStateException("SerializationService not found for provider: " + provider);
        }
        return service;
    }
}
