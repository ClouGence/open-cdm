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
package com.clougence.clouddm.init.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.console.web.model.vo.DriverVersionStatusVO;
import com.clougence.clouddm.console.web.model.vo.datasource.DriverDownloadProgressVO;
import com.clougence.clouddm.init.InitApplication;
import com.clougence.clouddm.init.component.log.InitMysqlDriverProgressBus;
import com.clougence.clouddm.platform.plugin.PluginLoadHelper;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.drivers.DriverPrepareProgress;
import com.clougence.drivers.DriverVersion;
import com.clougence.drivers.def.ResDef;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ThreadUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InitMysqlDriverService {

    public static final String MYSQL_DRIVER_FAMILY = "MySQL Connector/J";
    public static final String MYSQL_DRIVER_VERSION = "8.0.33";
    public static final String WS_EVENT_TYPE       = "INIT_MYSQL_DRIVER_PROGRESS";

    private final ExecutorService                  downloadExecutor;
    private final ConcurrentHashMap<String, Boolean> runningTasks = new ConcurrentHashMap<>();
    private final Object                           pluginLoadLock = new Object();

    private volatile boolean pluginsLoaded;

    public InitMysqlDriverService(){
        ThreadFactory threadFactory = ThreadUtils.daemonThreadFactory(this.getClass().getClassLoader(), "init-mysql-driver-%s");
        this.downloadExecutor = Executors.newSingleThreadExecutor(threadFactory);
    }

    public DriverVersionStatusVO checkDriverStatus() {
        ensurePluginDriversLoaded();

        DriverVersionStatusVO statusVO = new DriverVersionStatusVO();
        statusVO.setDriverFamily(MYSQL_DRIVER_FAMILY);
        statusVO.setDriverVersion(MYSQL_DRIVER_VERSION);
        statusVO.setWorkerWsn(new ArrayList<>());

        DriverVersion localVersion = PluginManager.driverLoader().findDriver(MYSQL_DRIVER_FAMILY, MYSQL_DRIVER_VERSION);
        if (localVersion != null) {
            PluginManager.driverLoader().refreshDriverVersion(localVersion);
        }

        statusVO.setAvailable(isPrepared(localVersion));
        return statusVO;
    }

    public void downloadDriver() {
        ensurePluginDriversLoaded();
        String taskKey = MYSQL_DRIVER_FAMILY + "::" + MYSQL_DRIVER_VERSION;
        if (this.runningTasks.putIfAbsent(taskKey, Boolean.TRUE) != null) {
            return;
        }

        this.downloadExecutor.execute(() -> {
            try {
                downloadDriverInternal();
            } catch (Exception e) {
                log.error("[InitMysqlDriverService] Download mysql driver failed.", e);
                publishProgress(0, 0, 0, "FAILED", false, null, null, e.getMessage());
            } finally {
                this.runningTasks.remove(taskKey);
            }
        });
    }

    private void downloadDriverInternal() {
        DriverVersion localVersion = PluginManager.driverLoader().findDriver(MYSQL_DRIVER_FAMILY, MYSQL_DRIVER_VERSION);
        if (localVersion == null) {
            throw new IllegalStateException("driver not found: " + MYSQL_DRIVER_FAMILY + " / " + MYSQL_DRIVER_VERSION);
        }

        log.info("[InitMysqlDriverService] Start mysql driver prepare. family={}, version={}", MYSQL_DRIVER_FAMILY, MYSQL_DRIVER_VERSION);
        resetPreparedResources(localVersion);
        prepareResources(localVersion);
        PluginManager.driverLoader().refreshDriverVersion(localVersion);
        publishCompletion(localVersion);
    }

    private void prepareResources(DriverVersion localVersion) {
        List<ResDef> resources = localVersion == null ? null : localVersion.getResources();
        if (CollectionUtils.isEmpty(resources)) {
            return;
        }

        int totalResourceCount = resources.size();
        AtomicInteger completedCounter = new AtomicInteger();
        for (ResDef resource : resources) {
            if (resource == null || StringUtils.isBlank(resource.getCoordinate())) {
                continue;
            }

            AtomicReference<RuntimeException> prepareError = new AtomicReference<>();
            PluginManager.driverLoader().prepareDriverVersion(localVersion, current -> current != resource, new DriverPrepareProgress() {

                @Override
                public void onStart(DriverVersion driverVersionValue, ResDef driverResource, int resourceIndex, int totalCount) {
                    publishProgress(totalResourceCount,
                        completedCounter.get(),
                        0,
                        "PREPARING",
                        false,
                        buildResourceCoordinate(driverResource),
                        null,
                        "prepare started");
                }

                @Override
                public void onProgress(DriverVersion driverVersionValue, ResDef driverResource, String fileName, long current, long total) {
                    publishProgress(totalResourceCount,
                        completedCounter.get(),
                        calcPercent(current, total),
                        "PREPARING",
                        false,
                        buildResourceCoordinate(driverResource),
                        fileName,
                        "preparing resource");
                }

                @Override
                public void onComplete(DriverVersion driverVersionValue, ResDef driverResource, int resourceIndex, int totalCount) {
                    publishProgress(totalResourceCount,
                        completedCounter.get(),
                        100,
                        "PREPARING",
                        false,
                        buildResourceCoordinate(driverResource),
                        null,
                        "resource prepared");
                }

                @Override
                public void onError(DriverVersion driverVersionValue, ResDef driverResource, Exception exception) {
                    prepareError.set(new RuntimeException(exception == null ? "prepare driver resource failed" : exception.getMessage(), exception));
                    publishProgress(totalResourceCount,
                        completedCounter.get(),
                        0,
                        "FAILED",
                        false,
                        buildResourceCoordinate(driverResource),
                        null,
                        exception == null ? "prepare driver resource failed" : exception.getMessage());
                }
            });

            if (prepareError.get() != null) {
                throw prepareError.get();
            }

            completedCounter.incrementAndGet();
            publishProgress(totalResourceCount,
                completedCounter.get(),
                100,
                "PREPARING",
                false,
                buildResourceCoordinate(resource),
                null,
                "resource prepared");
        }
    }

    private void publishCompletion(DriverVersion localVersion) {
        boolean available = isPrepared(localVersion);
        int totalFileCount = localVersion == null || CollectionUtils.isEmpty(localVersion.getResources()) ? 0 : localVersion.getResources().size();
        publishProgress(totalFileCount,
            totalFileCount,
            100,
            "COMPLETED",
            available,
            null,
            null,
            available ? "driver ready" : "driver unavailable");
    }

    private void ensurePluginDriversLoaded() {
        if (this.pluginsLoaded) {
            return;
        }

        synchronized (this.pluginLoadLock) {
            if (this.pluginsLoaded) {
                return;
            }

            File pluginPath1 = new File(GlobalConfUtils.getPluginDir("plugins"));
            File pluginPath2 = new File(GlobalConfUtils.getAppDataHome(), "plugins");
            PluginLoadHelper.loadPlugins(InitApplication.class.getClassLoader(), pluginPath1, pluginPath2);
            this.pluginsLoaded = true;
        }
    }

    private void resetPreparedResources(DriverVersion localVersion) {
        if (localVersion == null) {
            return;
        }

        localVersion.deleteFiles();
        localVersion.setPrepared(false);
        if (CollectionUtils.isEmpty(localVersion.getResources())) {
            return;
        }

        for (ResDef resource : localVersion.getResources()) {
            if (resource == null) {
                continue;
            }
            resource.setPrepared(false);
            resource.setFileDefList(null);
        }
    }

    private boolean isPrepared(DriverVersion driverVersion) {
        if (driverVersion == null) {
            return false;
        }
        List<ResDef> resources = driverVersion.getResources();
        if (CollectionUtils.isEmpty(resources)) {
            return true;
        }
        for (ResDef resource : resources) {
            if (resource == null || !resource.isPrepared()) {
                return false;
            }
        }
        return true;
    }

    private void publishProgress(int totalFileCount,
                                 int completedFileCount,
                                 int currentFilePercent,
                                 String status,
                                 boolean available,
                                 String resourceCoordinate,
                                 String currentFileName,
                                 String message) {
        DriverDownloadProgressVO progressVO = new DriverDownloadProgressVO();
        progressVO.setDriverFamily(MYSQL_DRIVER_FAMILY);
        progressVO.setDriverVersion(MYSQL_DRIVER_VERSION);
        progressVO.setTotalFileCount(totalFileCount);
        progressVO.setCompletedFileCount(completedFileCount);
        progressVO.setCurrentFilePercent(currentFilePercent);
        progressVO.setStatus(status);
        progressVO.setAvailable(available);
        progressVO.setResourceCoordinate(resourceCoordinate);
        progressVO.setCurrentFileName(currentFileName);
        progressVO.setMessage(message);
        InitMysqlDriverProgressBus.publish(progressVO);
    }

    private String buildResourceCoordinate(ResDef resource) {
        return resource == null ? "" : StringUtils.defaultIfBlank(resource.getCoordinate(), "");
    }

    private int calcPercent(long current, long total) {
        if (total <= 0L) {
            return 0;
        }
        long percent = Math.round((current * 100.0d) / total);
        return (int) Math.max(0L, Math.min(100L, percent));
    }
}
