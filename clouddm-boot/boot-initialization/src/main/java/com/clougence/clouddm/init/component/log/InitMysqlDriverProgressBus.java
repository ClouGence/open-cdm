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

import java.util.concurrent.CopyOnWriteArraySet;
import java.util.function.Consumer;

import com.clougence.clouddm.console.web.model.vo.datasource.DriverDownloadProgressVO;
import com.clougence.clouddm.init.service.InitMysqlDriverService;

public final class InitMysqlDriverProgressBus {

    private static final CopyOnWriteArraySet<Consumer<InstallUpgradeLogEvent>> LISTENERS = new CopyOnWriteArraySet<>();

    private static volatile DriverDownloadProgressVO currentProgress;

    private InitMysqlDriverProgressBus(){
    }

    public static void publish(DriverDownloadProgressVO progressVO) {
        currentProgress = copyProgress(progressVO);
        publish(new InstallUpgradeLogEvent(InitMysqlDriverService.WS_EVENT_TYPE, copyProgress(progressVO)));
    }

    public static InstallUpgradeLogEvent snapshotEvent() {
        DriverDownloadProgressVO snapshot = copyProgress(currentProgress);
        return snapshot == null ? null : new InstallUpgradeLogEvent(InitMysqlDriverService.WS_EVENT_TYPE, snapshot);
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

    private static void publish(InstallUpgradeLogEvent event) {
        if (event == null) {
            return;
        }
        for (Consumer<InstallUpgradeLogEvent> listener : LISTENERS) {
            listener.accept(event);
        }
    }

    private static DriverDownloadProgressVO copyProgress(DriverDownloadProgressVO progressVO) {
        if (progressVO == null) {
            return null;
        }

        DriverDownloadProgressVO copy = new DriverDownloadProgressVO();
        copy.setClusterId(progressVO.getClusterId());
        copy.setDriverFamily(progressVO.getDriverFamily());
        copy.setDriverVersion(progressVO.getDriverVersion());
        copy.setTotalFileCount(progressVO.getTotalFileCount());
        copy.setCompletedFileCount(progressVO.getCompletedFileCount());
        copy.setCurrentFilePercent(progressVO.getCurrentFilePercent());
        copy.setStatus(progressVO.getStatus());
        copy.setMessage(progressVO.getMessage());
        copy.setDetailMessage(progressVO.getDetailMessage());
        copy.setCurrentFileName(progressVO.getCurrentFileName());
        copy.setAvailable(progressVO.isAvailable());
        return copy;
    }
}
