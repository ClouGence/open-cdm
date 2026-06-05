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
package com.clougence.schema.mapping;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.clougence.utils.StringUtils;

class MappingInfo<T> {

    private final T                                source;
    private T                                      targetDefault;
    private final Map<String, T>                   targetVersions;
    private Map<String, String>                    attrDefault;
    private final Map<String, Map<String, String>> attrVersions;

    public MappingInfo(T mappingSource, T mappingTarget){
        this.source = mappingSource;
        this.targetVersions = new HashMap<>();
        this.attrVersions = new HashMap<>();
        this.targetDefault = mappingTarget;
        this.targetVersions.put("*", mappingTarget);
    }

    public void addVersion(String targetVersion, T mappingTarget) {
        if (StringUtils.isBlank(targetVersion)) {
            return;
        }

        if (this.targetDefault == null || "*".equals(targetVersion)) {
            this.targetDefault = mappingTarget;
        }

        if (!"*".equals(targetVersion)) {
            this.targetVersions.put(targetVersion.toLowerCase(), mappingTarget);
        }
    }

    public void addAttr(String targetVersion, Map<String, String> attrMap) {
        if (StringUtils.isBlank(targetVersion) || attrMap == null || attrMap.isEmpty()) {
            return;
        }

        if (this.attrDefault == null || "*".equals(targetVersion)) {
            this.attrDefault = attrMap;
        }

        if (!"*".equals(targetVersion)) {
            this.attrVersions.computeIfAbsent(targetVersion.toLowerCase(), s -> new HashMap<>()).putAll(attrMap);
        }
    }

    public T getTarget(String targetVersion) {
        if (targetVersion != null) {
            targetVersion = targetVersion.toLowerCase();
        }
        if (targetVersion == null || !this.targetVersions.containsKey(targetVersion)) {
            return this.targetDefault;
        } else {
            return this.targetVersions.get(targetVersion);
        }
    }

    public Map<String, String> getAttrMap(String targetVersion) {
        if (targetVersion != null) {
            targetVersion = targetVersion.toLowerCase();
        }
        if (targetVersion == null || !this.attrVersions.containsKey(targetVersion)) {
            return this.attrDefault == null ? Collections.emptyMap() : this.attrDefault;
        } else {
            return this.attrVersions.get(targetVersion);
        }
    }

    @Override
    public String toString() {
        return "Ent: from " + this.source + " to " + this.targetDefault;
    }

}
