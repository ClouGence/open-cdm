package com.clougence.clouddm.console.web.model.fo.datasource;

import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/9 15:35
 */
@Getter
@Setter
public class QueryDsConfigFO {

    @Min(value = 1, message = "datasource id must large than 0.")
    private long dataSourceId;
}
