package com.clougence.rdp.service.openapi.model;

import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2022/11/17 19:08:36
 */
@Getter
@Setter
public class ApiDeleteDsFO {

    @Min(value = 1, message = "{min.datasourceid}")
    private long dataSourceId;
}
