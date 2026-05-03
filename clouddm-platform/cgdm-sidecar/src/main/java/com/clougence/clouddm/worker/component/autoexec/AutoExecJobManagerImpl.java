package com.clougence.clouddm.worker.component.autoexec;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Resource;

import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.console.autoexec.ExecJobRService;
import com.clougence.clouddm.comm.model.auth.WorkerIdentity;
import com.clougence.clouddm.worker.component.report.ReportUtils;
import com.clougence.utils.ThreadUtils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AutoExecJobManagerImpl implements AutoExecJobManager, UnifiedPostConstruct {

    @Resource
    private ApplicationContext           applicationContext;

    @Resource
    private ExecJobRService              execJobRService;

    private ThreadPoolExecutor           threadPoolExecutor;

    private ScheduledThreadPoolExecutor  scheduledThreadPoolExecutor;

    private final Map<Long, AutoExecJob> map    = new ConcurrentHashMap<>();

    private final AtomicBoolean          inited = new AtomicBoolean();
    private WorkerIdentity               workerIdentity;

    @SneakyThrows
    public void init() {
        if (!this.inited.compareAndSet(false, true)) {
            return;
        }
        ThreadFactory tf = ThreadUtils.daemonThreadFactory(this.getClass().getClassLoader(), "AutoExecWorker-%s");
        // if jobIdQueue is full, ignore the latest additions
        this.threadPoolExecutor = new ThreadPoolExecutor(2, 20, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>(200), tf, new ThreadPoolExecutor.AbortPolicy());

        this.scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1, tf);
        this.scheduledThreadPoolExecutor.scheduleWithFixedDelay(this::activeJobReport, 10, 10, TimeUnit.SECONDS);

        log.info("autoExecWorkers started");
    }

    private void activeJobReport() {
        Set<Long> ids = map.keySet();
        if (ids.isEmpty()) {
            return;
        }
        try {
            this.execJobRService.reportActiveJobs(identity(), new ArrayList<>(ids));
        } catch (Exception e) {
            log.error("[ACTIVE JOB REPORT] error " + e.getMessage(), e);
        }
    }

    @Override
    public void stop() {
        if (this.inited.compareAndSet(true, false)) {
            this.threadPoolExecutor.shutdown();
            this.scheduledThreadPoolExecutor.shutdown();
        }
    }

    public void pauseJob(Long jobId) throws Exception {
        AutoExecJob task = map.get(jobId);
        if (task != null) {
            task.pause();
        }
    }

    public void submit(Long jobId) {
        if (map.containsKey(jobId)) {
            return;
        }

        AutoExecJob task = this.applicationContext.getBean(AutoExecJob.class);

        task.init(jobId);

        map.put(jobId, task);
        try {
            threadPoolExecutor.execute(() -> {
                try {
                    MDC.put("jobId", jobId.toString());
                    task.run();
                } finally {
                    map.remove(jobId);
                    MDC.remove("jobId");
                }
            });
        } catch (RejectedExecutionException e) {
            // jobQueue full
            this.map.remove(jobId);
        }
    }

    private WorkerIdentity identity() throws Exception {
        if (this.workerIdentity == null) {
            this.workerIdentity = ReportUtils.getIdentity();
        }
        return this.workerIdentity;
    }

}
