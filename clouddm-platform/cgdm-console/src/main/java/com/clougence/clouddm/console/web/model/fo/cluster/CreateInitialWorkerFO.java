package com.clougence.clouddm.console.web.model.fo.cluster;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.clougence.clouddm.console.web.constants.CloudOrIdcName;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/7 12:16
 */
@Getter
@Setter
public class CreateInitialWorkerFO {

    @Min(value = 1, message = "cluster id must be large than 0.")
    private long           clusterId;

    @NotNull(message = "deploy env type can not be null.")
    private CloudOrIdcName cloudOrIdcName;

    @NotNull(message = "region can not be null.")
    private String         region;
}
