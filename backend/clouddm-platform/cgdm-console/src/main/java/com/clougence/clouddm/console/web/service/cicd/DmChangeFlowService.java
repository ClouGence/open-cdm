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
package com.clougence.clouddm.console.web.service.cicd;

import java.io.File;
import java.util.List;
import java.util.Set;

import com.clougence.clouddm.console.web.model.fo.cicd.*;
import com.clougence.clouddm.console.web.model.vo.DmPageVO;
import com.clougence.clouddm.console.web.model.vo.cicd.ChangeFlowVO;
import com.clougence.clouddm.console.web.model.vo.cicd.GuideCreateChangeFlowVO;
import com.clougence.clouddm.platform.dal.model.cicd.ChangeFlowStatus;
import com.clougence.clouddm.platform.dal.model.cicd.DmChangeFlowDO;

public interface DmChangeFlowService {

    DmPageVO<ChangeFlowVO> queryChangeFlowListByPage(String ownerUid, ChangeFlowListFO fo);

    List<ChangeFlowVO> queryChangeFlowListByIds(String ownerUid, Set<Long> ids);

    List<DmChangeFlowDO> queryEnableDevopsByDsId(String ownerUid, long dsId);

    List<DmChangeFlowDO> queryEnableDevopsByScmId(String ownerUid, long scmId);

    List<DmChangeFlowDO> queryEnableDevopsByImId(String ownerUid, long imId);

    List<DmChangeFlowDO> queryEnableDevopsByScmHash(String ownerUid, long hash);

    List<DmChangeFlowDO> queryAllGitOpsByFlowId(String ownerUid, long flowId);

    DmChangeFlowDO queryMessageByFlowId(String ownerUid, long flowId);

    long toHash(GuideCheckFlowFO fo);

    GuideCreateChangeFlowVO createChangeFlow(String ownerUid, String currentUser, GuideCreateFO fo);

    DmChangeFlowDO queryFlowById(String ownerUid, long flowId);

    void updateMessageByFlowId(String ownerUid, long flowId, ChangeFlowImConfigFO fo);

    GuideCreateChangeFlowVO createGitOpsFlow(String ownerUid, long flowId, ChangeFlowGitOpsCreateFO fo);

    void updateInfoByFlowId(String ownerUid, long flowId, ChangeFlowUpdateFO fo);

    void deleteGitOpsFlow(String ownerUid, long flowId);

    void enableGitOpsFlow(String ownerUid, long flowId);

    void disableGitOpsFlow(String ownerUid, long flowId);

    void configGitOpsWebhook(String ownerUid, long flowId, boolean enable, String signingToken, boolean clearSigningToken);

    void configGitOpsTrigger(String ownerUid, long flowId, boolean enable);

    void configGitOpsCallback(String ownerUid, long flowId, ChangeFlowCallbackFO fo);

    void archiveFlow(String ownerUid, long flowId, String operatorUid);

    void recoverFlowTo(String ownerUid, long flowId, ChangeFlowStatus toStatus);

    void deleteFlow(String ownerUid, long flowId);

    File getCicdWorkspace(String ownerUid, long flowId);

    File getCicdTempSpace(String ownerUid, long flowId);
}
