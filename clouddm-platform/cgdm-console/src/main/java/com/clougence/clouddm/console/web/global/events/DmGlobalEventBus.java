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
package com.clougence.clouddm.console.web.global.events;

import java.io.IOException;

import org.noear.dami.Dami;

import com.clougence.clouddm.console.web.component.dsconfig.event.DriverDownloadEvent;
import com.clougence.clouddm.console.web.model.vo.editor.WsResult;
import com.clougence.clouddm.console.web.model.vo.editor.query.WsQueryResult;
import com.clougence.clouddm.console.web.model.vo.export.DmExportVO;
import com.clougence.clouddm.platform.dal.model.execution.DmExecAsyncTaskDO;
import com.clougence.utils.function.EConsumer;

/**
 * @author mode 2020-01-04 09:44
 * @since 1.1.3
 */
public class DmGlobalEventBus {

    // ------------------------------------------------------------------------
    //                                                              DmAsyncTask
    // ------------------------------------------------------------------------

    public static void addDmAsyncEventListen(EConsumer<DmExecAsyncTaskDO, IOException> consumer) {
        Dami.bus().listen("/DmAsyncTask", payload -> consumer.eAccept((DmExecAsyncTaskDO) payload.getContent()));
    }

    public static void triggerDmAsyncEvent(DmExecAsyncTaskDO taskDO) {
        if (taskDO.isShowInDock()) {
            Dami.bus().send("/DmAsyncTask", taskDO);
        }
    }

    // ------------------------------------------------------------------------
    //                                                                   Result
    // ------------------------------------------------------------------------

    public static void triggerQueryResultEvent(WsResult queryFO) {
        Dami.bus().send("/DmQueryResponse", queryFO);
    }

    public static void addQueryResultEventListen(EConsumer<WsQueryResult, IOException> consumer) {
        Dami.bus().listen("/DmQueryResponse", payload -> consumer.eAccept((WsQueryResult) payload.getContent()));
    }

    public static void triggerLanguageResultEvent(WsResult languageResult) {
        Dami.bus().send("/DmLanguageResponse", languageResult);
    }

    public static void addLanguageResultEventListen(EConsumer<WsResult, IOException> consumer) {
        Dami.bus().listen("/DmLanguageResponse", payload -> consumer.eAccept((WsResult) payload.getContent()));
    }

    public static void triggerQueryResultExportEvent(DmExportVO exportVO) {
        Dami.bus().send("/DmQueryExport", exportVO);
    }

    public static void addQueryResultExportListen(EConsumer<DmExportVO, IOException> consumer) {
        Dami.bus().listen("/DmQueryExport", payload -> consumer.eAccept((DmExportVO) payload.getContent()));
    }

    public static void triggerDriverDownloadEvent(DriverDownloadEvent event) {
        Dami.bus().send("/DmDriverDownload", event);
    }

    public static void addDriverDownloadEventListen(EConsumer<DriverDownloadEvent, IOException> consumer) {
        Dami.bus().listen("/DmDriverDownload", payload -> consumer.eAccept((DriverDownloadEvent) payload.getContent()));
    }
}
