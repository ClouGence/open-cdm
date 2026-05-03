package com.clougence.rdp.controller.model.fo.ticket;

import com.clougence.rdp.dal.enumeration.RdpTicketStatus;
import com.clougence.rdp.util.RdpPageDO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdpListTicketFO {

    private Long              ticketId;

    private String            userName;

    private Long              startTimeMs;

    private Long              endTimeMs;

    private String            ticketTitleName;

    private RdpTicketStatus   ticketStatus;

    private RdpTicketListType ticketListType;

    private RdpPageDO         page;

    @JsonIgnore
    private String            uid;

}
