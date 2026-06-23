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
package com.clougence.clouddm.console.web.global.notify;

import java.util.Collections;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.base.metadata.ds.DataSourceConfig;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForManage;
import com.clougence.clouddm.console.web.model.fo.security.ModifyAuthForAppend;
import com.clougence.clouddm.console.web.model.fo.security.ModifyUserAuthFO;
import com.clougence.clouddm.console.web.service.auth.RdpUserService;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.access.ObjectCacheDao;
import com.clougence.clouddm.platform.dal.access.entry.UserCacheEntry;
import com.clougence.clouddm.platform.dal.model.auth.AccountType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsConfigKv4DmDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.rdp.service.RdpNotifyService;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author mode 2020/11/7 17:11
 */
@Slf4j
@Service
public class DmDataSourceNotify implements RdpNotifyService {

    @Resource
    private DataSourceDal          dsDal;
    @Resource
    private DmAuthServiceForManage authServiceForManage;
    @Resource
    private ObjectCacheDao         cacheDao;
    @Resource
    private RdpUserService         userService;

    @Override
    public void onDsAdd(String operatorUid, long dsId) {
        this.syncConf(dsId, true);
        this.addAuth(operatorUid, dsId);
    }

    @Override
    public void onDsUpdate(long dsId) {
        DmDsConfigKv4DmDO dsKvConf = this.dsDal.configKv4DmMapper().queryByDsIdAndConfigName(dsId, DataSourceConfig.Fields.configVersion);
        if (dsKvConf != null) {
            long nextVersion = Long.parseLong(dsKvConf.getConfigValue()) + 1;
            this.dsDal.configKv4DmMapper().updateDsConfig(dsId, DataSourceConfig.Fields.configVersion, Long.toString(nextVersion));
        }

        this.syncConf(dsId, false);
    }

    protected void addAuth(String operatorUid, long dsId) {
        DmAuthUserDO userDO = userService.getUserByUid(operatorUid);

        if (userDO.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
            // primary user no need to add auth.
            return;
        }

        ModifyAuthForAppend item = new ModifyAuthForAppend();
        item.setResId(dsId);
        item.setResPaths(Collections.emptyList());

        ModifyUserAuthFO authFO = new ModifyUserAuthFO();
        authFO.setAuthKind(AuthKind.DataSource);
        authFO.setTargetUid(operatorUid);
        authFO.setAppends(Collections.singletonList(item));
        authFO.setUpdates(Collections.emptyList());
        authFO.setDeletes(Collections.emptyList());

        UserCacheEntry userCache = this.cacheDao.queryByUid(operatorUid);
        this.authServiceForManage.modifyUserAuth(userCache.getParentUid(), authFO);
    }

    public void onDsDelete(long dsId) {
        this.authServiceForManage.clearAuthOfRes(dsId, AuthKind.DataSource);
    }

    protected void syncConf(long dsId, boolean init) {
        DmDsDO dsDO = this.dsDal.dsMapper().selectById(dsId);
        this.dsDal.configKv4DmMapper().updateDsConfig(dsId, DataSourceConfig.Fields.securityType, dsDO.getSecurityType().name());
        this.dsDal.configKv4DmMapper().updateDsConfig(dsId, DataSourceConfig.Fields.userName, dsDO.getAccessKey());
        this.dsDal.configKv4DmMapper().updateDsConfig(dsId, DataSourceConfig.Fields.password, dsDO.getSecretKey());
        this.dsDal.configKv4DmMapper().updateDsConfig(dsId, DataSourceConfig.Fields.host, dsDO.getHost());
    }
}
