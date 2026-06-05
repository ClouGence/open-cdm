package com.clougence.clouddm.platform.dal.access;

import com.clougence.clouddm.platform.dal.mapper.auth.*;

public interface AuthDal {

    DmAuthApprovalMapper approvalMapper();

    DmAuthCsrfTokenMapper csrfTokenMapper();

    DmAuthMFAMapper mfaMapper();

    DmAuthResMapper resMapper();

    DmAuthRoleMapper roleMapper();

    DmAuthUserMapper userMapper();

    DmAuthVerifyMapper verifyMapper();
}
