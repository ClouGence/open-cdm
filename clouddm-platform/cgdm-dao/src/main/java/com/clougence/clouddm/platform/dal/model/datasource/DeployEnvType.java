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
package com.clougence.clouddm.platform.dal.model.datasource;

import static com.clougence.clouddm.platform.dal.model.datasource.DeployEnvInfoFetchType.MANUALLY_FILL;
import static com.clougence.clouddm.platform.dal.model.datasource.DeployEnvInfoFetchType.OPENAPI;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import lombok.Getter;

/**
 * deploy environment Enum
 *
 * @author bucketli 2020/2/20 11:31
 */
public enum DeployEnvType {

    SELF_MAINTENANCE("SELF_MAINTENANCE", true, Collections.singletonList(MANUALLY_FILL)),
    ALIBABA_CLOUD_HOSTED("ALIBABA_CLOUD_HOSTED", true, Arrays.asList(OPENAPI, MANUALLY_FILL)),
    AWS_CLOUD_HOSTED("AWS_CLOUD_HOSTED", true, Collections.singletonList(MANUALLY_FILL)),
    MICROSOFT_AZURE_CLOUD_HOSTED("MICROSOFT_AZURE_CLOUD_HOSTED", true, Collections.singletonList(MANUALLY_FILL)),
    HUAWEI_CLOUD_HOSTED("HUAWEI_CLOUD_HOSTED", true, Collections.singletonList(MANUALLY_FILL)),
    TENCENT_CLOUD_HOSTED("TENCENT_CLOUD_HOSTED", true, Collections.singletonList(MANUALLY_FILL)),
    LOCAL("LOCAL", false, Collections.singletonList(MANUALLY_FILL)),
    INDEPENDENT_CLOUD_PLATFORM("INDEPENDENT_CLOUD_PLATFORM", true, Collections.singletonList(MANUALLY_FILL));

    @Getter
    private final String                       typeName;
    @Getter
    private final boolean                      enable;
    @Getter
    private final List<DeployEnvInfoFetchType> fetchTypeList;

    DeployEnvType(String typeName, boolean enable, List<DeployEnvInfoFetchType> list){
        this.typeName = typeName;
        this.enable = enable;
        this.fetchTypeList = list;
    }

}
