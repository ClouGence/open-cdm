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
package com.clougence.clouddm.ds.mongodb.dsconf;

import com.clougence.utils.StringUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import lombok.Getter;

@Getter
public enum MongoConnectType {
    DEFAULT("default", "mongodb"),
    SRV("srv", "mongodb+srv"),;

    @JsonValue
    private final String code;
    private final String uriScheme;

    MongoConnectType(String code, String uriScheme){
        this.code = code;
        this.uriScheme = uriScheme;
    }

    @Override
    public String toString() {
        return this.code;
    }

    @JsonCreator
    public static MongoConnectType of(String code) {
        if (StringUtils.isBlank(code)) {
            return DEFAULT;
        }

        for (MongoConnectType connectType : values()) {
            if (connectType.code.equals(code)) {
                return connectType;
            }
        }
        throw new IllegalArgumentException("unsupported MongoDB connect type: " + code);
    }
}
