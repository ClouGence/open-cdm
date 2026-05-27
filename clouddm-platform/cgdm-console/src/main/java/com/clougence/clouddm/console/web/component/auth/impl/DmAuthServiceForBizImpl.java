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
package com.clougence.clouddm.console.web.component.auth.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.component.auth.DmResAuthService;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.service.envparam.DmEnvParamService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.model.auth.AccountType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthResDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.execution.DmExecFileDO;
import com.clougence.clouddm.sdk.model.analysis.resource.DsResPath;
import com.clougence.clouddm.sdk.model.env.EnvParamKeys;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.rdp.service.RdpAuthServiceForBiz;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2024/2/21 12:51:24
 */
@Service
@Slf4j
public class DmAuthServiceForBizImpl implements DmAuthServiceForBiz {
    @Resource
    private ExecutionDal         executionDal;
    @Resource
    private DataSourceDal datasourceDal;
    @Resource
    private AuthDal              authDal;
    @Resource
    private DmResAuthService     dmDsAuthService;
    @Resource
    private RdpAuthServiceForBiz rdpAuthServiceForBiz;
    @Resource
    private DmEnvParamService    dmEnvParamService;

    @Override
    public void checkResPath(String puid, String uid, long resId, AuthKind authKind, DsResPath resPath, String dataAuthLabel) {
        if (!this.checkResPathWithoutError(puid, uid, resId, authKind, resPath, dataAuthLabel)) {
            throwMessageError(resId, resPath, dataAuthLabel);
        }
    }

    @Override
    public void checkBrowseAuth(String puid, String uid, long resId, AuthKind authKind, DsResPath resPath, String dataAuthLabel) {
        if (!checkBrowseResPath(resId, uid, puid, resPath.getResPath(), dataAuthLabel)) {
            throwMessageError(resId, resPath, dataAuthLabel);
        }
    }

    private void throwMessageError(long resId, DsResPath resPath, String dataAuthLabel) {
        AuthInfo authKeyInfo = this.dmDsAuthService.getAuthInfo(dataAuthLabel);

        DmDsDO dsDO = this.datasourceDal.dsMapper().selectById(resId);
        String authRes = dsDO.getInstanceId() + resPath.getResPath();

        String dataAuthMsg = DmI18nUtils.getMessage(authKeyInfo.getKeyI18n());
        String authMessage = DmI18nUtils.getMessage(I18nRdpMsgKeys.COMM_DATA_AUTH_PERMISSION_ERROR.name(), authRes, dataAuthMsg);
        throw new ErrorMessageException(authMessage);
    }

    @Override
    public boolean checkResPathWithoutError(String puid, String uid, long resId, AuthKind authKind, DsResPath resPath, String dataAuthLabel) {
        if (authKind == AuthKind.DataSource) {
            DmDsDO dsDO = this.datasourceDal.dsMapper().selectById(resId);
            String enable = this.dmEnvParamService.queryParam(puid, dsDO.getDsEnvId(), EnvParamKeys.DM_ALLOW_ALL_STATEMENTS);
            if (StringUtils.equals(SecDataAuthLabel.DM_DAUTH_OTHER, dataAuthLabel) && StringUtils.equalsIgnoreCase("true", enable)) {
                return false;
            }
        }

        try {
            return this.rdpAuthServiceForBiz.checkResAuthWithoutError(puid, uid, resId, resPath, dataAuthLabel, authKind);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean checkResPathChildrenWithoutError(String puid, String uid, long resID, AuthKind authKind, DsResPath resPath, String dataAuthLabel) {
        return this.checkBrowseResPath(resID, uid, puid, resPath.getResPath(), dataAuthLabel);
    }

    @Override
    public boolean checkRoleAuthWithoutError(String puid, String uid, String roleAuthLabel) {
        try {
            return this.rdpAuthServiceForBiz.checkRoleAuth(puid, uid, roleAuthLabel);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void checkResultFile(String puid, String uid, String fileUniqueId) {
        DmExecFileDO fileDO = this.executionDal.fileMapper().queryFileByUniqueId(fileUniqueId);
        if (fileDO == null) {
            throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_RESULT_FILE_NOT_EXIST_ERROR.name()));
        }

        if (StringUtils.equals(fileDO.getOwnerUid(), puid)) {
            if (StringUtils.equals(puid, uid) || StringUtils.equals(fileDO.getUserId(), uid)) {
                return; // is primary account or owner
            }
        }

        throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_RESULT_FILE_NOT_PERMISSION_ERROR.name()));
    }

    private boolean checkBrowseResPath(long dsId, String uid, String puid, String path, String dataAuthLabel) {
        DmAuthUserDO userDO = authDal.userMapper().queryByUid(uid);
        if (userDO.getAccountType() == AccountType.PRIMARY_ACCOUNT || userDO.isResourceManageEnable()) {
            return true;
        }

        DmDsDO dsDO = datasourceDal.dsMapper().selectById(dsId);
        if (!dsDO.getUid().equals(puid)) {
            throw new IllegalArgumentException(DmI18nUtils.getMessage(I18nRdpMsgKeys.DS_IS_NOT_BELONG_YOU_PRIMARY_ERROR.name(), dsDO.getId()));
        }

        List<DmAuthResDO> parentAndSelfAuth = this.authDal.resMapper().queryByPathLike(dsId, uid, AuthKind.DataSource, Collections.singletonList(path));
        List<DmAuthResDO> subAuth = this.authDal.resMapper().queryByLikePath(dsId, uid, AuthKind.DataSource, path);

        parentAndSelfAuth = parentAndSelfAuth.stream().filter(r -> r.getAuthLabels().contains(dataAuthLabel)).collect(Collectors.toList());
        subAuth = subAuth.stream().filter(r -> r.getAuthLabels().contains(dataAuthLabel)).collect(Collectors.toList());

        return CollectionUtils.isNotEmpty(parentAndSelfAuth) || CollectionUtils.isNotEmpty(subAuth);
    }
}
