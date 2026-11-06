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
package com.clougence.clouddm.ds.iotdb.execute;

import java.util.Map;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.ds.iotdb.dsconf.IotdbSqlDialect;
import com.clougence.clouddm.dsfamily.execute.RdbSessionSpi;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.utils.StringUtils;

public class IotdbSessionSpi extends RdbSessionSpi {

    @Override
    public SessionContextDTO createSessionContext(DataSourceConfig config, Map<String, Object> params) {
        SessionContextDTO context = super.createSessionContext(config, params);
        String dialectName = null;
        if (params != null) {
            dialectName = (String) params.get(IotdbSqlDialect.DRIVER_PROPERTY);
        }
        if (dialectName == null && StringUtils.isNotBlank(context.getRdbCatalog())) {
            dialectName = context.getRdbCatalog();
        }
        IotdbSqlDialect dialect = IotdbSqlDialect.parse(dialectName);
        context.setRdbCatalog(dialect.name());
        context.setSqlParameters(Map.of(IotdbSqlDialect.DRIVER_PROPERTY, dialect.name()));
        return context;
    }

    @Override
    public QueryRequest createQueryRequest(DataSourceConfig config) {
        QueryRequest request = super.createQueryRequest(config);
        request.getResultConf().setFetchMoreResult(false);
        return request;
    }
}
