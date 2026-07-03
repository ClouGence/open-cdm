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

import java.lang.reflect.Method;
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
import com.clougence.utils.StringUtils;

/**
 * only for integration test
 *
 * @author mode create time is 2021/1/12
 **/
public abstract class RdbSessionFactory<T extends DataSourceConfig> implements SessionFactory<T> {

    @Override
    public Session createSession(DsResourceManager resourceRM, T dsConfig, SessionContextDTO contextDTO) throws Exception {
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

        applySessionCatalog(dsConfig, contextDTO);
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

    private void applySessionCatalog(T dsConfig, SessionContextDTO contextDTO) throws Exception {
        if (contextDTO == null || StringUtils.isBlank(contextDTO.getRdbCatalog())) {
            return;
        }

        Method setDefaultCatalog;
        try {
            setDefaultCatalog = dsConfig.getClass().getMethod("setDefaultCatalog", String.class);
        } catch (NoSuchMethodException ignored) {
            return;
        }
        setDefaultCatalog.invoke(dsConfig, contextDTO.getRdbCatalog());
    }

    protected void configSSL(T dsConfig, DsResourceManager resRM) throws Exception {
        SslConfig sslConfig = resRM.fetchSslConfig(dsConfig);
        if (sslConfig == null) {
            return;
        }

        dsConfig.setSslCaFilePath(localPath(sslConfig.getCaFile()));
        dsConfig.setSslCaFileFormat(format(sslConfig.getCaFile()));
        dsConfig.setSslClientCertFilePath(localPath(sslConfig.getClientCertFile()));
        dsConfig.setSslClientCertFileFormat(format(sslConfig.getClientCertFile()));
        dsConfig.setSslClientKeyFilePath(localPath(sslConfig.getClientKeyFile()));
        dsConfig.setSslClientKeyFileFormat(format(sslConfig.getClientKeyFile()));
    }

    private static String localPath(SslFile sslFile) {
        if (sslFile == null || sslFile.getFile() == null) {
            return null;
        }
        return sslFile.getFile().getAbsolutePath();
    }

    private static String format(SslFile sslFile) {
        if (sslFile == null) {
            return null;
        }
        return sslFile.getFormat();
    }

}
