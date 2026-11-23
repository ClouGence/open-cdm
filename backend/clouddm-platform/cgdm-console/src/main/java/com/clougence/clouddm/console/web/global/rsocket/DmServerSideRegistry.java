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
package com.clougence.clouddm.console.web.global.rsocket;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.transaction.annotation.Transactional;

import com.clougence.clouddm.api.common.crypt.CryptService;
import com.clougence.clouddm.comm.component.RSocketExceptionManager;
import com.clougence.clouddm.comm.component.server.ServerSideRegistry;
import com.clougence.clouddm.comm.constants.worker.WorkerConnStatus;
import com.clougence.clouddm.comm.model.RSocketAuthException;
import com.clougence.clouddm.comm.model.RSocketRequestWrapperDTO;
import com.clougence.clouddm.comm.model.auth.ConnAuthDTO;
import com.clougence.clouddm.comm.model.auth.WorkerIdentity;
import com.clougence.clouddm.comm.model.rsocket.RSocketRegisterInfo;
import com.clougence.clouddm.console.web.component.cluster.ConsoleClusterService;
import com.clougence.clouddm.console.web.global.notify.DmWorkerRegisterNotify;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.execution.DmExecSessionDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysWorkerDO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DmServerSideRegistry implements ServerSideRegistry {
    private final AuthDal                          authDal;
    private final SystemDal                        systemDal;
    private final ExecutionDal                     executionDal;
    private final List<DmWorkerRegisterNotify>     notifyServices;
    private final ConsoleClusterService            cluster;
    private final RSocketExceptionManager          exceptionManager;
    private final Map<String, RSocketRequester>    workerRequesterMap = new ConcurrentHashMap<>();
    private final Map<String, RSocketRegisterInfo> connections        = new ConcurrentHashMap<>();

    public DmServerSideRegistry(AuthDal authDal, SystemDal systemDal, ExecutionDal executionDal, List<DmWorkerRegisterNotify> notifyServices, ConsoleClusterService cluster,
                                RSocketExceptionManager exceptionManager){
        this.authDal = authDal;
        this.systemDal = systemDal;
        this.executionDal = executionDal;
        this.notifyServices = notifyServices;
        this.cluster = cluster;
        this.exceptionManager = exceptionManager;
    }

    @Override
    public void checkAuth(ConnAuthDTO auth) {
        try {
            DmAuthUserDO user = authDal.userMapper().queryByAccessKey(auth.getAk());
            DmSysWorkerDO worker = systemDal.workerMapper().getByWsn(auth.getWsn());
            if (hasInvalidWorkerCredentials(auth, user, worker)) {
                throw new RSocketAuthException("worker credentials are invalid");
            }
        } catch (RSocketAuthException e) {
            log.warn("Worker credentials rejected: wsn={}", auth.getWsn());
            throw e;
        } catch (Exception e) {
            reportFailure("authenticate", auth.getWsn(), e);
            throw e;
        }
    }

    private boolean hasInvalidWorkerCredentials(ConnAuthDTO auth, DmAuthUserDO user, DmSysWorkerDO worker) {
        if (user == null || user.isDisable() || worker == null) {
            return true;
        }

        if (!worker.getUid().equals(user.getUid())) {
            return true;
        }
        return !CryptService.INSTANCE.decryptUseDefaultKeyAndSalt(user.getSecretKey()).equals(auth.getSk());
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void register(RSocketRegisterInfo info) {
        if (!cluster.isAcceptingWork()) {
            throw new IllegalStateException("console is not accepting connections");
        }

        boolean rejected = false;
        try {
            DmSysWorkerDO worker = systemDal.workerMapper().lockByWsn(info.getWorkerSeqNumber());
            if (worker == null) {
                rejected = true;
                throw new IllegalArgumentException("worker not found");
            }

            RSocketRegisterInfo previous = connections.get(info.getWorkerSeqNumber());
            if (hasActiveOwner(worker, previous)) {
                rejected = true;
                throw new IllegalStateException("worker already has an active owner");
            }

            info.setOwnerConsoleId(cluster.getInstanceId());
            info.setConnectionGeneration(worker.getConnectionGeneration() + 1);
            worker.setOwnerInstanceId(info.getOwnerConsoleId());
            worker.setConnectionGeneration(info.getConnectionGeneration());
            worker.setWorkerIp(info.getWorkerIp());
            worker.setConnStatus(WorkerConnStatus.CONNECTED);
            worker.setLastHeartbeatReportMs(System.currentTimeMillis());
            systemDal.workerMapper().updateConnectionByWsn(worker);
            executionDal.sessionMapper().updateActiveBatchIdByWsn(info.getWorkerSeqNumber(), DmExecSessionDO.INTERRUPTED_BATCH_ID);
            notifyServices.forEach(notify -> notify.notifyRegister(info.getWorkerSeqNumber()));
            connections.put(info.getWorkerSeqNumber(), info);
            workerRequesterMap.put(info.getWorkerSeqNumber(), info.getRequester());
            if (!cluster.isAcceptingWork()) {
                rejected = true;
                try {
                    unRegister(info);
                } finally {
                    info.getRequester().rsocket().dispose();
                }

                throw new IllegalStateException("console stopped during worker registration");
            }

            log.info("Worker registered: wsn={}, owner={}, generation={}", info.getWorkerSeqNumber(), info.getOwnerConsoleId(), info.getConnectionGeneration());
        } catch (Exception e) {
            if (rejected) {
                log.warn("Worker registration rejected: wsn={}, owner={}, message={}", info.getWorkerSeqNumber(), cluster.getInstanceId(), e.getMessage());
            } else {
                reportFailure("register", info.getWorkerSeqNumber(), e);
            }

            throw e;
        }
    }

    private boolean hasActiveOwner(DmSysWorkerDO worker, RSocketRegisterInfo previous) {
        if (worker.getConnStatus() != WorkerConnStatus.CONNECTED || !cluster.isAlive(worker.getOwnerInstanceId())) {
            return false;
        }
        boolean localDisposedOwner = previous != null && cluster.getInstanceId().equals(worker.getOwnerInstanceId())
                                     && previous.getConnectionGeneration() == worker.getConnectionGeneration() && previous.getRequester().rsocket().isDisposed();
        return !localDisposedOwner;
    }

    @Override
    public void unRegister(RSocketRegisterInfo info) {
        if (info.getOwnerConsoleId() == null) {
            return;
        }

        try {
            connections.remove(info.getWorkerSeqNumber(), info);
            workerRequesterMap.remove(info.getWorkerSeqNumber(), info.getRequester());
            DmSysWorkerDO update = ownedUpdate(info);
            update.setConnStatus(WorkerConnStatus.DISCONNECTED);
            if (systemDal.workerMapper().updateConnectionByWsnAndOwner(update) == 1) {
                log.info("Worker unregistered: wsn={}, owner={}, generation={}", info.getWorkerSeqNumber(), info.getOwnerConsoleId(), info.getConnectionGeneration());
            }
        } catch (Exception e) {
            reportFailure("unregister", info.getWorkerSeqNumber(), e);
            throw e;
        }
    }

    private void reportFailure(String action, String wsn, Exception error) {
        log.error("Worker connection {} failed: wsn={}, console={}", action, wsn, cluster.getInstanceId(), error);
        exceptionManager.saveExcOrAlert(error);
    }

    private DmSysWorkerDO ownedUpdate(RSocketRegisterInfo info) {
        DmSysWorkerDO update = new DmSysWorkerDO();
        update.setWorkerSeqNumber(info.getWorkerSeqNumber());
        update.setOwnerInstanceId(info.getOwnerConsoleId());
        update.setConnectionGeneration(info.getConnectionGeneration());
        return update;
    }

    public RSocketRegisterInfo connection(String wsn) {
        return connections.get(wsn);
    }

    public void checkIncoming(RSocketRequestWrapperDTO request, RSocketRequester requester) {
        WorkerIdentity identity = request.getWorkerIdentity();
        if (identity == null || !cluster.isLeaseValid()) {
            throw new IllegalStateException("worker connection is unavailable");
        }

        RSocketRegisterInfo connection = connections.get(identity.getWorkerSeqNumber());
        DmSysWorkerDO worker = systemDal.workerMapper().getByWsn(identity.getWorkerSeqNumber());
        if (isStaleConnection(connection, worker, requester)) {
            throw new IllegalStateException("stale worker connection");
        }

        // Only the Console derives connection ownership; legacy Worker payloads contain no routing tokens.
        identity.setOwnerConsoleId(connection.getOwnerConsoleId());
        identity.setConnectionGeneration(connection.getConnectionGeneration());
    }

    private boolean isStaleConnection(RSocketRegisterInfo connection, DmSysWorkerDO worker, RSocketRequester requester) {
        if (connection == null || worker == null) {
            return true;
        }

        if (connection.getRequester().rsocket() != requester.rsocket() || requester.rsocket().isDisposed()) {
            return true;
        }

        if (worker.getConnectionGeneration() != connection.getConnectionGeneration()) {
            return true;
        }
        return !cluster.getInstanceId().equals(worker.getOwnerInstanceId());
    }

    @Override
    public RSocketRequester getRSocketRequester(String wsn) {
        return workerRequesterMap.get(wsn);
    }

    @Override
    public Map<String, RSocketRequester> getRequesterMap() { return workerRequesterMap; }
}
