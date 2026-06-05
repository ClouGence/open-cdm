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
package com.clougence.clouddm.worker.component.notify;

import java.util.List;

import com.clougence.clouddm.api.console.sqlaudit.SqlStatus;
import com.clougence.clouddm.sdk.service.secrules.Requester;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.resultset.echo.Result;

public interface SidecarSqlNotifyService {

    /* ---------------------------------------------------------------------------------- */
    /*  SQL Audit (AutoExec)  */
    /* ---------------------------------------------------------------------------------- */

    void recodeSqlForAutoExec(String uid, String sql, Requester requester, Long dsId, String sessionId, List<String> levels);

    void finishForAutoExec(String sessionId, String message, Long affectLine, String sql, SqlStatus result, List<String> levels, Long dsId);

    void confirmSession(String sessionId);

    void rollbackSession(String sessionId);

    void startTransaction(String sessionId);

    /* ---------------------------------------------------------------------------------- */
    /*  SQL Audit (ConsoleQuery) */
    /* ---------------------------------------------------------------------------------- */

    void beginForConsoleQuery(QueryRequest query, String sessionId);

    void finishForConsoleQuery(QueryRequest query, Result result);

    /* ---------------------------------------------------------------------------------- */
    /*  File Audit  */
    /* ---------------------------------------------------------------------------------- */

}
