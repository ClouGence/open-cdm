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
package com.clougence.clouddm.base.metadata.rdp.enumeration;

/**
 * @author <a href="https://gitee.com/LongLiS">cloudconceal</a> 2025/2/12 14:34
 */
public enum KafkaSecurityProtocol {

    SASL_SSL,
    SASL_PLAINTEXT,
    PLAINTEXT;

    public static KafkaSecurityProtocol valueOfCode(String mechanism) {
        for (KafkaSecurityProtocol value : KafkaSecurityProtocol.values()) {
            if (value.name().equals(mechanism)) {
                return value;
            }
        }

        return null;
    }

}
