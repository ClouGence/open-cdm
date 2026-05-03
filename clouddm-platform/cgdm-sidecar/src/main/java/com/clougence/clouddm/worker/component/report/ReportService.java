package com.clougence.clouddm.worker.component.report;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.ThreadUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Execute shell to get local system's info or using oshi library to collect system stat info
 *
 * @author wanshao create time is 2020/1/14
 **/
@Slf4j
@Component
public class ReportService {

    public static final Integer         HEART_BEAT_INTERVAL_SEC = 10;
    public static final Integer         METRIC_INTERVAL_SEC     = 5;
    private final AtomicBoolean         inited                  = new AtomicBoolean(false);
    private ScheduledThreadPoolExecutor executorService;

    @Resource
    private TaskHeartbeatReport         heartbeatReport;
    @Resource
    private TaskMetricReport            metricReport;

    public void init() throws Exception {
        if (inited.compareAndSet(false, true)) {
            log.info(this.getClass().getSimpleName() + " begin to start.");

            this.executorService = new ScheduledThreadPoolExecutor(2, new NamedThreadFactory("report-task", true));
            this.executorService.setRemoveOnCancelPolicy(true);
            this.executorService.scheduleAtFixedRate(this.heartbeatReport, HEART_BEAT_INTERVAL_SEC, HEART_BEAT_INTERVAL_SEC, TimeUnit.SECONDS);
            this.executorService.scheduleAtFixedRate(this.metricReport, METRIC_INTERVAL_SEC, METRIC_INTERVAL_SEC, TimeUnit.SECONDS);

            ThreadUtils.runDaemonThread(this::forceRequest);

            log.info(this.getClass().getSimpleName() + " start successfully.");
        }
    }

    private void forceRequest() {
        this.heartbeatReport.run();
        this.metricReport.run();
    }

    public void stop() {
        if (inited.compareAndSet(true, false)) {
            try {
                log.info(this.getClass().getSimpleName() + " begin to stop.");

                if (this.executorService != null) {
                    this.shutdownNowThreadPool(this.executorService);
                }

                log.info(this.getClass().getSimpleName() + " stop successfully.");
            } catch (Exception e) {
                String msg = this.getClass().getSimpleName() + " stop error,but ignore.msg:" + ExceptionUtils.getThrowableCount(e);
                log.error(msg, e);
            }
        }
    }

    private void shutdownNowThreadPool(ExecutorService executorService) {
        if (executorService != null) {
            try {
                executorService.shutdownNow();

                int MAX_TIMES = 3;
                int sTimes = 0;
                while (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
                    if (executorService.isShutdown() || executorService.isTerminated() || sTimes > MAX_TIMES) {
                        break;
                    }

                    executorService.shutdownNow();
                    sTimes++;
                }
            } catch (Exception e) {
                log.error("executor service shutdown now error.msg:" + ExceptionUtils.getRootCauseMessage(e), e);
            }
        }
    }
}
