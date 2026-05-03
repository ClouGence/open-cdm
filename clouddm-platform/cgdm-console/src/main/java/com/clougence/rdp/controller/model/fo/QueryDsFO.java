package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.Min;

import lombok.Data;

/**
 * @author bucketli 2021/1/9 15:35
 */
@Data
public class QueryDsFO {

    @Min(value = 1, message = "{min.datasourceid}")
    long dataSourceId;
}
