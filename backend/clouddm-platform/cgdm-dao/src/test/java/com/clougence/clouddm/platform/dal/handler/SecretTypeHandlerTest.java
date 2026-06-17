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
package com.clougence.clouddm.platform.dal.handler;

import static org.junit.Assert.*;

import java.lang.reflect.Proxy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Test;

import com.clougence.clouddm.base.metadata.ds.SshProxyFeatures;
import com.clougence.clouddm.platform.dal.handler.encrypt.SshProxyFeaturesTypeHandler;
import com.clougence.clouddm.platform.dal.handler.encrypt.StrSecretTypeHandler;

public class SecretTypeHandlerTest {

    @Test
    public void shouldEncryptAndDecryptStringSecret() throws Exception {
        AtomicReference<String> storage = new AtomicReference<>();
        PreparedStatement ps = preparedStatement(storage);

        new StrSecretTypeHandler().setNonNullParameter(ps, 1, "plain-password", null);

        assertNotEquals("plain-password", storage.get());
        assertEquals("plain-password", new StrSecretTypeHandler().getNullableResult(resultSet(storage.get()), "password"));
    }

    @Test
    public void shouldEncryptAndDecryptSecretFieldInJson() throws Exception {
        AtomicReference<String> storage = new AtomicReference<>();
        PreparedStatement ps = preparedStatement(storage);
        SshProxyFeatures features = new SshProxyFeatures();
        features.setHost("proxy.example.com");
        features.setPassword("proxy-password");

        SshProxyFeaturesTypeHandler handler = new SshProxyFeaturesTypeHandler();
        handler.setNonNullParameter(ps, 1, features, null);

        assertFalse(storage.get().contains("proxy-password"));
        assertEquals("proxy-password", handler.getNullableResult(resultSet(storage.get()), "proxy_features").getPassword());
    }

    private PreparedStatement preparedStatement(AtomicReference<String> storage) {
        return (PreparedStatement) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { PreparedStatement.class }, (proxy, method, args) -> {
            if ("setString".equals(method.getName())) {
                storage.set((String) args[1]);
                return null;
            }
            return null;
        });
    }

    private ResultSet resultSet(String value) {
        return (ResultSet) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { ResultSet.class }, (proxy, method, args) -> {
            if ("getString".equals(method.getName())) {
                return value;
            }
            return null;
        });
    }
}
