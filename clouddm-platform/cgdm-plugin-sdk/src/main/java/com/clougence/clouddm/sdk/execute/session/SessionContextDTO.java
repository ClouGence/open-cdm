package com.clougence.clouddm.sdk.execute.session;

import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * use by create session.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SessionContextDTO {

    private String       sessionId;
    private Integer      maxIdleTimeSec;
    private String       rdbCatalog;
    private String       rdbSchema;
    private boolean      rdbAutoCommit;
    private RdbIsolation rdbTxIsolation;
    private boolean      rdbReadOnly;

}
