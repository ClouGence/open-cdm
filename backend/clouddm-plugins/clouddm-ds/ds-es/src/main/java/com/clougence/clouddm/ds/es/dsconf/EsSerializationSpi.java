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
package com.clougence.clouddm.ds.es.dsconf;

import com.clougence.clouddm.ds.es.i18n.EsDsI18nKeys;
import com.clougence.clouddm.sdk.execute.dsconf.SerializationService;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.utils.JsonUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;

@Slf4j
public class EsSerializationSpi implements SerializationService {

    public static final String PROVIDER_NAME = "ES";
    private final ObjectMapper objectMapper;

    public EsSerializationSpi(ClassLoader classLoader){
        this.objectMapper = new ObjectMapper();
        this.objectMapper.setTypeFactory(TypeFactory.defaultInstance().withClassLoader(classLoader));
    }

    @Override
    public String name() {
        return PROVIDER_NAME;
    }

    @Override
    public String encode(Object argData) {
        return JsonUtils.toJson(argData);
    }

    @Override
    public Object decode(String jsonData, Type tryType) {
        try {
            return this.objectMapper.readValue(jsonData, this.objectMapper.getTypeFactory().constructType(tryType));
        } catch (JsonProcessingException e) {
            // Jackson errors can contain credential values from the input JSON.
            String msg = "Decode ES configuration failed";
            IllegalArgumentException sanitized = new IllegalArgumentException(msg);
            sanitized.setStackTrace(e.getStackTrace());
            log.error(msg, sanitized);
            throw ThirdPartyApiException.as().with(sanitized, EsDsI18nKeys.ES_CONFIG_DECODE_ERROR);
        }
    }
}
