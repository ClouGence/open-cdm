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

import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.mapper.auth.*;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;
import com.clougence.utils.StringUtils;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthDalImpl implements AuthDal {

    @Resource
    private DmAuthApprovalMapper     approvalMapper;
    @Resource
    private DmAuthCsrfTokenMapper    csrfTokenMapper;
    @Resource
    private DmAuthMfaChallengeMapper mfaChallengeMapper;
    @Resource
    private DmAuthMFAMapper          mfaMapper;
    @Resource
    private DmAuthResMapper          resMapper;
    @Resource
    private DmAuthRoleMapper         roleMapper;
    @Resource
    private DmAuthUserMapper         userMapper;
    @Resource
    private DmAuthVerifyMapper       verifyMapper;

    @Override
    public DmAuthApprovalMapper approvalMapper() {
        return approvalMapper;
    }

    @Override
    public DmAuthCsrfTokenMapper csrfTokenMapper() {
        return csrfTokenMapper;
    }

    @Override
    public DmAuthMfaChallengeMapper mfaChallengeMapper() {
        return mfaChallengeMapper;
    }

    @Override
    public DmAuthMFAMapper mfaMapper() {
        return mfaMapper;
    }

    @Override
    public DmAuthResMapper resMapper() {
        return resMapper;
    }

    @Override
    public DmAuthRoleMapper roleMapper() {
        return roleMapper;
    }

    @Override
    public DmAuthUserMapper userMapper() {
        return userMapper;
    }

    @Override
    public DmAuthVerifyMapper verifyMapper() {
        return verifyMapper;
    }

    // ---------- dal service methods ----------

    @Override
    public DmAuthUserDO queryRootUser() {
        return this.userMapper.queryByUid(ROOT_USER_UID);
    }

    @Override
    public boolean isRootUser(String uid) {
        return StringUtils.equals(uid, ROOT_USER_UID);
    }

    @Override
    public DmAuthUserDO queryLocalUserByLoginText(String loginText) {
        if (StringUtils.isBlank(loginText)) {
            return null;
        }

        DmAuthUserDO user = this.userMapper.queryLocalLoginUserByAccount(loginText);
        if (user != null) {
            return user;
        }

        user = this.userMapper.queryLocalLoginUserByEmail(loginText);
        if (user != null) {
            return user;
        }

        return this.userMapper.queryLocalLoginUserByPhone(loginText);
    }
}
