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

import com.clougence.clouddm.console.web.component.auth.model.ResourceAccessInfo;
import com.clougence.clouddm.console.web.component.dsconfig.mode.DsLevels;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthResDO;
import com.clougence.clouddm.sdk.model.analysis.resource.DsResPath;
import com.clougence.clouddm.sdk.security.auth.AuthInfo;
import com.clougence.clouddm.sdk.security.auth.AuthKind;

/**
 * @author mode 2020/12/8 15:21
 */
public interface DmResAuthService {

    List<DmAuthResDO> listAuthByUser(String targetUid, AuthKind authKind);

    List<DmAuthResDO> listAuthByUser(long dsId, String targetUid, AuthKind authKind, List<String> path);

    List<Long> listResByUser(String targetUid, AuthKind authKind);

    List<Long> listResByUserContainAnyAuth(String targetUid, AuthKind authKind);

    boolean checkResAuth(String puid, String uid, long dsId, DsResPath dsObj, String resAuth);

    /** The permissions of resources and functions are verified at the same time */
    <T extends DsResPath> List<T> filterAuthByUser(String puid, String uid, long dsId, List<T> dsResource, String resAuth);

    AuthInfo getAuthInfo(String authKey);

    ResourceAccessInfo getAllowBrowseInfo(DsLevels levels, String uid);

}
