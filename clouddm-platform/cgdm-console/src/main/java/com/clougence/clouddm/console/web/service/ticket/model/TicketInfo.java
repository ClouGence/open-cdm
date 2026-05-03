package com.clougence.clouddm.console.web.service.ticket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TicketInfo {

    private String  message;

    private boolean autoExec;

    private String  changeOwnerUid;
    private Long    changeId;
}
