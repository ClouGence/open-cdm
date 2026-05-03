package com.clougence.clouddm.console.web.model.vo.editor.query;

import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mode 2020-01-20 12:28
 * @since 1.1.3
 */
@Getter
@Setter
public class SessionVO {

    private String       sessionId;

    // rdb
    private String       rdbCatalog;
    private String       rdbSchema;
    private boolean      rdbAutoCommit;
    private RdbIsolation rdbTxIsolation;
    private boolean      rdbReadOnly;
}
