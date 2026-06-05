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

import com.clougence.clouddm.sdk.execute.resultset.echo.ReceiveMode;
import com.clougence.utils.format.WellKnowFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryResultConf extends ResultLimit implements Cloneable {

    // result fetch
    private boolean        fetchMoreResult   = false;
    private boolean        cacheResult       = false;
    private boolean        refreshStatus     = false;
    private boolean        returnAutoIncrKey = false;
    private ReceiveMode    receiveMode       = ReceiveMode.PAGINATED;

    // display format
    private int            displayChars;
    private WellKnowFormat dataFormat;
    private WellKnowFormat timeFormat;
    private WellKnowFormat dataTimeFormat;
    private WellKnowFormat timeWithZoneFormat;
    private WellKnowFormat dataTimeWithZoneFormat;

    @Override
    public QueryResultConf clone() {
        QueryResultConf conf = new QueryResultConf();

        conf.setFetchRecordCountLimit(this.getFetchRecordCountLimit());
        conf.setFetchResultSetBytesLimit(this.getFetchResultSetBytesLimit());
        conf.setFetchColumnBytesLimit(this.getFetchColumnBytesLimit());
        conf.setFetchElementBytesLimit(this.getFetchElementBytesLimit());
        conf.setFetchPageSize(this.getFetchPageSize());
        conf.setDisplayChars(this.getDisplayChars());
        conf.setQueryTimeoutSec(this.getQueryTimeoutSec());
        conf.fetchMoreResult = this.fetchMoreResult;
        conf.cacheResult = this.cacheResult;
        conf.returnAutoIncrKey = this.returnAutoIncrKey;
        conf.receiveMode = this.receiveMode;
        conf.displayChars = this.displayChars;
        conf.dataFormat = this.dataFormat;
        conf.timeFormat = this.timeFormat;
        conf.dataTimeFormat = this.dataTimeFormat;
        conf.timeWithZoneFormat = this.timeWithZoneFormat;
        conf.dataTimeWithZoneFormat = this.dataTimeWithZoneFormat;
        return conf;
    }
}
