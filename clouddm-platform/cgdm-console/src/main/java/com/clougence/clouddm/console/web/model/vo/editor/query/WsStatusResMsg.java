package com.clougence.clouddm.console.web.model.vo.editor.query;

import com.clougence.clouddm.sdk.execute.session.rdb.RdbIsolation;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2022/3/23 18:42:25
 */
@Getter
@Setter
public class WsStatusResMsg extends WsResMsg {

    private String       rdbCatalog;
    private String       rdbSchema;
    private boolean      rdbAutoCommit;
    private RdbIsolation rdbTxIsolation;
    private boolean      rdbReadOnly;
}
