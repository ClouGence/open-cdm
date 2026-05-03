package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.Min;

import lombok.Data;

/**
 * @author bucketli 2021/1/8 15:01
 */
@Data
public class DeleteDsFO {

    @Min(value = 1, message = "{min.datasourceid}")
    private long dataSourceId;
}
