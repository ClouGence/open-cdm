package com.clougence.clouddm.console.web.model.fo.ticket;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Ekko
 * @date 2024/5/10 14:11
*/
@Getter
@Setter
public class DmQueryTicketDetailFO {

    private Long    ticketId;

    private boolean refreshCache;
    @JsonIgnore
    private String  uid;
}
