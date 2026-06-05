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
package com.clougence.clouddm.console.web.service.editor.query;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbSupportLevel;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbSupportSpi;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class QueryCtx {

    private DsLevels               levels;
    private final DataSourceConfig dsConfig;
    private SessionContextDTO      ctxDTO;
    private Map<String, Object>    ctxParams;
    private final SessionSpi       sessionSpi;

    private final boolean          supportSwitchCatalog;
    private final boolean          supportSwitchSchema;
    private final boolean          supportSwitchIsolation;
    private final boolean          supportChangeAutoCommit;
    private final boolean          supportChangeReadOnly;

    private boolean                hasUnCommitted;
    private QueryStatus            queryStatus;
    private long                   startTime;
    private long                   prepareCost;
    private long                   queryCost;
    private long                   receiveCost;
    private final AtomicLong       receiveTimes;

    public QueryCtx(DsLevels levels, DataSourceConfig dsConfig, SessionContextDTO ctxDTO, Map<String, Object> ctxParams,//
                    SessionSpi sessionSpi, RdbSupportSpi supportSpi){
        this.levels = levels;
        this.dsConfig = dsConfig;
        this.ctxDTO = ctxDTO;
        this.ctxParams = ctxParams;
        this.sessionSpi = sessionSpi;
        this.supportSwitchCatalog = supportSpi.supportChangeCatalog(dsConfig) != RdbSupportLevel.No;
        this.supportSwitchSchema = supportSpi.supportChangeSchema(dsConfig) != RdbSupportLevel.No;
        this.supportSwitchIsolation = supportSpi.supportChangeIsolation(dsConfig) != RdbSupportLevel.No;
        this.supportChangeAutoCommit = supportSpi.supportChangeAutoCommit(dsConfig) != RdbSupportLevel.No;
        this.supportChangeReadOnly = supportSpi.supportChangeReadOnly(dsConfig) != RdbSupportLevel.No;
        this.hasUnCommitted = false;

        this.queryStatus = QueryStatus.Free;
        this.receiveTimes = new AtomicLong();
    }

    public void resetStatus() {
        this.queryStatus = QueryStatus.Free;
        this.prepareCost = 0;
        this.queryCost = 0;
        this.receiveCost = 0;
        this.receiveTimes.set(0);
    }

    public void incrementReceiveTimes() {
        this.receiveTimes.incrementAndGet();
    }
}
