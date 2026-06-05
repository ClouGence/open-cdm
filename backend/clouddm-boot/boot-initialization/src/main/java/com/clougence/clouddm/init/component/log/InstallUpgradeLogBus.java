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
package com.clougence.clouddm.init.component.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.clougence.utils.StringUtils;
import com.clougence.utils.format.WellKnowFormat;

public final class InstallUpgradeLogBus {

    private static final Logger                                                LOGGER          = LoggerFactory.getLogger("install-upgrade-logger");
    private static final int                                                   MAX_LOG_ENTRIES = 2000;
    private static final CopyOnWriteArrayList<InstallUpgradeLogEntry>          ENTRIES         = new CopyOnWriteArrayList<>();
    private static final CopyOnWriteArraySet<Consumer<InstallUpgradeLogEvent>> LISTENERS       = new CopyOnWriteArraySet<>();
    private static final Object                                                SCRIPT_LOCK     = new Object();
    private static final Map<String, InstallUpgradeScriptItem>                 SCRIPT_ITEMS    = new LinkedHashMap<>();

    private static String                                                      currentScript;
    private static String                                                      currentSql;
    private static Map<String, String>                                         currentNotice;

    private InstallUpgradeLogBus(){
    }

    public static void start(String operation, String jdbcUrl) {
        ENTRIES.clear();
        synchronized (SCRIPT_LOCK) {
            SCRIPT_ITEMS.clear();
            currentScript = null;
            currentSql = null;
            currentNotice = null;
        }
        publish(new InstallUpgradeLogEvent("RESET", operation));
        info("Start " + StringUtils.defaultIfBlank(operation, "install") + " flow.");
        if (StringUtils.isNotBlank(jdbcUrl)) {
            info("Target JDBC URL: " + jdbcUrl);
        }
    }

    public static void syncPlannedScripts(List<String> scriptNames) {
        synchronized (SCRIPT_LOCK) {
            SCRIPT_ITEMS.clear();
            currentScript = null;
            currentSql = null;
            if (scriptNames != null) {
                for (String scriptName : scriptNames) {
                    if (StringUtils.isBlank(scriptName)) {
                        continue;
                    }
                    SCRIPT_ITEMS.put(scriptName, new InstallUpgradeScriptItem(scriptName, "PENDING", "", ""));
                }
            }
        }
        publish(scriptSnapshotEvent());
    }

    public static void markScriptRunning(String scriptName) {
        InstallUpgradeScriptItem item = updateScript(scriptName, "RUNNING", "", "", true);
        if (item != null) {
            publish(new InstallUpgradeLogEvent("SCRIPT_UPDATE", item));
        }
    }

    public static void markScriptSuccess(String scriptName) {
        InstallUpgradeScriptItem item = updateScript(scriptName, "SUCCESS", "", "", false);
        if (item != null) {
            publish(new InstallUpgradeLogEvent("SCRIPT_UPDATE", item));
        }
    }

    public static void rememberSql(String sql) {
        if (StringUtils.isBlank(sql)) {
            return;
        }
        synchronized (SCRIPT_LOCK) {
            currentSql = sql;
        }
    }

    public static void markCurrentScriptFailed(Throwable throwable) {
        InstallUpgradeScriptItem item;
        synchronized (SCRIPT_LOCK) {
            if (StringUtils.isBlank(currentScript)) {
                return;
            }
            item = new InstallUpgradeScriptItem(currentScript, "ERROR", StringUtils.defaultIfBlank(currentSql, ""), stringifyThrowable(throwable));
            SCRIPT_ITEMS.put(currentScript, item);
            currentScript = null;
            currentSql = null;
        }
        publish(new InstallUpgradeLogEvent("SCRIPT_UPDATE", item));
    }

    public static void complete(String message) {
        info(message);
    }

    public static void fail(String message, Throwable throwable) {
        error(message, throwable);
    }

    public static void info(String message) {
        append("INFO", message);
    }

    public static void warn(String message) {
        append("WARN", message);
    }

    public static void notice(String code, String level) {
        if (StringUtils.isBlank(code)) {
            return;
        }

        Map<String, String> notice = Map.of("code", code, "level", StringUtils.defaultIfBlank(level, "info"));
        synchronized (SCRIPT_LOCK) {
            currentNotice = notice;
        }
        publish(new InstallUpgradeLogEvent("NOTICE", notice));
    }

    public static void error(String message, Throwable throwable) {
        append("ERROR", message);
        if (throwable == null) {
            return;
        }

        for (String line : stringifyThrowable(throwable).split("\\r?\\n")) {
            if (StringUtils.isNotBlank(line)) {
                append("ERROR", line);
            }
        }
    }

    public static List<InstallUpgradeLogEntry> snapshot() {
        return new ArrayList<>(ENTRIES);
    }

    public static List<InstallUpgradeScriptItem> snapshotScripts() {
        synchronized (SCRIPT_LOCK) {
            return new ArrayList<>(SCRIPT_ITEMS.values());
        }
    }

    public static InstallUpgradeLogEvent snapshotEvent() {
        return new InstallUpgradeLogEvent("SNAPSHOT", snapshot());
    }

    public static InstallUpgradeLogEvent scriptSnapshotEvent() {
        return new InstallUpgradeLogEvent("SCRIPT_SNAPSHOT", snapshotScripts());
    }

    public static InstallUpgradeLogEvent noticeSnapshotEvent() {
        synchronized (SCRIPT_LOCK) {
            if (currentNotice == null || currentNotice.isEmpty()) {
                return null;
            }
            return new InstallUpgradeLogEvent("NOTICE", currentNotice);
        }
    }

    public static void addListener(Consumer<InstallUpgradeLogEvent> listener) {
        if (listener != null) {
            LISTENERS.add(listener);
        }
    }

    public static void removeListener(Consumer<InstallUpgradeLogEvent> listener) {
        if (listener != null) {
            LISTENERS.remove(listener);
        }
    }

    private static void append(String level, String message) {
        String safeMessage = StringUtils.defaultIfBlank(message, "");
        InstallUpgradeLogEntry entry = new InstallUpgradeLogEntry(WellKnowFormat.WKF_DATE_TIME24.now(), level, safeMessage);
        ENTRIES.add(entry);
        trimBuffer();
        writeToLogger(level, safeMessage);
        publish(new InstallUpgradeLogEvent("APPEND", entry));
    }

    private static void trimBuffer() {
        while (ENTRIES.size() > MAX_LOG_ENTRIES) {
            ENTRIES.remove(0);
        }
    }

    private static void writeToLogger(String level, String message) {
        if ("ERROR".equalsIgnoreCase(level)) {
            LOGGER.error(message);
            return;
        }
        if ("WARN".equalsIgnoreCase(level)) {
            LOGGER.warn(message);
            return;
        }
        LOGGER.info(message);
    }

    private static void publish(InstallUpgradeLogEvent event) {
        for (Consumer<InstallUpgradeLogEvent> listener : LISTENERS) {
            listener.accept(event);
        }
    }

    private static InstallUpgradeScriptItem updateScript(String scriptName, String status, String failedSql, String errorDetail, boolean markRunning) {
        if (StringUtils.isBlank(scriptName)) {
            return null;
        }

        synchronized (SCRIPT_LOCK) {
            InstallUpgradeScriptItem item = new InstallUpgradeScriptItem(scriptName,
                status,
                StringUtils.defaultIfBlank(failedSql, ""),
                StringUtils.defaultIfBlank(errorDetail, ""));
            SCRIPT_ITEMS.put(scriptName, item);
            if (markRunning) {
                currentScript = scriptName;
                currentSql = null;
            } else if (scriptName.equals(currentScript)) {
                currentScript = null;
                currentSql = null;
            }
            return item;
        }
    }

    private static String stringifyThrowable(Throwable throwable) {
        if (throwable == null) {
            return "";
        }

        StringWriter stringWriter = new StringWriter();
        try (PrintWriter printWriter = new PrintWriter(stringWriter)) {
            throwable.printStackTrace(printWriter);
        }
        return stringWriter.toString();
    }
}
