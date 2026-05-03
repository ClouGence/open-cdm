package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * @author bucketli 2021/1/29 16:43
 */
@Data
public class UpdatePubHostFO {

    @Min(value = 1, message = "{min.datasourceid}")
    private long   dataSourceId;

    @NotBlank(message = "{notblank.publichost}")
    private String publicHost;

}
