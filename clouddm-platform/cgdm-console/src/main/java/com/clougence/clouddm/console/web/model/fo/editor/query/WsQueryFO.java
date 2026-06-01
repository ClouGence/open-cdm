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
package com.clougence.clouddm.console.web.model.fo.editor.query;

import java.util.List;

import com.clougence.clouddm.console.web.model.fo.editor.WsRequestFO;
import com.clougence.clouddm.sdk.execute.resultset.echo.ReceiveMode;
import com.clougence.clouddm.sdk.execute.session.QueryArg;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WsQueryFO extends WsRequestFO {

    private String         sessionId;
    private WsQueryType    queryType;

    private String         queryString;
    private List<QueryArg> queryArgs;
    private boolean        force;
    private ReceiveMode    receiveMode;

    private boolean        rdbAutoCommit;
    private boolean        rdbReadOnly;
    private RdbIsolation   rdbIsolation;

    private boolean        viewOriginData;

    @Override
    public String resultOriginal() {
        return this.queryType == null ? "" : this.queryType.name();
    }

    @Override
    public String resultSessionId() {
        return this.sessionId;
    }
}
