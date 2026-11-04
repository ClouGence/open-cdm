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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import com.clougence.clouddm.ds.opensearch.dsconf.OpenSearchConfig;
import com.clougence.clouddm.ds.opensearch.execute.dsfactory.OpenSearchDsFactory;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.drivers.DsConfigKeys;
import com.clougence.drivers.DsObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

final class OpenSearchHttpStub implements AutoCloseable {
    private final HttpServer                            server;
    private final ExecutorService                       workers   = Executors.newCachedThreadPool();
    final List<String>                                  requests  = new CopyOnWriteArrayList<>();
    volatile Function<HttpExchange, OpenSearchResponse> responder = exchange -> json(500, "{}");
    volatile String                                     version   = "3.9.0";
    private DsObject<OpenSearchClient>                  resource;

    OpenSearchHttpStub() throws IOException{
        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.setExecutor(workers);
        server.createContext("/", exchange -> {
            try (exchange) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                exchange.setAttribute("requestBody", body);
                requests.add(exchange.getRequestMethod() + " " + exchange.getRequestURI() + "\n" + body);
                OpenSearchResponse response;
                if (exchange.getRequestURI().getPath().equals("/")) {
                    response = json(200, "{\"version\":{\"distribution\":\"opensearch\",\"number\":\"" + version + "\"}}");
                } else {
                    response = responder.apply(exchange);
                }
                if (response == null) {
                    return;
                } // Simulate a disconnected peer, without a response.
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                if (exchange.getRequestMethod().equals("HEAD")) {
                    exchange.sendResponseHeaders(response.status(), -1);
                } else {
                    exchange.sendResponseHeaders(response.status(), response.body().length);
                    exchange.getResponseBody().write(response.body());
                }
            } catch (IOException ignored) {
                // Cancellation/timeout tests deliberately close the socket before the stub responds.
            }
        });
        server.start();
    }

    OpenSearchSession session(OpenSearchConfig config, Map<String, String> options) {
        config.setHost("http://127.0.0.1:" + server.getAddress().getPort());
        Properties properties = new Properties();
        properties.setProperty(DsConfigKeys.HOST.getConfigKey(), config.getHost());
        properties.setProperty("maxConnections", "1");
        properties.setProperty("maxConnectionsPerRoute", "1");
        options.forEach(properties::setProperty);
        resource = new OpenSearchDsFactory().create(properties);
        SessionContextDTO context = new SessionContextDTO();
        context.setSessionId("http-unit-test");
        requests.clear();
        return new OpenSearchSession(context, config, resource);
    }

    OpenSearchClient client() {
        return resource.getTarget();
    }

    static OpenSearchResponse json(int status, String json) {
        return new OpenSearchResponse(status, json.getBytes(StandardCharsets.UTF_8));
    }

    static OpenSearchResponse metadata(HttpExchange exchange) {
        return switch (exchange.getRequestURI().getPath()) {
            case "/_cluster/settings" -> json(200, "{\"transient\":{},\"persistent\":{},\"defaults\":{}}");
            case "/_index_template" -> json(200, "{\"index_templates\":[]}");
            case "/_template" -> json(200, "{}");
            case "/_resolve/index/books" -> json(200, "{\"indices\":[{\"name\":\"books\"}],\"aliases\":[],\"data_streams\":[]}");
            case "/books/_mapping" -> json(200, "{\"books\":{\"mappings\":{}}}");
            case "/books/_settings" -> json(200, "{\"books\":{\"settings\":{\"index\":{}}}}");
            default -> null;
        };
    }

    @Override
    public void close() {
        if (resource != null) {
            resource.close();
        }
        server.stop(0);
        workers.shutdownNow();
    }
}
