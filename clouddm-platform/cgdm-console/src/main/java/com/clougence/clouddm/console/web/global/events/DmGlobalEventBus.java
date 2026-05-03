package com.clougence.clouddm.console.web.global.events;

import java.io.IOException;

import org.noear.dami.Dami;

import com.clougence.clouddm.console.web.dal.model.DmAsyncTaskDO;
import com.clougence.clouddm.console.web.model.vo.datasource.DriverDownloadProgressVO;
import com.clougence.clouddm.console.web.model.vo.editor.query.WsResMsg;
import com.clougence.clouddm.console.web.model.vo.export.DmExportVO;
import com.clougence.utils.function.EConsumer;

/**
 * @author mode 2020-01-04 09:44
 * @since 1.1.3
 */
public class DmGlobalEventBus {

    // ------------------------------------------------------------------------
    //                                                              DmAsyncTask
    // ------------------------------------------------------------------------

    public static void addDmAsyncEventListen(EConsumer<DmAsyncTaskDO, IOException> consumer) {
        Dami.bus().listen("/DmAsyncTask", payload -> consumer.eAccept((DmAsyncTaskDO) payload.getContent()));
    }

    public static void triggerDmAsyncEvent(DmAsyncTaskDO taskDO) {
        if (taskDO.isShowInDock()) {
            Dami.bus().send("/DmAsyncTask", taskDO);
        }
    }

    // ------------------------------------------------------------------------
    //                                                                   Result
    // ------------------------------------------------------------------------

    public static void triggerQueryResultEvent(WsResMsg queryFO) {
        Dami.bus().send("/DmQueryResponse", queryFO);
    }

    public static void addQueryResultEventListen(EConsumer<WsResMsg, IOException> consumer) {
        Dami.bus().listen("/DmQueryResponse", payload -> consumer.eAccept((WsResMsg) payload.getContent()));
    }

    public static void triggerQueryResultExportEvent(DmExportVO exportVO) {
        Dami.bus().send("/DmQueryExport", exportVO);
    }

    public static void addQueryResultExportListen(EConsumer<DmExportVO, IOException> consumer) {
        Dami.bus().listen("/DmQueryExport", payload -> consumer.eAccept((DmExportVO) payload.getContent()));
    }

    public static void triggerDriverDownloadEvent(DriverDownloadProgressVO progressVO) {
        Dami.bus().send("/DmDriverDownload", progressVO);
    }

    public static void addDriverDownloadEventListen(EConsumer<DriverDownloadProgressVO, IOException> consumer) {
        Dami.bus().listen("/DmDriverDownload", payload -> consumer.eAccept((DriverDownloadProgressVO) payload.getContent()));
    }

}
