package com.clougence.clouddm.console.web.model.vo.editor.query;

import java.util.List;

import com.clougence.clouddm.sdk.execute.resultset.echo.ResultSetRow;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2022/3/23 18:42:25
 */
@Getter
@Setter
public class WsResultSetResMsg extends WsResMsg {

    private String             resultId;
    private int                fetchCount;
    private String             receiveCost;
    private List<ResultSetRow> rowSet;
}
