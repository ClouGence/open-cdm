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
package com.clougence.rdp.service;

import java.util.List;

import com.clougence.clouddm.sdk.model.analysis.resource.DsResPath;
import com.clougence.clouddm.sdk.security.auth.AuthKind;
import com.clougence.clouddm.platform.dal.model.auth.DmAuthResDO;

/**
 * @author mode 2020/12/8 15:21
 */
public interface RdpAuthServiceForBiz {

    boolean checkRoleAuth(String puid, String uid, String roleAuth);

    void checkOperateOtherUserAuth(String loginUid, String targetUid);

    void checkResOwnership(String puid, long resId, AuthKind authKind);

    boolean checkResAuthWithoutError(String puid, String uid, long resId, DsResPath resPath, String dataAuthLabel, AuthKind authKind);

    void checkResAuth(String puid, String uid, long resId, DsResPath resPath, String dataAuthLabel, AuthKind authKind);

    List<DmAuthResDO> listAuthByUser(String targetUid, AuthKind authKind);

    List<Long> listResByUser(String targetUid, AuthKind authKind);

    List<DmAuthResDO> listSpecifiedAuthOfUser(String targetUid, String dataAuthLabel, AuthKind authKind);
}
