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
