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
package com.clougence.clouddm.console.web.model.vo;

import com.clougence.clouddm.base.metadata.ds.ConfigValType;
import com.clougence.clouddm.base.metadata.ds.DsConfigGroup;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2022/8/10 11:26:59
 */
@Getter
@Setter
public class DefaultDsKvConfigVO {

    private String        configName;
    private DsConfigGroup configGroup;
    private String        description;
    private boolean       valueRequire;
    private String        defaultValue;
    private String        valueAdvance;
    private ConfigValType confValType;
    private boolean       lazy;
}
