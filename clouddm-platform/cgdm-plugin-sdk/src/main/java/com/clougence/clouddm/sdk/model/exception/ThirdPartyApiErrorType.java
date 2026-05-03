package com.clougence.clouddm.sdk.model.exception;

// special handle
public enum ThirdPartyApiErrorType {
    // wait next time
    CONNECTION_ERROR,

    // delete template cache 
    APPROVAL_TEMPLATE_NOT_EXISTS,

    // fail ticket
    OTHER,
}
