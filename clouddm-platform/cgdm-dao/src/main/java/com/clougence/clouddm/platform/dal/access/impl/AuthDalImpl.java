package com.clougence.clouddm.platform.dal.access.impl;

import org.springframework.stereotype.Service;

import com.clougence.clouddm.platform.dal.access.AuthDal;
import com.clougence.clouddm.platform.dal.mapper.auth.*;

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
}
