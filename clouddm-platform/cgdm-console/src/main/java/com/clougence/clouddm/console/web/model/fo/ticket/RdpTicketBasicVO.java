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
package com.clougence.clouddm.console.web.model.fo.ticket;

import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.clouddm.console.web.dal.enumeration.RdpApprovalBiz;
import com.clougence.clouddm.console.web.dal.enumeration.RdpApprovalType;
import com.clougence.clouddm.console.web.dal.enumeration.RdpTicketStatus;
import com.clougence.clouddm.console.web.dal.model.RdpTicketDO;
import com.clougence.clouddm.console.web.dal.model.RdpUserDO;
import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.clougence.utils.format.DateFormatType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdpTicketBasicVO {

    // ----- from table rdp_ticket_inst ------

    private Long            id;

    private String          bizId;

    private String          gmtCreate;

    private String          gmtModified;

    private String          userName;

    private Long            resId;

    private String          resourceName;

    private String          resourceDesc;

    private String          resourceType;

    private String          targetInfo;

    private RdpApprovalType approType;

    private RdpApprovalBiz  approBiz;

    private String          approTemplateName;

    private String          description;

    private String          ticketTitle;

    private RdpTicketStatus ticketStatus;

    private String          finishTime;

    private String          expectedExecTime;

    //    private boolean         isNormal = true;

    public static RdpTicketBasicVO generateVO(RdpTicketDO ticketDO, String resourceType, RdpUserDO ownerUserDO) {
        RdpTicketBasicVO vo = new RdpTicketBasicVO();
        vo.setId(ticketDO.getId());
        vo.setBizId(ticketDO.getBizId());
        vo.setGmtCreate(DateFormatType.s_yyyyMMdd_HHmmss.format(ticketDO.getGmtCreate()));
        vo.setGmtModified(DateFormatType.s_yyyyMMdd_HHmmss.format(ticketDO.getGmtModified()));
        vo.setResId(ticketDO.getBindDsId());
        vo.setTargetInfo(ticketDO.getTargetInfo());
        vo.setApproType(ticketDO.getApproType());
        vo.setApproBiz(ticketDO.getApproBiz());
        vo.setApproTemplateName(ticketDO.getApproTemplateName());
        vo.setDescription(ticketDO.getDescription());
        vo.setTicketTitle(ticketDO.getTicketTitle());
        vo.setTicketStatus(ticketDO.getTicketStatus());
        vo.setFinishTime(DateFormatType.s_yyyyMMdd_HHmmss.format(ticketDO.getFinishTime()));

        vo.setUserName((ownerUserDO != null) ? ownerUserDO.getUsername() : DmI18nUtils.getMessage(I18nRdpMsgKeys.USER_NOT_EXIST_ERROR.name()));

        vo.setResourceType(resourceType);

        return vo;
    }

}
