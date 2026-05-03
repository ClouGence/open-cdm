package com.clougence.clouddm.console.web.model.fo.ticket;

import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Ekko
 * @date 2024/5/10 14:30
*/
@Getter
@Setter
public class DmRetryTicketFO {

    @Min(value = 1, message = "ticketId must large than 0.")
    private long ticketId;
}
