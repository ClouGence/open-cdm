package com.clougence.clouddm.console.web.dal.enumeration;

public enum AutoExecJobStatus {
    INIT,
    WAIT_EXEC,
    EXECUTING,
    FAILED,
    PAUSE,
    PAUSING,
    // end status
    FINISH,
    TERMINATION
}
