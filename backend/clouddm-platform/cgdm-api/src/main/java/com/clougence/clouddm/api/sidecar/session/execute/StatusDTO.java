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
package com.clougence.clouddm.api.sidecar.session.execute;

import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class StatusDTO {

    // it not going to change.
    private Integer          maxIdleTimeSec;
    private long             createTime;

    // will change.
    private String           curCatalog;
    private String           curSchema;
    private boolean          autoCommit;
    private boolean          readOnly;
    private RdbIsolation     isolation;
    private boolean          hasUnCommitted;

    // instantaneous

    private volatile boolean executing;
    private volatile String  curBatchId;
    private volatile String  curQueryId;
    private volatile long    lastRequestTime;

    private volatile int     waitBatchSize;
    private volatile int     waitQuerySize;
    private volatile int     eventGoods;
}
