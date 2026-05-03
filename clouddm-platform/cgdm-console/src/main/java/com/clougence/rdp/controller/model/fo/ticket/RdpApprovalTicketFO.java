package com.clougence.rdp.controller.model.fo.ticket;

import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdpApprovalTicketFO {

    @Min(value = 1, message = "ticketId must large than 0.")
    private long    ticketId;

    private boolean rejected;

    private String  comment;
}
