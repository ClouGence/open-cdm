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
package com.clougence.clouddm.ds.opensearch.execute;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.apache.hc.client5.http.impl.async.HttpAsyncClients;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.ds.opensearch.dsconf.OpenSearchConfig;
import com.clougence.clouddm.ds.opensearch.i18n.OpenSearchDsI18nKeys;
import com.clougence.clouddm.sdk.execute.session.QueryRequest;
import com.clougence.clouddm.sdk.execute.session.QueryResultConf;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.sdk.sql.analysis.behavior.TargetType;
import com.clougence.drivers.DsObject;
import com.clougence.sql.es.EsBehaviorAnalysisSpi;
import com.clougence.sql.es.EsSqlI18nKeys;
import com.clougence.sql.es.RestProduct;

public class OpenSearchSessionPolicyTest {
    private OpenSearchSession session;
    private OpenSearchConfig  config;

    @Before
    public void prepareUnstartedClient() throws Exception {
        config = new OpenSearchConfig();
        config.setHost("http://localhost:9200");
        config.setVersion("8.19.0");
        var client = new OpenSearchClient(HttpAsyncClients.createDefault(), new ScheduledThreadPoolExecutor(1), List.of(URI.create(config.getHost())), config);
        // Simulate the version already accepted by the factory probe, without starting HTTP or exposing a production setter.
        var field = OpenSearchClient.class.getDeclaredField("serverVersion");
        field.setAccessible(true);
        field.set(client, "3.2.0");
        var resource = new DsObject<>(new Properties(), client, null);
        var context = new SessionContextDTO();
        context.setSessionId("policy-test");
        session = new OpenSearchSession(context, config, resource);
    }

    @After
    public void closeClient() {
        if (session != null) {
            session.close();
        }
    }

    @Test
    public void executionUsesProbedVersionAndFixedProduct() {
        assertRejected("GET /books/_search?sort=_shard_doc:asc", EsSqlI18nKeys.OPENSEARCH_FEATURE_UNSUPPORTED);
        assertRejected("GET /books/_knn_search", EsSqlI18nKeys.OPENSEARCH_FEATURE_UNSUPPORTED);
        assertFalse(session.isExecuting());
    }

    @Test
    public void executionRechecksAuthorizedResourceScope() {
        assertRejected("GET /books/_search", EsSqlI18nKeys.ES_RESOURCE_SCOPE_MISMATCH);
    }

    @Test
    public void authorizedWriteHonorsReadOnlyBeforeDispatch() {
        QueryRequest query = new QueryRequest();
        query.setResultConf(new QueryResultConf());
        query.setQueryBody("DELETE /books/_doc/1");
        session.setReadOnly(true);
        try (var behavior = new EsBehaviorAnalysisSpi(RestProduct.OPENSEARCH.parameters("3.2.0")).analysisBehaviorStream(new StringReader(query.getQueryBody()), Map.of(), 1, 0)) {
            query.setRelations(behavior.toList().get(0).getRelations());
        }
        query.setResolvedResources(Map.of(TargetType.Table, List.of("books")));
        try {
            session.executeQuery(query, null);
            fail();
        } catch (ThirdPartyApiException expected) {
            assertEquals(OpenSearchDsI18nKeys.OPENSEARCH_READ_ONLY, expected.getMessageKey());
        }
    }

    @Test
    public void closedSessionDoesNotParseOrDispatch() {
        session.close();
        assertRejected("GET /books/_search", OpenSearchDsI18nKeys.OPENSEARCH_CLIENT_CLOSED);
    }

    @Test
    public void sessionContextCarriesProductAndVersion() {
        config.setVersion("3.9.0");
        var context = new OpenSearchSessionSpi().createSessionContext(config, Map.of(RestProduct.PARAMETER, "ELASTICSEARCH"));
        assertEquals("OPENSEARCH", context.getSqlParameters().get(RestProduct.PARAMETER));
        assertEquals("3.9.0", context.getSqlParameters().get("version"));
    }

    private void assertRejected(String text, String key) {
        QueryRequest query = new QueryRequest();
        query.setResultConf(new QueryResultConf());
        query.setDsType(DataSourceType.ElasticSearch);
        query.setQueryBody(text);
        try {
            session.executeQuery(query, null);
            fail(text);
        } catch (ThirdPartyApiException expected) {
            assertEquals(key, expected.getMessageKey());
        }
    }
}
