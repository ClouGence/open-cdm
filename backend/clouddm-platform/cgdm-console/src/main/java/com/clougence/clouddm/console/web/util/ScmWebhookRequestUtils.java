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
package com.clougence.clouddm.console.web.util;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.clougence.clouddm.sdk.scm.ScmWebhookException;

import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;

public final class ScmWebhookRequestUtils {

    private ScmWebhookRequestUtils(){
    }

    public static Map<String, List<String>> readHeaders(HttpServletRequest request) {
        Map<String, List<String>> headers = new HashMap<>();
        Enumeration<String> names = request.getHeaderNames();
        if (names == null) {
            return headers;
        }
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            headers.put(name, Collections.list(request.getHeaders(name)));
        }
        return headers;
    }

    public static String readUtf8Body(HttpServletRequest request, int maxBodyBytes) throws IOException {
        byte[] bodyBytes;
        try (ServletInputStream in = request.getInputStream()) {
            bodyBytes = in.readNBytes(maxBodyBytes + 1);
        }
        if (bodyBytes.length > maxBodyBytes) {
            String limit = maxBodyBytes + " bytes";
            if (maxBodyBytes % (1024 * 1024) == 0) {
                limit = maxBodyBytes / (1024 * 1024) + " MiB";
            }
            throw new ScmWebhookException(400, "webhook body exceeds " + limit);
        }
        try {
            return StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT)
                .decode(ByteBuffer.wrap(bodyBytes))
                .toString();
        } catch (CharacterCodingException e) {
            throw new ScmWebhookException(400, "webhook body is not valid UTF-8");
        }
    }
}
