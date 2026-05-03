package com.clougence.clouddm.worker.component.rsocket;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.comm.WorkerRSocketClient;
import com.clougence.clouddm.worker.component.report.ReportService;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.SystemUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @author wanshao create time is 2021/1/14
 **/
@Slf4j
@Service
public class RSocketClientServiceImpl {

    @Resource
    private ReportService       reportService;
    @Resource
    private WorkerRSocketClient workerRSocketClient;

    public void init() throws Exception {
        String appMode = SystemUtils.getSystemProperty("app.mode", "distributed");
        if (StringUtils.equalsIgnoreCase(appMode, "embedded")) {
            log.info("-Dapp.mode=embedded");
        }

        log.info("load RSocket...");

        // connect remote
        log.info("load RSocket...");
        this.workerRSocketClient.start();
        this.reportService.init();

        log.info("Console rsocket server started.");
    }

    public void stop() {
        try {
            log.info(this.getClass().getSimpleName() + " begin to stop.");

            // remote
            this.reportService.stop();
            this.workerRSocketClient.stop();

            log.info(this.getClass().getSimpleName() + " stop successfully.");
        } catch (Exception e) {
            String errMsg = this.getClass().getSimpleName() + " stop failed,but ignore,msg: " + ExceptionUtils.getRootCauseMessage(e);
            log.error(errMsg, e);
        }
    }
}
