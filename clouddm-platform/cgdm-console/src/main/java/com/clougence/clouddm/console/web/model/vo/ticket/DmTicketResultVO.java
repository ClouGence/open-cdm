package com.clougence.clouddm.console.web.model.vo.ticket;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author: Ekko
 * @Date: 2024-06-05 10:21
 */
@Getter
@Setter
public class DmTicketResultVO {

    private Long            ticketId;
    private List<CheckedVO> checkedVOS;
    private boolean         confirm;
    private boolean         failure;
}
