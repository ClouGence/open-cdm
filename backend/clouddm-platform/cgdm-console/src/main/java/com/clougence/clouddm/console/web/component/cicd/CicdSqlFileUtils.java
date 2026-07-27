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
package com.clougence.clouddm.console.web.component.cicd;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;

public final class CicdSqlFileUtils {

    /**
     * Maximum size of one SQL file read into memory, in bytes. Files above 50 MiB are rejected before
     * {@link Files#readAllBytes(java.nio.file.Path)} is called.
     */
    public static final long MAX_SQL_FILE_BYTES = 50L * 1024 * 1024;

    private CicdSqlFileUtils(){
    }

    public static String readUtf8(File file) throws IOException {
        if (Files.size(file.toPath()) > MAX_SQL_FILE_BYTES) {
            String message = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_SQL_FILE_TOO_LARGE_ERROR, file.getName());
            throw new IOException(message);
        }
        byte[] bytes = Files.readAllBytes(file.toPath());
        try {
            String content = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT)
                .decode(ByteBuffer.wrap(bytes))
                .toString();
            return content.startsWith("\uFEFF") ? content.substring(1) : content;
        } catch (CharacterCodingException e) {
            String message = DmI18nUtils.getMessage(I18nDmMsgKeys.CICD_SQL_FILE_INVALID_UTF8_ERROR, file.getName());
            throw new IOException(message, e);
        }
    }
}
