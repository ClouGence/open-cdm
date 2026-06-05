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
package com.clougence.clouddm.sdk.model.faker;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class FakerConfigDTO {

    private DataSourceType       dsType;
    private long                 dsId;
    private String               catalog;
    private String               schema;
    private boolean              transaction;
    private boolean              ignoreErrors;
    private Integer              pThreadCnt;
    private Integer              wThreadCnt;
    private String               yaml;
    private FakerRunModel        runModel;

    // for full
    private Map<String, Integer> writerTotal;

    // for increment
    private Integer              workingTime;
    private Integer              insertRatio;
    private Integer              updateRatio;
    private Integer              deleteRatio;
}
