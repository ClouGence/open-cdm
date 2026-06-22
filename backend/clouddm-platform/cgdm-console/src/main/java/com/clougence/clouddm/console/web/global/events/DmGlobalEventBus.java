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
import com.clougence.clouddm.console.web.model.vo.export.OpAuditExportProgressVO;
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

    public static void triggerQueryResultEvent(WsResult e) {
        Dami.bus().send("/DmQueryResponse", e);
    }

    public static void addQueryResultEventListen(EConsumer<WsQueryResult, IOException> c) {
        Dami.bus().listen("/DmQueryResponse", p -> c.eAccept((WsQueryResult) p.getContent()));
    }

    public static void triggerLanguageResultEvent(WsResult r) {
        Dami.bus().send("/DmLanguageResponse", r);
    }

    public static void addLanguageResultEventListen(EConsumer<WsResult, IOException> c) {
        Dami.bus().listen("/DmLanguageResponse", p -> c.eAccept((WsResult) p.getContent()));
    }

    public static void triggerQueryResultExportEvent(DmExportVO e) {
        Dami.bus().send("/DmQueryExport", e);
    }

    public static void addQueryResultExportListen(EConsumer<DmExportVO, IOException> c) {
        Dami.bus().listen("/DmQueryExport", p -> c.eAccept((DmExportVO) p.getContent()));
    }

    public static void triggerOpAuditExportEvent(OpAuditExportProgressVO vo) {
        Dami.bus().send("/DmOpAuditExport", vo);
    }

    public static void addOpAuditExportListen(EConsumer<OpAuditExportProgressVO, IOException> c) {
        Dami.bus().listen("/DmOpAuditExport", p -> c.eAccept((OpAuditExportProgressVO) p.getContent()));
    }

    public static void triggerDriverDownloadEvent(DriverDownloadEvent e) {
        Dami.bus().send("/DmDriverDownload", e);
    }

    public static void addDriverDownloadEventListen(EConsumer<DriverDownloadEvent, IOException> c) {
        Dami.bus().listen("/DmDriverDownload", p -> c.eAccept((DriverDownloadEvent) p.getContent()));
    }
}
