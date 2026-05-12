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
package com.clougence.rdp.service.impl;

import static com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel.RDP_DAUTH_DS_READ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.console.web.dal.enumeration.AccountType;
import com.clougence.clouddm.sdk.model.analysis.resource.DsResPath;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.dal.mapper.RdpDataSourceMapper;
import com.clougence.clouddm.console.web.dal.mapper.DmResAuthMapper;
import com.clougence.clouddm.console.web.dal.mapper.RdpRoleMapper;
import com.clougence.clouddm.console.web.dal.mapper.RdpUserMapper;
import com.clougence.clouddm.console.web.dal.model.RdpDataSourceDO;
import com.clougence.clouddm.console.web.dal.model.DmResAuthDO;
import com.clougence.clouddm.console.web.dal.model.RdpRoleDO;
import com.clougence.clouddm.console.web.dal.model.RdpUserDO;
import com.clougence.rdp.global.exception.ErrorMessageException;
import com.clougence.rdp.service.RdpAuthServiceForBiz;
import com.clougence.clouddm.console.web.util.RdpAuthUtils;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

/**
 * @author bucketli 2024/2/21 15:48:53
 */
@Service
@Slf4j
public class RdpAuthServiceForBizImpl implements RdpAuthServiceForBiz {

    @Resource
    private RdpUserMapper       rdpUserMapper;

    @Resource
    private RdpRoleMapper       rdpRoleMapper;

    @Resource
    private RdpDataSourceMapper rdpDataSourceMapper;

    @Resource
    private DmResAuthMapper resAuthMapper;

    @Override
    public boolean checkRoleAuth(String puid, String uid, String roleAuth) {
        // primary not check
        if (StringUtils.equals(puid, uid)) {
            return true;
        }

        // user must exist
        RdpUserDO userDO = this.rdpUserMapper.queryByUid(uid);
        if (userDO == null) {
            return false;
        }

        RdpRoleDO roleDO = this.rdpRoleMapper.selectById(userDO.getRoleId());
        if (roleDO == null) {
            return false;
        }

        List<String> labels = roleDO.getRoleAuthLabels();
        return labels != null && labels.contains(roleAuth);
    }

    @Override
    public void checkOperateOtherUserAuth(String loginUid, String targetUid) {
        //check self auth
        boolean selfCheck = loginUid.equals(targetUid);

        //Pass
        if (selfCheck) {
            return;
        }

        RdpUserDO loginUser = rdpUserMapper.queryByUid(loginUid);
        RdpUserDO targetUser = rdpUserMapper.queryByUid(targetUid);

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

    @Override
    public void checkResOwnership(String puid, long resId, AuthKind authKind) {
        if (authKind == AuthKind.DataSource) {
            RdpDataSourceDO dsDO = rdpDataSourceMapper.queryDsIdentityById(resId);
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

    @Override
    public boolean checkResAuthWithoutError(String puid, String uid, long resId, DsResPath resPath, String dataAuthLabel, AuthKind authKind) {
        if (authKind == AuthKind.DataSource) {
            RdpDataSourceDO dsDO = this.rdpDataSourceMapper.queryDsIdentityById(resId);
            return checkDsAuth(puid, uid, dsDO, resPath, dataAuthLabel);
        } else {
            throw new IllegalArgumentException("Unsupported auth kind:" + authKind);
        }
    }

    @Override
    public void checkResAuth(String puid, String uid, long resId, DsResPath resPath, String dataAuthLabel, AuthKind authKind) {
        if (authKind == AuthKind.DataSource) {
            RdpDataSourceDO dsDO = this.rdpDataSourceMapper.queryDsIdentityById(resId);
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

    private boolean checkDsAuth(String puid, String uid, RdpDataSourceDO dsDO, DsResPath resPath, String dataAuthLabel) {
        if (dsDO == null) {
            throw new IllegalArgumentException(DmI18nUtils.getMessage(I18nRdpMsgKeys.DS_CHECK_NOT_EXIST_ERROR.name()));
        }

        //The DataSource owner have all privileges
        if (dsDO.getUid().equals(uid)) {
            return true;
        }

        //the user role is ds manager
        if (rdpUserMapper.queryByUid(uid).isResourceManageEnable()) {
            return true;
        }

        //The DataSource owner is not the primary user.
        if (!dsDO.getUid().equals(puid)) {
            throw new IllegalArgumentException(DmI18nUtils.getMessage(I18nRdpMsgKeys.DS_IS_NOT_BELONG_YOU_PRIMARY_ERROR.name(), dsDO.getId()));
        }

        List<Predicate<String>> authedPathNames = new ArrayList<>();
        List<String> queryPaths = Collections.singletonList(resPath.getResPath());
        List<DmResAuthDO> dsAuthDOs = this.resAuthMapper.queryByPathLike(dsDO.getId(), uid, AuthKind.DataSource, queryPaths);
        for (DmResAuthDO dsAuthDO : dsAuthDOs) {
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

    @Override
    public List<DmResAuthDO> listAuthByUser(String targetUid, AuthKind authKind) {
        if (authKind == AuthKind.DataSource) {
            List<DmResAuthDO> resAuthDOList = listDsAuth(targetUid, null);
            RdpUserDO userDO = rdpUserMapper.queryByUid(targetUid);
            if (userDO.getAccountType() == AccountType.PRIMARY_ACCOUNT || userDO.isResourceManageEnable()) {
                return resAuthDOList;
            } else {
                return resAuthDOList.stream().filter(r -> r.getAuthLabels().contains(RDP_DAUTH_DS_READ) && r.isEffective()).collect(Collectors.toList());
            }
        } else {
            throw new IllegalArgumentException("Unsupported auth kind:" + authKind);
        }
    }

    @Override
    public List<Long> listResByUser(String targetUid, AuthKind authKind) {
        if (authKind == AuthKind.DataSource) {
            RdpUserDO userDO = rdpUserMapper.queryByUid(targetUid);
            if (userDO.getAccountType() == AccountType.PRIMARY_ACCOUNT || userDO.isResourceManageEnable()) {
                if (userDO.getParentId() != null) {
                    targetUid = rdpUserMapper.queryById(userDO.getParentId()).getUid();
                }
                List<RdpDataSourceDO> dsDOs = this.rdpDataSourceMapper.listByUserWithGmtOrder(targetUid);
                return dsDOs.stream().map(RdpDataSourceDO::getId).collect(Collectors.toList());
            } else {
                List<DmResAuthDO> result = this.resAuthMapper.listByKind(targetUid, AuthKind.DataSource);
                return result.stream().map(DmResAuthDO::getResId).distinct().collect(Collectors.toList());
            }
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<DmResAuthDO> listSpecifiedAuthOfUser(String targetUid, String dataAuthLabel, AuthKind authKind) {
        if (authKind == AuthKind.DataSource) {
            return listDsAuth(targetUid, Collections.singletonList(dataAuthLabel));
        } else {
            throw new IllegalArgumentException("Unsupported auth kind:" + authKind);
        }
    }

    private List<DmResAuthDO> listDsAuth(String targetUid, List<String> filterDataAuthLabels) {
        RdpUserDO userDO = rdpUserMapper.queryByUid(targetUid);
        List<DmResAuthDO> result = new ArrayList<>();
        if (userDO.getAccountType() == AccountType.PRIMARY_ACCOUNT || userDO.isResourceManageEnable()) {
            if (userDO.getParentId() != null) {
                targetUid = rdpUserMapper.queryById(userDO.getParentId()).getUid();
            }
            List<RdpDataSourceDO> dsDOs = this.rdpDataSourceMapper.listByUserWithGmtOrder(targetUid);

            for (RdpDataSourceDO dsDO : dsDOs) {
                DmResAuthDO authDO = new DmResAuthDO();
                authDO.setResId(dsDO.getId());
                authDO.setResDesc(dsDO.getInstanceDesc());
                authDO.setResInstId(dsDO.getInstanceId());
                result.add(authDO);
            }
        } else {
            result = this.resAuthMapper.listByKind(targetUid, AuthKind.DataSource);
            if (filterDataAuthLabels != null && !filterDataAuthLabels.isEmpty()) {
                result = result.stream().filter(t -> t.getAuthLabels().containsAll(filterDataAuthLabels) && t.isEffective()).collect(Collectors.toList());
            }
        }

        return result;
    }
}
