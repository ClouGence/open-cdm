package com.clougence.clouddm.sdk.security.auth;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.clougence.clouddm.sdk.Spi;
import com.clougence.clouddm.sdk.security.auth.def.SecSysRole;

/**
 * @author mode create time is 2021/2/23
 **/
public interface AuthInfoSpi extends Spi {

    void registryAuthLabel(AuthBinder labelBinder);

    default List<String> innerRoleDef() {
        return Arrays.asList(SecSysRole.ADMIN_ROLE_NAME, SecSysRole.DBA_ROLE_NAME, SecSysRole.PM_ROLE_NAME, SecSysRole.DEV_ROLE_NAME);
    }

    default List<String> innerRoleAuthLabels(String roleName) {
        return Collections.emptyList();
    }
}
