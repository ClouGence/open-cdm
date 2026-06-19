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

import java.sql.Connection;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.sdk.execute.dsconf.SslConfig;
import com.clougence.clouddm.sdk.execute.dsconf.SslFile;
import com.clougence.clouddm.sdk.execute.resource.DsResourceManager;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionFactory;
import com.clougence.drivers.DsObject;

/**
 * only for integration test
 *
 * @author mode create time is 2021/1/12
 **/
public abstract class RdbSessionFactory<T extends DataSourceConfig> implements SessionFactory<T> {

    @Override
    public Session createSession(DsResourceManager resourceRM, T dsConfig, SessionContextDTO contextDTO) throws Exception {
        // so_timeout(SocketReadTimeout) must >= query timeout_sec
        Integer soTimeoutSec = dsConfig.getSoTimeoutSec();
        if (resourceRM.isTask()) {
            Integer exportQueryTimeoutSec = dsConfig.getExportMaxQueryTimeoutSec();
            dsConfig.setSoTimeoutSec(maxTimeoutSec(soTimeoutSec, exportQueryTimeoutSec));
        } else {
            Integer onlineQueryTimeoutSec = dsConfig.getOnlineMaxQueryTimeoutSec();
            dsConfig.setSoTimeoutSec(maxTimeoutSec(soTimeoutSec, onlineQueryTimeoutSec));
        }

        SecurityType securityType = dsConfig.getSecurityType();
        if (securityType != null) {
            switch (securityType) {
                case NONE:
                    dsConfig.setUserName(null);
                    dsConfig.setPassword(null);
                    break;
                case ONLY_USER:
                    dsConfig.setPassword(null);
                    break;
                case ONLY_PASSWD:
                    dsConfig.setUserName(null);
                    break;
                case USER_PASSWD:
                case AK_SK:
                    break;
                default:
                    throw new UnsupportedOperationException("Unsupported security type: " + securityType);
            }
        }

        if (dsConfig.getSslMode() != null && dsConfig.getSslMode() != SslMode.DISABLED) {
            configSSL(dsConfig, resourceRM);
        }

        DsObject<Connection> dsObject = resourceRM.requestResource(dsConfig);

        try {
            return this.newSession(dsConfig, contextDTO, dsObject, resourceRM);
        } catch (Exception e) {
            if (dsObject != null) {
                dsObject.close();
            }
            throw e;
        }
    }

    protected abstract Session newSession(T dsConfig, SessionContextDTO contextDTO, DsObject<Connection> dsObject, DsResourceManager ownerRM) throws Exception;

    protected void configSSL(T dsConfig, DsResourceManager resRM) throws Exception {
        SslConfig sslConfig = resRM.fetchSslConfig(dsConfig);
        if (sslConfig == null) {
            return;
        }

        dsConfig.setSslCaFilePath(localPath(sslConfig.getCaFile()));
        dsConfig.setSslClientCertFilePath(localPath(sslConfig.getClientCertFile()));
        dsConfig.setSslClientKeyFilePath(localPath(sslConfig.getClientKeyFile()));
    }

    private static String localPath(SslFile sslFile) {
        if (sslFile == null || sslFile.getFile() == null) {
            return null;
        }
        return sslFile.getFile().getAbsolutePath();
    }

    private static Integer maxTimeoutSec(Integer firstTimeoutSec, Integer secTimeoutSec) {
        if (firstTimeoutSec == null && secTimeoutSec == null) {
            return null;
        } else if (firstTimeoutSec == null) {
            return secTimeoutSec;
        } else if (secTimeoutSec == null) {
            return firstTimeoutSec;
        } else {
            return Math.max(firstTimeoutSec, secTimeoutSec);
        }
    }
}
