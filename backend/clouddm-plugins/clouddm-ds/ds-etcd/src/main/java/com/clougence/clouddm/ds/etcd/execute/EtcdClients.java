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

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.clougence.clouddm.ds.etcd.i18n.EtcdDsI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;

import javax.net.ssl.SSLException;

import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.ClientBuilder;
import io.etcd.jetcd.KeyValue;
import io.etcd.jetcd.cluster.Member;
import io.etcd.jetcd.options.GetOption;

public class EtcdClients implements AutoCloseable {

    static final int           KEY_LIMIT = 500;
    private final Client       client;
    private final long         timeoutMs;
    private volatile boolean   closed;

    public EtcdClients(String endpoints, String user, String password, boolean tls, File caFile, long timeoutMs){
        this.timeoutMs = timeoutMs;
        String scheme = "http://";
        if (tls) {
            scheme = "https://";
        }
        String[] targets = new String[endpoints.split(",").length];
        int index = 0;
        for (String endpoint : endpoints.split(",")) {
            targets[index] = scheme + endpoint.trim();
            index++;
        }
        ClientBuilder builder = Client.builder().endpoints(targets);
        if (user != null && !user.isBlank()) {
            builder.user(ByteSequence.from(user, StandardCharsets.UTF_8));
            String secret = password == null ? "" : password;
            builder.password(ByteSequence.from(secret, StandardCharsets.UTF_8));
        }
        if (caFile != null) {
            try {
                builder.sslContext(ssl -> ssl.trustManager(caFile));
            } catch (SSLException e) {
                throw ThirdPartyApiException.as().with(e);
            }
        }
        Thread thread = Thread.currentThread();
        ClassLoader contextClassLoader = thread.getContextClassLoader();
        try {
            thread.setContextClassLoader(EtcdClients.class.getClassLoader());
            this.client = builder.build();
        } finally {
            thread.setContextClassLoader(contextClassLoader);
        }
    }

    public List<String> listMembers() throws Exception {
        checkOpen();
        List<Member> members = this.client.getClusterClient().listMember().get(this.timeoutMs, TimeUnit.MILLISECONDS).getMembers();
        List<String> names = new ArrayList<>();
        for (Member member : members) {
            names.add(member.getName());
        }
        return names;
    }

    public List<Map<String, String>> listKeys(String prefix) throws Exception {
        checkOpen();
        ByteSequence start;
        GetOption.Builder optionBuilder = GetOption.newBuilder().withLimit(KEY_LIMIT);
        if (prefix == null || prefix.isEmpty()) {
            // Range key cannot be empty. \0 with range end \0 means all keys.
            start = ByteSequence.from(new byte[] { 0 });
            optionBuilder.withRange(ByteSequence.from(new byte[] { 0 }));
        } else {
            start = ByteSequence.from(prefix, StandardCharsets.UTF_8);
            optionBuilder.isPrefix(true);
        }
        List<KeyValue> values = this.client.getKVClient()
            .get(start, optionBuilder.build())
            .get(this.timeoutMs, TimeUnit.MILLISECONDS)
            .getKvs();
        List<Map<String, String>> rows = new ArrayList<>();
        for (KeyValue value : values) {
            rows.add(toRow(value));
        }
        return rows;
    }

    public Map<String, String> getKey(String key) throws Exception {
        checkOpen();
        List<KeyValue> values = this.client.getKVClient()
            .get(ByteSequence.from(key, StandardCharsets.UTF_8))
            .get(this.timeoutMs, TimeUnit.MILLISECONDS)
            .getKvs();
        if (values.isEmpty()) {
            return null;
        }
        return toRow(values.get(0));
    }

    public void putKey(String key, String value) throws Exception {
        checkOpen();
        String stored = value == null ? "" : value;
        this.client.getKVClient()
            .put(ByteSequence.from(key, StandardCharsets.UTF_8), ByteSequence.from(stored, StandardCharsets.UTF_8))
            .get(this.timeoutMs, TimeUnit.MILLISECONDS);
    }

    public long deleteKey(String key) throws Exception {
        checkOpen();
        return this.client.getKVClient()
            .delete(ByteSequence.from(key, StandardCharsets.UTF_8))
            .get(this.timeoutMs, TimeUnit.MILLISECONDS)
            .getDeleted();
    }

    @Override
    public synchronized void close() {
        if (this.closed) {
            return;
        }
        this.closed = true;
        this.client.close();
    }

    private void checkOpen() {
        if (this.closed) {
            throw ThirdPartyApiException.as().with(EtcdDsI18nKeys.ETCD_CLIENTS_CLOSED);
        }
    }

    private static Map<String, String> toRow(KeyValue value) {
        Map<String, String> row = new LinkedHashMap<>();
        row.put("key", value.getKey().toString(StandardCharsets.UTF_8));
        row.put("value", value.getValue().toString(StandardCharsets.UTF_8));
        row.put("version", String.valueOf(value.getVersion()));
        row.put("createRevision", String.valueOf(value.getCreateRevision()));
        row.put("modRevision", String.valueOf(value.getModRevision()));
        row.put("lease", String.valueOf(value.getLease()));
        return row;
    }
}
