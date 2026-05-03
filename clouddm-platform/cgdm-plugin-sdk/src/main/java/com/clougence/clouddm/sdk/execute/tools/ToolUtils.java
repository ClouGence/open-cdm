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
