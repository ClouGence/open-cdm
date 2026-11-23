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
package com.clougence.clouddm.console.web.component.config;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.console.web.component.approval.ApprovalStarter;
import com.clougence.clouddm.console.web.component.cluster.model.ProviderConfigChange;
import com.clougence.clouddm.console.web.constants.LoginAuthType;
import com.clougence.clouddm.console.web.service.login.LoginStarter;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysUserConfDO;
import com.clougence.clouddm.platform.dal.model.system.UserConfigTagType;
import com.clougence.rdp.service.model.UserConfigMO;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProviderConfigRefresh implements UnifiedPostConstruct {
    private ScheduledExecutorService               executor;
    private boolean                                stopped;
    private final Map<String, Map<String, String>> loginSnapshots    = new HashMap<>();
    private final Map<String, Map<String, String>> approvalSnapshots = new HashMap<>();
    @Resource
    private AuthDal                                authDal;
    @Resource
    private SystemDal                              systemDal;
    @Resource
    @Lazy
    private UserConfigServiceImpl                  userConfigService;
    @Resource
    @Lazy
    private LoginStarter                           loginStarter;
    @Resource
    @Lazy
    private ApprovalStarter                        approvalStarter;

    @Override
    public synchronized void init() {
        if (this.stopped || this.executor != null) {
            return;
        }

        this.executor = Executors.newSingleThreadScheduledExecutor(task -> {
            Thread thread = new Thread(task, "provider-config-refresh");
            thread.setDaemon(true);
            return thread;
        });
        this.executor.scheduleWithFixedDelay(this::reconcile, 60, 60, TimeUnit.SECONDS);
    }

    public void notifyUserConfig(String ownerUid, List<UserConfigMO> configList) {
        ProviderConfigChange change = new ProviderConfigChange();
        change.setOwnerUid(ownerUid);
        for (UserConfigMO config : configList) {
            UserConfigTagType tag = config.getTagType();
            if (RootUserConfig.Fields.accountAuthType.equals(config.getConfig()) || tag == UserConfigTagType.LDAP_CONFIG || tag == UserConfigTagType.OIDC) {
                change.setLogin(true);
            }

            if (tag == UserConfigTagType.FEISHU || tag == UserConfigTagType.WECHAT || tag == UserConfigTagType.DINGTALK) {
                change.setLogin(true);
                change.setApproval(true);
            }
        }

        if (change.isLogin() || change.isApproval()) {
            this.scheduleRefresh(change);
        }
    }

    private void scheduleRefresh(ProviderConfigChange change) {
        try {
            this.executor.execute(() -> {
                try {
                    this.apply(change);
                } catch (Exception e) {
                    log.error("Refresh local provider configuration failed: ownerUid={}", change.getOwnerUid(), e);
                }
            });
        } catch (RejectedExecutionException e) {
            // A committed update is recovered by the background check if shutdown rejects this task.
            log.warn("Provider configuration notification rejected: ownerUid={}", change.getOwnerUid(), e);
        }
    }

    public void apply(ProviderConfigChange change) {
        RuntimeException failure = null;
        if (change.isLogin()) {
            try {
                this.refreshLogin(change.getOwnerUid());
            } catch (RuntimeException e) {
                failure = e;
            }
        }

        if (change.isApproval()) {
            try {
                this.refreshApproval(change.getOwnerUid());
            } catch (RuntimeException e) {
                if (failure == null) {
                    failure = e;
                } else {
                    failure.addSuppressed(e);
                }
            }
        }

        if (failure != null) {
            throw failure;
        }
    }

    private void reconcile() {
        try {
            for (DmAuthUserDO user : this.authDal.userMapper().listPrimaryAccount()) {
                ProviderConfigChange change = new ProviderConfigChange();
                change.setOwnerUid(user.getUid());
                change.setLogin(true);
                change.setApproval(true);
                try {
                    this.apply(change);
                } catch (Exception e) {
                    log.error("Reconcile provider configuration failed: ownerUid={}", user.getUid(), e);
                }
            }
        } catch (Exception e) {
            log.error("List provider configuration owners failed", e);
        }
    }

    private void refreshLogin(String ownerUid) {
        synchronized (loginSnapshots) {
            refresh(ownerUid, true, loginSnapshots);
        }
    }

    private void refreshApproval(String ownerUid) {
        synchronized (approvalSnapshots) {
            refresh(ownerUid, false, approvalSnapshots);
        }
    }

    @Override
    public synchronized void stop() {
        this.stopped = true;
        if (this.executor != null) {
            this.executor.shutdownNow();
        }
    }

    private void refresh(String ownerUid, boolean loginConfig, Map<String, Map<String, String>> snapshots) {
        Set<UserConfigTagType> configGroups = new HashSet<>();
        if (loginConfig) {
            for (LoginAuthType type : LoginAuthType.values()) {
                if (type.getBindType().getProvider() != null) {
                    configGroups.add(type.getConfigGroup());
                }
            }
        } else {
            configGroups.add(UserConfigTagType.FEISHU);
            configGroups.add(UserConfigTagType.WECHAT);
            configGroups.add(UserConfigTagType.DINGTALK);
        }

        Map<String, String> savedValues = new HashMap<>();
        for (DmSysUserConfDO config : systemDal.userConfMapper().listByUid(ownerUid)) {
            savedValues.put(config.getConfigName(), config.getConfigValue());
        }

        Map<String, String> currentSnapshot = new TreeMap<>();
        Map<String, String> previousSnapshot = snapshots.getOrDefault(ownerUid, Map.of());
        List<UserConfigMO> changes = new ArrayList<>();
        for (UserConfigKvDef definition : userConfigService.fetchUserDefinedDefaultConfig(ownerUid)) {
            String key = definition.getConfigName();
            boolean selected = configGroups.contains(definition.getUserConfigTagType());
            if (loginConfig && RootUserConfig.Fields.accountAuthType.equals(key)) {
                selected = true;
            }

            if (!selected) {
                continue;
            }

            String value = savedValues.getOrDefault(key, definition.getDefaultValue());
            currentSnapshot.put(key, value);
            if (previousSnapshot.containsKey(key) && Objects.equals(previousSnapshot.get(key), value)) {
                continue;
            }

            UserConfigMO change = new UserConfigMO();
            change.setConfig(key);
            change.setOldValue(previousSnapshot.get(key));
            change.setNewValue(value);
            change.setTagType(definition.getUserConfigTagType());
            change.setInsert(!previousSnapshot.containsKey(key));
            change.setUpdate(previousSnapshot.containsKey(key));
            changes.add(change);
        }

        if (changes.isEmpty()) {
            return;
        }

        try {
            if (loginConfig) {
                loginStarter.notifyUserConfig(ownerUid, changes);
            } else {
                approvalStarter.notifyUserConfig(ownerUid, changes);
            }

            snapshots.put(ownerUid, currentSnapshot);
        } catch (Exception e) {
            // A failed restart may already have stopped or partially replaced clients.
            snapshots.remove(ownerUid);
            log.error("apply provider configuration failed, ownerUid={}", ownerUid, e);
            throw new IllegalStateException("provider configuration is not ready", e);
        }
    }

}
