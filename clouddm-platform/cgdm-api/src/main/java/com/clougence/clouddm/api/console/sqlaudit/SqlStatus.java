package com.clougence.clouddm.api.console.sqlaudit;

public enum SqlStatus {

    RUNNING("RUNNING"),
    SUCCESS("SUCCESS"),
    WAIT_CONFIRM("WAIT_CONFIRM"),
    ROLLBACK("ROLLBACK"),
    FAILURE("FAILURE"),
    ERROR("ERROR");

    private String status;

    SqlStatus(String result){
        this.status = result;
    }
}
