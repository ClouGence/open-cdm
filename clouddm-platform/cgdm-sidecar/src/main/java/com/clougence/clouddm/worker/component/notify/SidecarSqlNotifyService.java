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
