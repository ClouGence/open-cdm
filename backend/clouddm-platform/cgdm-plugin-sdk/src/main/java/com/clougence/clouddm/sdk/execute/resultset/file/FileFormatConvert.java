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
package com.clougence.clouddm.sdk.execute.resultset.file;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.slf4j.Logger;

import com.clougence.clouddm.sdk.Spi;

public interface FileFormatConvert extends Spi {

    // MIME, https://mime.wcode.net/zh-hans/
    String name();

    String extension();

    String descriptionI18n();

    String iconName();

    long convert(String exportId, DmFileType srcType, File srcFile, File dstFile, Logger logger, FileFormatConvertReport report, String option) throws IOException;

    default Map<String, Object> getOption() { return Collections.emptyMap(); }
}
