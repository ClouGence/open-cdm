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

import com.clougence.clouddm.api.common.rpc.ResWebData;
import com.clougence.clouddm.console.web.component.cicd.model.ChangeTicketInfoResult;
import com.clougence.clouddm.console.web.model.fo.cicd.ChangeListFO;
import com.clougence.clouddm.console.web.model.vo.DmPageVO;
import com.clougence.clouddm.console.web.model.vo.cicd.ChangeSqlPreviewVO;
import com.clougence.clouddm.console.web.model.vo.cicd.ChangeVO;
import com.clougence.clouddm.console.web.service.cicd.domain.ChangeTriggerContext;
import com.clougence.clouddm.console.web.service.cicd.domain.CreateSuggest;
import com.clougence.clouddm.platform.dal.model.cicd.DmChangeDO;

public interface DmChangeService {

    DmPageVO<ChangeVO> queryChangeByFlowAndQuery(String ownerUid, long flowId, ChangeListFO fo);

    DmChangeDO queryChangeById(long changeId);

    ChangeSqlPreviewVO previewChangeSql(long changeId, int startLine, int lineCount, String contentName);

    ChangeTicketInfoResult fetchChangeApprovalByChangeId(long changeId);

    void retryChange(String curUid, long changeId);

    void restartChange(String curUid, long changeId);

    void closeChange(String curUid, long changeId);

    void verifyFlow(String ownerUid, long flowId);

    CreateSuggest createChangeSuggest(String ownerUid, long flowId, String commitId);

    ResWebData<String> triggerChangeSuggest(String ownerUid, long flowId, ChangeTriggerContext triggerContext);
}
