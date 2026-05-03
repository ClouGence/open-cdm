package com.clougence.clouddm.api.sidecar.autoexec;

public enum AutoExecMessageType {
    TASK_START,
    TASK_FINISH,
    TASK_FAILED,
    TASK_WAIT_CONFIRM,
    TASK_RETRY,

    TRANSACTION_FINISH,
    TRANSACTION_ROLLBACK,
    TASK_SKIP,

    JOB_FINISH,
    JOB_FAILED,
    JOB_PAUSE,
    CREATE_SESSION_FAILED,
    QUERY_ID
}
