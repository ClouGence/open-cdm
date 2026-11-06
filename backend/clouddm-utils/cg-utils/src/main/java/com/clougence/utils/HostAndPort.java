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
package com.clougence.utils;

import java.net.URI;

import lombok.Getter;

/** Parses an endpoint without DNS lookups. IPv6 ports must follow a closing bracket. */
@Getter
public final class HostAndPort {

    private final String host;
    private final int    port;

    private HostAndPort(String host, int port){
        this.host = host;
        this.port = port;
    }

    public static HostAndPort parse(String address) {
        return parse(address, -1);
    }

    public static HostAndPort fromParts(String host, int port) {
        HostAndPort endpoint = parse(host);
        if (endpoint.getPort() >= 0) {
            throw new IllegalArgumentException("host already contains a port");
        }

        if (port < 1 || port > 65535) {
            throw new IllegalArgumentException("port is out of range");
        }
        return new HostAndPort(endpoint.getHost(), port);
    }

    public static HostAndPort parse(String address, int defaultPort) {
        String value = StringUtils.trimToNull(address);
        if (value == null) {
            throw new IllegalArgumentException("host is required");
        }

        String host = value;
        String portText = null;
        if (value.startsWith("[")) {
            int close = value.indexOf(']');
            if (close < 0 || (close + 1 < value.length() && value.charAt(close + 1) != ':')) {
                throw new IllegalArgumentException("invalid bracketed host");
            }

            host = value.substring(1, close);
            if (host.indexOf(':') < 0) {
                throw new IllegalArgumentException("brackets require an IPv6 address");
            }

            if (close + 1 < value.length()) {
                portText = value.substring(close + 2);
            }
        } else {
            int colon = value.indexOf(':');
            if (colon >= 0 && colon == value.lastIndexOf(':')) {
                host = value.substring(0, colon);
                portText = value.substring(colon + 1);
            }
        }

        if (StringUtils.isBlank(host) || host.indexOf('[') >= 0 || host.indexOf(']') >= 0) {
            throw new IllegalArgumentException("invalid host");
        }

        if (host.indexOf(':') >= 0) {
            URI.create("tcp://[" + host + "]");
        }

        int port = defaultPort;
        if (portText != null) {
            if (portText.isEmpty() || !portText.chars().allMatch(c -> c >= '0' && c <= '9')) {
                throw new IllegalArgumentException("invalid port");
            }

            port = Integer.parseInt(portText);
            if (port < 1 || port > 65535) {
                throw new IllegalArgumentException("port is out of range");
            }
        }
        return new HostAndPort(host, port);
    }

    public String formatHost() {
        if (this.host.indexOf(':') >= 0) {
            return "[" + this.host + "]";
        }
        return this.host;
    }

    @Override
    public String toString() {
        if (this.port < 0) {
            return formatHost();
        }
        return formatHost() + ":" + this.port;
    }
}
