package com.clougence.rdp.controller.model.fo;

import com.clougence.clouddm.base.metadata.ds.DataSourceType;
import com.clougence.rdp.dal.enumeration.DeployEnvType;
import com.clougence.rdp.dal.enumeration.HostType;
import com.clougence.rdp.dal.enumeration.LifeCycleState;

import lombok.Data;

/**
 * @author bucketli 2021/1/29 15:47
 */
@Data
public class ListDsFO {

    private DataSourceType type;

    private DeployEnvType  deployType;

    private LifeCycleState lifeCycleState;

    private HostType       hostType;

    private Long           dataSourceId;

    private String         dataSourceDescLike;

    private String         dsHostLike;

    private String         instanceIdLike;
}
