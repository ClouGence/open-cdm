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
package com.clougence.clouddm.console.web.model.fo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2023/11/23 16:47:05
 */
@Getter
@Setter
public class AddProductClusterFO {

    @NotNull(message = "{notnull.product}")
    private String product;

    @NotBlank(message = "{notblank.productversion}")
    private String productVersion;

    private String clusterDesc;

    private String clusterCode;

    @NotBlank(message = "{notblank.apiaddr}")
    private String apiAddr;
}
