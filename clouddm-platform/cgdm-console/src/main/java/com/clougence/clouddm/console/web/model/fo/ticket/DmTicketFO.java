package com.clougence.clouddm.console.web.model.fo.ticket;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DmTicketFO {

    @NotNull(message = "ticketId must not null")
    private Long ticketId;
}
