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
    private DmAuthApprovalMapper  approvalMapper;
    @Resource
    private DmAuthCsrfTokenMapper csrfTokenMapper;
    @Resource
    private DmAuthMFAMapper       mfaMapper;
    @Resource
    private DmAuthResMapper       resMapper;
    @Resource
    private DmAuthRoleMapper      roleMapper;
    @Resource
    private DmAuthUserMapper      userMapper;
    @Resource
    private DmAuthVerifyMapper    verifyMapper;

    @Override
    public DmAuthApprovalMapper approvalMapper() {
        return approvalMapper;
    }

    @Override
    public DmAuthCsrfTokenMapper csrfTokenMapper() {
        return csrfTokenMapper;
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
