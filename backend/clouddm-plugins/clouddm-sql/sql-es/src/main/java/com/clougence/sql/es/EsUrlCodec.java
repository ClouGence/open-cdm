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
package com.clougence.sql.es;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;

final class EsUrlCodec {
    private EsUrlCodec(){
    }

    static String decode(String text, boolean query, EsSourceRange range) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < text.length();) {
            char ch = text.charAt(i);
            if (ch != '%') {
                if (query && ch == '+') {
                    ch = ' ';
                }
                result.append(ch);
                i++;
                continue;
            }
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            while (i < text.length() && text.charAt(i) == '%') {
                if (i + 2 >= text.length()) {
                    throw new EsParseException(range, EsSqlI18nKeys.ES_URL_INVALID);
                }
                int high = Character.digit(text.charAt(i + 1), 16);
                int low = Character.digit(text.charAt(i + 2), 16);
                if (high < 0 || low < 0) {
                    throw new EsParseException(range, EsSqlI18nKeys.ES_URL_INVALID);
                }
                bytes.write(high * 16 + low);
                i += 3;
            }
            try {
                result.append(StandardCharsets.UTF_8.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(bytes.toByteArray())));
            } catch (CharacterCodingException e) {
                throw new EsParseException(range, EsSqlI18nKeys.ES_URL_INVALID);
            }
        }
        return result.toString();
    }
}
