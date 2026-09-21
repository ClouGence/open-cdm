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
package com.clougence.clouddm.worker.component.session.result;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.clougence.clouddm.sdk.execute.resultset.echo.Result;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.ResultBuilder.ResultSetRowsBuild;

abstract class JdbcResultSetRowsBuild<T extends Result> extends AbstractResultBuild<T> implements ResultSetRowsBuild {

    JdbcResultSetRowsBuild(String resultId, String sessionID, QueryRequest query, ResultListenerContainer listeners){
        super(resultId, sessionID, query, listeners);
    }

    @Override
    public final void receiveRow(boolean silent, ResultSet resultSet) throws SQLException, IOException {
        this.columnBindings().bind(resultSet);
        this.receiveBoundRow(silent);
    }

    protected abstract JdbcResultSetColumnBindings columnBindings();

    protected abstract void receiveBoundRow(boolean silent) throws SQLException, IOException;
}
