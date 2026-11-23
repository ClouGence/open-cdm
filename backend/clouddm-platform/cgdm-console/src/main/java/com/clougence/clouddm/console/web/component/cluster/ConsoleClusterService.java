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
package com.clougence.clouddm.console.web.component.cluster;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.boot.UnifiedPostConstructOrder;
import com.clougence.clouddm.comm.component.server.RSocketServerSender;
import com.clougence.clouddm.console.web.component.cluster.model.ConsoleLeaseState;
import com.clougence.clouddm.console.web.component.config.ConsoleClusterProperties;
import com.clougence.clouddm.console.web.global.ws.WsChannel;
import com.clougence.clouddm.console.web.model.vo.cluster.ConsoleInstanceVO;
import com.clougence.clouddm.platform.dal.mapper.system.DmSysConsoleMapper;
import com.clougence.clouddm.platform.dal.model.system.DmSysConsoleDO;
import com.clougence.utils.StringUtils;
import com.clougence.utils.SystemUtils;

import jakarta.annotation.PreDestroy;
import jakarta.annotation.Resource;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
// Register after Console services initialize, so reverse shutdown drains this lease before their resources close.
@UnifiedPostConstructOrder(2)
public class ConsoleClusterService implements UnifiedPostConstruct {
    private static final int           LEASE_SECONDS          = 10;
    private static final int           RENEW_INTERVAL_SECONDS = 2;

    @Resource
    private ConsoleClusterProperties   properties;
    @Resource
    private DmSysConsoleMapper         mapper;
    @Resource
    private ApplicationContext         context;
    @Getter
    private final String               instanceId             = UUID.randomUUID().toString().replace("-", "");
    private final AtomicBoolean        stopping               = new AtomicBoolean();
    private ScheduledExecutorService   renewal;
    private volatile ConsoleLeaseState state                  = ConsoleLeaseState.STARTING;
    private volatile long              leaseDeadline;

    @Override
    public synchronized void init() {
        if (stopping.get() || renewal != null) {
            return;
        }

        String consoleInstanceId = getConsoleInstanceId();
        if (!ConsoleClusterProperties.isValidInstanceId(consoleInstanceId)) {
            throw new IllegalArgumentException("clouddm.console.cluster.instance-id is required");
        }

        String url = properties.getAdvertiseUrl();
        if (properties.isEnabled()) {
            if ("embedded".equals(SystemUtils.getSystemProperty("app.mode", "distributed"))) {
                throw new IllegalArgumentException("console cluster requires distributed deployment mode");
            }

            if (StringUtils.isBlank(url) || StringUtils.isBlank(properties.getSecret())) {
                throw new IllegalArgumentException("cluster advertise-url and secret are required");
            }

            URI uri = URI.create(url);
            if (isInvalidAdvertiseUrl(uri)) {
                throw new IllegalArgumentException("cluster advertise-url must be an HTTP base URL");
            }
        }

        DmSysConsoleDO member = new DmSysConsoleDO();
        member.setInstanceId(instanceId);
        member.setConsoleInstanceId(consoleInstanceId);
        member.setAdvertiseUrl(StringUtils.defaultString(url));
        member.setState(state.name());
        long leaseStartedNanos = System.nanoTime();
        try {
            mapper.insertConsole(member);
        } catch (DuplicateKeyException e) {
            if (mapper.replaceExpiredConsole(member) != 1) {
                throw new IllegalStateException("Console instance ID is already in use: " + consoleInstanceId, e);
            }
        }

        leaseDeadline = leaseStartedNanos + TimeUnit.SECONDS.toNanos(LEASE_SECONDS);
        renewal = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread thread = new Thread(r, "console-lease");
            thread.setDaemon(true);
            return thread;
        });

        renewal.scheduleWithFixedDelay(this::renew, RENEW_INTERVAL_SECONDS, RENEW_INTERVAL_SECONDS, TimeUnit.SECONDS);
    }

    private boolean isInvalidAdvertiseUrl(URI uri) {
        if (uri.getHost() == null) {
            return true;
        }

        if (!"http".equals(uri.getScheme()) && !"https".equals(uri.getScheme())) {
            return true;
        }
        return uri.getUserInfo() != null || uri.getQuery() != null || uri.getFragment() != null;
    }

    public String getConsoleInstanceId() { return properties.getInstanceId(); }

    public DmSysConsoleDO findAliveByConsoleInstanceId(String consoleInstanceId) {
        return mapper.queryAliveByConsoleInstanceId(consoleInstanceId);
    }

    public boolean isLeaseValid() { return !stopping.get() && System.nanoTime() < leaseDeadline; }

    public boolean isAcceptingWork() { return state == ConsoleLeaseState.READY && isLeaseValid() && !stopping.get(); }

    public boolean isAlive(String id) {
        if (!properties.isEnabled() && instanceId.equals(id)) {
            return !stopping.get();
        }
        return id != null && mapper.queryByInstanceIdAndLeaseUntilAfterNow(id) != null;
    }

    public DmSysConsoleDO findAlive(String id) {
        return mapper.queryByInstanceIdAndLeaseUntilAfterNow(id);
    }

    public List<DmSysConsoleDO> readyMembers() {
        return mapper.listByStateAndLeaseUntilAfterNow(ConsoleLeaseState.READY.name());
    }

    public List<ConsoleInstanceVO> listInstances() {
        List<DmSysConsoleDO> members = mapper.listConsolesWithStatus();
        return members.stream().map(member -> new ConsoleInstanceVO(member.getConsoleInstanceId(), member.getAdvertiseUrl(), member.getState())).toList();
    }

    public DmSysConsoleDO schedulerLeader() {
        if (!properties.isEnabled()) {
            return mapper.queryByInstanceIdAndLeaseUntilAfterNow(instanceId);
        }

        String owner = mapper.querySchedulerOwnerInstanceId();
        DmSysConsoleDO leader = null;
        if (owner != null) {
            leader = mapper.queryByInstanceIdAndLeaseUntilAfterNow(owner);
        }

        // A live owner keeps its role while draining, until its in-flight work has stopped.
        if (leader == null && isAcceptingWork()) {
            mapper.updateSchedulerOwnerInstanceId(owner, instanceId);
            owner = mapper.querySchedulerOwnerInstanceId();
            if (owner != null) {
                leader = mapper.queryByInstanceIdAndLeaseUntilAfterNow(owner);
            }
        }
        return leader;
    }

    public boolean isSchedulerLeader() {
        if (!isAcceptingWork()) {
            return false;
        }

        if (!properties.isEnabled()) {
            return true;
        }

        DmSysConsoleDO leader = schedulerLeader();
        return leader != null && instanceId.equals(leader.getInstanceId());
    }

    public void requireSchedulerLeader() {
        if (!isSchedulerLeader()) {
            throw new IllegalStateException("console is not the scheduler leader");
        }
    }

    public void markReady() {
        if (!isLeaseValid() || stopping.get()) {
            throw new IllegalStateException("console lease is not valid");
        }

        state = ConsoleLeaseState.READY;
        renew();
    }

    private void renew() {
        boolean clustered = properties.isEnabled();
        try {
            if (clustered && !isLeaseValid()) {
                loseLease();
                return;
            }

            if (!clustered && stopping.get()) {
                return;
            }

            long leaseStartedNanos = System.nanoTime();
            int updated;
            if (clustered) {
                updated = mapper.updateStateAndLeaseUntil(instanceId, state.name());
            } else {
                updated = mapper.updateStateAndLeaseUntilExcludingState(instanceId, state.name(), ConsoleLeaseState.DRAINING.name());
            }

            if (updated != 1) {
                leaseDeadline = 0;
                if (clustered) {
                    loseLease();
                }
                return;
            }

            leaseDeadline = leaseStartedNanos + TimeUnit.SECONDS.toNanos(LEASE_SECONDS);
        } catch (Exception e) {
            log.error("Renew console lease failed: instanceId={}", instanceId, e);
            if (clustered && !isLeaseValid()) {
                loseLease();
            }
        }
    }

    private void loseLease() {
        leaseDeadline = 0;
        if (stopping.compareAndSet(false, true)) {
            state = ConsoleLeaseState.DRAINING;
            closeConnections();
        }
    }

    private void closeConnections() {
        try {
            context.getBean(WsChannel.class).stop();
        } catch (Exception e) {
            log.warn("Close console websocket connections failed", e);
        }

        try {
            context.getBean(RSocketServerSender.class).shutdown();
        } catch (Exception e) {
            log.warn("Close console worker connections failed", e);
        }
    }

    @PreDestroy
    @Override
    public synchronized void stop() {
        if (stopping.compareAndSet(false, true)) {
            state = ConsoleLeaseState.DRAINING;
            closeConnections();
        }

        if (renewal != null) {
            renewal.shutdownNow();
        }

        try {
            mapper.updateStateAndLeaseUntilToNow(instanceId, ConsoleLeaseState.DRAINING.name());
        } catch (Exception e) {
            log.warn("Drain console lease failed: instanceId={}", instanceId, e);
        }
    }
}
