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
package com.clougence.clouddm.console.web.model.fo.datasource;

import com.clougence.clouddm.platform.dal.model.datasource.HostType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/26 16:23
 */
@Getter
@Setter
public class EnableDsQueryFO {

    @Min(value = 1, message = "datasource id must large than 0.")
    private long     dataSourceId;

    @Min(value = 1, message = "cluster id must large than 0.")
    private long     clusterId;

    @NotNull(message = "hostType can not be null.")
    private HostType hostType;
}
