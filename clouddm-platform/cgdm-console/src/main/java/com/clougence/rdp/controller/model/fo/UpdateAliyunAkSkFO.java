package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * @author bucketli 2021/1/29 21:08
 */
@Data
public class UpdateAliyunAkSkFO {

    @NotBlank(message = "{notblank.aliyunak}")
    private String aliyunAk;

    @NotBlank(message = "{notblank.aliyunsk}")
    private String aliyunSk;
}
