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
package com.clougence.clouddm.console.web.service.system;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.console.web.component.config.ConsoleConfig;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.JsonUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class InstallationReportStarter implements UnifiedPostConstruct {

    private static final String INSTALLATION_REPORT_PATH = "/apis/clouddm/installations";

    private final HttpClient httpClient = HttpClient.newBuilder()
        .connectTimeout(Duration.ofSeconds(3))
        .build();

    @Resource
    private ConsoleConfig config;
    @Resource
    private InstallationReportState installationReportState;

    @Override
    public void init() {
        Thread thread = new Thread(this::reportIfNecessary, "clouddm-installation-report");
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void stop() {

    }

    private void reportIfNecessary() {
        String version = GlobalConfUtils.getAppVersion();
        if (GlobalConfUtils.UNKNOWN_VERSION.equals(version)) {
            log.warn("Skip installation report because CloudDM version is unknown.");
            return;
        }
        try {
            String type = this.installationReportState.reportIfNecessary(version, pendingType -> report(version, pendingType));
            if (type != null) {
                log.info("CloudDM installation reported, version={}, type={}", version, type);
            }
        } catch (Exception e) {
            log.warn("CloudDM installation report failed. msg:{}", ExceptionUtils.getRootCauseMessage(e));
        }
    }

    private boolean report(String version, String type) {
        try {
            String body = JsonUtils.toJson(Map.of("version", version, "type", type));
            String reportUrl = "https://" + this.config.getInstallationReportServer() + INSTALLATION_REPORT_PATH;
            HttpRequest request = HttpRequest.newBuilder(URI.create(reportUrl))
                .timeout(Duration.ofSeconds(5))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                .build();
            HttpResponse<Void> response = this.httpClient.send(request, HttpResponse.BodyHandlers.discarding());
            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                log.warn("Installation report failed, status={}", response.statusCode());
                return false;
            }
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("CloudDM installation report interrupted.");
            return false;
        } catch (Exception e) {
            log.warn("CloudDM installation report failed. msg:{}", ExceptionUtils.getRootCauseMessage(e));
            return false;
        }
    }
}
