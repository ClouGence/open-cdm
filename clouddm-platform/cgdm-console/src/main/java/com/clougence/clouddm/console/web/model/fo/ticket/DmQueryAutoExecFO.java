package com.clougence.clouddm.console.web.model.fo.ticket;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DmQueryAutoExecFO {

    @NotNull(message = "ticketId must not null")
    private Long ticketId;

    @NotNull(message = "taskId must not null")
    private Long taskId;
}
