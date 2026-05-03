package com.clougence.rdp.controller.model.fo.ticket;

import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RdpCancelTicketFO {

    @Min(value = 1, message = "ticketId must large than 0.")
    private long ticketId;

}
