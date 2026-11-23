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

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.DmErrorCode;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.api.common.i18n.I18nDictionary;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.access.ObjectQueryDao;
import com.clougence.clouddm.platform.dal.access.SystemDal;
import com.clougence.clouddm.platform.dal.access.entry.*;
import com.clougence.clouddm.platform.dal.i18n.I18nDaoKeys;
import com.clougence.clouddm.platform.dal.model.auth.AccountType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthRoleDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysClusterDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysEnvDO;
import com.clougence.clouddm.platform.dal.model.system.DmSysWorkerDO;

import jakarta.annotation.Resource;

/**
 * @author bucketli 2024/2/27 13:34:10
 */
@Service
public class ObjectQueryDaoImpl implements ObjectQueryDao {

    @Resource
    private SystemDal      systemDal;
    @Resource
    private DataSourceDal  dsDal;
    @Resource
    private AuthDal        authDal;
    @Resource
    private I18nDictionary i18nDictionary;

    @Override
    public UserEntry queryByUid(String uid) {
        return this.toUserEntry(this.authDal.userMapper().queryByUid(uid));
    }

    @Override
    public UserEntry queryByUserNumberId(Long id) {
        return this.toUserEntry(this.authDal.userMapper().queryById(id));
    }

    @Override
    public UserEntry queryByAk(String ak) {
        return this.toUserEntry(this.authDal.userMapper().queryByAccessKey(ak));
    }

    private UserEntry toUserEntry(DmAuthUserDO userDO) {
        if (userDO == null) {
            return null;
        }

        DmAuthRoleDO roleDO = this.authDal.roleMapper().selectById(userDO.getRoleId());
        UserEntry entry = new UserEntry();
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

        return entry;
    }

    @Override
    public DsEntry queryByDsId(Long dsId) {
        DmDsDO dsDO = this.dsDal.dsMapper().selectById(dsId);
        if (dsDO == null) {
            return null;
        }

        DsEntry result = new DsEntry();
        result.setDsNumId(dsDO.getId());
        result.setDsInstId(dsDO.getInstanceId());
        result.setDsInstDesc(dsDO.getInstanceDesc());
        result.setOwnerUid(dsDO.getUid());
        result.setDsType(dsDO.getDataSourceType());
        result.setEnvId(dsDO.getDsEnvId());
        result.setClusterId(dsDO.getBindClusterId());
        return result;
    }

    @Override
    public ClusterEntry queryByClusterId(Long clusterId) {
        DmSysClusterDO clusterDO = this.systemDal.clusterMapper().selectById(clusterId);
        if (clusterDO == null) {
            return null;
        }

        ClusterEntry result = new ClusterEntry();
        result.setClusterNumId(clusterId);
        result.setOwnerUid(clusterDO.getUid());
        return result;
    }

    @Override
    public EnvEntry queryByEnvId(Long envId) {
        DmSysEnvDO envDO = this.systemDal.envMapper().selectById(envId);
        if (envDO == null) {
            return null;
        }

        EnvEntry result = new EnvEntry();
        result.setEnvNumId(envDO.getId());
        result.setEnvName(envDO.getEnvName());
        result.setOwnerUid(envDO.getOwnerUid());
        return result;
    }

    @Override
    public WorkerEntry queryByWorkerId(Long workerId) {
        DmSysWorkerDO workerDO = this.systemDal.workerMapper().selectById(workerId);
        if (workerDO == null) {
            return null;
        }

        WorkerEntry result = new WorkerEntry();
        result.setWorkerNumId(workerDO.getId());
        result.setOwnerUid(workerDO.getUid());
        result.setWsn(workerDO.getWorkerSeqNumber());
        return result;
    }

    @Override
    public WorkerEntry queryByWsn(String wsn) {
        DmSysWorkerDO workerDO = this.systemDal.workerMapper().getByWsn(wsn);
        if (workerDO == null) {
            return null;
        }

        WorkerEntry result = new WorkerEntry();
        result.setWorkerNumId(workerDO.getId());
        result.setOwnerUid(workerDO.getUid());
        result.setWsn(workerDO.getWorkerSeqNumber());
        return result;
    }

    @Override
    public void ownDataSource(String uid, Long dsId) {
        DsEntry entry = this.queryByDsId(dsId);

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
        ClusterEntry entry = this.queryByClusterId(clusterId);

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
        EnvEntry entry = this.queryByEnvId(envId);

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
        WorkerEntry entry = this.queryByWorkerId(workerId);

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

}
