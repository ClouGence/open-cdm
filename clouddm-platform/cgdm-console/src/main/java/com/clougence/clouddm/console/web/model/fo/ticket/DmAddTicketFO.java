package com.clougence.clouddm.console.web.model.fo.ticket;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Ekko
 * @date 2024/5/8 16:16
*/
@Getter
@Setter
public class DmAddTicketFO {

    @NotNull(message = "{ticket.dbLevels.notblank}")
    private List<String> dbLevels;

    @NotNull(message = "{ticket.sql.notnull}")
    private String       rawSql;

    private String       description;

    private String       rollBackSql;

    @NotBlank(message = "{ticket.title.notblank}")
    private String       ticketTitle;

    private Long         expectedAffectedRows;

    private boolean      force;

}
