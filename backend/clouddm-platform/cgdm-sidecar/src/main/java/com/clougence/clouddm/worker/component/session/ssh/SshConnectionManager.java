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
package com.clougence.clouddm.worker.component.session.ssh;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.console.configs.ConfigRService;
import com.clougence.clouddm.base.metadata.ds.*;
import com.clougence.clouddm.sdk.service.cache.CacheService;
import com.clougence.utils.StringUtils;
import com.jcraft.jsch.*;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SshConnectionManager {

    @Resource
    private ConfigRService configRService;
    @Resource(name = "sidecarCacheService")
    private CacheService   cacheService;

    public Session openTunnelSession(Long sshConfigId) throws Exception {
        SshConfig config = resolveConfig(sshConfigId, null);

        log.info("open ssh tunnel session, sshConfigId={}, sshHost={}, sshPort={}, username={}, authType={}, proxyType={}, strictHostKeyChecking={}",//
                sshConfigId, config.getHost(), config.getPort(), config.getUsername(), config.getAuthType(), config.getProxyType(), strictHostKeyChecking(config));
        return this.openSession(config, true, false);
    }

    public Session openSession(SshConfig config, boolean useKnownHosts, boolean forceNoStrictChecking) throws Exception {
        validate(config);
        JSch jsch = new JSch();
        byte[] privateKey = resolvePrivateKey(config.getPrivateKeyData());
        if (privateKey != null) {
            byte[] passphrase = StringUtils.isBlank(config.getPrivateKeyPassphrase()) ? null : config.getPrivateKeyPassphrase().getBytes(StandardCharsets.UTF_8);
            jsch.addIdentity(config.getName() == null ? "ssh-config" : config.getName(), privateKey, null, passphrase);
        }

        Path knownHostsFile = null;
        if (useKnownHosts) {
            knownHostsFile = writeKnownHosts(config);
            if (knownHostsFile != null) {
                jsch.setKnownHosts(knownHostsFile.toString());
            }
        }

        Session session = jsch.getSession(config.getUsername(), config.getHost(), config.getPort());
        if (StringUtils.isNotBlank(config.getPassword())) {
            session.setPassword(config.getPassword());
            session.setUserInfo(new StaticUserInfo(config.getPassword(), config.getPrivateKeyPassphrase()));
        }

        configureProxy(session, config);
        Properties properties = new Properties();
        properties.setProperty("StrictHostKeyChecking", forceNoStrictChecking || !strictHostKeyChecking(config) ? "no" : "yes");
        session.setConfig(properties);

        int timeoutMs = intFeature(config, "connectTimeoutMs", 30000);
        int serverAliveIntervalMs = intFeature(config, "serverAliveIntervalMs", 0);
        if (serverAliveIntervalMs > 0) {
            session.setServerAliveInterval(serverAliveIntervalMs);
        }
        session.connect(timeoutMs);
        if (knownHostsFile != null) {
            Files.deleteIfExists(knownHostsFile);
        }
        return session;
    }

    private SshConfig resolveConfig(Long sshConfigId, SshConfig sshConfig) throws Exception {
        if (sshConfig != null) {
            return sshConfig;
        }
        if (sshConfigId == null) {
            throw new IllegalArgumentException("sshConfigId or sshConfig is required.");
        }

        return (SshConfig) this.cacheService.getObjectIfAbsent("ssh-config:" + sshConfigId, 5, TimeUnit.MINUTES, key -> this.configRService.fetchSshConfig(sshConfigId));
    }

    private void configureProxy(Session session, SshConfig config) {
        SshProxyType proxyType = config.getProxyType() == null ? SshProxyType.NO_PROXY : config.getProxyType();
        if (proxyType == SshProxyType.NO_PROXY) {
            return;
        }

        SshProxyFeatures features = config.getProxyFeatures();
        if (features == null) {
            throw new IllegalArgumentException("proxyFeatures is required when proxyType is " + proxyType);
        }

        String host = features.getHost();
        int port = features.getPort() == null ? 0 : features.getPort();
        String user = features.getUsername();
        String password = features.getPassword();
        boolean useUserPassword = features.getSecurityType() == SecurityType.USER_PASSWD;
        if (StringUtils.isBlank(host) || port <= 0) {
            throw new IllegalArgumentException("proxy host and port are required.");
        }

        Proxy proxy;
        switch (proxyType) {
            case HTTP -> {
                ProxyHTTP http = new ProxyHTTP(host, port);
                if (useUserPassword) {
                    http.setUserPasswd(user, password);
                }
                proxy = http;
            }
            case SOCKS4 -> {
                ProxySOCKS4 socks4 = new ProxySOCKS4(host, port);
                if (useUserPassword) {
                    socks4.setUserPasswd(user, password);
                }
                proxy = socks4;
            }
            case SOCKS5 -> {
                ProxySOCKS5 socks5 = new ProxySOCKS5(host, port);
                if (useUserPassword) {
                    socks5.setUserPasswd(user, password);
                }
                proxy = socks5;
            }
            default -> throw new IllegalArgumentException("unsupported proxyType: " + proxyType);
        }
        session.setProxy(proxy);
    }

    private Path writeKnownHosts(SshConfig config) throws IOException {
        List<SshKnownHost> knownHosts = knownHosts(config);
        if (knownHosts.isEmpty()) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (SshKnownHost item : knownHosts) {
            if (item == null) {
                continue;
            }
            String host = item.getHost();
            int port = item.getPort();
            String type = item.getType();
            String key = item.getKey();
            if (StringUtils.isBlank(host) || StringUtils.isBlank(type) || StringUtils.isBlank(key)) {
                continue;
            }
            builder.append(port == 22 ? host : "[" + host + "]:" + port).append(' ').append(type).append(' ').append(key).append('\n');
        }
        if (builder.length() == 0) {
            return null;
        }
        Path file = Files.createTempFile("clouddm-ssh-known-hosts-", ".txt");
        Files.writeString(file, builder.toString(), StandardCharsets.UTF_8);
        return file;
    }

    private List<SshKnownHost> knownHosts(SshConfig config) {
        SshConFeatures features = config.getConFeatures();
        if (features == null || features.getKnownHosts() == null) {
            return Collections.emptyList();
        }
        return features.getKnownHosts();
    }

    private boolean strictHostKeyChecking(SshConfig config) {
        SshConFeatures features = config.getConFeatures();
        if (features == null) {
            return false;
        }
        if (features.getHostKey() != null) {
            return features.getHostKey().isStrictChecking();
        }
        return features.isStrictHostKeyChecking();
    }

    private int intFeature(SshConfig config, String key, int defaultValue) {
        if (config.getConFeatures() == null) {
            return defaultValue;
        }
        if ("connectTimeoutMs".equals(key)) {
            return config.getConFeatures().getConnectTimeoutMs() == null ? defaultValue : config.getConFeatures().getConnectTimeoutMs();
        }
        if ("serverAliveIntervalMs".equals(key)) {
            return config.getConFeatures().getServerAliveIntervalMs() == null ? defaultValue : config.getConFeatures().getServerAliveIntervalMs();
        }
        return defaultValue;
    }

    private byte[] resolvePrivateKey(String data) throws IOException {
        if (StringUtils.isBlank(data)) {
            return null;
        }
        if (data.startsWith("base64://")) {
            return Base64.getDecoder().decode(data.substring("base64://".length()));
        }
        if (data.startsWith("base64:")) {
            return Base64.getDecoder().decode(data.substring("base64:".length()));
        }
        if (data.startsWith("http://") || data.startsWith("https://")) {
            try (InputStream in = new URL(data).openStream()) {
                return in.readAllBytes();
            }
        }
        if (data.startsWith("resource://")) {
            String path = data.substring("resource://".length());
            try (InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
                if (in == null) {
                    throw new FileNotFoundException("resource not found: " + path);
                }
                return in.readAllBytes();
            }
        }
        return data.getBytes(StandardCharsets.UTF_8);
    }

    private void validate(SshConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("ssh config can not be null.");
        }
        if (StringUtils.isBlank(config.getHost())) {
            throw new IllegalArgumentException("ssh host can not be blank.");
        }
        if (StringUtils.isBlank(config.getUsername())) {
            throw new IllegalArgumentException("ssh username can not be blank.");
        }
        if (config.getAuthType() == null) {
            throw new IllegalArgumentException("ssh authType can not be null.");
        }
    }

    private static class StaticUserInfo implements UserInfo, UIKeyboardInteractive {

        private final String password;
        private final String passphrase;

        private StaticUserInfo(String password, String passphrase){
            this.password = password;
            this.passphrase = passphrase;
        }

        @Override
        public String getPassphrase() { return this.passphrase; }

        @Override
        public String getPassword() { return this.password; }

        @Override
        public boolean promptPassword(String message) {
            return StringUtils.isNotBlank(this.password);
        }

        @Override
        public boolean promptPassphrase(String message) {
            return StringUtils.isNotBlank(this.passphrase);
        }

        @Override
        public boolean promptYesNo(String message) {
            return false;
        }

        @Override
        public void showMessage(String message) {
        }

        @Override
        public String[] promptKeyboardInteractive(String destination, String name, String instruction, String[] prompt, boolean[] echo) {
            if (prompt == null || prompt.length == 0 || StringUtils.isBlank(this.password)) {
                return new String[0];
            }
            String[] response = new String[prompt.length];
            Arrays.fill(response, this.password);
            return response;
        }
    }
}
