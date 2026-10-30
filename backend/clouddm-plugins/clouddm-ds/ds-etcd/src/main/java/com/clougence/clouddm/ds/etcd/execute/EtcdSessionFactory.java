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
package com.clougence.clouddm.ds.etcd.execute;

import com.clougence.clouddm.base.metadata.ds.SecurityType;
import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.ds.etcd.dsconf.EtcdConfig;
import com.clougence.clouddm.ds.etcd.i18n.EtcdDsI18nKeys;
import com.clougence.clouddm.sdk.execute.dsconf.SslConfig;
import com.clougence.clouddm.sdk.execute.dsconf.SslFile;
import com.clougence.clouddm.sdk.execute.resource.DsResourceManager;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionFactory;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.drivers.DsObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EtcdSessionFactory implements SessionFactory<EtcdConfig> {

    @Override
    public Session createSession(DsResourceManager resources, EtcdConfig config, SessionContextDTO context) {
        DsObject<EtcdClients> resource = null;
        try {
            if (config.getSecurityType() == null || config.getSecurityType() == SecurityType.NONE) {
                config.setUserName(null);
                config.setPassword(null);
            }
            if (config.getSslMode() == SslMode.CA) {
                resolveCa(resources, config);
            }
            resource = resources.requestResource(config);
            return new EtcdSession(context, config, resource);
        } catch (Exception e) {
            if (resource != null) {
                resource.close();
            }
            String msg = "Create etcd session failed";
            log.error(msg, e);
            if (e instanceof ThirdPartyApiException apiException) {
                throw apiException;
            }
            throw ThirdPartyApiException.as().with(e);
        }
    }

    private void resolveCa(DsResourceManager resources, EtcdConfig config) throws Exception {
        SslConfig ssl = resources.fetchSslConfig(config);
        config.setSslCaFilePath(null);
        if (ssl == null) {
            throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_SSL_CA_REQUIRED);
        }
        SslFile ca = ssl.getCaFile();
        if (ca == null || ca.getFile() == null) {
            throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_SSL_CA_REQUIRED);
        }
        config.setSslCaFilePath(ca.getFile().getAbsolutePath());
    }
}
