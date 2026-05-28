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
package com.clougence.clouddm.platform.dal.access.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.boot.UnifiedPostConstruct;
import com.clougence.clouddm.api.common.exception.DmErrorCode;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.i18n.I18nDictionary;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.access.entry.*;
import com.clougence.clouddm.platform.dal.i18n.I18nDaoKeys;
import com.clougence.clouddm.platform.dal.model.auth.AccountType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthRoleDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysClusterDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysWorkerDO;
import com.clougence.utils.ClassUtils;
import com.clougence.utils.ExceptionUtils;
import com.clougence.utils.ThreadUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2024/2/27 13:34:10
 */
@Service
@Slf4j
public class ObjectCacheDaoImpl implements ObjectCacheDao, UnifiedPostConstruct {

    @Resource
    private SystemDal                          systemDal;
    @Resource
    private DataSourceDal                      dsDal;
    @Resource
    private AuthDal                            authDal;
    @Resource
    private I18nDictionary                     i18nDictionary;
    @Resource
    private ApplicationContext                 appContext;

    private final AtomicBoolean                running        = new AtomicBoolean(false);
    private final Map<String, UserCacheEntry>  akUserCache    = new ConcurrentHashMap<>();
    private final Map<Long, WorkerCacheEntry>  idWorkerCache  = new ConcurrentHashMap<>();
    private final Map<Long, EnvCacheEntry>     idEnvCache     = new ConcurrentHashMap<>();
    private final Map<Long, DsCacheEntry>      idDsCache      = new ConcurrentHashMap<>();
    private final Map<Long, ClusterCacheEntry> idClusterCache = new ConcurrentHashMap<>();

    @Override
    public void init() throws Exception {
        if (running.compareAndSet(false, true)) {
            ClassLoader loader = ClassUtils.getClassLoader(this.appContext.getClassLoader());
            ThreadFactory tFactory = ThreadUtils.daemonThreadFactory(loader, "dal-cache-cleaner");
            ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor(tFactory);
            cleaner.scheduleAtFixedRate(() -> {
                try {
                    // invalid user cache
                    Iterator<UserCacheEntry> uit = this.akUserCache.values().iterator();
                    List<String> rAks = new ArrayList<>();
                    while (uit.hasNext()) {
                        UserCacheEntry e = uit.next();
                        if (e.getAddTime().plusHours(1).isAfter(LocalDateTime.now())) {
                            rAks.add(e.getAk());
                        }
                    }

                    for (String ak : rAks) {
                        removeUserFromCache(ak);
                    }

                    // invalid worker cache
                    Iterator<WorkerCacheEntry> wit = this.idWorkerCache.values().iterator();
                    List<Long> wIds = new ArrayList<>();
                    while (wit.hasNext()) {
                        WorkerCacheEntry e = wit.next();
                        if (e.getAddTime().plusHours(1).isAfter(LocalDateTime.now())) {
                            wIds.add(e.getWorkerNumId());
                        }
                    }

                    for (Long id : wIds) {
                        removeWorkerFromCache(id);
                    }

                    // invalid cluster cache
                    Iterator<ClusterCacheEntry> cit = this.idClusterCache.values().iterator();
                    List<Long> cIds = new ArrayList<>();
                    while (cit.hasNext()) {
                        ClusterCacheEntry e = cit.next();
                        if (e.getAddTime().plusHours(1).isAfter(LocalDateTime.now())) {
                            cIds.add(e.getClusterNumId());
                        }
                    }

                    for (Long id : cIds) {
                        removeClusterFromCache(id);
                    }

                    // invalid ds cache
                    Iterator<DsCacheEntry> dsit = this.idDsCache.values().iterator();
                    List<Long> dsIds = new ArrayList<>();
                    while (dsit.hasNext()) {
                        DsCacheEntry e = dsit.next();
                        if (e.getAddTime().plusHours(1).isAfter(LocalDateTime.now())) {
                            dsIds.add(e.getDsNumId());
                        }
                    }

                    for (Long id : dsIds) {
                        removeDataSourceCache(id);
                    }
                } catch (Throwable e) {
                    log.error(this.getClass().getSimpleName() + " error.msg:" + ExceptionUtils.getRootCauseMessage(e), e);
                }
            }, 60, 60, TimeUnit.SECONDS);
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public UserCacheEntry queryByUid(String uid) {
        UserCacheEntry result = this.queryUserCacheByUid(uid);

        if (result == null) {
            synchronized (this.akUserCache) {
                result = this.queryUserCacheByUid(uid);
                if (result == null) {
                    DmAuthUserDO userDO = this.authDal.userMapper().queryByUid(uid);
                    result = this.fillUserToCache(userDO);
                }
            }
        }

        return result;
    }

    @Override
    public UserCacheEntry queryByUserNumberId(Long id) {
        UserCacheEntry result = this.queryUserCacheByID(id);

        if (result == null) {
            synchronized (this.akUserCache) {
                result = this.queryUserCacheByID(id);
                if (result == null) {
                    DmAuthUserDO userDO = this.authDal.userMapper().queryById(id);
                    result = this.fillUserToCache(userDO);
                }
            }
        }

        return result;
    }

    @Override
    public UserCacheEntry queryByAk(String ak) {
        UserCacheEntry result = this.akUserCache.get(ak);

        if (result == null) {
            synchronized (this.akUserCache) {
                result = this.akUserCache.get(ak);
                if (result == null) {
                    DmAuthUserDO userDO = this.authDal.userMapper().queryByAccessKey(ak);
                    result = fillUserToCache(userDO);
                }
            }
        }

        return result;
    }

    protected UserCacheEntry queryUserCacheByUid(String uid) {
        UserCacheEntry result = null;
        if (!this.akUserCache.isEmpty()) {
            for (UserCacheEntry entry : this.akUserCache.values()) {
                if (entry.getUid().equals(uid)) {
                    result = entry;
                }
            }
        }

        return result;
    }

    protected UserCacheEntry queryUserCacheByID(Long uid) {
        UserCacheEntry result = null;
        if (!this.akUserCache.isEmpty()) {
            for (UserCacheEntry entry : this.akUserCache.values()) {
                if (entry.getUserNumId().equals(uid)) {
                    result = entry;
                }
            }
        }

        return result;
    }

    protected UserCacheEntry fillUserToCache(DmAuthUserDO userDO) {
        if (userDO == null) {
            return null;
        }

        DmAuthRoleDO roleDO = this.authDal.roleMapper().selectById(userDO.getRoleId());
        UserCacheEntry entry = new UserCacheEntry();
        entry.setUid(userDO.getUid());
        if (userDO.getAccountType() == AccountType.SUB_ACCOUNT) {
            DmAuthUserDO parentUserDO = this.authDal.userMapper().queryById(userDO.getParentId());
            entry.setParentUid(parentUserDO.getUid());
        } else {
            entry.setParentUid(userDO.getUid());
        }
        entry.setUserNumId(userDO.getId());
        entry.setUserName(userDO.getUsername());
        entry.setRoleName(roleDO.getRoleName());
        entry.setAk(userDO.getAccessKey());
        entry.setUserType(userDO.getAccountType());
        entry.setBindType(userDO.getBindType());
        this.akUserCache.put(entry.getAk(), entry);

        return entry;
    }

    @Override
    public DsCacheEntry queryByDsId(Long dsId) {
        DsCacheEntry result = this.idDsCache.get(dsId);

        if (result == null) {
            synchronized (this.idDsCache) {
                result = this.idDsCache.get(dsId);
                if (result == null) {
                    DmDsDO dsDO = this.dsDal.dsMapper().selectById(dsId);
                    if (dsDO != null) {
                        result = new DsCacheEntry();
                        result.setDsNumId(dsDO.getId());
                        result.setDsInstId(dsDO.getInstanceId());
                        result.setDsInstDesc(dsDO.getInstanceDesc());
                        result.setOwnerUid(dsDO.getUid());
                        result.setDsType(dsDO.getDataSourceType());
                        result.setEnvId(dsDO.getDsEnvId());

                        DmDsConfigDO configDO = this.dsDal.configMapper().queryById(dsDO.getUid(), dsDO.getId());
                        if (configDO != null) {
                            result.setClusterId(configDO.getBindClusterId());
                        }
                        this.idDsCache.put(dsDO.getId(), result);
                    }
                }
            }
        }

        return result;
    }

    @Override
    public ClusterCacheEntry queryByClusterId(Long clusterId) {
        ClusterCacheEntry result = this.idClusterCache.get(clusterId);

        if (result == null) {
            synchronized (this.idClusterCache) {
                result = this.idClusterCache.get(clusterId);
                if (result == null) {
                    DmSysClusterDO clusterDO = this.systemDal.clusterMapper().selectById(clusterId);
                    if (clusterDO != null) {
                        result = new ClusterCacheEntry();
                        result.setClusterNumId(clusterId);
                        result.setOwnerUid(clusterDO.getUid());
                        this.idClusterCache.put(clusterId, result);
                    }
                }
            }
        }

        return result;
    }

    @Override
    public EnvCacheEntry queryByEnvId(Long envId) {
        EnvCacheEntry result = this.idEnvCache.get(envId);

        if (result == null) {
            synchronized (this.idEnvCache) {
                result = this.idEnvCache.get(envId);
                if (result == null) {
                    DmSysEnvDO envDO = this.systemDal.envMapper().selectById(envId);
                    if (envDO != null) {
                        result = new EnvCacheEntry();
                        result.setEnvNumId(envDO.getId());
                        result.setEnvName(envDO.getEnvName());
                        result.setOwnerUid(envDO.getOwnerUid());
                        this.idEnvCache.put(envId, result);
                    }
                }
            }
        }

        return result;
    }

    @Override
    public WorkerCacheEntry queryByWorkerId(Long workerId) {
        WorkerCacheEntry result = this.idWorkerCache.get(workerId);

        if (result == null) {
            synchronized (this.idWorkerCache) {
                result = this.idWorkerCache.get(workerId);
                if (result == null) {
                    DmSysWorkerDO workerDO = this.systemDal.workerMapper().selectById(workerId);
                    if (workerDO != null) {
                        result = new WorkerCacheEntry();
                        result.setWorkerNumId(workerDO.getId());
                        result.setOwnerUid(workerDO.getUid());
                        result.setWsn(workerDO.getWorkerSeqNumber());
                        this.idWorkerCache.put(workerId, result);
                    }
                }
            }
        }

        return result;
    }

    @Override
    public WorkerCacheEntry queryByWsn(String wsn) {
        WorkerCacheEntry result = this.queryWorkerCacheByWsn(wsn);

        if (result == null) {
            synchronized (this.idWorkerCache) {
                result = this.queryWorkerCacheByWsn(wsn);
                if (result == null) {
                    DmSysWorkerDO workerDO = this.systemDal.workerMapper().getByWsn(wsn);
                    if (workerDO != null) {
                        result = new WorkerCacheEntry();
                        result.setWorkerNumId(workerDO.getId());
                        result.setOwnerUid(workerDO.getUid());
                        result.setWsn(workerDO.getWorkerSeqNumber());
                        this.idWorkerCache.put(workerDO.getId(), result);
                    }
                }
            }
        }

        return result;
    }

    protected WorkerCacheEntry queryWorkerCacheByWsn(String wsn) {
        WorkerCacheEntry result = null;
        if (!this.idWorkerCache.isEmpty()) {
            for (WorkerCacheEntry entry : this.idWorkerCache.values()) {
                if (entry.getWsn().equals(wsn)) {
                    result = entry;
                }
            }
        }

        return result;
    }

    @Override
    public void ownDataSource(String uid, Long dsId) {
        DsCacheEntry entry = this.queryByDsId(dsId);

        if (entry == null) {
            String msg = this.i18nDictionary.getMessage(I18nDaoKeys.DAO_DATA_SOURCE_NOT_EXIST, dsId);
            throw new ErrorMessageException(DmErrorCode.O_DATA.code(), msg);
        }

        if (!entry.getOwnerUid().equals(uid)) {
            throw noAuthorityException();
        }
    }

    @Override
    public void ownCluster(String uid, Long clusterId) {
        ClusterCacheEntry entry = this.queryByClusterId(clusterId);

        if (entry == null) {
            String msg = this.i18nDictionary.getMessage(I18nDaoKeys.DAO_CLUSTER_NOT_EXIST, clusterId);
            throw new ErrorMessageException(DmErrorCode.O_DATA.code(), msg);
        }

        if (!entry.getOwnerUid().equals(uid)) {
            throw noAuthorityException();
        }
    }

    @Override
    public void ownEnv(String uid, Long envId) {
        EnvCacheEntry entry = this.queryByEnvId(envId);

        if (entry == null) {
            String msg = this.i18nDictionary.getMessage(I18nDaoKeys.DAO_ENV_NOT_EXIST, envId);
            throw new ErrorMessageException(DmErrorCode.O_DATA.code(), msg);
        }

        if (!entry.getOwnerUid().equals(uid)) {
            throw noAuthorityException();
        }
    }

    @Override
    public void ownWorker(String uid, Long workerId) {
        WorkerCacheEntry entry = this.queryByWorkerId(workerId);

        if (entry == null) {
            String msg = this.i18nDictionary.getMessage(I18nDaoKeys.DAO_WORKER_NOT_EXIST, workerId);
            throw new ErrorMessageException(DmErrorCode.O_DATA.code(), msg);
        }

        if (!entry.getOwnerUid().equals(uid)) {
            throw noAuthorityException();
        }
    }

    private ErrorMessageException noAuthorityException() {
        String msg = this.i18nDictionary.getMessage(I18nDaoKeys.DAO_NO_AUTHORITY);
        return new ErrorMessageException(DmErrorCode.O_AUTHORITY.code(), msg);
    }

    protected void removeUserFromCache(String ak) {
        synchronized (this.akUserCache) {
            this.akUserCache.remove(ak);
        }
    }

    @Override
    public void removeWorkerFromCache(Long workerId) {
        synchronized (this.idWorkerCache) {
            this.idWorkerCache.remove(workerId);
        }
    }

    @Override
    public void removeClusterFromCache(Long clusterId) {
        synchronized (this.idClusterCache) {
            this.idClusterCache.remove(clusterId);
        }
    }

    @Override
    public void removeDataSourceCache(Long dsId) {
        synchronized (this.idDsCache) {
            this.idDsCache.remove(dsId);
        }
    }
}
