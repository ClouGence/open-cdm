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
package com.clougence.clouddm.console.web.component.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "clouddm.console.cluster")
public class ConsoleClusterProperties {
    public static final String INSTANCE_ID_PROPERTY = "clouddm.console.cluster.instance-id";

    public static boolean isValidInstanceId(String value) {
        return value != null && !value.isBlank();
    }

    public static String getInstanceIdOverride() {
        String property = System.getProperty(INSTANCE_ID_PROPERTY);
        if (property != null) {
            return property;
        }

        return System.getenv("CONSOLE_INSTANCE_ID");
    }

    public static String resolveInstanceId(String configuredInstanceId) {
        String override = getInstanceIdOverride();
        if (override != null && !override.isBlank()) {
            return override;
        }
        return configuredInstanceId;
    }

    public String getInstanceId() { return resolveInstanceId(instanceId); }

    private boolean enabled;
    private String  instanceId;
    private String  advertiseUrl;
    private String  secret;
}
