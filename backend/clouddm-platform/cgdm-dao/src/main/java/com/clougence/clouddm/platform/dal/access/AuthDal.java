package com.clougence.clouddm.platform.dal.access;

import com.clougence.clouddm.platform.dal.mapper.auth.*;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthUserDO;

public interface AuthDal {

    String ROOT_USER_UID = "9999999999999999";

    DmAuthApprovalMapper approvalMapper();

    DmAuthCsrfTokenMapper csrfTokenMapper();

    DmAuthMFAMapper mfaMapper();

    DmAuthResMapper resMapper();

    DmAuthRoleMapper roleMapper();

    DmAuthUserMapper userMapper();

    DmAuthVerifyMapper verifyMapper();

    // ---------- dal service methods ----------

    DmAuthUserDO queryRootUser();

    boolean isRootUser(String uid);

    DmAuthUserDO queryLocalUserByLoginText(String loginText);
}
