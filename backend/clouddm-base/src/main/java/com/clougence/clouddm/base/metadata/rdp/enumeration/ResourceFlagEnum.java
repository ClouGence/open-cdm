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

import lombok.Getter;

/**
 * @author chunlin create time is 2024/7/19
 */
@Getter
public enum ResourceFlagEnum {
    DATASOURCE("DATASOURCE", "InstantId"),
    ACCOUNT("ACCOUNT", "Username"),
    ROLE("ROLE", "RoleName"),
    DS_ENV("DS_ENV", "DsEnvName"),
    DATA_JOB("DATA_JOB", "DataJobName");

    private final String resourceFlag;
    private final String flagDesc;

    ResourceFlagEnum(String resourceFlag, String flagDesc){
        this.resourceFlag = resourceFlag;
        this.flagDesc = flagDesc;
    }

    public static String getFlagDesc(String resourceFlag) {
        for (ResourceFlagEnum value : values()) {
            if (value.resourceFlag.equals(resourceFlag)) {
                return value.flagDesc;
            }
        }
        return null;
    }
}
