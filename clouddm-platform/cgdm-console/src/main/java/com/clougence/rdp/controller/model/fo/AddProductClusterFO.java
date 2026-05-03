package com.clougence.rdp.controller.model.fo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.clougence.rdp.dal.enumeration.RdpProduct;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2023/11/23 16:47:05
 */
@Getter
@Setter
public class AddProductClusterFO {

    @NotNull(message = "{notnull.product}")
    private RdpProduct product;

    @NotBlank(message = "{notblank.productversion}")
    private String     productVersion;

    private String     clusterDesc;

    private String     clusterCode;

    @NotBlank(message = "{notblank.apiaddr}")
    private String     apiAddr;
}
