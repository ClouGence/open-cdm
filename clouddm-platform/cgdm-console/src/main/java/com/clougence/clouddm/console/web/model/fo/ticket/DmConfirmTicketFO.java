package com.clougence.clouddm.console.web.model.fo.ticket;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.clougence.clouddm.console.web.constants.DmConfirmActionType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Ekko
 * @date 2024/5/9 14:35
*/
@Getter
@Setter
public class DmConfirmTicketFO {

    @Min(value = 1, message = "ticketId must large than 0.")
    private long                ticketId;

    @NotNull(message = "confirmActionType can not be null.")
    private DmConfirmActionType confirmActionType;

    @JsonIgnore
    private String              confirmUid;

    private String              comment;

    private DmAutoExecConfigFO  autoExecConfig;
}
