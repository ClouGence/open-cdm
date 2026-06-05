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
package com.clougence.clouddm.dsfamily.execute;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.sdk.execute.ExecuteVariables;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.QueryResultConf;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionSpi;
import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.clougence.utils.HexadecimalUtils;
import com.clougence.utils.RandomUtils;
import com.clougence.utils.StringUtils;

public class RdbSessionSpi implements SessionSpi {

    @Override
    public SessionContextDTO createSessionContext(DataSourceConfig dsConfig, Map<String, Object> params) {
        String defaultDb = dsConfig.getDefaultDataBase();
        String defaultSchema = dsConfig.getDefaultSchema();

        if (params != null) {
            if (params.containsKey(PARAMS_DEFAULT_DB)) {
                defaultDb = (String) params.getOrDefault(PARAMS_DEFAULT_DB, defaultDb);
            }
            if (params.containsKey(PARAMS_DEFAULT_SCHEMA)) {
                defaultSchema = (String) params.getOrDefault(PARAMS_DEFAULT_SCHEMA, defaultSchema);
            }
        }

        SessionContextDTO contextDTO = new SessionContextDTO();
        contextDTO.setMaxIdleTimeSec(dsConfig.getMaxIdleTimeSec());
        contextDTO.setSessionId(UUID.randomUUID().toString().replace("-", ""));

        contextDTO.setRdbCatalog(defaultDb);
        contextDTO.setRdbSchema(defaultSchema);
        contextDTO.setRdbAutoCommit(dsConfig.getAutoCommit() == null || dsConfig.getAutoCommit());
        contextDTO.setRdbTxIsolation(RdbIsolation.valueOfCode(dsConfig.getIsolation()));
        contextDTO.setRdbReadOnly(Boolean.TRUE.equals(dsConfig.getReadOnly()));
        return contextDTO;
    }

    @Override
    public QueryRequest createQueryRequest(SessionContextDTO contextDTO, DataSourceConfig dsConfig, Map<String, Object> params, String uid, String clientIp, boolean console) {
        // request
        QueryRequest query = new QueryRequest();
        query.setQueryId(newQueryId());
        query.setUsingValueProcess(false);
        query.setQueryDsType(dsConfig.getDataSourceType());
        query.setRequestTime(new Date());

        query.setVariables(new HashMap<>());
        query.getVariables().put(ExecuteVariables.CURRENT_CATALOG, (String) params.get(SessionSpi.PARAMS_DEFAULT_DB));
        query.getVariables().put(ExecuteVariables.CURRENT_SCHEMA, (String) params.get(SessionSpi.PARAMS_DEFAULT_SCHEMA));
        query.getVariables().put(ExecuteVariables.CURRENT_UID, uid);
        query.getVariables().put(ExecuteVariables.CLIENT_IP, clientIp);

        // result
        QueryResultConf result = new QueryResultConf();
        result.setFetchMoreResult(true);

        // return
        query.setResultConf(result);
        return query;
    }

    @Override
    public String newQueryId() {
        return ("Q" + StringUtils.leftPad(Long.toString(System.currentTimeMillis(), 16), 11, '0') + //
                StringUtils.leftPad(HexadecimalUtils.bytes2hex(RandomUtils.nextBytes(2)), 4, '0'))
            .toLowerCase();
    }
}
