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

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clougence.clouddm.platform.dal.model.approval.ApprovalStatus;
import com.clougence.clouddm.platform.dal.model.approval.DmApprovalDO;
import com.clougence.clouddm.platform.dal.model.approval.ArgApprovalQueryObj;

/**
 * @author Ekko
 * @date 2024/5/13 14:43
*/
public interface DmApprovalMapper extends BaseMapper<DmApprovalDO> {

    void updateStatusByEnum(@Param("ticketId") long ticketId, @Param("ticketStatus") ApprovalStatus ticketStatus, @Param("statusMessage") String statusMessage);

    IPage<DmApprovalDO> listTicketByConditionAndPage(Page page, @Param("ticketQuery") ArgApprovalQueryObj ticketQueryObject, @Param("puid") String puid);

    IPage<DmApprovalDO> listConfirmTicketByConditionAndPage(Page page, @Param("ticketQuery") ArgApprovalQueryObj ticketQueryObject);

    IPage<DmApprovalDO> listAuthTicketByConditionAndPage(Page page, @Param("ticketQuery") ArgApprovalQueryObj ticketQueryObject);

    DmApprovalDO queryByBizId(@Param("bizId") String bizId);

    DmApprovalDO queryById(@Param("id") Long id);

    DmApprovalDO queryByApproIdentity(@Param("approIdentity") String approIdentity, @Param("type") String type, @Param("puid") String puid);

    int updateModified(Long id);

    List<Long> listUnFinishTicketIdList(@Param("time") Date time);

    DmApprovalDO selectByIdForUpdate(@Param("ticketId") Long ticketId);

    void updateThirdApprovalInfo(@Param("ticketId") Long ticketId, @Param("approvalIdentity") String approvalIdentity, @Param("url") String url);

    void updateComment(@Param("ticketId") Long ticketId, @Param("comment") String comment);

    void updateTicketInfo(@Param("ticketId") Long ticketId, @Param("ticketInfo") String ticketInfo);
}
