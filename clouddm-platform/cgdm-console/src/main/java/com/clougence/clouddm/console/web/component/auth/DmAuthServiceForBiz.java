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

import com.clougence.clouddm.platform.dal.model.auth.DmAuthResDO;
import com.clougence.clouddm.sdk.model.analysis.resource.DsResPath;
import com.clougence.clouddm.sdk.security.auth.AuthKind;

/**
 * @author bucketli 2024/2/21 09:40:00
 */
public interface DmAuthServiceForBiz {

    boolean checkRoleAuth(String puid, String uid, String roleAuth);

    void checkOperateOtherUserAuth(String loginUid, String targetUid);

    void checkResOwnership(String puid, long resId, AuthKind authKind);

    boolean checkResAuthWithoutError(String puid, String uid, long resId, DsResPath resPath, String dataAuthLabel, AuthKind authKind);

    void checkResAuth(String puid, String uid, long resId, DsResPath resPath, String dataAuthLabel, AuthKind authKind);

    List<DmAuthResDO> listAuthByUser(String targetUid, AuthKind authKind);

    List<Long> listResByUser(String targetUid, AuthKind authKind);

    List<DmAuthResDO> listSpecifiedAuthOfUser(String targetUid, String dataAuthLabel, AuthKind authKind);

    void checkResPath(String puid, String uid, long resID, AuthKind authKind, DsResPath resPath, String dataAuthLabel);

    void checkBrowseAuth(String puid, String uid, long resId, AuthKind authKind, DsResPath dsResource, String dataAuthLabel);

    boolean checkResPathWithoutError(String puid, String uid, long resID, AuthKind authKind, DsResPath resPath, String dataAuthLabel);

    boolean checkResPathChildrenWithoutError(String puid, String uid, long resID, AuthKind authKind, DsResPath resPath, String dataAuthLabel);

    boolean checkRoleAuthWithoutError(String puid, String uid, String roleAuthLabel);

    void checkResultFile(String puid, String uid, String fileUniqueId);
}
