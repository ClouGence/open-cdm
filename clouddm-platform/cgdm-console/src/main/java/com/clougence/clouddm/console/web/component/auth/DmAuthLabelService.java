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
package com.clougence.clouddm.console.web.component.auth;

import java.util.List;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.clouddm.sdk.security.auth.AuthElementType;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.clouddm.sdk.security.auth.AuthKind;

public interface DmAuthLabelService {

    AuthInfo getAuthLabel(String authLabelKey);

    List<AuthInfo> getRoleAuthLabel();

    List<AuthInfo> getDataAuthLabel();

    List<AuthInfo> getAllAuthLabel(AuthKind kindType);

    List<AuthInfo> getAllAuthLabelForAuthTreeDef(AuthKind kindType, AuthElementType elementType, DataSourceType dsType);

    List<AuthInfo> getAllCategory();

    List<AuthInfo> getCascadeAuthByLabel(String authLabel);

    List<String> normalizeRoleAuthLabels(List<String> authLabels);
}
