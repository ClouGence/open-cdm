package com.clougence.rdp.controller.model.fo.ticket;

import com.clougence.rdp.constant.I18nRdpMsgKeys;
import com.clougence.rdp.dal.enumeration.RdpApprovalBiz;
import com.clougence.rdp.dal.enumeration.RdpApprovalType;
import com.clougence.rdp.dal.enumeration.RdpTicketStatus;
import com.clougence.rdp.dal.model.RdpTicketDO;
import com.clougence.rdp.dal.model.RdpUserDO;
import com.clougence.rdp.util.RdpI18nUtils;
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

        vo.setUserName((ownerUserDO != null) ? ownerUserDO.getUsername() : RdpI18nUtils.getMessage(I18nRdpMsgKeys.USER_NOT_EXIST_ERROR.name()));

        vo.setResourceType(resourceType);

        return vo;
    }

}
