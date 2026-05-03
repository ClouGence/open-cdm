package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * @author bucketli 2021/1/28 12:20
 */
@Data
public class UpdateAliyunRdsAkSkFO {

    @Min(value = 1, message = "{min.datasourceid}")
    private long   dataSourceId;

    @NotBlank(message = "{notblank.accesskey}")
    private String accessKey;

    @NotBlank(message = "{notblank.secretkey}")
    private String secretKey;
}
