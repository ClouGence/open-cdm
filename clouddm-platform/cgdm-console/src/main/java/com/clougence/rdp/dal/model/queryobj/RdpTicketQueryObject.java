package com.clougence.rdp.dal.model.queryobj;

import java.util.Date;
import java.util.List;

import com.clougence.rdp.dal.enumeration.RdpTicketStatus;

import lombok.Builder;
import lombok.Data;

/**
 * @author Ekko
 * @date 2024/5/10 13:46
*/
@Data
@Builder
public class RdpTicketQueryObject {

    private Long            ticketId;

    private String          ticketBizId;

    private RdpTicketStatus ticketStatus;

    private List<String>    uids;

    private Date            startTime;

    private Date            endTime;

    private String          ticketTitleName;

    private List<Long>      dsIds;

    private String          approvalPersonUid;
}
