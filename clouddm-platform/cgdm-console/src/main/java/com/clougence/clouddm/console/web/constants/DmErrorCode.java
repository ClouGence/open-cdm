package com.clougence.clouddm.console.web.constants;

public enum DmErrorCode {

    //COMM ERROR 100xx
    COMM_SYSTEM_ERROR("10001"),

    // DS ERROR  101xx
    DS_DISCONNECT_ERROR("10103"),

    //Worker & Cluster & RPC   102xx
    CLUSTER_HAVE_NO_WORKS_ERROR("10201"),

    //Ticket error
    TICKET_SQL_PARSE_FAILED("20001");

    private final String code;

    DmErrorCode(String code){
        this.code = code;
    }

    public String code() {
        return this.code;
    }
}
