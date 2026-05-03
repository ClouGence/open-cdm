package com.clougence.rdp.controller.model.fo.ticket;

/**
 * @author wanshao create time is 2021/3/2
 **/
public enum RdpTicketListType {
    /**
     * Self created tickets
     */
    SELF_CREATE,
    /**
     * wait confirm tickets
     */
    WAIT_SELF_PROCESS,
    /**
     * Self created and wait to confirm tickets
     */
    ALL
}
