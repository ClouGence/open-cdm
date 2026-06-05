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
package com.clougence.clouddm.console.web.service.system;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DsVersionsServiceImpl {

    private Object buildInfo() {
        Properties properties = new Properties();
        try {
            InputStream asStream = DsVersionsServiceImpl.class.getClassLoader().getResourceAsStream("build-info.properties");
            if (asStream != null) {
                properties.load(asStream);
            }
        } catch (Exception ignore) {
            // ignore
        }

        if (properties.isEmpty()) {
            properties.put("mainVersion", "unknown");
            properties.put("time", "unknown");
        }
        return properties;
    }

    public Map<String, Object> fetchDsVersions() {
        Map<String, Object> allVersions = new HashMap<>();

        allVersions.put("buildInfo", buildInfo());

        return allVersions;
    }

}
