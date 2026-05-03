package com.clougence.clouddm.console.web.model.vo.editor.query;

import java.util.List;

import com.clougence.clouddm.sdk.execute.resultset.echo.ReceiveMode;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2022/3/23 18:42:25
 */
@Getter
@Setter
public class WsResultSetMetaMsg extends WsResMsg {

    private String       resultId;
    private String       resource;
    private List<String> columnList;
    private List<String> columnType;
    private ReceiveMode  receiveMode;

    private String       receiveCost;
    private String       cacheFile;
    private String       querySql;
    private String       original;
    private List<String> rewriteTags;
}
