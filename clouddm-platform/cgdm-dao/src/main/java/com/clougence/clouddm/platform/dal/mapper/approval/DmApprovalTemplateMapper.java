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
package com.clougence.clouddm.platform.dal.mapper.approval;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalType;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalTemplateDO;

/**
 * @author wanshao create time is 2021/1/26
 **/
public interface DmApprovalTemplateMapper extends BaseMapper<DmApprovalTemplateDO> {

    List<DmApprovalTemplateDO> listByPrimaryUid(String primaryUid);

    List<DmApprovalTemplateDO> listByPrimaryUidAndType(@Param("primaryUid") String primaryUid, @Param("type") ApprovalType type);

    DmApprovalTemplateDO queryByUidAndTemId(@Param("primaryUid") String primaryUid, @Param("templateId") String templateId);

    void deleteByPrimaryUid(String primaryUid, ApprovalType approvalType);

    int insertTemplateBatch(@Param("cacheList") List<DmApprovalTemplateDO> cacheList);

    void updateTemplateBatch(@Param("cacheList") List<DmApprovalTemplateDO> cacheList);

    void updateApprovalTemplateBatch(@Param("cacheList") List<DmApprovalTemplateDO> cacheList);
}
