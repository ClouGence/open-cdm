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
package com.clougence.clouddm.ds.es.dsconf;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.clougence.clouddm.ds.es.i18n.EsDsI18nKeys;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;

public final class EsEndpoints {
    private EsEndpoints(){
    }

    public static List<URI> parse(String value) {
        List<URI> endpoints = new ArrayList<>();
        try {
            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException();
            }
            String scheme = null;
            for (String entry : value.split(",", -1)) {
                URI uri = URI.create(entry.trim());
                if ((!"http".equals(uri.getScheme()) && !"https".equals(uri.getScheme())) || uri.getHost() == null || uri.getUserInfo() != null || uri.getQuery() != null
                    || uri.getFragment() != null || (!uri.getRawPath().isEmpty() && !"/".equals(uri.getRawPath())) || uri.getPort() == 0 || uri.getPort() > 65535) {
                    throw new IllegalArgumentException();
                }
                if (scheme != null && !scheme.equals(uri.getScheme())) {
                    throw new IllegalArgumentException();
                }
                scheme = uri.getScheme();
                endpoints.add(uri);
            }
        } catch (IllegalArgumentException e) {
            throw ThirdPartyApiException.as().with(EsDsI18nKeys.ES_ENDPOINTS_INVALID);
        }
        return List.copyOf(endpoints);
    }
}
