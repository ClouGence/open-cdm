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
package com.clougence.clouddm.sdk.execute.tools;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class ToolUtils {

    public static final String  MDCKEY_LOG_PATH = "plugin_logPath";
    public static final String  MDCKEY_LOG_FILE = "plugin_logFile";
    private static final Logger logger          = LoggerFactory.getLogger("tools-logger");

    public static Logger getLoggerAppender() { return logger; }

    public static File getLogFile() { return new File(MDC.get(MDCKEY_LOG_FILE)); }

    public static File getLogPath() { return new File(MDC.get(MDCKEY_LOG_PATH)); }

    public static ToolResultDTO build(boolean success) {
        ToolResultDTO dto = new ToolResultDTO();
        dto.setSuccess(success);
        dto.setBody(null);
        return dto;
    }

    public static ToolResultDTO buildSuccess() {
        ToolResultDTO dto = new ToolResultDTO();
        dto.setSuccess(true);
        dto.setBody(null);
        return dto;
    }

    public static ToolResultDTO buildSuccess(String data) {
        ToolResultDTO dto = new ToolResultDTO();
        dto.setSuccess(true);
        dto.setBody(data);
        return dto;
    }

    public static ToolResultDTO buildError(String msg) {
        ToolResultDTO dto = new ToolResultDTO();
        dto.setSuccess(false);
        dto.setMessage(msg);
        return dto;
    }

    public static ToolResultDTO buildError(Exception e) {
        ToolResultDTO dto = new ToolResultDTO();
        dto.setSuccess(false);
        dto.setMessage(e.getMessage());
        return dto;
    }

    public static ToolRequestDTO buildRequest(String body) {
        ToolRequestDTO dto = new ToolRequestDTO();
        dto.setBody(body);
        return dto;
    }

}
