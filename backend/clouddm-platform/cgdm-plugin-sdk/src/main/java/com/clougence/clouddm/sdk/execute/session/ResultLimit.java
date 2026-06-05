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
package com.clougence.clouddm.sdk.execute.session;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResultLimit implements Cloneable {

    // some limit
    private long fetchRecordCountLimit;
    private long fetchResultSetBytesLimit;
    private long fetchColumnBytesLimit;
    private long fetchElementBytesLimit;
    private long fetchPageSize;
    private int  queryTimeoutSec;

    @Override
    public ResultLimit clone() {
        ResultLimit conf = new ResultLimit();

        conf.fetchRecordCountLimit = this.fetchRecordCountLimit;
        conf.fetchResultSetBytesLimit = this.fetchResultSetBytesLimit;
        conf.fetchColumnBytesLimit = this.fetchColumnBytesLimit;
        conf.fetchElementBytesLimit = this.fetchElementBytesLimit;
        conf.fetchPageSize = this.fetchPageSize;
        conf.queryTimeoutSec = this.queryTimeoutSec;
        return conf;
    }
}
