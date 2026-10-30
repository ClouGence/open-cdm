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
package com.clougence.clouddm.console.web.global.mcp.utils;

import java.lang.reflect.Type;

/**
 * JSON helper class. It is supposed to be used by "tools" and "structured output" functionalities.
 */
public class Json {

    private Json(){
    }

    /**
     * The abstract JSON codec interface.
     */
    public interface JsonCodec {

        /**
         * Convert the given object to JSON.
         *
         * @param o the object to convert.
         * @return the JSON string.
         */
        String toJson(Object o);

        /**
         * Convert the given JSON string to an object of the given class.
         *
         * @param json the JSON string.
         * @param type the class of the object.
         * @param <T>  the type of the object.
         * @return the object.
         */
        <T> T fromJson(String json, Class<T> type);

        /**
         * Convert the given JSON string to an object of the given type.
         *
         * @param json the JSON string.
         * @param type the type of the object.
         * @param <T>  the type of the object.
         * @return the object.
         */
        <T> T fromJson(String json, Type type);
    }

    private static final JsonCodec CODEC = loadCodec();

    private static JsonCodec loadCodec() {
        return new JacksonJsonCodec();
    }

    /**
     * Convert the given object to JSON.
     *
     * @param o the object to convert.
     * @return the JSON string.
     */
    public static String toJson(Object o) {
        return CODEC.toJson(o);
    }

    /**
     * Convert the given JSON string to an object of the given class.
     *
     * @param json the JSON string.
     * @param type the class of the object.
     * @param <T>  the type of the object.
     * @return the object.
     */
    public static <T> T fromJson(String json, Class<T> type) {
        return CODEC.fromJson(json, type);
    }

    /**
     * Convert the given JSON string to an object of the given type.
     *
     * @param json the JSON string.
     * @param type the type of the object.
     * @param <T>  the type of the object.
     * @return the object.
     */
    public static <T> T fromJson(String json, Type type) {
        return CODEC.fromJson(json, type);
    }
}
