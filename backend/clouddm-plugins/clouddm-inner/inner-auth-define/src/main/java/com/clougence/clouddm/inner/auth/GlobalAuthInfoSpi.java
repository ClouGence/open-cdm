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
package com.clougence.clouddm.inner.auth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.clougence.clouddm.sdk.security.auth.AuthBinder;
import com.clougence.clouddm.sdk.security.auth.AuthHelper;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.clouddm.sdk.security.auth.AuthInfoSpi;
import com.clougence.clouddm.sdk.security.auth.def.SecAuthCategory;
import com.clougence.clouddm.sdk.security.auth.def.SecDataAuthLabel;
import com.clougence.clouddm.sdk.security.auth.def.SecRoleAuthLabel;
import com.clougence.clouddm.sdk.security.auth.def.SecSysRole;
import com.clougence.utils.StringUtils;

/**
 * @author mode 2021/1/6 19:00
 */
public class GlobalAuthInfoSpi implements AuthInfoSpi {

    private static final List<String> ADMIN_ROLE_LABELS = new ArrayList<>();
    private static final List<String> DBA_ROLE_LABELS   = new ArrayList<>();
    private static final List<String> PM_ROLE_LABELS    = new ArrayList<>();
    private static final List<String> DEV_ROLE_LABELS   = new ArrayList<>();

    private static List<String> convertLabelKeys(List<AuthInfo> authInfos) {
        return authInfos.stream().map(AuthInfo::getKey).collect(Collectors.toList());
    }

    static {
        ADMIN_ROLE_LABELS.addAll(convertLabelKeys(AuthHelper.lookUpLabel(SecRoleAuthLabel.class)));
        DBA_ROLE_LABELS.addAll(convertLabelKeys(AuthHelper.lookUpLabel(SecRoleAuthLabel.class, Collections.singletonList(SecSysRole.DBA_ROLE_NAME))));
        PM_ROLE_LABELS.addAll(convertLabelKeys(AuthHelper.lookUpLabel(SecRoleAuthLabel.class, Collections.singletonList(SecSysRole.PM_ROLE_NAME))));
        DEV_ROLE_LABELS.addAll(convertLabelKeys(AuthHelper.lookUpLabel(SecRoleAuthLabel.class, Collections.singletonList(SecSysRole.DEV_ROLE_NAME))));
    }

    @Override
    public String name() {
        return "global";
    }

    @Override
    public List<String> innerRoleAuthLabels(String roleName) {
        if (StringUtils.equals(roleName, SecSysRole.ADMIN_ROLE_NAME)) {
            return ADMIN_ROLE_LABELS;
        } else if (StringUtils.equals(roleName, SecSysRole.DBA_ROLE_NAME)) {
            return DBA_ROLE_LABELS;
        } else if (StringUtils.equals(roleName, SecSysRole.DEV_ROLE_NAME)) {
            return DEV_ROLE_LABELS;
        } else if (StringUtils.equals(roleName, SecSysRole.PM_ROLE_NAME)) {
            return PM_ROLE_LABELS;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void registryAuthLabel(AuthBinder labelBinder) {
        for (AuthInfo authInfo : AuthHelper.lookUp(SecAuthCategory.class)) {
            labelBinder.addAuthInfo(authInfo);
        }
        for (AuthInfo authInfo : AuthHelper.lookUp(SecRoleAuthLabel.class)) {
            labelBinder.addAuthInfo(authInfo);
        }
        for (AuthInfo authInfo : AuthHelper.lookUp(SecDataAuthLabel.class)) {
            labelBinder.addAuthInfo(authInfo);
        }
    }
}
