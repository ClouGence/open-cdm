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
package com.clougence.clouddm.console.web.dal.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clougence.clouddm.console.web.dal.enumeration.RdpTicketStatus;
import com.clougence.clouddm.console.web.dal.model.DmApprovalDO;
import com.clougence.clouddm.console.web.dal.model.queryobj.RdpTicketQueryObject;

/**
 * @author Ekko
 * @date 2024/5/13 14:43
*/
public interface DmApprovalMapper extends BaseMapper<DmApprovalDO> {

    void updateTicketStatus(long ticketId, String ticketStatus, String statusMessage);

    default void updateTicketStatusByEnum(long ticketId, RdpTicketStatus ticketStatus, String statusMessage) {
        this.updateTicketStatus(ticketId, ticketStatus.name(), statusMessage);
    }

    IPage<DmApprovalDO> listTicketByConditionAndPage(Page page, @Param("ticketQuery") RdpTicketQueryObject ticketQueryObject, @Param("puid") String puid);

    IPage<DmApprovalDO> listConfirmTicketByConditionAndPage(Page page, @Param("ticketQuery") RdpTicketQueryObject ticketQueryObject);

    IPage<DmApprovalDO> listAuthTicketByConditionAndPage(Page page, @Param("ticketQuery") RdpTicketQueryObject ticketQueryObject);

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
