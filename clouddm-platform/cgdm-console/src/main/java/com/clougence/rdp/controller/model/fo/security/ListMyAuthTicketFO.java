package com.clougence.rdp.controller.model.fo.security;

import com.clougence.rdp.dal.enumeration.RdpTicketStatus;
import com.clougence.rdp.util.RdpPageDO;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListMyAuthTicketFO {

    private Long            startTimeMs;

    private Long            endTimeMs;

    private String          ticketTitleName;

    private RdpTicketStatus ticketStatus;

    private RdpPageDO       page;

    @JsonIgnore
    private String          uid;
}
