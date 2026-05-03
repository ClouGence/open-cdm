package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.Min;

import lombok.Data;

/**
 * @author bucketli 2021/2/1 10:42
 */
@Data
public class QueryRdsWhiteListFO {

    @Min(value = 1, message = "{min.datasourceid}")
    private long dataSourceId;
}
