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
package com.clougence.clouddm.platform.dal.mapper.cicd;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clougence.clouddm.platform.dal.model.cicd.ArgChangeFlowQueryObj;
import com.clougence.clouddm.platform.dal.model.cicd.ChangeFlowStatus;
import com.clougence.clouddm.platform.dal.model.cicd.ChangeFlowType;
import com.clougence.clouddm.platform.dal.model.cicd.DmChangeFlowDO;

public interface DmChangeFlowMapper extends BaseMapper<DmChangeFlowDO> {
    IPage<DmChangeFlowDO> listFlowByConditionAndPage(Page<?> page, ArgChangeFlowQueryObj param, String ownerUid);

    List<DmChangeFlowDO> listFlowByIds(String ownerUid, Set<Long> ids);

    List<DmChangeFlowDO> queryByIds(String ownerUid, Collection<Long> flowIds);

    DmChangeFlowDO queryByOwnerAndId(String ownerUid, long flowId);

    DmChangeFlowDO queryByOwnerAndIdForUpdate(String ownerUid, long flowId);

    List<DmChangeFlowDO> queryChildren(String ownerUid, long parentFlowId);

    List<DmChangeFlowDO> queryChildrenByParentIds(String ownerUid, Collection<Long> parentFlowIds);

    List<DmChangeFlowDO> queryParentCandidates(String ownerUid);

    int countChildren(String ownerUid, long parentFlowId);

    List<DmChangeFlowDO> queryEnabledByOwnerAndDsId(String ownerUid, long dsId);

    List<DmChangeFlowDO> queryEnabledByOwnerAndScmId(String ownerUid, long scmId);

    List<DmChangeFlowDO> queryEnabledByOwnerAndImId(String ownerUid, long imId);

    List<DmChangeFlowDO> queryEnabledByOwnerAndHash(String ownerUid, long flowHashcode);

    void updateManagerByOwnerAndId(String ownerUid, long flowId, String newData);

    void updateNameByOwnerAndId(String ownerUid, long flowId, String newData);

    void updateDescByOwnerAndId(String ownerUid, long flowId, String newData);

    void updateStatusByOwnerAndId(String ownerUid, long flowId, ChangeFlowStatus newData);

    int updateParentByOwnerAndId(String ownerUid, long flowId, ChangeFlowType flowType, Long parentFlowId,
                                 boolean enableWebhook, boolean enableTrigger);

    void updateMessageConfigByOwnerAndId(String ownerUid, long flowId, DmChangeFlowDO flow);

    void disableByOwnerAndScmId(String ownerUid, long scmId);

    void disableByOwnerAndImId(String ownerUid, long imId);

    int deleteByOwnerAndId(String ownerUid, long flowId);

    void enableFlowByOwnerAndId(String ownerUid, long flowId);

    void disableFlowByOwnerAndId(String ownerUid, long flowId);

    void enableWebHookByOwnerAndId(String ownerUid, long flowId);

    void disableWebHookByOwnerAndId(String ownerUid, long flowId);

    void enableTriggerByOwnerAndId(String ownerUid, long flowId);

    void disableTriggerByOwnerAndId(String ownerUid, long flowId);

    void configCallBackByOwnerAndId(String ownerUid, long flowId, boolean enable, String httpMethod, String httpUrl);

    void configTriggerByOwnerAndId(String ownerUid, long flowId, boolean enable, String token);

    void updateScmRepoMetadata(@Param("ownerUid") String ownerUid, @Param("flowId") long flowId, @Param("repoSpace") String repoSpace, @Param("repoName") String repoName,
                               @Param("repoUrl") String repoUrl);

    void updateWebhookSigningToken(@Param("ownerUid") String ownerUid, @Param("flowId") long flowId, @Param("signingToken") String signingToken);
}
