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

import java.sql.Connection;
import java.util.Map;

import com.clougence.clouddm.ds.iotdb.dsconf.IotdbConfig;
import com.clougence.clouddm.ds.iotdb.dsconf.IotdbSqlDialect;
import com.clougence.clouddm.sdk.execute.resource.DsResourceManager;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionFactory;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.drivers.DsObject;

public class IotdbSessionFactory implements SessionFactory<IotdbConfig> {

    @Override
    public Session createSession(DsResourceManager resourceManager, IotdbConfig config, SessionContextDTO context) throws Exception {
        Map<String, String> parameters = context.getSqlParameters();
        IotdbSqlDialect dialect = IotdbSqlDialect.TREE;
        if (parameters != null) {
            dialect = IotdbSqlDialect.parse(parameters.get(IotdbSqlDialect.DRIVER_PROPERTY));
        }

        String database = null;
        if (dialect == IotdbSqlDialect.TABLE) {
            database = context.getRdbSchema();
        }
        DsObject<Connection> resource = resourceManager.requestResource(config.forSession(dialect, database));
        try {
            IotdbJdbcSession jdbc = new IotdbJdbcSession(resource.getTarget());
            IotdbSession session = new IotdbSession(context.getSessionId(), config, resource, jdbc);
            session.initSession(resourceManager, context);
            return session;
        } catch (ReflectiveOperationException e) {
            resource.close();
            throw ThirdPartyApiException.as().with(e);
        } catch (Exception e) {
            resource.close();
            throw e;
        }
    }
}
