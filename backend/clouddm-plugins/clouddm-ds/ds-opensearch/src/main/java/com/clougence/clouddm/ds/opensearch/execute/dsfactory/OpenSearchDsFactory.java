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
package com.clougence.clouddm.ds.opensearch.execute.dsfactory;

import java.util.Properties;

import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.apache.hc.core5.io.CloseMode;

import com.clougence.clouddm.ds.opensearch.i18n.OpenSearchDsI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.drivers.DsFactory;
import com.clougence.drivers.DsObject;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OpenSearchDsFactory implements DsFactory<CloseableHttpAsyncClient> {
    @Override
    public DsObject<CloseableHttpAsyncClient> create(Properties properties) {
        CloseableHttpAsyncClient client = HttpAsyncClients.custom()
            .disableAutomaticRetries()
            .disableRedirectHandling()
            .disableCookieManagement()
            .build();
        try {
            client.start();
            return new DsObject<>(properties, client, this, target -> target.close(CloseMode.IMMEDIATE));
        } catch (RuntimeException e) {
            client.close(CloseMode.IMMEDIATE);
            String msg = "Create OpenSearch HTTP client failed";
            log.error(msg, e);
            throw ThirdPartyApiException.as().with(e, OpenSearchDsI18nKeys.OPENSEARCH_CLIENT_CREATE_ERROR);
        }
    }
}
