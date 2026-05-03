package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.Min;

import lombok.Data;

/**
 * @author bucketli 2021/1/29 16:43
 */
@Data
public class DeleteAccountFO {

    @Min(value = 1, message = "{min.datasourceid}")
    private long dataSourceId;
}
