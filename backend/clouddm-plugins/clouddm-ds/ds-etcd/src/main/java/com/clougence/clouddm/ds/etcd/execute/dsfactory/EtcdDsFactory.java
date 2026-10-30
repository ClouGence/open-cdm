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
package com.clougence.clouddm.ds.etcd.execute.dsfactory;

import java.io.File;
import java.util.Properties;

import com.clougence.clouddm.base.metadata.ds.SslMode;
import com.clougence.clouddm.ds.etcd.dsconf.EtcdConfig;
import com.clougence.clouddm.ds.etcd.execute.EtcdClients;
import com.clougence.clouddm.ds.etcd.i18n.EtcdDsI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;
import com.clougence.utils.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EtcdDsFactory implements DsFactory<EtcdClients> {

    @Override
    public DsObject<EtcdClients> create(Properties dsConfig) {
        String endpoints = dsConfig.getProperty(EtcdConfig.ENDPOINTS);
        String user = dsConfig.getProperty(DsConfigKeys.USER.getConfigKey());
        String password = dsConfig.getProperty(DsConfigKeys.PASSWORD.getConfigKey());
        SslMode sslMode = SslMode.valueOf(dsConfig.getProperty(DsConfigKeys.SSL_MODE.getConfigKey(), SslMode.DISABLED.name()));
        long timeoutMs = Long.parseLong(dsConfig.getProperty("api.timeout.ms", "10000"));
        try {
            boolean tls = sslMode != SslMode.DISABLED;
            File caFile = null;
            if (sslMode == SslMode.CA) {
                String caPath = dsConfig.getProperty(DsConfigKeys.SSL_CA_FILE.getConfigKey());
                if (StringUtils.isBlank(caPath) || !new File(caPath).isFile()) {
                    throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_SSL_CA_REQUIRED);
                }
                caFile = new File(caPath);
            }
            EtcdClients clients = new EtcdClients(endpoints, user, password, tls, caFile, timeoutMs);
            return new DsObject<>(dsConfig, clients, this);
        } catch (RuntimeException e) {
            String msg = "Create etcd client failed, instanceID=" + dsConfig.getProperty(DsConfigKeys.ID.getConfigKey());
            log.error(msg, e);
            throw ThirdPartyApiException.as().with(e);
        }
    }
}
