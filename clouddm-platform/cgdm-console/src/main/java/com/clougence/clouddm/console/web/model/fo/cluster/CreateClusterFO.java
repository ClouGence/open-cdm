package com.clougence.clouddm.console.web.model.fo.cluster;

import javax.validation.constraints.NotNull;

import com.clougence.clouddm.console.web.constants.CloudOrIdcName;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/1/7 14:24
 */
@Getter
@Setter
public class CreateClusterFO {

    @NotNull(message = "{notnull.region}")
    private String         region;

    @NotNull(message = "{notnull.cloudoridcname}")
    private CloudOrIdcName cloudOrIdcName;

    private String         clusterDesc;
}
