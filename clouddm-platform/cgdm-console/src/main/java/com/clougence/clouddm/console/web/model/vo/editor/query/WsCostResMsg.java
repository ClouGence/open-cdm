package com.clougence.clouddm.console.web.model.vo.editor.query;

import com.clougence.clouddm.console.web.service.editor.query.QueryStatus;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2022/3/23 18:42:25
 */
@Getter
@Setter
public class WsCostResMsg extends WsResMsg {

    private QueryStatus step;
    private long        startTime;
    private long        preCost;
    private long        queryCost;
    private long        rcvCost;
}
