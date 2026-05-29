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
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.model.vo.datasource.DriverDownloadProgressVO;
import com.clougence.clouddm.init.InitApplication;
import com.clougence.clouddm.init.component.log.InitMysqlDriverProgressBus;
import com.clougence.clouddm.init.constant.I18nInitFieldKeys;
import com.clougence.clouddm.platform.dal.config.DmDalConfig;
import com.clougence.clouddm.platform.plugin.PluginLoadHelper;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.drivers.DriverBinding;
import com.clougence.drivers.DriverPrepareProgress;
import com.clougence.drivers.DriverVersion;
import com.clougence.drivers.def.ResDef;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.ThreadUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class InitMysqlDriverService {

    public static final String    WS_EVENT_TYPE = "INIT_MYSQL_DRIVER_PROGRESS";
    private final ExecutorService downloadExecutor;
    private volatile boolean      downloadRunning;

    public enum RuntimeDriverStatus {
        UNAVAILABLE,
        DOWNLOADING,
        READY
    }

    public InitMysqlDriverService(){
        ThreadFactory threadFactory = ThreadUtils.daemonThreadFactory(this.getClass().getClassLoader(), "init-mysql-driver-%s");
        this.downloadExecutor = Executors.newSingleThreadExecutor(threadFactory);
    }

    public RuntimeDriverStatus driverStatus() {
        if (this.downloadRunning) {
            return RuntimeDriverStatus.DOWNLOADING;
        } else {
            return resolveDriverStatus();
        }
    }

    private RuntimeDriverStatus resolveDriverStatus() {
        try {
            DriverVersion ver = DmDalConfig.mainDsDriverVersion();
            if (!ver.isPrepared()) {
                return RuntimeDriverStatus.UNAVAILABLE;
            }

            DriverBinding binding = PluginManager.driverLoader().createBinding(//
                    DmDalConfig.class.getClassLoader(), DmDalConfig.MYSQL_DRIVER_RUNTIME_FAMILY, DmDalConfig.MYSQL_DRIVER_VERSION);
            if (DmDalConfig.isDriverClassAvailable(binding)) {
                return RuntimeDriverStatus.READY;
            }
        } catch (Exception e) {
            log.debug("[InitMysqlDriverService] Runtime MySQL driver is not ready: {}", e.getMessage());
        }
        return RuntimeDriverStatus.UNAVAILABLE;
    }

    //
    //
    //

    public synchronized void downloadDriver() {
        RuntimeDriverStatus status = driverStatus();
        if (status == RuntimeDriverStatus.READY) {
            publishCompletion();
            return;
        }
        if (status == RuntimeDriverStatus.DOWNLOADING) {
            publishDownloadStarted();
            return;
        }

        this.downloadRunning = true;
        publishDownloadStarted();
        this.downloadExecutor.execute(() -> {
            try {
                File pluginPath1 = new File(GlobalConfUtils.getPluginDir("plugins"));
                File pluginPath2 = new File(GlobalConfUtils.getAppDataHome(), "plugins");
                PluginLoadHelper.loadPlugins(InitApplication.class.getClassLoader(), pluginPath1, pluginPath2);
                downloadDriverInternal();
            } catch (Exception e) {
                log.error("[InitMysqlDriverService] Download mysql driver failed.", e);
                String summary = i18n(I18nInitFieldKeys.INIT_MYSQL_DRIVER_PREPARE_FAILED);
                publishProgress(0, 0, 0, "FAILED", false, null, summary, StringUtils.defaultIfBlank(e.getMessage(), summary));
            } finally {
                this.downloadRunning = false;
            }
        });
    }

    private void downloadDriverInternal() {
        if (resolveDriverStatus() == RuntimeDriverStatus.READY) {
            this.publishCompletion();
            return;
        }

        DriverVersion ver = DmDalConfig.mainDsDriverVersion();
        log.info("[InitMysqlDriverService] Start mysql runtime driver prepare. family={}, version={}, maven={}", //
                DmDalConfig.MYSQL_DRIVER_RUNTIME_FAMILY, DmDalConfig.MYSQL_DRIVER_VERSION, DmDalConfig.MYSQL_DRIVER_MAVEN_COORDINATE);
        this.prepareDriver(ver);

        if (resolveDriverStatus() != RuntimeDriverStatus.READY) {
            throw new IllegalStateException("Runtime MySQL driver class is unavailable.");
        }

        this.publishCompletion();
    }

    private void publishCompletion() {
        boolean available = resolveDriverStatus() == RuntimeDriverStatus.READY;
        String msg = available ? i18n(I18nInitFieldKeys.INIT_MYSQL_DRIVER_READY) : i18n(I18nInitFieldKeys.INIT_MYSQL_DRIVER_UNAVAILABLE);
        publishProgress(1, available ? 1 : 0, 100, "COMPLETED", available, null, msg);
    }

    private void publishDownloadStarted() {
        publishProgress(0, 0, 0, "PREPARING", false, null, i18n(I18nInitFieldKeys.INIT_MYSQL_DRIVER_PREPARE_STARTED));
    }

    private void prepareDriver(DriverVersion ver) {
        if (ver == null || CollectionUtils.isEmpty(ver.getResources())) {
            throw new IllegalStateException("runtime maven driver resource is unavailable.");
        }

        ResDef mavenResource = ver.getResources().get(0);
        Set<String> completedFiles = ConcurrentHashMap.newKeySet();

        AtomicReference<RuntimeException> prepareError = new AtomicReference<>();
        PluginManager.driverLoader().prepareDriverVersion(ver, c -> c != mavenResource, new DriverPrepareProgress() {

            @Override
            public void onStart(DriverVersion driverVersionValue, ResDef driverResource, int resourceIndex, int totalCount) {
                String msg = i18n(I18nInitFieldKeys.INIT_MYSQL_DRIVER_PREPARE_STARTED);
                publishProgress(resolveDriverFileCount(driverResource), completedFiles.size(), 0, "PREPARING", false, null, msg);
            }

            @Override
            public void onProgress(DriverVersion driverVersionValue, ResDef driverResource, String fileName, long current, long total) {
                if (StringUtils.isNotBlank(fileName) && total > 0 && current >= total) {
                    completedFiles.add(fileName);
                }

                String msg = buildDownloadMessage(fileName, current, total);
                publishProgress(resolveDriverFileCount(driverResource), completedFiles.size(), calcPercent(current, total), "PREPARING", false, fileName, msg);
            }

            @Override
            public void onComplete(DriverVersion driverVersionValue, ResDef driverResource, int resourceIndex, int totalCount) {
                String msg = i18n(I18nInitFieldKeys.INIT_MYSQL_DRIVER_FILE_DOWNLOAD_COMPLETE);
                publishProgress(resolveDriverFileCount(driverResource), resolveDriverFileCount(driverResource), 100, "PREPARING", false, null, msg);
            }

            @Override
            public void onError(DriverVersion driverVersionValue, ResDef driverResource, Exception exception) {
                String summary = i18n(I18nInitFieldKeys.INIT_MYSQL_DRIVER_PREPARE_FAILED);
                String detailMessage = buildPrepareErrorMessage(exception);
                prepareError.set(new RuntimeException(detailMessage, exception));
                publishProgress(resolveDriverFileCount(driverResource), completedFiles.size(), 0, "FAILED", false, null, summary, detailMessage);
            }
        });

        if (prepareError.get() != null) {
            throw prepareError.get();
        }

        int totalFileCount = resolveDriverFileCount(mavenResource);
        String msg = i18n(I18nInitFieldKeys.INIT_MYSQL_DRIVER_FILE_DOWNLOAD_COMPLETE);
        publishProgress(totalFileCount, totalFileCount, 100, "PREPARING", false, null, msg);

        if (CollectionUtils.isEmpty(mavenResource.getFileDefList())) {
            throw new IllegalStateException("prepared mysql driver files not found.");
        }
    }

    private void publishProgress(int totalFileCount, int completedFileCount, int currentFilePercent, String status, boolean available, String currentFileName, String message) {
        publishProgress(totalFileCount, completedFileCount, currentFilePercent, status, available, currentFileName, message, null);
    }

    private void publishProgress(int totalFileCount, int completedFileCount, int currentFilePercent, String status, boolean available, String currentFileName, String message,
                                 String detailMessage) {
        DriverDownloadProgressVO progressVO = new DriverDownloadProgressVO();
        progressVO.setDriverFamily(DmDalConfig.MYSQL_DRIVER_RUNTIME_FAMILY);
        progressVO.setDriverVersion(DmDalConfig.MYSQL_DRIVER_VERSION);
        progressVO.setTotalFileCount(totalFileCount);
        progressVO.setCompletedFileCount(completedFileCount);
        progressVO.setCurrentFilePercent(currentFilePercent);
        progressVO.setStatus(status);
        progressVO.setAvailable(available);
        progressVO.setCurrentFileName(currentFileName);
        progressVO.setMessage(message);
        progressVO.setDetailMessage(detailMessage);
        InitMysqlDriverProgressBus.publish(progressVO);
    }

    private int resolveDriverFileCount(ResDef resource) {
        if (resource == null || CollectionUtils.isEmpty(resource.getFileDefList())) {
            return 1;
        }
        return resource.getFileDefList().size();
    }

    private String buildDownloadMessage(String fileName, long current, long total) {
        String displayName = StringUtils.defaultIfBlank(fileName, i18n(I18nInitFieldKeys.INIT_MYSQL_DRIVER_FILE_DEFAULT_NAME));
        int percent = calcPercent(current, total);
        return i18n(I18nInitFieldKeys.INIT_MYSQL_DRIVER_FILE_DOWNLOADING, displayName, percent);
    }

    private String i18n(I18nInitFieldKeys key, Object... args) {
        return DmI18nUtils.getMessage(key.name(), args);
    }

    static String buildPrepareErrorMessage(Throwable e) {
        if (e == null) {
            return "Prepare mysql driver failed.";
        }

        Throwable[] eList = ExceptionUtils.getThrowables(e);
        Throwable rootCause = ExceptionUtils.getRootCause(e);
        rootCause = rootCause == null ? e : rootCause;

        StringBuilder message = new StringBuilder("Prepare mysql driver failed.");
        String rootMessage = StringUtils.trimToNull(ExceptionUtils.getMessage(rootCause));
        if (rootMessage != null) {
            message.append(" Root cause: ").append(rootMessage).append('.');
        }

        Throwable transferContext = findMavenTransferContext(eList, rootCause);
        String transferMessage = transferContext == null ? null : StringUtils.trimToNull(ExceptionUtils.getMessage(transferContext));
        if (transferMessage != null && message.indexOf(transferMessage) < 0) {
            message.append(" Maven transfer: ").append(transferMessage).append('.');
        }
        return message.toString();
    }

    private static Throwable findMavenTransferContext(Throwable[] eList, Throwable rootCause) {
        if (eList == null) {
            return null;
        }

        for (int i = eList.length - 1; i >= 0; i--) {
            Throwable candidate = eList[i];
            if (candidate == null || candidate == rootCause) {
                continue;
            }
            String text = StringUtils.defaultString(candidate.getMessage()).toLowerCase(Locale.ROOT);
            if (text.contains("could not transfer artifact") || text.contains("could not be resolved") || text.contains("artifact descriptor")) {
                return candidate;
            }
        }
        return null;
    }

    private int calcPercent(long current, long total) {
        if (total <= 0L) {
            return 0;
        }
        return (int) Math.clamp(Math.round((current * 100.0d) / total), 0L, 100L);
    }
}
