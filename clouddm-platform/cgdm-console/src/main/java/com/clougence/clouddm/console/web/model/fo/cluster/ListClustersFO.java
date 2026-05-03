package com.clougence.clouddm.console.web.model.fo.cluster;

import com.clougence.clouddm.console.web.constants.CloudOrIdcName;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bucketli 2021/2/1 12:42
 */
@Getter
@Setter
public class ListClustersFO {

    private String         clusterNameLike;

    private String         region;

    private CloudOrIdcName cloudOrIdcName;

    private String         clusterDescLike;
}
