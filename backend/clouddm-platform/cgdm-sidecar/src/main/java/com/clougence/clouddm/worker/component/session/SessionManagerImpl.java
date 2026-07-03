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
package com.clougence.clouddm.worker.component.session;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.GlobalConfUtils;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.exception.DmErrorCode;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.sidecar.session.drivers.DriverRef;
import com.clougence.clouddm.api.sidecar.session.drivers.DriverUtils;
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
import com.clougence.clouddm.worker.component.session.ssh.SshTunnelHandle;
import com.clougence.clouddm.worker.component.session.ssh.SshTunnelManager;
import com.clougence.clouddm.worker.global.config.DmSidecarConfig;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.StringUtils;
import com.clougence.utils.io.IOUtils;

import jakarta.annotation.Resource;
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
    private SidecarSqlNotifyService         notifyService;
    @Resource
    private SshTunnelManager                sshTunnelManager;
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

        SshTunnelHandle tunnel = null;
        boolean sshEnabled = false;
        try {
            int configMaxSessionCount = this.dmConfig.getMaxSessionCount();
            if (counter.incrementAndGet() > configMaxSessionCount) {
                throw new IllegalStateException("exceed session max pool size: " + configMaxSessionCount);
            }
            DsPluginInfo pluginInfo = PluginManager.findDsPlugin(dsConfig.getDataSourceType());
            if (pluginInfo == null) {
                throw new UnsupportedOperationException("no plugin found for dsType '" + dsConfig.getDataSourceType() + "'.");
            }

            // driver
            DriverRef driverRef = DriverUtils.parseDriverRef(dsConfig.getDriverVersion());

            // tunnel
            sshEnabled = this.sshTunnelManager.isEnabled(dsConfig);
            if (sshEnabled) {
                tunnel = this.sshTunnelManager.open(dsConfig);
                dsConfig = tunnel.getDsConfig();
            }

            applySessionCatalog(dsConfig, contextDTO);

            // session
            SessionFactory factory = pluginInfo.createSessionFactory(driverRef.getDriverFamily(), driverRef.getDriverVersion());
            Session session = factory.createSession(rm, dsConfig, contextDTO);
            log.info("finish create datasource session, sessionId={}, dsType={}, runtimeHost={}, sshEnabled={}, sshConfigId={}",//
                    newSessionId, dsConfig.getDataSourceType(), dsConfig.getHost(), sshEnabled, dsConfig.getSshConfigId());
            SshTunnelHandle sessionTunnel = tunnel;
            session.addCloseListener(sessionId -> IOUtils.closeQuietly(sessionTunnel));
            session.addCloseListener(this::closeSessionById);

            SessionSupport ss = new SessionSupport();
            ss.setSessionId(newSessionId);
            ss.setLocalWsn(this.localWsn);
            ss.setFileService(this.fileService);
            ss.setNotifyService(this.notifyService);
            ss.setResultProcessSpi(this.findValueProcessSpi());
            SessionAgent agent = new SessionAgent(session, ss, rm, maxIdleTimeSec);

            this.sessionMap.put(newSessionId, agent);
            return agent;
        } catch (Throwable e) {
            counter.decrementAndGet();
            IOUtils.closeQuietly(tunnel);
            log.warn("failed create datasource session, sessionId={}, dsType={}, driverVersion={}, host={}, sshEnabled={}, sshConfigId={}",//
                    newSessionId, dsConfig.getDataSourceType(), dsConfig.getDriverVersion(), dsConfig.getHost(), sshEnabled, dsConfig.getSshConfigId(), e);

            if (isPluginPackageCorrupted(e)) {
                String message = "Datasource plugin is damaged, failed to load plugin resource. dsType='" + dsConfig.getDataSourceType() + "', driverVersion='"
                                 + dsConfig.getDriverVersion() + "'.";
                throw new ErrorMessageException(DmErrorCode.PLUGIN_DAMAGED_ERROR.code(), message);
            }
            throw e;
        }
    }

    private static void applySessionCatalog(DataSourceConfig dsConfig, SessionContextDTO contextDTO) throws Exception {
        if (contextDTO == null || StringUtils.isBlank(contextDTO.getRdbCatalog())) {
            return;
        }

        Method setDefaultCatalog;
        try {
            setDefaultCatalog = dsConfig.getClass().getMethod("setDefaultCatalog", String.class);
        } catch (NoSuchMethodException ignored) {
            return;
        }
        setDefaultCatalog.invoke(dsConfig, contextDTO.getRdbCatalog());
    }

    private static boolean isPluginPackageCorrupted(Throwable e) {
        for (Throwable item : ExceptionUtils.getThrowableList(e)) {
            if (!(item instanceof IndexOutOfBoundsException)) {
                continue;
            }

            for (StackTraceElement stack : item.getStackTrace()) {
                String className = stack.getClassName();
                if (StringUtils.startsWith(className, "com.clougence.utils.jar.") || StringUtils.startsWith(className, "com.clougence.utils.loader.")) {
                    return true;
                }
            }
        }
        return false;
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
