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
package com.clougence.clouddm.console.web.util;

import java.util.Map;

import com.clougence.utils.ExceptionUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2021/1/6 11:25
 */
@Slf4j
public class RdpJacksonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // refer to:https://stackoverflow.com/questions/69811740/how-do-i-enable-the-jsr310-support-for-localdate-using-jackson
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static <T> T readJson(String message, Class<T> mapClass) {
        try {
            return objectMapper.readerFor(mapClass).readValue(message);
        } catch (Exception e) {
            String msg = "read form json string error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    public static <T> T toObj(String jsonStr, Class<T> clz) {
        try {
            return objectMapper.readValue(jsonStr, clz);
        } catch (JsonProcessingException e) {
            String msg = "toObj form json string error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    public static <T> T readJsonWithUnknown(String message, Class<T> mapClass) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.readerFor(mapClass).readValue(message);
        } catch (Exception e) {
            String msg = "read form json string error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }

    public static <T> T readFromMap(Map<String, Object> message, Class<T> mapClass) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return objectMapper.convertValue(message, mapClass);
        } catch (Exception e) {
            String msg = "read form json string error.msg:" + ExceptionUtils.getRootCauseMessage(e);
            log.error(msg, e);
            throw new RuntimeException(msg, e);
        }
    }
}
