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

import static com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel.RDP_DAUTH_DS_READ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.api.common.exception.ErrorMessageException;
import com.clougence.clouddm.console.web.component.auth.DmAuthServiceForBiz;
import com.clougence.clouddm.console.web.component.auth.DmResAuthService;
import com.clougence.clouddm.console.web.global.i18n.DmI18nUtils;
import com.clougence.clouddm.console.web.global.i18n.I18nDmMsgKeys;
import com.clougence.clouddm.console.web.global.i18n.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.service.envparam.DmEnvParamService;
import com.clougence.clouddm.console.web.util.RdpAuthUtils;
import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.access.DataSourceDal;
import com.clougence.clouddm.platform.dal.access.ExecutionDal;
import com.clougence.clouddm.platform.dal.model.auth.AccountType;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthResDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthRoleDO;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.clouddm.platform.dal.model.datasource.DmDsDO;
import com.clougence.clouddm.platform.dal.model.execution.DmExecFileDO;
import com.clougence.clouddm.sdk.model.analysis.resource.DsResPath;
import com.clougence.clouddm.sdk.model.env.EnvParamKeys;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.utils.CollectionUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2024/2/21 15:48:53
 */
@Service
@Slf4j
public class DmAuthServiceForBizImpl implements DmAuthServiceForBiz {
    @Resource
    private ExecutionDal      executionDal;
    @Resource
    private DataSourceDal     dsDal;
    @Resource
    private AuthDal           authDal;
    @Resource
    private DmResAuthService  dmDsAuthService;
    @Resource
    private DmEnvParamService dmEnvParamService;

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

        DmDsDO dsDO = this.dsDal.dsMapper().selectById(resId);
        String authRes = dsDO.getInstanceId() + resPath.getResPath();

        String dataAuthMsg = DmI18nUtils.getMessage(authKeyInfo.getKeyI18n());
        String authMessage = DmI18nUtils.getMessage(I18nRdpMsgKeys.COMM_DATA_AUTH_PERMISSION_ERROR.name(), authRes, dataAuthMsg);
        throw new ErrorMessageException(authMessage);
    }

    @Override
    public boolean checkResPathWithoutError(String puid, String uid, long resId, AuthKind authKind, DsResPath resPath, String dataAuthLabel) {
        if (authKind == AuthKind.DataSource) {
            DmDsDO dsDO = this.dsDal.dsMapper().selectById(resId);
            String enable = this.dmEnvParamService.queryParam(puid, dsDO.getDsEnvId(), EnvParamKeys.DM_ALLOW_ALL_STATEMENTS);
            if (StringUtils.equals(SecDataAuthLabel.DM_DAUTH_OTHER, dataAuthLabel) && StringUtils.equalsIgnoreCase("true", enable)) {
                return false;
            }
        }

        try {
            return this.checkResAuthWithoutError(puid, uid, resId, resPath, dataAuthLabel, authKind);
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
            return this.checkRoleAuth(puid, uid, roleAuthLabel);
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
                return;
            }
        }

        throw new ErrorMessageException(DmI18nUtils.getMessage(I18nDmMsgKeys.CONSOLE_QUERY_RESULT_FILE_NOT_PERMISSION_ERROR.name()));
    }

    private boolean checkBrowseResPath(long dsId, String uid, String puid, String path, String dataAuthLabel) {
        DmAuthUserDO userDO = authDal.userMapper().queryByUid(uid);
        if (userDO.getAccountType() == AccountType.PRIMARY_ACCOUNT || userDO.isResourceManageEnable()) {
            return true;
        }

        DmDsDO dsDO = dsDal.dsMapper().selectById(dsId);
        if (!dsDO.getUid().equals(puid)) {
            throw new IllegalArgumentException(DmI18nUtils.getMessage(I18nRdpMsgKeys.DS_IS_NOT_BELONG_YOU_PRIMARY_ERROR.name(), dsDO.getId()));
        }

        List<DmAuthResDO> parentAndSelfAuth = this.authDal.resMapper().queryByPathLike(dsId, uid, AuthKind.DataSource, Collections.singletonList(path));
        List<DmAuthResDO> subAuth = this.authDal.resMapper().queryByLikePath(dsId, uid, AuthKind.DataSource, path);

        parentAndSelfAuth = parentAndSelfAuth.stream().filter(r -> r.getAuthLabels().contains(dataAuthLabel)).collect(Collectors.toList());
        subAuth = subAuth.stream().filter(r -> r.getAuthLabels().contains(dataAuthLabel)).collect(Collectors.toList());

        return CollectionUtils.isNotEmpty(parentAndSelfAuth) || CollectionUtils.isNotEmpty(subAuth);
    }

    @Override
    public boolean checkRoleAuth(String puid, String uid, String roleAuth) {
        // primary not check
        if (StringUtils.equals(puid, uid)) {
            return true;
        }

        // user must exist
        DmAuthUserDO userDO = this.authDal.userMapper().queryByUid(uid);
        if (userDO == null) {
            return false;
        }

        DmAuthRoleDO roleDO = this.authDal.roleMapper().selectById(userDO.getRoleId());
        if (roleDO == null) {
            return false;
        }

        List<String> labels = roleDO.getRoleAuthLabels();
        return labels != null && labels.contains(roleAuth);
    }

    public void checkOperateOtherUserAuth(String loginUid, String targetUid) {
        //check self auth
        boolean selfCheck = loginUid.equals(targetUid);

        //Pass
        if (selfCheck) {
            return;
        }

        DmAuthUserDO loginUser = authDal.userMapper().queryByUid(loginUid);
        DmAuthUserDO targetUser = authDal.userMapper().queryByUid(targetUid);

        //Fail, cross primary user.
        if (targetUser == null) {
            throw new IllegalArgumentException("target user not exist.");
        }

        if (targetUser.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
            //Fail, cross primary user.
            if (loginUser.getAccountType() == AccountType.PRIMARY_ACCOUNT) {
                throw new IllegalArgumentException("Current login user have no authority to query target user resource.");
            }

            //Fail, sub-account query other primary account resource
            if (!Objects.equals(loginUser.getParentId(), targetUser.getId())) {
                throw new IllegalArgumentException("Current login user have no authority to query target user resource.");
            }
        } else {
            //Fail.primary account query not it's sub-account resource
            if (loginUser.getAccountType() == AccountType.PRIMARY_ACCOUNT && !Objects.equals(loginUser.getId(), targetUser.getParentId())) {
                throw new IllegalArgumentException("Current login user have no authority to query target user resource.");
            }

            //Fail.two sub-account have diff primary account
            if (loginUser.getAccountType() == AccountType.SUB_ACCOUNT && !Objects.equals(loginUser.getParentId(), targetUser.getParentId())) {
                throw new IllegalArgumentException("Current login user have no authority to query target user resource.");
            }
        }
    }

    public void checkResOwnership(String puid, long resId, AuthKind authKind) {
        if (authKind == AuthKind.DataSource) {
            DmDsDO dsDO = dsDal.dsMapper().queryDsIdentityById(resId);
            if (dsDO == null) {
                throw new IllegalArgumentException(DmI18nUtils.getMessage(I18nRdpMsgKeys.DS_CHECK_NOT_EXIST_ERROR.name(), resId));
            }

            if (!dsDO.getUid().equals(puid)) {
                throw new IllegalArgumentException(DmI18nUtils.getMessage(I18nRdpMsgKeys.DS_IS_NOT_BELONG_YOU_PRIMARY_ERROR.name(), resId));
            }
        } else {
            throw new IllegalArgumentException("Unsupported auth kind:" + authKind);
        }
    }

    public boolean checkResAuthWithoutError(String puid, String uid, long resId, DsResPath resPath, String dataAuthLabel, AuthKind authKind) {
        if (authKind == AuthKind.DataSource) {
            DmDsDO dsDO = this.dsDal.dsMapper().queryDsIdentityById(resId);
            return checkDsAuth(puid, uid, dsDO, resPath, dataAuthLabel);
        } else {
            throw new IllegalArgumentException("Unsupported auth kind:" + authKind);
        }
    }

    public void checkResAuth(String puid, String uid, long resId, DsResPath resPath, String dataAuthLabel, AuthKind authKind) {
        if (authKind == AuthKind.DataSource) {
            DmDsDO dsDO = this.dsDal.dsMapper().queryDsIdentityById(resId);
            if (!this.checkDsAuth(puid, uid, dsDO, resPath, dataAuthLabel)) {
                if (StringUtils.equals(resPath.getResPath(), "/")) {
                    throw new ErrorMessageException(RdpAuthUtils.missDataAuthMsg(resId, dsDO.getInstanceId(), dataAuthLabel));
                } else {
                    String res = dsDO.getInstanceId() + "(" + resPath.getResPath() + ")";
                    throw new ErrorMessageException(RdpAuthUtils.missDataAuthMsg(resId, res, dataAuthLabel));
                }
            }
        } else {
            throw new IllegalArgumentException("Unsupported auth kind:" + authKind);
        }
    }

    private boolean checkDsAuth(String puid, String uid, DmDsDO dsDO, DsResPath resPath, String dataAuthLabel) {
        if (dsDO == null) {
            throw new IllegalArgumentException(DmI18nUtils.getMessage(I18nRdpMsgKeys.DS_CHECK_NOT_EXIST_ERROR.name()));
        }

        //The DataSource owner have all privileges
        if (dsDO.getUid().equals(uid)) {
            return true;
        }

        //the user role is ds manager
        if (authDal.userMapper().queryByUid(uid).isResourceManageEnable()) {
            return true;
        }

        //The DataSource owner is not the primary user.
        if (!dsDO.getUid().equals(puid)) {
            throw new IllegalArgumentException(DmI18nUtils.getMessage(I18nRdpMsgKeys.DS_IS_NOT_BELONG_YOU_PRIMARY_ERROR.name(), dsDO.getId()));
        }

        List<Predicate<String>> authedPathNames = new ArrayList<>();
        List<String> queryPaths = Collections.singletonList(resPath.getResPath());
        List<DmAuthResDO> dsAuthDOs = this.authDal.resMapper().queryByPathLike(dsDO.getId(), uid, AuthKind.DataSource, queryPaths);
        for (DmAuthResDO dsAuthDO : dsAuthDOs) {
            // filter resAuth
            if (dsAuthDO.getAuthLabels() == null || !dsAuthDO.getAuthLabels().contains(dataAuthLabel) || !dsAuthDO.isEffective()) {
                continue;
            }

            // diffuse
            authedPathNames.add(s -> dsAuthDO.getResPath().startsWith(s) || s.startsWith(dsAuthDO.getResPath()));
        }

        boolean checkResult = false;
        for (Predicate<String> authedPath : authedPathNames) {
            if (authedPath.test(resPath.getResPath())) {
                checkResult = true;
                break;
            }
        }

        return checkResult;
    }

    public List<DmAuthResDO> listAuthByUser(String targetUid, AuthKind authKind) {
        if (authKind == AuthKind.DataSource) {
            List<DmAuthResDO> resAuthDOList = listDsAuth(targetUid, null);
            DmAuthUserDO userDO = authDal.userMapper().queryByUid(targetUid);
            if (userDO.getAccountType() == AccountType.PRIMARY_ACCOUNT || userDO.isResourceManageEnable()) {
                return resAuthDOList;
            } else {
                return resAuthDOList.stream().filter(r -> r.getAuthLabels().contains(RDP_DAUTH_DS_READ) && r.isEffective()).collect(Collectors.toList());
            }
        } else {
            throw new IllegalArgumentException("Unsupported auth kind:" + authKind);
        }
    }

    public List<Long> listResByUser(String targetUid, AuthKind authKind) {
        if (authKind == AuthKind.DataSource) {
            DmAuthUserDO userDO = authDal.userMapper().queryByUid(targetUid);
            if (userDO.getAccountType() == AccountType.PRIMARY_ACCOUNT || userDO.isResourceManageEnable()) {
                if (userDO.getParentId() != null) {
                    targetUid = authDal.userMapper().queryById(userDO.getParentId()).getUid();
                }
                List<DmDsDO> dsDOs = this.dsDal.dsMapper().listByUserWithGmtOrder(targetUid);
                return dsDOs.stream().map(DmDsDO::getId).collect(Collectors.toList());
            } else {
                List<DmAuthResDO> result = this.authDal.resMapper().listByKind(targetUid, AuthKind.DataSource);
                return result.stream().map(DmAuthResDO::getResId).distinct().collect(Collectors.toList());
            }
        } else {
            return Collections.emptyList();
        }
    }

    public List<DmAuthResDO> listSpecifiedAuthOfUser(String targetUid, String dataAuthLabel, AuthKind authKind) {
        if (authKind == AuthKind.DataSource) {
            return listDsAuth(targetUid, Collections.singletonList(dataAuthLabel));
        } else {
            throw new IllegalArgumentException("Unsupported auth kind:" + authKind);
        }
    }

    private List<DmAuthResDO> listDsAuth(String targetUid, List<String> filterDataAuthLabels) {
        DmAuthUserDO userDO = authDal.userMapper().queryByUid(targetUid);
        List<DmAuthResDO> result = new ArrayList<>();
        if (userDO.getAccountType() == AccountType.PRIMARY_ACCOUNT || userDO.isResourceManageEnable()) {
            if (userDO.getParentId() != null) {
                targetUid = authDal.userMapper().queryById(userDO.getParentId()).getUid();
            }
            List<DmDsDO> dsDOs = this.dsDal.dsMapper().listByUserWithGmtOrder(targetUid);

            for (DmDsDO dsDO : dsDOs) {
                DmAuthResDO authDO = new DmAuthResDO();
                authDO.setResId(dsDO.getId());
                authDO.setResDesc(dsDO.getInstanceDesc());
                authDO.setResInstId(dsDO.getInstanceId());
                result.add(authDO);
            }
        } else {
            result = this.authDal.resMapper().listByKind(targetUid, AuthKind.DataSource);
            if (filterDataAuthLabels != null && !filterDataAuthLabels.isEmpty()) {
                result = result.stream().filter(t -> t.getAuthLabels().containsAll(filterDataAuthLabels) && t.isEffective()).collect(Collectors.toList());
            }
        }

        return result;
    }
}
