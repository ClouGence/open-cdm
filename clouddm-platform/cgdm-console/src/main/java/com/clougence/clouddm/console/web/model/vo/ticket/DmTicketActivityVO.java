package com.clougence.clouddm.console.web.model.vo.ticket;

import java.util.List;

import com.clougence.rdp.dal.enumeration.RdpTicketProcessActivityStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DmTicketActivityVO {

    private Long                           processActivityId;

    private Long                           processId;
    private Long                           ticketId;

    private String                         gmtCreate;

    private String                         gmtModified;

    private RdpTicketProcessActivityStatus activityStatus;

    private String                         activityTitle;

    private String                         finishTime;

    private String                         remark;

    private List<String>                   approvalUserList;

    private String                         startTime;
}
