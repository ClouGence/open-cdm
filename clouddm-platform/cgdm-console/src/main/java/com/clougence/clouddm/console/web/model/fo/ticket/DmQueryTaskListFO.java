package com.clougence.clouddm.console.web.model.fo.ticket;

import javax.validation.constraints.NotNull;

import com.clougence.clouddm.console.web.dal.enumeration.AutoExecTaskStatus;
import com.clougence.rdp.util.RdpPageDO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DmQueryTaskListFO {

    @NotNull(message = "ticketId must not null")
    private Long               ticketId;

    private AutoExecTaskStatus taskStatus;
    private RdpPageDO          page;
}
