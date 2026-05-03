package com.clougence.clouddm.worker.component.session;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.sidecar.session.drivers.DriverRef;
import com.clougence.clouddm.api.sidecar.session.drivers.DriverUtils;
import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.platform.plugin.DsPluginInfo;
import com.clougence.clouddm.platform.plugin.PluginManager;
import com.clougence.clouddm.sdk.execute.resource.DsResourceManager;
import com.clougence.clouddm.sdk.execute.session.Session;
import com.clougence.clouddm.sdk.execute.session.SessionContextDTO;
import com.clougence.clouddm.sdk.execute.session.SessionFactory;
import com.clougence.clouddm.sdk.execute.session.result.ValueProcessService;
import com.clougence.clouddm.sdk.service.file.FileService;
import com.clougence.clouddm.worker.component.notify.SidecarSqlNotifyService;
import com.clougence.clouddm.worker.global.config.DmSidecarConfig;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.utils.StringUtils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SessionManagerImpl implements SessionManager, UnifiedPostConstruct {

    private final Map<String, SessionAgent> sessionMap  = new ConcurrentHashMap<>();
    private final AtomicBoolean             inited      = new AtomicBoolean(false);
    private final AtomicInteger             counter     = new AtomicInteger();
    @Resource
    private DmSidecarConfig                 dmConfig;
    @Resource
    private SidecarSqlNotifyService         sidecarSqlNotifyService;
    @Resource
    private FileService                     fileService = null;
    private Thread                          sessionManagerThread;
    private String                          localWsn    = null;
    private ValueProcessService             valueProcessService;

    @Override
    public void init() throws Exception {
        if (inited.compareAndSet(false, true)) {
            this.sessionManagerThread = new Thread(this::checkAndClearSession);
            this.sessionManagerThread.setDaemon(true);
            this.sessionManagerThread.setName("SessionManager-Cleaner");
            this.sessionManagerThread.start();
            this.localWsn = GlobalConfUtils.loadGlobalConf().getWsn();
        }
    }

    private ValueProcessService findValueProcessSpi() {
        if (this.valueProcessService != null) {
            return this.valueProcessService;
        }

        try {
            this.valueProcessService = PluginManager.findService(ValueProcessService.class);
        } catch (UnsupportedOperationException ignored) {
        }

        return this.valueProcessService;
    }

    @Override
    public void stop() {
        if (inited.compareAndSet(true, false)) {
            try {
                if (this.sessionManagerThread != null) {
                    this.sessionManagerThread.interrupt();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public int getMaxSessionCount() { return this.dmConfig.getMaxSessionCount(); }

    @Override
    public int getSessionCount() { return this.sessionMap.size(); }

    @Override
    public boolean hasSessionById(String sessionId) {
        return this.sessionMap.containsKey(sessionId);
    }

    @Override
    public SessionAgent getSessionById(String sessionId) {
        return this.sessionMap.get(sessionId);
    }

    @SneakyThrows
    @Override
    public SessionAgent createSession(DsResourceManager rm, DataSourceConfig dsConfig, SessionContextDTO contextDTO) {
        if (!rm.isReady()) {
            throw new RuntimeException("ResourceManager is not ready.");
        }

        String newSessionId = contextDTO.getSessionId();
        if (StringUtils.isBlank(newSessionId)) {
            throw new RuntimeException(newSessionId + " newSessionId is blank.");
        }

        Integer maxIdleTimeSec = contextDTO.getMaxIdleTimeSec();
        if (maxIdleTimeSec == null || maxIdleTimeSec <= 0) {
            maxIdleTimeSec = Integer.MAX_VALUE;
        }

        if (this.sessionMap.containsKey(newSessionId)) {
            throw new RuntimeException(newSessionId + " newSessionId is exist.");
        }

        try {
            int configMaxSessionCount = this.dmConfig.getMaxSessionCount();
            if (counter.incrementAndGet() > configMaxSessionCount) {
                throw new IllegalStateException("exceed session max pool size: " + configMaxSessionCount);
            }

            DsPluginInfo pluginInfo = PluginManager.findDsPlugin(dsConfig.getDataSourceType());
            if (pluginInfo == null) {
                throw new UnsupportedOperationException("no plugin found for dsType '" + dsConfig.getDataSourceType() + "'.");
            }

            DriverRef driverRef = DriverUtils.parseDriverRef(dsConfig.getDriverVersion());
            SessionFactory factory = pluginInfo.createSessionFactory(driverRef.getDriverFamily(), driverRef.getDriverVersion());
            Session session = factory.createSession(rm, dsConfig, contextDTO);
            session.addCloseListener(this::closeSessionById);

            SessionSupport ss = new SessionSupport();
            ss.setSessionId(newSessionId);
            ss.setLocalWsn(this.localWsn);
            ss.setFileService(this.fileService);
            ss.setNotifyService(this.sidecarSqlNotifyService);
            ss.setResultProcessSpi(this.findValueProcessSpi());
            SessionAgent agent = new SessionAgent(session, ss, rm, maxIdleTimeSec);

            this.sessionMap.put(newSessionId, agent);
            return agent;
        } catch (Throwable e) {
            counter.decrementAndGet();
            throw e;
        }
    }

    /** close by outside */
    @Override
    public void closeSessionById(String sessionId) {
        if (this.sessionMap.containsKey(sessionId)) {
            Session rdbSession = this.sessionMap.get(sessionId);
            if (rdbSession == null) {
                return;
            }

            this.sessionMap.remove(sessionId);
            this.counter.decrementAndGet();
            try {
                rdbSession.close();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /** close by daemon (session timeout) */
    protected void checkAndClearSession() {
        while (inited.get()) {
            try {
                Thread.sleep(5000);
                List<String> toClose = new ArrayList<>();
                for (SessionAgent session : this.sessionMap.values()) {
                    if (session.tryIdle()) {
                        toClose.add(session.getSessionId());
                    }
                }
                String sids = StringUtils.join(toClose.toArray(), ",");
                log.info("checkAndClearSession -> " + (StringUtils.isBlank(sids) ? "empty." : sids));
                closeTarget(toClose);

            } catch (InterruptedException ignore) {
                //
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    void closeTarget(List<String> sessionIds) {
        if (sessionIds == null || sessionIds.isEmpty()) {
            return;
        }
        for (String sessionId : sessionIds) {
            closeSessionById(sessionId);
        }
    }
}
