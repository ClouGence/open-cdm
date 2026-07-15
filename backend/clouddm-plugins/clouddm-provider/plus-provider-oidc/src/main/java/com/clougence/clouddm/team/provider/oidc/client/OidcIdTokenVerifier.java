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
package com.clougence.clouddm.team.provider.oidc.client;

import java.security.interfaces.ECPublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

import com.alibaba.fastjson2.JSONObject;
import com.auth0.jwk.Jwk;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.clougence.clouddm.sdk.model.exception.ThirdPartyApiException;
import com.clougence.clouddm.team.provider.oidc.constants.OidcI18nKey;
import com.clougence.utils.StringUtils;

final class OidcIdTokenVerifier {

    private final OidcCfg          conf;
    private final Map<String, Jwk> signingJwkMap;
    private String                 issuer;

    OidcIdTokenVerifier(OidcCfg conf){
        this.conf = conf;
        this.signingJwkMap = new HashMap<>();
    }

    void initialize(String issuer, JSONObject jwksJson) {
        this.issuer = issuer;
        for (Map<String, Object> jwkData : getObjectMaps(jwksJson, "keys")) {
            if (!StringUtils.equalsIgnoreCase((String) jwkData.get("use"), "sig")) {
                continue;
            }

            Jwk jwk = Jwk.fromValues(jwkData);
            this.signingJwkMap.put(jwk.getId(), jwk);
        }
    }

    Set<String> getSigningKeyIds() { return this.signingJwkMap.keySet(); }

    void verify(String idToken) throws Exception {
        DecodedJWT decoded = JWT.decode(idToken);
        Algorithm algorithm = createAlgorithm(decoded.getAlgorithm(), this.signingJwkMap.get(decoded.getKeyId()));
        JWTVerifier verifier = JWT.require(algorithm).acceptLeeway(5).withIssuer(this.issuer).build();
        verifier.verify(idToken);
    }

    private Algorithm createAlgorithm(String algorithm, Jwk jwk) throws Exception {
        return switch (StringUtils.defaultString(algorithm, "")) {
            case "RS256" -> Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);
            case "RS384" -> Algorithm.RSA384((RSAPublicKey) jwk.getPublicKey(), null);
            case "RS512" -> Algorithm.RSA512((RSAPublicKey) jwk.getPublicKey(), null);
            case "HS256" -> Algorithm.HMAC256(this.conf.getClientSecret());
            case "HS384" -> Algorithm.HMAC384(this.conf.getClientSecret());
            case "HS512" -> Algorithm.HMAC512(this.conf.getClientSecret());
            case "ES256" -> Algorithm.ECDSA256((ECPublicKey) jwk.getPublicKey(), null);
            case "ES384" -> Algorithm.ECDSA384((ECPublicKey) jwk.getPublicKey(), null);
            case "ES512" -> Algorithm.ECDSA512((ECPublicKey) jwk.getPublicKey(), null);
            default -> throw ThirdPartyApiException.as().with(OidcI18nKey.OIDC_API_ALGORITHM_ERROR, algorithm);
        };
    }

    private List<Map<String, Object>> getObjectMaps(JSONObject data, String key) {
        Object value = data.get(key);
        if (!(value instanceof List<?> values)) {
            return Collections.emptyList();
        }

        List<Map<String, Object>> maps = new ArrayList<>(values.size());
        for (Object item : values) {
            maps.add(toStringObjectMap(item, key));
        }
        return maps;
    }

    private Map<String, Object> toStringObjectMap(Object value, String key) {
        if (!(value instanceof Map<?, ?> rawMap)) {
            throw ThirdPartyApiException.as().with(OidcI18nKey.OIDC_API_WELLKNOWN_ERROR, "invalid " + key + " item");
        }

        Map<String, Object> result = new HashMap<>(rawMap.size());
        for (Map.Entry<?, ?> entry : rawMap.entrySet()) {
            Object entryKey = entry.getKey();
            if (!(entryKey instanceof String)) {
                throw ThirdPartyApiException.as().with(OidcI18nKey.OIDC_API_WELLKNOWN_ERROR, "invalid " + key + " key");
            }
            result.put((String) entryKey, entry.getValue());
        }
        return result;
    }
}
