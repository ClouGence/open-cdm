package com.clougence.rdp.component.ticket.model;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wanshao create time is 2021/2/5
 **/
@Getter
@Setter
public class RdpExecStageContextMO {

    private List<String> execUserName;

    private String       execMsg;

    // Is it automatically executed by the system
    private boolean      isAutoExecute;
}
