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

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.Locale;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.context.i18n.LocaleContextHolder;

import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;

public class CicdSqlFileUtilsTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    @Test
    public void shouldReadUtf8BomAndPreserveCrlf() throws Exception {
        File sql = temporaryFolder.newFile("001.sql");
        Files.write(sql.toPath(), new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF, 's', 'e', 'l', 'e', 'c', 't', ' ', '1', ';', '\r', '\n' });

        assertEquals("select 1;\r\n", CicdSqlFileUtils.readUtf8(sql));
    }

    @Test
    public void shouldRejectInvalidUtf8AndIncludeFilename() throws Exception {
        File sql = temporaryFolder.newFile("invalid.sql");
        Files.write(sql.toPath(), new byte[] { (byte) 0xC3, (byte) 0x28 });
        DmI18nUtils.getInstance();
        LocaleContextHolder.setLocale(Locale.US);

        try {
            CicdSqlFileUtils.readUtf8(sql);
            fail("invalid UTF-8 must be rejected");
        } catch (IOException expected) {
            assertEquals("SQL file is not valid UTF-8: invalid.sql", expected.getMessage());
        } finally {
            LocaleContextHolder.resetLocaleContext();
        }
    }

    @Test
    public void shouldRejectOversizedSqlFileWithLocalizedMessage() throws Exception {
        File sql = temporaryFolder.newFile("oversized.sql");
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(sql, "rw")) {
            randomAccessFile.setLength(CicdSqlFileUtils.MAX_SQL_FILE_BYTES + 1);
        }
        DmI18nUtils.getInstance();
        LocaleContextHolder.setLocale(Locale.SIMPLIFIED_CHINESE);

        try {
            CicdSqlFileUtils.readUtf8(sql);
            fail("oversized SQL file must be rejected");
        } catch (IOException expected) {
            assertEquals("SQL 文件超过 50 MiB：oversized.sql", expected.getMessage());
        } finally {
            LocaleContextHolder.resetLocaleContext();
        }
    }
}
