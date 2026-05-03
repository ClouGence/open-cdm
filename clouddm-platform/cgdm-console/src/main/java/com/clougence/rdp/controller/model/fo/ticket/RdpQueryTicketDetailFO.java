package com.clougence.rdp.controller.model.fo.ticket;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdpQueryTicketDetailFO {

    @NotNull(message = "ticketId can't be null")
    private Long    ticketId;

    private boolean refreshCache;
}
